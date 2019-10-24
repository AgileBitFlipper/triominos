package com.thirdsonsoftware

class LogTest extends GroovyTestCase {
    void testInfo() {
        Log.Info("This is a log information message.")
        assertTrue(true)
    }

    void testError() {
        Log.Error("This is an error message.")
        assertTrue(true)
    }

    void testDebug() {
        Log.Debug("This is a debug message.")
        assertTrue(true)
    }

    void testWarning() {
        Log.Warning("This is a warning message.")
        assertTrue(true)
    }
}
