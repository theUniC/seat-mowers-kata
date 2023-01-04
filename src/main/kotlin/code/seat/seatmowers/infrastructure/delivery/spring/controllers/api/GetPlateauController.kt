package code.seat.seatmowers.infrastructure.delivery.spring.controllers.api

import code.seat.seatmowers.application.query.getplateau.GetPlateauQuery
import code.seat.seatmowers.infrastructure.delivery.spring.dtos.PlateauOutputDto
import code.seat.seatmowers.infrastructure.delivery.spring.exceptions.ResourceNotFoundException
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import java.util.concurrent.Future
import kotlin.jvm.optionals.getOrElse

@RestController
@Tag(name = "Plateau")
class GetPlateauController(val queryGateway: QueryGateway) {
    @GetMapping("/plateaus/{id}", produces = ["application/hal+json"])
    @ApiResponses(
        ApiResponse(description = "The plateau", content = [Content(mediaType = "application/hal+json", schema = Schema(implementation = PlateauOutputDto::class))]),
        ApiResponse(description = "When plateau was not found", responseCode = "404")
    )
    fun handleRequest(@PathVariable id: UUID): Future<PlateauOutputDto> =
        queryGateway
            .query(GetPlateauQuery(id), ResponseTypes.optionalInstanceOf(PlateauOutputDto::class.java))
            .thenApply { op -> op.getOrElse { throw ResourceNotFoundException() } }
            .thenApply { p ->
                p.add(
                    linkTo(methodOn(GetPlateauController::class.java).handleRequest(id)).withSelfRel(),
                    linkTo(methodOn(GetPlateauMowersController::class.java).handleRequest(id)).withRel("mowers")
                )
            }
}
