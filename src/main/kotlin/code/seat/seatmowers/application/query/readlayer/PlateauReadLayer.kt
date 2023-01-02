package code.seat.seatmowers.application.query.readlayer

import code.seat.seatmowers.infrastructure.delivery.spring.dtos.PlateauOutputDto
import java.util.Optional
import java.util.UUID

interface PlateauReadLayer {
    fun all(offset: Int, limit: Int): List<PlateauOutputDto>
    fun byId(id: UUID): Optional<PlateauOutputDto>
}
