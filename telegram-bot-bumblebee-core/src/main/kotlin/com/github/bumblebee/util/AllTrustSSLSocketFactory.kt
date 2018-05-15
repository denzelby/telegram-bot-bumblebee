package com.github.bumblebee.util

import java.security.cert.CertificateException
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object FeignUtils {

    fun allTrustSocketFactory(): SSLSocketFactory {

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
