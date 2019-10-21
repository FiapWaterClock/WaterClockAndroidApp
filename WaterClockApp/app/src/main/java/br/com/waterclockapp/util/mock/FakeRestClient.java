package br.com.waterclockapp.util.mock;

import java.util.concurrent.TimeUnit;

import br.com.waterclockapp.util.App;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FakeRestClient {
    private static final String TAG = "RestClient";

    public static Retrofit retrofitHttp = null;


    public static FakeApiInterface getApiInterface() {
        if (retrofitHttp == null) {

            try {
                retrofitHttp = new Retrofit.Builder()
                        .baseUrl("http://mockapi")
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(httpClient().build())
                        .build();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return retrofitHttp.create(FakeApiInterface.class);
    }

    /**
     * @returne
     */
    private static OkHttpClient.Builder httpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        // add your other interceptors …
        // add logging as last interceptor
        httpClient.addInterceptor(logging);
        httpClient.connectTimeout(5, TimeUnit.MINUTES);
        httpClient.writeTimeout(5, TimeUnit.MINUTES);
        httpClient.readTimeout(5, TimeUnit.MINUTES);
        httpClient.addInterceptor(new FakeInterceptor(App.getInstance()));
        return httpClient;
    }

}
