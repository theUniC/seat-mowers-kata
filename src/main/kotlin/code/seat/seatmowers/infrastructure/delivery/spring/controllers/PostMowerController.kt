package code.seat.seatmowers.infrastructure.delivery.spring.controllers

import code.seat.seatmowers.application.command.DeployRoverCommand
import code.seat.seatmowers.infrastructure.delivery.spring.dtos.MowerInputDto
import code.seat.seatmowers.infrastructure.delivery.spring.dtos.MowerOutputDto
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import java.util.concurrent.Future

@RestController
@RequestMapping("/plateaus/{plateauId}")
class PostMowerController(val commandGateway: CommandGateway) {
    @PostMapping("/mowers")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    fun handleRequest(@PathVariable plateauId: UUID, @RequestBody mowerInputDto: MowerInputDto): Future<MowerOutputDto> {
        val id = UUID.randomUUID()
        return commandGateway
            .send<Unit>(DeployRoverCommand(plateauId, id, mowerInputDto.x, mowerInputDto.y, mowerInputDto.direction))
            .thenApply { MowerOutputDto(id, plateauId, mowerInputDto.x, mowerInputDto.y, mowerInputDto.direction) }
    }
}
