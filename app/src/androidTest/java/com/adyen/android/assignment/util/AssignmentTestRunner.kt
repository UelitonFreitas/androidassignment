package com.adyen.android.assignment.util

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.adyen.android.assignment.TestApp

/**
 * Custom runner to disable dependency injection.
 */
class AssignmentTestRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        return super.newApplication(cl, TestApp::class.java.name, context)
    }
}
