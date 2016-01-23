package com.github.bumblebee.command.brent.meduza;

import com.github.bumblebee.command.brent.meduza.response.MeduzaStockResponse;
import feign.Client;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.slf4j.Slf4jLogger;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.X509Certificate;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

@Service
public class MeduzaStockProvider {

    private final MeduzaApi client;

    public MeduzaStockProvider() {

        this.client = Feign.builder()
                .decoder(new GsonDecoder())
                .logLevel(feign.Logger.Level.BASIC)
                .logger(new Slf4jLogger())
                .client(new Client.Default(allTrustSocketFactory(), (hostname, session) -> true))
                .target(MeduzaApi.class, MeduzaApi.API_ROOT);
    }

    SSLSocketFactory allTrustSocketFactory() {

        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws CertificateException {

                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws CertificateException {

                    }

                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
        };

        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            return sc.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }

    public MeduzaStockResponse getCurrentStocks() {

        return this.client.queryStock();
    }
}
