package com.example.breezil.pixxo.api;

import android.support.annotation.Nullable;

import com.example.breezil.pixxo.model.ImagesResult;

import java.util.Collections;

import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.example.breezil.pixxo.BuildConfig.BASE_URL;

public class DemoClient {

    public static RestApiInterface apiInterface;
    public static RestApiInterface getClient() {

        if (apiInterface == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            // set your desired log level
            //logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);


            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.interceptors().add(logging);
            builder.addInterceptor(logging);
            OkHttpClient httpClient = builder
                    .connectionSpecs(Collections.singletonList(getSpec()))
                    .addInterceptor(logging)
                    .build();


            Retrofit client = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)

                    .build();
            apiInterface = client.create(RestApiInterface.class);
        }
        return apiInterface;
    }

    public interface RestApiInterface{

        @GET("api/")
        Call<ImagesResult> getImages(@Query("key")@Nullable String key,
                                     @Query("q")@Nullable String search,
                                     @Query("category")@Nullable String category,
                                       @Query("page")int page,
                                       @Query("per_page")int per_page
        );
    }

    private static ConnectionSpec getSpec(){
        return new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)
                .cipherSuites(
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
                .build();
    }
}
