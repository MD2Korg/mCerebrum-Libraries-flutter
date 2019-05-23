package org.md2k.core.cerebralcortex.cerebralcortexwebapi.utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit object used for constructing HTTP request calls.
 */
public class RetrofitClient {

    /**
     * Declare a retrofit variable.
     */
    private static Retrofit retrofit = null;

    /**
     * Constructs a new <code>retrofit</code> object with the proper URL for the request call.
     *
     * @param baseUrl URL to request.
     * @return The constructed <code>retrofit</code> object.
     */
    public static Retrofit getClient(String baseUrl) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}