package code.seat.seatmowers.infrastructure.delivery.spring.controllers

import code.seat.seatmowers.infrastructure.delivery.spring.entities.Execution
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import javax.persistence.EntityManager

@Controller
class GetExecutionsListController(val entityManager: EntityManager) {
    @GetMapping("/executions")
    fun handleRequest(model: Model): String {
        model.addAttribute(
            "executions",
            entityManager
                .createQuery("SELECT e FROM Execution e", Execution::class.java)
                .resultList
        )

        return "executions"
    }
}
