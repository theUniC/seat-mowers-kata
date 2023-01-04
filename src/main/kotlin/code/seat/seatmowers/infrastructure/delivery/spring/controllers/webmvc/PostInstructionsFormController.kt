package code.seat.seatmowers.infrastructure.delivery.spring.controllers.webmvc

import code.seat.seatmowers.application.command.CreatePlateauCommand
import code.seat.seatmowers.application.command.DeployMowerCommand
import code.seat.seatmowers.application.command.MoveMowerCommand
import code.seat.seatmowers.application.query.getmower.GetMowerQuery
import code.seat.seatmowers.infrastructure.delivery.spring.dtos.MowerOutputDto
import code.seat.seatmowers.infrastructure.delivery.spring.dtos.PlateauOutputDto
import code.seat.seatmowers.infrastructure.delivery.spring.entities.Execution
import code.seat.seatmowers.infrastructure.delivery.spring.exceptions.ParseException
import code.seat.seatmowers.infrastructure.delivery.spring.repositories.ExecutionRepository
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import java.util.UUID

@Controller
class PostInstructionsFormController(val commandGateway: CommandGateway, val queryGateway: QueryGateway, val executionRepository: ExecutionRepository) {
    private enum class Direction {
        N, W, E, S
    }

    @PostMapping("/instructions")
    fun handleRequest(model: Model, @RequestParam("instructions") instructions: String): String {
        val ls = instructions.lines()
        val plateauId = UUID.randomUUID()
        val plateau = createPlateauFrom(ls.first().trim(), plateauId)
        val mowers = ls.drop(1).chunked(2).map { p ->
            val (mowerSpec, movements) = p
            val mower = deployMowerFromSpec(plateau.getId(), mowerSpec.trim())
            moveMower(plateau.getId(), mower.getId(), movements.trim())
            mower
        }

        val execution = Execution(plateauId, instructions)
        executionRepository.save(execution)

        Thread.sleep(5_000) // Wait 5 secs for query side to be consistent with write side

        model.addAllAttributes(
            mapOf(
                "execution" to execution,
                "mowers" to mowers.map { m ->
                    queryGateway
                        .query(GetMowerQuery(m.getId()), ResponseTypes.optionalInstanceOf(MowerOutputDto::class.java))
                        .thenApply { o -> o.get() }
                        .get()
                }
            )
        )

        return "results"
    }

    private fun createPlateauFrom(spec: String, plateauId: UUID): PlateauOutputDto {
        assertPlateauSpecIsValid(spec)
        val (x, y) = parsePlateuCoordinatesFromSpec(spec)
        commandGateway.sendAndWait<Any>(CreatePlateauCommand(plateauId, x, y))

        return PlateauOutputDto(plateauId, x, y)
    }

    private fun assertPlateauSpecIsValid(spec: String) {
        if (!"[0-9]+ [0-9]+".toRegex().matches(spec)) {
            throw ParseException(ParseException.Type.PLATEAU, spec)
        }
    }

    private fun parsePlateuCoordinatesFromSpec(spec: String): Pair<Int, Int> =
        spec
            .split(' ')
            .map { s -> s.toInt() }
            .zipWithNext()
            .first()

    private fun deployMowerFromSpec(plateauId: UUID, spec: String): MowerOutputDto {
        assertMowerSpecIsValid(spec)
        val (x, y, direction) = parseMowerCoordinatesFromSpec(spec)
        val id = UUID.randomUUID()
        commandGateway.sendAndWait<Any>(DeployMowerCommand(plateauId, id, x, y, direction.toString()))

        return MowerOutputDto(id, plateauId, x, y, direction.toString())
    }

    private fun assertMowerSpecIsValid(spec: String) {
        if (!"[0-9]+ [0-9]+ [NWES]".toRegex().matches(spec)) {
            throw ParseException(ParseException.Type.MOWER, spec)
        }
    }

    private fun parseMowerCoordinatesFromSpec(spec: String): Triple<Int, Int, Direction> {
        val parts = spec.split(' ')
        return Triple(parts[0].toInt(), parts[1].toInt(), Direction.valueOf(parts[2]))
    }

    private fun moveMower(plateauId: UUID, mowerId: UUID, movements: String) {
        assertMowerMovementsAreValid(movements)
        movements.forEach { m ->
            commandGateway.sendAndWait(MoveMowerCommand(plateauId, mowerId, m.toString()))
        }
    }

    private fun assertMowerMovementsAreValid(movements: String) {
        if (!"[LRM]+".toRegex().matches(movements)) {
            throw ParseException(ParseException.Type.MOVEMENTS, movements)
        }
    }
}
