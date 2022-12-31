package code.seat.seatmowers.domainmodel.mower

class PositionIsAlreadyOccupied(position: Position) : RuntimeException("Position $position is already occupied")
