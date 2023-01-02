package code.seat.seatmowers.application.query.readlayer

import code.seat.seatmowers.infrastructure.delivery.spring.dtos.MowerOutputDto
import java.util.UUID

interface MowerReadLayer {
    fun byPlateau(plateauId: UUID): List<MowerOutputDto>
}
