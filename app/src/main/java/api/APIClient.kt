package api

import android.content.Context
import com.google.gson.GsonBuilder
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import serializer.DateJSONParser
import utils.Configuration
import utils.Configuration.Companion.TOKEN
import utils.ViewUtils
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object APIClient {

    // constants
    private const val TIMEOUT_SECONDS = 500L

    // variables
    private var config: Configuration? = null

    private var retrofit: Retrofit? = null
    private var maximoInstance: Retrofit? = null
    private var unsafeRetrofit: Retrofit? = null

    // instance gson converter factory in a lazy way
    private val gsonConverter by lazy {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.registerTypeAdapter(Date::class.java, DateJSONParser())
        val gson = gsonBuilder
            .setLenient()
            .create()
        GsonConverterFactory.create(gson)
    }

    // instance interceptor in a lazy way
    private val interceptor by lazy {
        HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { log -> ViewUtils.log(log) })
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    // instance unsafe okhttp client in a lazy way
    private val unsafeOkHttpClient by lazy {

        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            @Throws(CertificateException::class)
            @Suppress("TrustAllX509TrustManager")
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            @Throws(CertificateException::class)
            @Suppress("TrustAllX509TrustManager")
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        })
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())

        val connectionSpec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS).build()

        OkHttpClient.Builder()
            .connectionSpecs(listOf(connectionSpec))


            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
            .hostnameVerifier { _, _ -> true }
            .addInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .header("Authorization", "Assertion " + config?.getString(TOKEN))
                    .method(original.method(), original.body())
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(interceptor)
            .build()
    }

    // instance okhttp client in a lazy way
    private val okHttpClient by lazy {
        val connectionSpec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS).build()

        OkHttpClient.Builder()
            .connectionSpecs(listOf(connectionSpec))
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .header("Authorization", "Assertion " + config?.getString(TOKEN))
                    .method(original.method(), original.body())
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(interceptor)
            .build()
    }

    @Synchronized
    fun getInstance(context: Context): Retrofit {
        if (retrofit == null) {
            config = Configuration(context)
            retrofit = Retrofit.Builder()
                .baseUrl("URL")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(gsonConverter)
                .client(okHttpClient)
                .build()
        }
        return retrofit!!
    }

    @Synchronized
    fun getUnsafeInstance(context: Context): Retrofit {
        if (unsafeRetrofit == null) {
            config = Configuration(context)
            unsafeRetrofit = Retrofit.Builder()
                .baseUrl("url")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(gsonConverter)
                .client(unsafeOkHttpClient)
                .build()
        }
        return unsafeRetrofit!!
    }

}