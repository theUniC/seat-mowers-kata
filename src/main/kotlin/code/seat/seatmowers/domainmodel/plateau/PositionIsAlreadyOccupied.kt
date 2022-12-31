package code.seat.seatmowers.domainmodel.plateau

class PositionIsAlreadyOccupied(position: Position) : RuntimeException("Position $position is already occupied")
