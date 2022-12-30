package code.seat.seatmowers.domainmodel.plateau

import code.seat.seatmowers.application.command.CreatePlateau
import org.axonframework.messaging.annotation.MessageHandlerInvocationException
import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.axonframework.test.matchers.Matchers
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.util.UUID

object PlateauSpec : Spek({
    describe("Plateau aggregate") {
        val fixture: FixtureConfiguration<Plateau> by memoized { AggregateTestFixture(Plateau::class.java) }

        describe("Creation") {
            it("throws an exception when coordinates are not valid") {
                fixture
                    .givenNoPriorActivity()
                    .`when`(CreatePlateau(UUID.randomUUID(), 0, 15))
                    .expectException(MessageHandlerInvocationException::class.java)
            }
        }
    }
})
