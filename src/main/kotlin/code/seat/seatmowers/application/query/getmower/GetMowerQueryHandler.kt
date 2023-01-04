package code.seat.seatmowers.application.query.getmower

import code.seat.seatmowers.application.query.readlayer.MowerReadLayer
import code.seat.seatmowers.infrastructure.delivery.spring.dtos.MowerOutputDto
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component
import java.util.Optional

@Component
class GetMowerQueryHandler(val mowerReadLayer: MowerReadLayer) {
    @QueryHandler
    fun handle(query: GetMowerQuery): Optional<MowerOutputDto> =
        mowerReadLayer.byId(query.mowerId)
}
