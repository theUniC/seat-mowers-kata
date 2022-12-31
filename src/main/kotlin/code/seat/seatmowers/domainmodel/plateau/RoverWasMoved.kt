package code.seat.seatmowers.domainmodel.plateau

import java.util.UUID

data class RoverWasMoved(val plateauId: UUID, val id: UUID, val to: String)
