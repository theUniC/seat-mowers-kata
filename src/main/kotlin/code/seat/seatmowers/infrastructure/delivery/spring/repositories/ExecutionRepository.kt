package code.seat.seatmowers.infrastructure.delivery.spring.repositories

import code.seat.seatmowers.infrastructure.delivery.spring.entities.Execution
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface ExecutionRepository : CrudRepository<Execution, UUID>
