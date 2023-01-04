package code.seat.seatmowers.application.query.readlayer

import code.seat.seatmowers.infrastructure.delivery.spring.dtos.MowerOutputDto
import java.util.Optional
import java.util.UUID

interface MowerReadLayer {
    fun byPlateau(plateauId: UUID): List<MowerOutputDto>
    fun byId(mowerId: UUID): Optional<MowerOutputDto>
}
