package code.seat.seatmowers.domainmodel.plateau

import java.util.UUID

data class RoverWasDeployed(val id: UUID, val x: Int, val y: Int, val direction: String)
