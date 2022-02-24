package kr.loner.domain.usecase.base

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.lang.RuntimeException
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.jvm.Throws

abstract class NonParamCoroutineUseCase<R>(private val coroutineDispatcher: CoroutineDispatcher) {
    suspend operator fun invoke(): Result<R> {
        return try {
            withContext(coroutineDispatcher) {
                execute().let {
                    Result.success(it)
                }
            }
        } catch (e: Exception) {
            Logger.getLogger(NonParamCoroutineUseCase::class.java.simpleName)
                .log(Level.WARNING, "throw error", e)
            Result.failure(e)
        }

    }

    @Throws(RuntimeException::class)
    protected abstract suspend fun execute(): R
}