package com.example.b2bfront.service;

import com.example.b2bfront.util.Constants;
import okhttp3.*;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ApiService {

    private final OkHttpClient client;
    private final Gson gson;
    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

    // Token d'authentification (sera défini après login)
    private String authToken;

    public ApiService() {
        this.client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .followRedirects(true)
                .followSslRedirects(true)
                .build();
        this.gson = new Gson();

        // Afficher l'URL du backend au démarrage
        System.out.println("ApiService initialized with backend URL: " + Constants.API_BASE_URL);
    }

    public void setAuthToken(String token) {
        this.authToken = token;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String get(String endpoint) throws IOException {
        String url = buildUrl(endpoint);
        System.out.println("GET Request: " + url);

        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json");

        // Ajouter le token d'authentification si disponible
        if (authToken != null && !authToken.isEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer " + authToken);
        }

        return executeRequest(requestBuilder.build());
    }

    public String post(String endpoint, Object body) throws IOException {
        String url = buildUrl(endpoint);
        String json = gson.toJson(body);

        System.out.println("POST Request: " + url);
        System.out.println("Request Body: " + json);

        RequestBody requestBody = RequestBody.create(json, JSON_MEDIA_TYPE);

        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json");

        // Ajouter le token d'authentification si disponible
        if (authToken != null && !authToken.isEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer " + authToken);
        }

        return executeRequest(requestBuilder.build());
    }

    public <T> T get(String endpoint, Class<T> responseType) throws IOException {
        String jsonResponse = get(endpoint);
        return gson.fromJson(jsonResponse, responseType);
    }

    public <T> T post(String endpoint, Object body, Class<T> responseType) throws IOException {
        String jsonResponse = post(endpoint, body);
        return gson.fromJson(jsonResponse, responseType);
    }

    private String executeRequest(Request request) throws IOException {
        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "";

            if (!response.isSuccessful()) {
                String errorMsg = String.format("HTTP %d %s - %s",
                    response.code(),
                    response.message(),
                    responseBody);
                System.err.println("Error: " + errorMsg);

                // Message d'erreur spécifique pour 403
                if (response.code() == 403) {
                    throw new IOException("Accès refusé (403) - Vérifiez la configuration Spring Security du backend. " +
                            "Voir BACKEND_FIX_403.md pour les solutions.");
                }

                throw new IOException(errorMsg);
            }

            System.out.println("Response: " + responseBody);
            return responseBody;
        }
    }

    private String buildUrl(String endpoint) {
        // S'assurer que l'endpoint commence par /
        String cleanEndpoint = endpoint.startsWith("/") ? endpoint : "/" + endpoint;
        return Constants.API_BASE_URL + cleanEndpoint;
    }

    /**
     * Teste la connexion au backend
     */
    public boolean testConnection() {
        try {
            get(Constants.ENDPOINT_HEALTH);
            return true;
        } catch (IOException e) {
            System.err.println("Backend connection test failed: " + e.getMessage());
            return false;
        }
    }
}
