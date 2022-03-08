package kr.loner.data.remote.interceptor

import kr.loner.data.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthenticationInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val request = original.url.newBuilder().apply {
            addQueryParameter("api_key", BuildConfig.API_KEY)
            addQueryParameter("api_key", BuildConfig.LANGUAGE)
        }.build().let { orgUrlRequest ->
            original.newBuilder().apply { url(orgUrlRequest).build() }.method(original.method, original.body)
        }.build()
        return chain.proceed(request)
    }
}