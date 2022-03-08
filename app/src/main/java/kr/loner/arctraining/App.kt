package kr.loner.arctraining

import android.app.Application
import timber.log.Timber

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement) =
                    "MovieApp://${element.fileName}:${element.lineNumber}#${element.methodName}"
            })
        }
    }
}