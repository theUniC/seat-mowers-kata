package code.seat.seatmowers.infrastructure.delivery.spring.controllers.api

import code.seat.seatmowers.application.command.DeployMowerCommand
import code.seat.seatmowers.infrastructure.delivery.spring.dtos.MowerInputDto
import code.seat.seatmowers.infrastructure.delivery.spring.dtos.MowerOutputDto
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.hateoas.mediatype.problem.Problem
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import java.util.concurrent.Future
import javax.validation.Valid

@RestController
@Tag(name = "Plateau")
class PostMowerController(val commandGateway: CommandGateway) {
    @PostMapping("/plateaus/{plateauId}/mowers", consumes = ["application/json"])
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(
        ApiResponse(description = "The deployed mower", responseCode = "200", content = [Content(mediaType = "application/hal+json", schema = Schema(implementation = MowerOutputDto::class))]),
        ApiResponse(description = "When the given plateau does not exist", responseCode = "404"),
        ApiResponse(description = "When provided data is not valid", responseCode = "400", content = [Content(mediaType = "application/problem+json", schema = Schema(implementation = Problem::class))])
    )
    fun handleRequest(@PathVariable plateauId: UUID, @Valid @RequestBody mowerInputDto: MowerInputDto): Future<MowerOutputDto> {
        val id = UUID.randomUUID()
        return commandGateway
            .send<Unit>(DeployMowerCommand(plateauId, id, mowerInputDto.x, mowerInputDto.y, mowerInputDto.direction.uppercase()))
            .thenApply { MowerOutputDto(id, plateauId, mowerInputDto.x, mowerInputDto.y, mowerInputDto.direction) }
            .thenApply { m ->
                m.add(
                    linkTo(methodOn(GetMowerController::class.java).handleRequest(m.getId())).withSelfRel(),
                    linkTo(methodOn(GetPlateauController::class.java).handleRequest(m.getPlateauId())).withRel("plateau")
                )
            }
    }

    @MutationMapping
    fun deployMower(@Argument plateauId: UUID, x: Int, y: Int, direction: String): MowerOutputDto {
        val id = UUID.randomUUID()
        commandGateway.sendAndWait<Unit>(DeployMowerCommand(plateauId, id, x, y, direction[0].uppercase()))
        return MowerOutputDto(id, plateauId, x, y, direction)
    }
}
