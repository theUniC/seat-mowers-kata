package code.seat.seatmowers.infrastructure.delivery.spring.dtos

import org.springframework.hateoas.CollectionModel

class MowerOutputDtoCollection(ms: List<MowerOutputDto>) : CollectionModel<MowerOutputDto>(ms)
