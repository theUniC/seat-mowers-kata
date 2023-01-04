package code.seat.seatmowers.infrastructure.delivery.spring.controllers.webmvc

import code.seat.seatmowers.application.query.getallplateaumowers.GetAllPlateauMowersQuery
import code.seat.seatmowers.infrastructure.delivery.spring.dtos.MowerOutputDto
import code.seat.seatmowers.infrastructure.delivery.spring.entities.Execution
import code.seat.seatmowers.infrastructure.delivery.spring.exceptions.ResourceNotFoundException
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import java.util.UUID
import javax.persistence.EntityManager

@Controller
class GetExecutionResultController(val queryGateway: QueryGateway, val em: EntityManager) {
    @GetMapping("/execution/{id}")
    fun handleRequest(@PathVariable id: UUID, model: Model): String {
        val execution = em.find(Execution::class.java, id) ?: throw ResourceNotFoundException()

        val mowers = queryGateway
            .query(GetAllPlateauMowersQuery(id), ResponseTypes.multipleInstancesOf(MowerOutputDto::class.java))
            .get()

        model.addAllAttributes(
            mapOf(
                "execution" to execution,
                "mowers" to mowers
            )
        )

        return "results"
    }
}
