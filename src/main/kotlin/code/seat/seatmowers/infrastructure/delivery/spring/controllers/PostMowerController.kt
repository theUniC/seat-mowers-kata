package code.seat.seatmowers.infrastructure.delivery.spring.controllers

import code.seat.seatmowers.application.command.DeployRoverCommand
import code.seat.seatmowers.infrastructure.delivery.spring.dtos.MowerInputDto
import code.seat.seatmowers.infrastructure.delivery.spring.dtos.MowerOutputDto
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import java.util.concurrent.Future
import javax.validation.Valid

@RestController
@Tag(name = "Mower")
class PostMowerController(val commandGateway: CommandGateway) {
    @PostMapping("/plateaus/{plateauId}/mowers")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(
        ApiResponse(description = "The deployed mower", responseCode = "200", content = [Content(mediaType = "application/json", schema = Schema(implementation = MowerOutputDto::class))]),
        ApiResponse(description = "When the given plateau does not exist", responseCode = "404")
    )
    @ResponseBody
    fun handleRequest(@PathVariable plateauId: UUID, @Valid @RequestBody mowerInputDto: MowerInputDto): Future<MowerOutputDto> {
        val id = UUID.randomUUID()
        return commandGateway
            .send<Unit>(DeployRoverCommand(plateauId, id, mowerInputDto.x, mowerInputDto.y, mowerInputDto.direction.uppercase()))
            .thenApply { MowerOutputDto(id, plateauId, mowerInputDto.x, mowerInputDto.y, mowerInputDto.direction) }
    }
}
