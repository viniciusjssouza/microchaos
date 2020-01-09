package microchaos.support

import org.junit.contrib.java.lang.system.internal.CheckExitCalled
import org.junit.contrib.java.lang.system.internal.NoExitSecurityManager
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler
import org.junit.jupiter.api.fail
import kotlin.test.assertEquals

class SystemExitExtension(private val expectedStatus: Int = 0) : BeforeEachCallback,
    AfterEachCallback,
    TestExecutionExceptionHandler {

    private val originalManager: SecurityManager? = System.getSecurityManager()
    private val noExitManager = NoExitSecurityManager(originalManager)

    override fun beforeEach(context: ExtensionContext?) {
        System.setSecurityManager(noExitManager)
    }

    override fun afterEach(context: ExtensionContext?) {
        if (noExitManager.isCheckExitCalled) {
            val status = noExitManager.getStatusOfFirstCheckExitCall()
            assertEquals(
                expectedStatus,
                status,
                "Expected status on System.exit() does not match"
            )
        } else {
            fail("No System.exit() called")
        }
        System.setSecurityManager(originalManager)
    }

    override fun handleTestExecutionException(context: ExtensionContext?, throwable: Throwable?) {
        if (throwable == null || throwable is CheckExitCalled) {
            return
        } else {
            throw throwable
        }
    }
}