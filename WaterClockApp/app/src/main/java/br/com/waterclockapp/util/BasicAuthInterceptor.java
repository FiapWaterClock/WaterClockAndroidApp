package br.com.waterclockapp.util;


import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class BasicAuthInterceptor implements Interceptor {

    private String credentials;

    public BasicAuthInterceptor(String user, String password) {
        this.credentials = Credentials.basic(user, password);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        //final String basic = "Basic " + credentials;
        Request original = chain.request();

        Request.Builder requestBuilder = original.newBuilder()
                .header("Authorization", credentials);
        //requestBuilder.header("Accept", "application/json");
        requestBuilder.method(original.method(),original.body());


        Request request = requestBuilder.build();
        return chain.proceed(request);
    }

}