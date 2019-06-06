package com.igoosd.rpss.configure;

import com.igoosd.common.exception.RoadPricingException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * 2018/3/26.
 */
@Configuration
@Slf4j
public class RestTemplateConfigure {


    @Bean
    public RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(getClientHttpRequestFactory());
        return restTemplate;
    }

    @Bean
    public ClientHttpRequestFactory getClientHttpRequestFactory() {
        OkHttp3ClientHttpRequestFactory factory = new OkHttp3ClientHttpRequestFactory(getHttpClient());
        return factory;
    }


    @Bean
    public OkHttpClient getHttpClient() {
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, getTrustManager(), new SecureRandom());
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .sslSocketFactory(sslContext.getSocketFactory(), getTrustManager()[0])
                    .hostnameVerifier((s, sslSession) -> true).build();

            return okHttpClient;
        } catch (Exception e) {
            throw new RoadPricingException("okHttpClient 初始化异常...");
        }
    }

    private static X509TrustManager[] getTrustManager() {
        return new X509TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }
        };
    }


}
