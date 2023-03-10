package code.seat.seatmowers.infrastructure.delivery.spring.controllers.api

import code.seat.seatmowers.application.query.getmower.GetMowerQuery
import code.seat.seatmowers.infrastructure.delivery.spring.dtos.MowerOutputDto
import code.seat.seatmowers.infrastructure.delivery.spring.exceptions.ResourceNotFoundException
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
import org.springframework.hateoas.MediaTypes
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.util.Optional
import java.util.UUID
import java.util.concurrent.Future
import kotlin.jvm.optionals.getOrElse

@RestController
@Tag(name = "Mower")
class GetMowerController(val queryGateway: QueryGateway) {
    @GetMapping("/mowers/{id}", produces = [MediaTypes.HAL_JSON_VALUE])
    @Operation(summary = "Get a mower by its ID")
    @ApiResponses(
        ApiResponse(description = "The mower", content = [Content(schema = Schema(implementation = MowerOutputDto::class), mediaType = MediaTypes.HAL_JSON_VALUE)]),
        ApiResponse(description = "When given mower was not found", responseCode = "404")
    )
    fun handleRequest(@PathVariable id: UUID): Future<MowerOutputDto> =
        queryGateway
            .query(GetMowerQuery(id), ResponseTypes.optionalInstanceOf(MowerOutputDto::class.java))
            .thenApply { om -> om.getOrElse { throw ResourceNotFoundException() } }
            .thenApply { m ->
                m.add(
                    linkTo(methodOn(GetMowerController::class.java).handleRequest(id)).withSelfRel(),
                    linkTo(methodOn(GetPlateauController::class.java).handleRequest(m.getPlateauId())).withRel("plateau")
                )
            }

    @QueryMapping
    fun mower(@Argument id: UUID): Optional<MowerOutputDto> =
        queryGateway
            .query(GetMowerQuery(id), ResponseTypes.optionalInstanceOf(MowerOutputDto::class.java))
            .get()
}
