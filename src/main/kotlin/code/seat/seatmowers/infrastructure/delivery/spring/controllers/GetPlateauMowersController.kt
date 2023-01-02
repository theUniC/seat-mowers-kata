package code.seat.seatmowers.infrastructure.delivery.spring.controllers

import code.seat.seatmowers.application.query.getallplateaumowers.GetAllPlateauMowersQuery
import code.seat.seatmowers.infrastructure.delivery.spring.dtos.MowerOutputDto
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import java.util.concurrent.Future

@RestController
class GetPlateauMowersController(val queryGateway: QueryGateway) {
    @GetMapping("/plateaus/{plateauId}/mowers")
    @ResponseBody
    fun handleRequest(@PathVariable plateauId: UUID): Future<List<MowerOutputDto>> =
        queryGateway
            .query(GetAllPlateauMowersQuery(plateauId), ResponseTypes.multipleInstancesOf(MowerOutputDto::class.java))
}
