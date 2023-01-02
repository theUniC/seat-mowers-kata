package code.seat.seatmowers.infrastructure.delivery.spring.dtos

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import javax.validation.constraints.PositiveOrZero

data class MowerInputDto(
    @get:PositiveOrZero val x: Int,
    @get:PositiveOrZero val y: Int,
    @get:NotBlank
    @get:Pattern(regexp = "[nwes]", flags = [Pattern.Flag.CASE_INSENSITIVE])
    val direction: String
)
