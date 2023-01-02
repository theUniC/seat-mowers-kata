package code.seat.seatmowers.infrastructure.delivery.spring.dtos

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

data class MowerMovementInputDto(
    @get:NotBlank
    @get:Pattern(regexp = "[mlr]", flags = [Pattern.Flag.CASE_INSENSITIVE])
    val movement: String
)
