package code.seat.seatmowers.application.query.getallplateaus

import code.seat.seatmowers.application.query.readlayer.PlateauReadLayer
import code.seat.seatmowers.infrastructure.delivery.spring.dtos.PlateauOutputDto
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
class GetAllPlateausQueryHandler(private val plateauReadLayer: PlateauReadLayer) {
    @QueryHandler
    fun handle(query: GetAllPlateausQuery): List<PlateauOutputDto> =
        plateauReadLayer.all(query.offset, query.limit)
}
