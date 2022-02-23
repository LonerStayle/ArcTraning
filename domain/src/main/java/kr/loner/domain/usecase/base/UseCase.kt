package kr.loner.domain.usecase.base

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.lang.RuntimeException
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.jvm.Throws


abstract class UseCase<in P, R>(private val coroutineDispatcher: CoroutineDispatcher) {
    suspend operator fun invoke(param: P): Result<R> {
        return try {
            withContext(coroutineDispatcher) {
                execute(param).let {
                    Result.success(it)
                }
            }
        } catch (e: Exception) {
            Logger.getLogger(UseCase::class.java.simpleName).log(Level.WARNING, "throw error", e)
            Result.failure(e)
        }
    }

    @Throws(RuntimeException::class)
    protected abstract suspend fun execute(param: P): R
}