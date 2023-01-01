package code.seat.seatmowers.infrastructure.delivery.spring.dtos

import java.util.UUID

data class MowerOutputDto(val id: UUID, val plateauId: UUID, val x: Int, val y: Int, val direction: String)
