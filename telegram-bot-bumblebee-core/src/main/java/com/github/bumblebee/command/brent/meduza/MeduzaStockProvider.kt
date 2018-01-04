package com.github.bumblebee.command.brent.meduza

import feign.Client
import feign.Feign
import feign.gson.GsonDecoder
import feign.slf4j.Slf4jLogger
import org.springframework.stereotype.Service
import java.security.cert.CertificateException
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Service
class MeduzaStockProvider {

    private val client: MeduzaApi = Feign.builder()
            .decoder(GsonDecoder())
            .logLevel(feign.Logger.Level.BASIC)
            .logger(Slf4jLogger())
            .client(Client.Default(allTrustSocketFactory()) { _, _ -> true })
            .target(MeduzaApi::class.java, MeduzaApi.API_ROOT)

    fun getCurrentStocks(): MeduzaStockResponse = this.client.queryStock()

    private fun allTrustSocketFactory(): SSLSocketFactory {

        // Create a trust manager that does not validate certificate chains
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            @Throws(CertificateException::class)
            override fun checkClientTrusted(x509Certificates: Array<java.security.cert.X509Certificate>, s: String) {

            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(x509Certificates: Array<java.security.cert.X509Certificate>, s: String) {

            }

            override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate>? {
                return null
            }
        })

        val sc = SSLContext.getInstance("SSL")
        sc.init(null, trustAllCerts, java.security.SecureRandom())
        return sc.socketFactory
    }
}
