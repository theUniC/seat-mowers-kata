package code.seat.seatmowers.infrastructure.delivery.spring.controllers.api

import code.seat.seatmowers.application.command.MoveMowerCommand
import code.seat.seatmowers.infrastructure.delivery.spring.dtos.MowerMovementInputDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.hateoas.MediaTypes
import org.springframework.hateoas.mediatype.problem.Problem
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import java.util.concurrent.Future
import javax.validation.Valid

@RestController
@Tag(name = "Plateau")
class PutMowerController(val commandGateway: CommandGateway) {
    @PutMapping("/plateaus/{plateauId}/mowers/{mowerId}", consumes = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(summary = "Move a mower on a given plateau")
    @ApiResponses(
        ApiResponse(description = "When the move has been done correctly", responseCode = "200"),
        ApiResponse(description = "When the given plateau does not exist", responseCode = "404"),
        ApiResponse(description = "When provided data is not correct", responseCode = "400", content = [Content(mediaType = MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE, schema = Schema(implementation = Problem::class))])
    )
    fun handleRequest(@PathVariable plateauId: UUID, @PathVariable mowerId: UUID, @Valid @RequestBody mowerMovementInputDto: MowerMovementInputDto): Future<Unit> =
        commandGateway
            .send(MoveMowerCommand(plateauId, mowerId, mowerMovementInputDto.movement))

    @MutationMapping
    fun moveMower(@Argument plateauId: UUID, @Argument id: UUID, @Argument movement: String): Boolean =
        commandGateway
            .send<Unit>(MoveMowerCommand(plateauId, id, movement))
            .thenApply { true }
            .get()
}
