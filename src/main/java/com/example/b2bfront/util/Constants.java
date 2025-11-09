package com.example.b2bfront.util;

public class Constants {

    // Backend API Configuration
    // Le backend a déjà le context path /api, donc on ne le met pas dans l'URL de base
    private static final String DEFAULT_API_BASE_URL = "http://localhost:8082/api";

    // Résolution de l'URL du backend (variable d'environnement > propriété système > défaut)
    public static final String API_BASE_URL = resolveBackendUrl();

    // API Endpoints
    public static final String ENDPOINT_HEALTH = "/health";
    public static final String ENDPOINT_LOGIN = "/auth/login";
    public static final String ENDPOINT_REGISTER = "/auth/register";
    public static final String ENDPOINT_USERS = "/users";

    // Delivery Endpoints
    public static final String ENDPOINT_DELIVERIES = "/deliveries";
    public static final String ENDPOINT_SHIPPING_ADDRESS = "/shipping-addresses";

    // Order Endpoints (pour intégration avec module commandes)
    public static final String ENDPOINT_ORDERS = "/orders";

    // Payment Endpoints (pour intégration avec module paiement)
    public static final String ENDPOINT_PAYMENTS = "/payments";

    // Application Configuration
    public static final String APP_TITLE = "B2B Front Application";
    public static final int APP_WIDTH = 800;
    public static final int APP_HEIGHT = 600;

    private Constants() {
        // Prevent instantiation
    }

    /**
     * Résout l'URL du backend en ordre de priorité:
     * 1. Variable d'environnement BACKEND_URL
     * 2. Propriété système backend.url
     * 3. URL par défaut
     */
    private static String resolveBackendUrl() {
        // Vérifier variable d'environnement
        String envUrl = System.getenv("BACKEND_URL");
        if (envUrl != null && !envUrl.isBlank()) {
            return normalizeUrl(envUrl);
        }

        // Vérifier propriété système
        String sysUrl = System.getProperty("backend.url");
        if (sysUrl != null && !sysUrl.isBlank()) {
            return normalizeUrl(sysUrl);
        }

        // URL par défaut
        return normalizeUrl(DEFAULT_API_BASE_URL);
    }

    /**
     * Normalise l'URL en enlevant le slash final s'il existe
     */
    private static String normalizeUrl(String url) {
        return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
    }

    /**
     * Affiche la configuration actuelle
     */
    public static void printConfiguration() {
        System.out.println("=================================");
        System.out.println("B2B Frontend Configuration");
        System.out.println("=================================");
        System.out.println("Backend URL: " + API_BASE_URL);
        System.out.println("=================================");
    }
}
