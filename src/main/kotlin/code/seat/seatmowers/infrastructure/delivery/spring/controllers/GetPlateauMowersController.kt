package code.seat.seatmowers.infrastructure.delivery.spring.controllers

import code.seat.seatmowers.application.query.getallplateaumowers.GetAllPlateauMowersQuery
import code.seat.seatmowers.infrastructure.delivery.spring.dtos.MowerOutputDto
import io.swagger.v3.oas.annotations.media.ArraySchema
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
import org.springframework.hateoas.mediatype.problem.Problem
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import java.util.concurrent.Future

@RestController
@Tag(name = "Mower")
class GetPlateauMowersController(val queryGateway: QueryGateway) {
    @GetMapping("/plateaus/{plateauId}/mowers", produces = ["application/hal+json"])
    @ApiResponses(
        ApiResponse(description = "All the deployed mowers from a given plateau", content = [Content(mediaType = "application/hal+json", array = ArraySchema(schema = Schema(implementation = MowerOutputDto::class)))]),
        ApiResponse(description = "When the given plateau does not exist", responseCode = "404", content = [Content(mediaType = "application/problem+json", schema = Schema(implementation = Problem::class))])
    )
    @ResponseBody
    fun handleRequest(@PathVariable plateauId: UUID): Future<CollectionModel<MowerOutputDto>> =
        queryGateway
            .query(GetAllPlateauMowersQuery(plateauId), ResponseTypes.multipleInstancesOf(MowerOutputDto::class.java))
            .thenApply { ms ->
                CollectionModel
                    .of(ms, linkTo(methodOn(GetPlateauMowersController::class.java).handleRequest(plateauId)).withSelfRel())
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
