package code.seat.seatmowers.domainmodel.mower

import java.util.UUID

data class MowerWasMoved(val plateauId: UUID, val id: UUID, val to: String)
