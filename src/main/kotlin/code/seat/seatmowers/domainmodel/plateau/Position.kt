package code.seat.seatmowers.domainmodel.plateau

import lombok.Value

@Value
data class Position(val coordinates: Coordinates, val direction: Direction) {
    private val deltas = mapOf(
        Direction.NORTH to Pair(0, 1),
        Direction.EAST to Pair(1, 0),
        Direction.SOUTH to Pair(0, -1),
        Direction.WEST to Pair(-1, 0)
    )

    fun moveForward() = Position(
        Coordinates(
            deltas[direction]!!.first + coordinates.x,
            deltas[direction]!!.second + coordinates.y
        ),
        direction
    )

    fun turnLeft() = Position(
        coordinates,
        direction.left()
    )

    fun turnRight() = Position(
        coordinates,
        direction.right()
    )
}
