package code.seat.seatmowers.domainmodel

import code.seat.seatmowers.application.command.CreatePlateau
import code.seat.seatmowers.domainmodel.plateau.NewPlateauWasCreated
import code.seat.seatmowers.domainmodel.plateau.Plateau
import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.util.UUID

private const val INVALID_COORDINATE_COMPONENT = 0
private const val VALID_COORDINATE_COMPONENT = 15

object PlateauSpec : Spek({
    describe("Plateau aggregate") {
        val fixture: FixtureConfiguration<Plateau> by memoized { AggregateTestFixture(Plateau::class.java) }

        describe("Creation") {
            it("throws an exception when X coordinate is not valid") {
                fixture
                    .givenNoPriorActivity()
                    .`when`(CreatePlateau(UUID.randomUUID(), INVALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT))
                    .expectException(IllegalStateException::class.java)
            }

            it("throws an exception when Y coordinate is not valid") {
                fixture
                    .givenNoPriorActivity()
                    .`when`(CreatePlateau(UUID.randomUUID(), VALID_COORDINATE_COMPONENT, INVALID_COORDINATE_COMPONENT))
                    .expectException(IllegalStateException::class.java)
            }

            it("Creates a new plateau successfully") {
                val id = UUID.randomUUID()
                fixture
                    .givenNoPriorActivity()
                    .`when`(CreatePlateau(id, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT))
                    .expectSuccessfulHandlerExecution()
                    .expectEvents(NewPlateauWasCreated(id, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT))
            }
        }
    }
})
