package com.example.b2bfront.service;

import com.example.b2bfront.model.Livraison;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class LivraisonService {

    private static final String API_BASE_URL = "http://localhost:8082/api/deliveries";
    private final HttpClient client;
    private final ObjectMapper objectMapper;

    public LivraisonService() {
        this.client = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    /**
     * Appelle l'endpoint GET /api/deliveries/calculate-shipping
     */
    public CompletableFuture<Double> calculateFrais(String ville) {
        String encodedVille = URLEncoder.encode(ville, StandardCharsets.UTF_8);
        String url = API_BASE_URL + "/calculate-shipping?city=" + encodedVille;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(jsonBody -> {
                    try {
                        Map<String, Object> responseMap = objectMapper.readValue(jsonBody, new TypeReference<>() {});
                        Object fraisObj = responseMap.get("shippingCost");
                        if (fraisObj == null) {
                            fraisObj = responseMap.get("frais");
                        }
                        if (fraisObj instanceof Number) {
                            return ((Number) fraisObj).doubleValue();
                        }
                        throw new RuntimeException("Clé 'shippingCost' ou 'frais' non trouvée ou invalide.");
                    } catch (Exception e) {
                        throw new RuntimeException("Impossible de parser la réponse (JSON invalide ?): " + e.getMessage() + " [Réponse: " + jsonBody + "]", e);
                    }
                });
    }

    /**
     * Appelle l'endpoint POST /api/deliveries
     */
    public CompletableFuture<Livraison> createLivraison(Livraison livraison) {
        try {
            String jsonBody = objectMapper.writeValueAsString(livraison);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE_URL))
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> {
                        String body = response.body();
                        int statusCode = response.statusCode();

                        try {
                            if (statusCode >= 200 && statusCode < 300) {
                                return objectMapper.readValue(body, Livraison.class);
                            } else {
                                Map<String, Object> errorMap = objectMapper.readValue(body, new TypeReference<>() {});
                                String errorMessage = (String) errorMap.getOrDefault("message", "Erreur inconnue du backend.");
                                throw new RuntimeException(errorMessage);
                            }
                        } catch (Exception e) {
                            throw new RuntimeException("Impossible de parser la réponse: " + e.getMessage() + " [Body: " + body + "]");
                        }
                    });

        } catch (Exception e) {
            return CompletableFuture.failedFuture(new RuntimeException("Impossible de créer le JSON: " + e.getMessage(), e));
        }
    }

    /**
     * NOUVELLE MÉTHODE
     * Met à jour le transporteur d'une livraison existante.
     * Appelle PUT /api/deliveries/{id}/transporteur
     */
    public CompletableFuture<Void> updateTransporteur(Long livraisonId, String transporteur) {

        String url = API_BASE_URL + "/" + livraisonId + "/transporteur";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "text/plain") // On envoie juste du texte brut
                .PUT(HttpRequest.BodyPublishers.ofString(transporteur))
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    if (response.statusCode() >= 200 && response.statusCode() < 300) {
                        return null; // Succès, on ne retourne rien (Void)
                    } else {
                        // Gérer l'erreur
                        throw new RuntimeException("Échec de la mise à jour du transporteur: " + response.body());
                    }
                });
    }
}