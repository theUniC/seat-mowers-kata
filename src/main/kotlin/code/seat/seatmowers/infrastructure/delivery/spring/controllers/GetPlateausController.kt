package code.seat.seatmowers.infrastructure.delivery.spring.controllers

import code.seat.seatmowers.application.query.getallplateaus.GetAllPlateausQuery
import code.seat.seatmowers.infrastructure.delivery.spring.dtos.PlateauOutputDto
import org.axonframework.extensions.kotlin.query
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import java.util.Optional
import java.util.concurrent.Future

@RestController
class GetPlateausController(val queryGateway: QueryGateway) {
    @GetMapping("/plateaus")
    @ResponseBody
    fun handleRequest(@RequestParam offset: Optional<Int>, @RequestParam limit: Optional<Int>): Future<List<PlateauOutputDto>> =
        queryGateway
            .query(GetAllPlateausQuery(offset.orElse(0), limit.orElse(10)), ResponseTypes.multipleInstancesOf(PlateauOutputDto::class.java))
}
