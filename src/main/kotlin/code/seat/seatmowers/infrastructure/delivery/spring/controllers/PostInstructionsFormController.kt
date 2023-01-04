package code.seat.seatmowers.infrastructure.delivery.spring.controllers

import code.seat.seatmowers.application.command.CreatePlateauCommand
import code.seat.seatmowers.application.command.DeployMowerCommand
import code.seat.seatmowers.application.command.MoveMowerCommand
import code.seat.seatmowers.application.query.getmower.GetMowerQuery
import code.seat.seatmowers.infrastructure.delivery.spring.dtos.MowerOutputDto
import code.seat.seatmowers.infrastructure.delivery.spring.dtos.PlateauOutputDto
import code.seat.seatmowers.infrastructure.delivery.spring.exceptions.ParseException
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import java.util.UUID

@Controller
class PostInstructionsFormController(val commandGateway: CommandGateway, val queryGateway: QueryGateway) {
    private enum class Direction {
        N, W, E, S
    }

    @PostMapping("/instructions")
    fun handleRequest(model: Model, @RequestParam("instructions") instructions: String): String {
        val ls = instructions.lines()
        val plateau = createPlateauFrom(ls.first().trim())
        val mowers = ls.drop(1).chunked(2).map { p ->
            val (mowerSpec, movements) = p
            val mower = deployMowerFromSpec(plateau.getId(), mowerSpec.trim())
            moveMower(plateau.getId(), mower.getId(), movements.trim())
            mower
        }

        Thread.sleep(10_000) // Wait 10 secs for query side to be consistent with write side

        model.addAttribute(
            "mowers",
            mowers.map { m ->
                queryGateway
                    .query(GetMowerQuery(m.getId()), ResponseTypes.optionalInstanceOf(MowerOutputDto::class.java))
                    .thenApply { o -> o.get() }
                    .get()
            }
        )

        return "results.html"
    }

    private fun createPlateauFrom(spec: String): PlateauOutputDto {
        assertPlateauSpecIsValid(spec)
        val (x, y) = parsePlateuCoordinatesFromSpec(spec)
        val id = UUID.randomUUID()
        commandGateway.sendAndWait<Any>(CreatePlateauCommand(id, x, y))

        return PlateauOutputDto(id, x, y)
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
