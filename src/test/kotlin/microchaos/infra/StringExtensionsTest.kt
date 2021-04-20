package microchaos.infra

import org.assertj.core.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

internal class StringExtensionsTest {

    internal data class SlugifyTestCases(
            val input: String,
            val expected: String
    )

    companion object {
        @JvmStatic
        private fun slugifyTestCases(): Array<SlugifyTestCases> {
            return arrayOf(
                    SlugifyTestCases(input = "testA", expected = "test-a"),
                    SlugifyTestCases(input = "testAbC", expected = "test-a-b-c"),
                    SlugifyTestCases(input = "test-a", expected = "test-a"),
                    SlugifyTestCases(input = "test123test", expected = "test-123-test"),
                    SlugifyTestCases(input = "test_123+test", expected = "test-123-test"),
                    SlugifyTestCases(input = "test_123(TEST", expected = "test-123-test")
            )
        }
    }

    @ParameterizedTest
    @MethodSource("slugifyTestCases")
    fun `slugify a string`(testCase: SlugifyTestCases) {
        val result = testCase.input.slugify()
        Assertions.assertThat(result).isEqualTo(testCase.expected)
    }

}