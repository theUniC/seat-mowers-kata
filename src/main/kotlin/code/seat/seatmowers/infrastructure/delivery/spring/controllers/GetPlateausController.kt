package code.seat.seatmowers.infrastructure.delivery.spring.controllers

import code.seat.seatmowers.application.query.getallplateaus.GetAllPlateausQuery
import code.seat.seatmowers.infrastructure.delivery.spring.dtos.PlateauOutputDto
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import java.util.Optional
import java.util.concurrent.Future

@RestController
@Tag(name = "Plateau")
class GetPlateausController(val queryGateway: QueryGateway) {
    @GetMapping("/plateaus", produces = ["application/hal+json"])
    @ApiResponses(
        ApiResponse(description = "The plateaus collection", content = [Content(mediaType = "application/hal+json", array = ArraySchema(schema = Schema(implementation = PlateauOutputDto::class)))])
    )
    @ResponseBody
    fun handleRequest(@RequestParam offset: Optional<Int>, @RequestParam limit: Optional<Int>): Future<CollectionModel<PlateauOutputDto>> =
        queryGateway
            .query(GetAllPlateausQuery(offset.orElse(0), limit.orElse(10)), ResponseTypes.multipleInstancesOf(PlateauOutputDto::class.java))
            .thenApply { ps ->
                CollectionModel
                    .of(ps, linkTo(methodOn(GetPlateausController::class.java).handleRequest(Optional.empty(), Optional.empty())).withSelfRel())
            }
}
