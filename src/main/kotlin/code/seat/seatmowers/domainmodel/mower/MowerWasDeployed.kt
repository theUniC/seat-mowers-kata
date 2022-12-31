package code.seat.seatmowers.domainmodel.mower

import java.util.UUID

data class MowerWasDeployed(val id: UUID, val x: Int, val y: Int, val direction: String)
