package code.seat.seatmowers.application.command

import java.util.UUID

data class CreatePlateauCommand(
    val id: UUID,
    val x: Int,
    val y: Int
)
