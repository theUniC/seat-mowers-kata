package code.seat.seatmowers.infrastructure.delivery.spring.controllers.api

import code.seat.seatmowers.application.query.getallplateaumowers.GetAllPlateauMowersQuery
import code.seat.seatmowers.infrastructure.delivery.spring.dtos.MowerOutputDto
import code.seat.seatmowers.infrastructure.delivery.spring.dtos.MowerOutputDtoCollection
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.MediaTypes
import org.springframework.hateoas.mediatype.problem.Problem
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import java.util.concurrent.Future

@RestController
@Tag(name = "Plateau")
class GetPlateauMowersController(val queryGateway: QueryGateway) {
    @GetMapping("/plateaus/{plateauId}/mowers", produces = [MediaTypes.HAL_JSON_VALUE])
    @Operation(summary = "Get all deployed mowers to a given plateau")
    @ApiResponses(
        ApiResponse(description = "All the deployed mowers from a given plateau", content = [Content(mediaType = MediaTypes.HAL_JSON_VALUE, schema = Schema(implementation = MowerOutputDtoCollection::class))]),
        ApiResponse(description = "When the given plateau does not exist", responseCode = "404", content = [Content(mediaType = MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE, schema = Schema(implementation = Problem::class))])
    )
    fun handleRequest(@PathVariable plateauId: UUID): Future<CollectionModel<MowerOutputDto>> =
        queryGateway
            .query(GetAllPlateauMowersQuery(plateauId), ResponseTypes.multipleInstancesOf(MowerOutputDto::class.java))
            .thenApply { ms ->
                ms.forEach { m ->
                    m.add(
                        linkTo(methodOn(GetMowerController::class.java).handleRequest(m.getId())).withSelfRel(),
                        linkTo(methodOn(GetPlateauController::class.java).handleRequest(m.getPlateauId())).withRel("plateau")
                    )
                }
                ms
            }
            .thenApply { ms ->
                MowerOutputDtoCollection(ms)
                    .add(
                        linkTo(methodOn(GetPlateauMowersController::class.java).handleRequest(plateauId)).withSelfRel()
                    )
            }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleException(ex: IllegalArgumentException) = ex.message

    @QueryMapping
    fun mowers(@Argument plateauId: UUID): List<MowerOutputDto> =
        queryGateway
            .query(GetAllPlateauMowersQuery(plateauId), ResponseTypes.multipleInstancesOf(MowerOutputDto::class.java))
            .get()
}
