# 🚚 Backend DeliveryController - Code Complet Requis

## ⚠️ IMPORTANT
Le frontend est prêt et fonctionnel. Il essaie d'appeler ces endpoints dans votre backend.
Vous devez créer ce controller dans votre backend Spring Boot.

---

## 📁 Structure Backend Requise

```
src/main/java/com/b2b/
├── controller/
│   └── DeliveryController.java
├── service/
│   └── DeliveryService.java
├── repository/
│   └── DeliveryRepository.java
└── model/
    ├── Delivery.java
    ├── DeliveryStatus.java (enum)
    └── ShippingAddress.java
```

---

## 🔧 1. Model - Delivery.java

```java
package com.b2b.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "deliveries")
@Data
public class Delivery {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long orderId;
    
    @Column(nullable = false)
    private String carrier; // Ex: "Maroc Poste", "Jumia Express"
    
    @Column(unique = true, nullable = false)
    private String trackingNumber;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus status = DeliveryStatus.PENDING;
    
    @Embedded
    private ShippingAddress shippingAddress;
    
    @Column(nullable = false)
    private Double shippingCost;
    
    private LocalDateTime estimatedDeliveryDate;
    private LocalDateTime actualDeliveryDate;
    
    @Column(length = 500)
    private String notes;
    
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
}
```

---

## 🔧 2. Enum - DeliveryStatus.java

```java
package com.b2b.model;

public enum DeliveryStatus {
    PENDING("En attente"),
    IN_TRANSIT("En cours"),
    DELIVERED("Livrée"),
    RETURNED("Retournée"),
    CANCELLED("Annulée");
    
    private final String displayName;
    
    DeliveryStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
```

---

## 🔧 3. Embeddable - ShippingAddress.java

```java
package com.b2b.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class ShippingAddress {
    private String recipientName;
    private String fullAddress;
    private String city;
    private String postalCode;
    private String phoneNumber;
}
```

---

## 🔧 4. Repository - DeliveryRepository.java

```java
package com.b2b.repository;

import com.b2b.model.Delivery;
import com.b2b.model.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    
    List<Delivery> findByOrderId(Long orderId);
    
    List<Delivery> findByStatus(DeliveryStatus status);
    
    Optional<Delivery> findByTrackingNumber(String trackingNumber);
}
```

---

## 🔧 5. Service - DeliveryService.java

```java
package com.b2b.service;

import com.b2b.model.Delivery;
import com.b2b.model.DeliveryStatus;
import com.b2b.repository.DeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class DeliveryService {
    
    @Autowired
    private DeliveryRepository deliveryRepository;
    
    // Tarifs de livraison par ville (exemple pour le Maroc)
    private static final Map<String, Double> CITY_SHIPPING_COSTS = new HashMap<>();
    
    static {
        // Grandes villes - Tarif standard
        CITY_SHIPPING_COSTS.put("Casablanca", 25.0);
        CITY_SHIPPING_COSTS.put("Rabat", 25.0);
        CITY_SHIPPING_COSTS.put("Marrakech", 30.0);
        CITY_SHIPPING_COSTS.put("Fès", 30.0);
        CITY_SHIPPING_COSTS.put("Tanger", 35.0);
        CITY_SHIPPING_COSTS.put("Agadir", 40.0);
        CITY_SHIPPING_COSTS.put("Meknès", 30.0);
        CITY_SHIPPING_COSTS.put("Oujda", 45.0);
        CITY_SHIPPING_COSTS.put("Kenitra", 25.0);
        CITY_SHIPPING_COSTS.put("Tétouan", 35.0);
        CITY_SHIPPING_COSTS.put("Safi", 35.0);
        CITY_SHIPPING_COSTS.put("Mohammedia", 25.0);
        
        // Tarif par défaut pour les autres villes
        CITY_SHIPPING_COSTS.put("DEFAULT", 50.0);
    }
    
    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll();
    }
    
    public Delivery getDeliveryById(Long id) {
        return deliveryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Livraison non trouvée avec l'ID: " + id));
    }
    
    public List<Delivery> getDeliveriesByOrderId(Long orderId) {
        return deliveryRepository.findByOrderId(orderId);
    }
    
    public List<Delivery> getDeliveriesByStatus(DeliveryStatus status) {
        return deliveryRepository.findByStatus(status);
    }
    
    public Delivery trackDelivery(String trackingNumber) {
        return deliveryRepository.findByTrackingNumber(trackingNumber)
            .orElseThrow(() -> new RuntimeException("Aucune livraison trouvée avec le numéro: " + trackingNumber));
    }
    
    @Transactional
    public Delivery createDelivery(Delivery delivery) {
        // Générer un numéro de suivi unique
        if (delivery.getTrackingNumber() == null || delivery.getTrackingNumber().isEmpty()) {
            delivery.setTrackingNumber(generateTrackingNumber());
        }
        
        // Calculer les frais de livraison si non fournis
        if (delivery.getShippingCost() == null || delivery.getShippingCost() == 0) {
            String city = delivery.getShippingAddress().getCity();
            delivery.setShippingCost(calculateShippingCost(city));
        }
        
        // Date estimée de livraison (3-5 jours ouvrables)
        if (delivery.getEstimatedDeliveryDate() == null) {
            delivery.setEstimatedDeliveryDate(LocalDateTime.now().plusDays(4));
        }
        
        delivery.setCreatedAt(LocalDateTime.now());
        delivery.setUpdatedAt(LocalDateTime.now());
        
        return deliveryRepository.save(delivery);
    }
    
    @Transactional
    public Delivery updateDeliveryStatus(Long id, DeliveryStatus newStatus) {
        Delivery delivery = getDeliveryById(id);
        delivery.setStatus(newStatus);
        delivery.setUpdatedAt(LocalDateTime.now());
        
        // Si livré, définir la date de livraison réelle
        if (newStatus == DeliveryStatus.DELIVERED) {
            delivery.setActualDeliveryDate(LocalDateTime.now());
        }
        
        return deliveryRepository.save(delivery);
    }
    
    @Transactional
    public Delivery updateDelivery(Long id, Delivery updatedDelivery) {
        Delivery delivery = getDeliveryById(id);
        
        delivery.setCarrier(updatedDelivery.getCarrier());
        delivery.setShippingAddress(updatedDelivery.getShippingAddress());
        delivery.setShippingCost(updatedDelivery.getShippingCost());
        delivery.setEstimatedDeliveryDate(updatedDelivery.getEstimatedDeliveryDate());
        delivery.setNotes(updatedDelivery.getNotes());
        delivery.setUpdatedAt(LocalDateTime.now());
        
        return deliveryRepository.save(delivery);
    }
    
    public Double calculateShippingCost(String city) {
        String normalizedCity = city.trim();
        return CITY_SHIPPING_COSTS.getOrDefault(normalizedCity, CITY_SHIPPING_COSTS.get("DEFAULT"));
    }
    
    private String generateTrackingNumber() {
        return "TRK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
```

---

## 🔧 6. Controller - DeliveryController.java ⭐ PRINCIPAL

```java
package com.b2b.controller;

import com.b2b.model.Delivery;
import com.b2b.model.DeliveryStatus;
import com.b2b.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/deliveries")
@CrossOrigin(origins = "*") // Pour permettre les requêtes depuis le frontend
public class DeliveryController {
    
    @Autowired
    private DeliveryService deliveryService;
    
    /**
     * GET /api/deliveries
     * Récupère toutes les livraisons
     */
    @GetMapping
    public ResponseEntity<List<Delivery>> getAllDeliveries() {
        List<Delivery> deliveries = deliveryService.getAllDeliveries();
        return ResponseEntity.ok(deliveries);
    }
    
    /**
     * GET /api/deliveries/{id}
     * Récupère une livraison par ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Delivery> getDeliveryById(@PathVariable Long id) {
        try {
            Delivery delivery = deliveryService.getDeliveryById(id);
            return ResponseEntity.ok(delivery);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * GET /api/deliveries/order/{orderId}
     * Récupère toutes les livraisons d'une commande
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<Delivery>> getDeliveriesByOrderId(@PathVariable Long orderId) {
        List<Delivery> deliveries = deliveryService.getDeliveriesByOrderId(orderId);
        return ResponseEntity.ok(deliveries);
    }
    
    /**
     * GET /api/deliveries/status/{status}
     * Récupère les livraisons par statut
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Delivery>> getDeliveriesByStatus(@PathVariable String status) {
        try {
            DeliveryStatus deliveryStatus = DeliveryStatus.valueOf(status.toUpperCase());
            List<Delivery> deliveries = deliveryService.getDeliveriesByStatus(deliveryStatus);
            return ResponseEntity.ok(deliveries);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * GET /api/deliveries/track/{trackingNumber}
     * Suit une livraison par numéro de tracking
     */
    @GetMapping("/track/{trackingNumber}")
    public ResponseEntity<Delivery> trackDelivery(@PathVariable String trackingNumber) {
        try {
            Delivery delivery = deliveryService.trackDelivery(trackingNumber);
            return ResponseEntity.ok(delivery);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * GET /api/deliveries/calculate-shipping?city={city}
     * Calcule les frais de livraison selon la ville
     */
    @GetMapping("/calculate-shipping")
    public ResponseEntity<Map<String, Double>> calculateShippingCost(@RequestParam String city) {
        Double cost = deliveryService.calculateShippingCost(city);
        Map<String, Double> response = new HashMap<>();
        response.put("shippingCost", cost);
        return ResponseEntity.ok(response);
    }
    
    /**
     * POST /api/deliveries
     * Crée une nouvelle livraison
     */
    @PostMapping
    public ResponseEntity<Delivery> createDelivery(@RequestBody Delivery delivery) {
        try {
            Delivery createdDelivery = deliveryService.createDelivery(delivery);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDelivery);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * POST /api/deliveries/{id}/status
     * Met à jour le statut d'une livraison
     */
    @PostMapping("/{id}/status")
    public ResponseEntity<Delivery> updateDeliveryStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> statusUpdate) {
        try {
            String statusStr = statusUpdate.get("status");
            DeliveryStatus newStatus = DeliveryStatus.valueOf(statusStr.toUpperCase());
            Delivery updatedDelivery = deliveryService.updateDeliveryStatus(id, newStatus);
            return ResponseEntity.ok(updatedDelivery);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * PUT /api/deliveries/{id}
     * Met à jour une livraison complète
     */
    @PutMapping("/{id}")
    public ResponseEntity<Delivery> updateDelivery(
            @PathVariable Long id,
            @RequestBody Delivery delivery) {
        try {
            Delivery updatedDelivery = deliveryService.updateDelivery(id, delivery);
            return ResponseEntity.ok(updatedDelivery);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
```

---

## 🔧 7. Configuration Security (TRÈS IMPORTANT!)

Dans votre `SecurityConfig.java`, assurez-vous que les endpoints de livraison sont accessibles :

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/deliveries/**").permitAll()  // ✅ IMPORTANT
            .requestMatchers("/api/auth/**").permitAll()
            .anyRequest().authenticated()
        );
    
    return http.build();
}
```

---

## 📊 8. Base de données

Ajoutez dans `application.properties` ou `application.yml` :

```properties
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.datasource.url=jdbc:mysql://localhost:3306/b2b_db
spring.datasource.username=root
spring.datasource.password=votre_password
```

---

## ✅ 9. Tester les Endpoints

Une fois le backend redémarré, testez dans votre navigateur ou Postman :

1. **Liste des livraisons** : `http://localhost:8082/api/deliveries`
2. **Calculer frais** : `http://localhost:8082/api/deliveries/calculate-shipping?city=Casablanca`

---

## 🚀 10. Démarrage

1. **Copiez tous ces fichiers dans votre backend IntelliJ**
2. **Redémarrez le backend Spring Boot**
3. **Le frontend va maintenant fonctionner automatiquement** ✅

---

## 📝 Notes Importantes

- Tous les endpoints retournent du JSON
- Le frontend attend exactement ces noms de champs
- Le calcul des frais est basé sur les villes marocaines
- Le tracking number est généré automatiquement
- Les dates sont au format ISO 8601

---

## 🎯 Résultat Attendu

Après avoir ajouté ce code dans votre backend :
- ✅ Le frontend pourra charger toutes les livraisons
- ✅ Créer de nouvelles livraisons
- ✅ Changer les statuts
- ✅ Calculer les frais selon la ville
- ✅ Suivre les livraisons par numéro

---

**Bonne chance ! 🚀**

