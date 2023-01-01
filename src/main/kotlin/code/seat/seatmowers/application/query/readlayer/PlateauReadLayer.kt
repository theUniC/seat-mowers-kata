package code.seat.seatmowers.application.query.readlayer

import code.seat.seatmowers.infrastructure.delivery.spring.dtos.PlateauOutputDto

interface PlateauReadLayer {
    fun all(offset: Int, limit: Int): List<PlateauOutputDto>
}
