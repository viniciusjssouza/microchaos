package microchaos.model

import microchaos.support.BiasedRandom
import microchaos.support.closeToComparator
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

internal class BehaviorTest {

    interface ProbabilityTestCase { val probabilities: Array<Double> }

    internal data class RangeTestCase(
        val expectedSum: Double,
        val ranges: Array<Double>,
        override val probabilities: Array<Double>
    ) : ProbabilityTestCase

    internal data class DrawTestCase (
        override val probabilities: Array<Double>,
        val randomResult: Double,
        val expectedResponseIdx: Int
    ) : ProbabilityTestCase

    companion object {
        @JvmStatic
        private fun rangeTestCases(): Array<RangeTestCase> {
            return arrayOf(
                RangeTestCase(probabilities = arrayOf(), expectedSum = 0.0, ranges = arrayOf()),
                RangeTestCase(probabilities = arrayOf(0.0), expectedSum = 0.0, ranges = arrayOf(0.0)),
                RangeTestCase(probabilities = arrayOf(0.5, 0.5), expectedSum = 1.0, ranges = arrayOf(0.5, 1.0)),
                RangeTestCase(
                    probabilities = arrayOf(20.0, 50.0, 30.0),
                    expectedSum = 100.0,
                    ranges = arrayOf(0.2, 0.7, 1.0)
                ),
                RangeTestCase(
                    probabilities = arrayOf(1.0, 0.5, 1.5, 2.0),
                    expectedSum = 5.0,
                    ranges = arrayOf(0.2, 0.3, 0.6, 1.0)
                )
            )
        }

        @JvmStatic
        private fun drawTestCases(): Array<DrawTestCase> {
            return arrayOf(
                DrawTestCase(probabilities = arrayOf(1.0), randomResult = 0.0, expectedResponseIdx = 0),
                DrawTestCase(probabilities = arrayOf(0.2, 0.3), randomResult = 0.0, expectedResponseIdx = 0),
                DrawTestCase(probabilities = arrayOf(0.2, 0.3), randomResult = 0.399, expectedResponseIdx = 0),
                DrawTestCase(probabilities = arrayOf(0.2, 0.3), randomResult = 0.5, expectedResponseIdx = 1),
                DrawTestCase(probabilities = arrayOf(0.2, 0.3), randomResult = 0.4, expectedResponseIdx = 1),
                DrawTestCase(probabilities = arrayOf(0.2, 0.3), randomResult = 1.0, expectedResponseIdx = 1),
                DrawTestCase(probabilities = arrayOf(30.0, 20.0, 50.0), randomResult = 0.0, expectedResponseIdx = 0),
                DrawTestCase(probabilities = arrayOf(30.0, 20.0, 50.0), randomResult = 0.299, expectedResponseIdx = 0),
                DrawTestCase(probabilities = arrayOf(30.0, 20.0, 50.0), randomResult = 0.3, expectedResponseIdx = 1),
                DrawTestCase(probabilities = arrayOf(30.0, 20.0, 50.0), randomResult = 0.499, expectedResponseIdx = 1),
                DrawTestCase(probabilities = arrayOf(30.0, 20.0, 50.0), randomResult = 0.5, expectedResponseIdx = 2),
                DrawTestCase(probabilities = arrayOf(30.0, 20.0, 50.0), randomResult = 1.0, expectedResponseIdx = 2)
            )
        }
    }

    @Test
    fun `draw response when the response list is empty`() {
        val behavior = Behavior(commands = emptyList())
        Assertions.assertThrows(AssertionError::class.java) { behavior.drawResponse() }
    }

    @ParameterizedTest
    @MethodSource("drawTestCases")
    fun `draw response`(testCase: DrawTestCase) {
        val behavior = Behavior(commands = emptyList(), response = responsesForTestCase(testCase))
        val fakeRandom = BiasedRandom(nextDouble = testCase.randomResult)
        val response = behavior.drawResponse(fakeRandom)
        assertThat(response).isSameAs(behavior.response[testCase.expectedResponseIdx])
    }

    @ParameterizedTest
    @MethodSource("rangeTestCases")
    fun `calculate total probability`(testCase: RangeTestCase) {
        val behavior = Behavior(commands = emptyList(), response = responsesForTestCase(testCase))
        assertThat(behavior.totalProbability).isEqualTo(testCase.expectedSum)
    }

    @ParameterizedTest
    @MethodSource("rangeTestCases")
    fun `calculate range ends`(testCase: RangeTestCase) {
        val behavior = Behavior(commands = emptyList(), response = responsesForTestCase(testCase))
        assertThat(behavior.rangeEnds).usingElementComparator(closeToComparator()).containsExactly(*testCase.ranges)
    }

    private fun responsesForTestCase(testCase: ProbabilityTestCase): List<Response> {
        return testCase.probabilities.map { Response(status = 200, probability = it) }
    }
}