package code.seat.seatmowers.application.query.getallplateaumowers

import code.seat.seatmowers.application.query.readlayer.MowerReadLayer
import code.seat.seatmowers.infrastructure.delivery.spring.dtos.MowerOutputDto
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
class GetAllPlateauMowersQueryHandler(private val mowerReadLayer: MowerReadLayer) {
    @QueryHandler
    fun handle(query: GetAllPlateauMowersQuery): List<MowerOutputDto> =
        mowerReadLayer.byPlateau(query.plateauId)
}
