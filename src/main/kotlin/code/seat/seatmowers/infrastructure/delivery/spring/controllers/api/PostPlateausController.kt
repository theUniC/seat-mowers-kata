package code.seat.seatmowers.infrastructure.delivery.spring.controllers.api

import code.seat.seatmowers.application.command.CreatePlateauCommand
import code.seat.seatmowers.infrastructure.delivery.spring.dtos.PlateauInputDto
import code.seat.seatmowers.infrastructure.delivery.spring.dtos.PlateauOutputDto
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
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import java.util.concurrent.Future
import javax.validation.Valid

@RestController
@Tag(name = "Plateau")
class PostPlateausController(val commandGateway: CommandGateway) {
    @PostMapping("/plateaus", consumes = [MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE], produces = [MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new plateau")
    @ApiResponses(
        ApiResponse(description = "The new plateau", responseCode = "200", content = [Content(mediaType = MediaTypes.HAL_JSON_VALUE, schema = Schema(implementation = PlateauOutputDto::class))]),
        ApiResponse(description = "When the given plateau does not exist", responseCode = "404"),
        ApiResponse(description = "When provided data is not correct", responseCode = "400", content = [Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = Schema(implementation = Problem::class))])
    )
    fun handleRequest(@Valid @RequestBody plateauInputDto: PlateauInputDto): Future<PlateauOutputDto> {
        val plateauId = UUID.randomUUID()
        return commandGateway
            .send<Any>(CreatePlateauCommand(plateauId, plateauInputDto.x, plateauInputDto.y))
            .thenApply { PlateauOutputDto(plateauId, plateauInputDto.x, plateauInputDto.y) }
            .thenApply { p ->
                p.add(
                    linkTo(methodOn(GetPlateauController::class.java).handleRequest(plateauId)).withSelfRel(),
                    linkTo(methodOn(GetPlateauMowersController::class.java).handleRequest(plateauId)).withRel("mowers")
                )
            }
    }

    @MutationMapping
    fun createPlateau(@Argument x: Int, @Argument y: Int): PlateauOutputDto {
        val plateauId = UUID.randomUUID()
        commandGateway.sendAndWait<Any>(CreatePlateauCommand(plateauId, x, y))
        return PlateauOutputDto(plateauId, x, y)
    }
}
