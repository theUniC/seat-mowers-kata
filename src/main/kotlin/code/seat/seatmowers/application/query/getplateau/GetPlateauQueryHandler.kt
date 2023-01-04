package code.seat.seatmowers.application.query.getplateau

import code.seat.seatmowers.application.query.readlayer.PlateauReadLayer
import code.seat.seatmowers.infrastructure.delivery.spring.dtos.PlateauOutputDto
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component
import java.util.Optional

@Component
class GetPlateauQueryHandler(val plateauReadLayer: PlateauReadLayer) {
    @QueryHandler
    fun handle(query: GetPlateauQuery): Optional<PlateauOutputDto> =
        plateauReadLayer.byId(query.id)
}
