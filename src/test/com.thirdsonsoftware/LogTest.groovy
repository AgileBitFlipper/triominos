package com.thirdsonsoftware

class LogTest extends GroovyTestCase {
    void testInfo() {
        Log.Info(this.class.name,"This is a log information message.")
        assertTrue(true)
    }

    void testError() {
        Log.Error(this.class.name,"This is an error message.")
        assertTrue(true)
    }

    void testDebug() {
        Log.Debug(this.class.name,"This is a debug message.")
        assertTrue(true)
    }

    void testWarning() {
        Log.Warning(this.class.name,"This is a warning message.")
        assertTrue(true)
    }
}
