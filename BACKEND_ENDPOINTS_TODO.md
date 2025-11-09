# Endpoints Backend à Implémenter

## 🎯 Pour que le module de livraison fonctionne complètement

Votre backend doit implémenter les endpoints suivants :

---

## 📦 Endpoints Livraison (Delivery)

### 1. GET /api/deliveries
**Description** : Récupérer toutes les livraisons  
**Réponse** : Liste de `Delivery[]`

```java
@GetMapping("/deliveries")
public ResponseEntity<List<Delivery>> getAllDeliveries() {
    List<Delivery> deliveries = deliveryService.findAll();
    return ResponseEntity.ok(deliveries);
}
```

---

### 2. GET /api/deliveries/{id}
**Description** : Récupérer une livraison par ID  
**Réponse** : `Delivery`

```java
@GetMapping("/deliveries/{id}")
public ResponseEntity<Delivery> getDeliveryById(@PathVariable Long id) {
    Delivery delivery = deliveryService.findById(id);
    return ResponseEntity.ok(delivery);
}
```

---

### 3. GET /api/deliveries/order/{orderId}
**Description** : Récupérer les livraisons d'une commande  
**Réponse** : `Delivery[]`

```java
@GetMapping("/deliveries/order/{orderId}")
public ResponseEntity<List<Delivery>> getDeliveriesByOrderId(@PathVariable Long orderId) {
    List<Delivery> deliveries = deliveryService.findByOrderId(orderId);
    return ResponseEntity.ok(deliveries);
}
```

---

### 4. GET /api/deliveries/status/{status}
**Description** : Filtrer les livraisons par statut  
**Paramètre** : status = PENDING | IN_PROGRESS | DELIVERED | RETURNED | CANCELLED  
**Réponse** : `Delivery[]`

```java
@GetMapping("/deliveries/status/{status}")
public ResponseEntity<List<Delivery>> getDeliveriesByStatus(@PathVariable String status) {
    DeliveryStatus deliveryStatus = DeliveryStatus.valueOf(status);
    List<Delivery> deliveries = deliveryService.findByStatus(deliveryStatus);
    return ResponseEntity.ok(deliveries);
}
```

---

### 5. POST /api/deliveries
**Description** : Créer une nouvelle livraison  
**Body** : `Delivery` (JSON)  
**Réponse** : `Delivery` créée avec ID et tracking number

```java
@PostMapping("/deliveries")
public ResponseEntity<Delivery> createDelivery(@RequestBody Delivery delivery) {
    Delivery created = deliveryService.create(delivery);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
}
```

**Exemple de body :**
```json
{
  "orderId": 123,
  "shippingAddress": {
    "recipientName": "Mohammed Ali",
    "fullAddress": "123 Rue Hassan II",
    "city": "Casablanca",
    "postalCode": "20000",
    "phoneNumber": "0612345678"
  },
  "carrier": "Maroc Poste",
  "status": "PENDING"
}
```

---

### 6. POST /api/deliveries/{id}/status
**Description** : Mettre à jour le statut d'une livraison  
**Body** : `{ "status": "IN_PROGRESS" }`  
**Réponse** : `Delivery` mise à jour

```java
@PostMapping("/deliveries/{id}/status")
public ResponseEntity<Delivery> updateDeliveryStatus(
        @PathVariable Long id, 
        @RequestBody Map<String, String> statusUpdate) {
    String newStatus = statusUpdate.get("status");
    Delivery updated = deliveryService.updateStatus(id, DeliveryStatus.valueOf(newStatus));
    return ResponseEntity.ok(updated);
}
```

---

### 7. GET /api/deliveries/track/{trackingNumber}
**Description** : Suivre une livraison par numéro de tracking  
**Réponse** : `Delivery`

```java
@GetMapping("/deliveries/track/{trackingNumber}")
public ResponseEntity<Delivery> trackDelivery(@PathVariable String trackingNumber) {
    Delivery delivery = deliveryService.findByTrackingNumber(trackingNumber);
    return ResponseEntity.ok(delivery);
}
```

---

### 8. GET /api/deliveries/calculate-shipping
**Description** : Calculer les frais de livraison selon la ville  
**Paramètre** : city (query parameter)  
**Réponse** : `{ "shippingCost": 25.0 }`

```java
@GetMapping("/deliveries/calculate-shipping")
public ResponseEntity<Map<String, Double>> calculateShippingCost(@RequestParam String city) {
    Double cost = deliveryService.calculateShippingCost(city);
    return ResponseEntity.ok(Map.of("shippingCost", cost));
}
```

**Logique de calcul suggérée :**
```java
public Double calculateShippingCost(String city) {
    city = city.toLowerCase().trim();
    
    // Grandes villes - frais réduits
    if (city.equals("casablanca") || city.equals("rabat") || city.equals("tanger")) {
        return 25.0;
    }
    
    // Villes moyennes
    if (city.equals("marrakech") || city.equals("fes") || city.equals("agadir") || 
        city.equals("oujda") || city.equals("meknes")) {
        return 35.0;
    }
    
    // Autres villes
    return 50.0;
}
```

---

## 📍 Endpoints Adresses de Livraison (Shipping Address)

### 9. GET /api/shipping-addresses
**Description** : Récupérer les adresses de l'utilisateur connecté  
**Réponse** : `ShippingAddress[]`

```java
@GetMapping("/shipping-addresses")
public ResponseEntity<List<ShippingAddress>> getUserShippingAddresses(
        @AuthenticationPrincipal UserDetails userDetails) {
    List<ShippingAddress> addresses = shippingAddressService.findByUser(userDetails.getUsername());
    return ResponseEntity.ok(addresses);
}
```

---

### 10. POST /api/shipping-addresses
**Description** : Créer ou mettre à jour une adresse  
**Body** : `ShippingAddress` (JSON)  
**Réponse** : `ShippingAddress` créée

```java
@PostMapping("/shipping-addresses")
public ResponseEntity<ShippingAddress> saveShippingAddress(@RequestBody ShippingAddress address) {
    ShippingAddress saved = shippingAddressService.save(address);
    return ResponseEntity.ok(saved);
}
```

---

## 🔧 Endpoint de Santé (Health Check)

### Option 1 : Activer Spring Boot Actuator

Dans `pom.xml`, ajouter :
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

Dans `application.properties` :
```properties
management.endpoints.web.exposure.include=health
management.endpoint.health.show-details=always
```

**Endpoint disponible** : `GET /api/actuator/health`

---

### Option 2 : Créer un endpoint personnalisé

```java
@RestController
@RequestMapping("/api")
public class HealthController {
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "timestamp", LocalDateTime.now().toString()
        ));
    }
}
```

---

## 📊 Modèles Backend à Créer

### Delivery.java (Entity)
```java
@Entity
@Table(name = "deliveries")
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long orderId;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shipping_address_id")
    private ShippingAddress shippingAddress;
    
    @Column(nullable = false)
    private String carrier;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus status;
    
    @Column(nullable = false)
    private Double shippingCost;
    
    @Column(unique = true)
    private String trackingNumber;
    
    private LocalDateTime estimatedDeliveryDate;
    private LocalDateTime actualDeliveryDate;
    
    @Column(length = 1000)
    private String notes;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    // Getters et Setters
    
    @PrePersist
    protected void onCreate() {
        this.trackingNumber = generateTrackingNumber();
        this.createdAt = LocalDateTime.now();
        this.estimatedDeliveryDate = LocalDateTime.now().plusDays(3);
    }
    
    private String generateTrackingNumber() {
        return "TRK" + System.currentTimeMillis() + 
               (int)(Math.random() * 1000);
    }
}
```

### DeliveryStatus.java (Enum)
```java
public enum DeliveryStatus {
    PENDING,
    IN_PROGRESS,
    DELIVERED,
    RETURNED,
    CANCELLED
}
```

### ShippingAddress.java (Entity)
```java
@Entity
@Table(name = "shipping_addresses")
public class ShippingAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String recipientName;
    
    @Column(nullable = false)
    private String fullAddress;
    
    @Column(nullable = false)
    private String city;
    
    private String postalCode;
    
    @Column(nullable = false)
    private String phoneNumber;
    
    // Getters et Setters
}
```

---

## 🧪 Tester les Endpoints

### Avec Postman ou curl

```bash
# Test de santé
curl http://localhost:8082/api/health

# Créer une livraison
curl -X POST http://localhost:8082/api/deliveries \
  -H "Content-Type: application/json" \
  -d '{
    "orderId": 1,
    "shippingAddress": {
      "recipientName": "Test User",
      "fullAddress": "123 Test St",
      "city": "Casablanca",
      "postalCode": "20000",
      "phoneNumber": "0612345678"
    },
    "carrier": "Maroc Poste",
    "status": "PENDING"
  }'

# Calculer les frais
curl http://localhost:8082/api/deliveries/calculate-shipping?city=Casablanca

# Tracker une livraison
curl http://localhost:8082/api/deliveries/track/TRK1234567890123
```

---

## 📝 Checklist d'Implémentation

- [ ] Créer les entités : Delivery, ShippingAddress
- [ ] Créer l'enum : DeliveryStatus
- [ ] Créer les repositories : DeliveryRepository, ShippingAddressRepository
- [ ] Créer les services : DeliveryService, ShippingAddressService
- [ ] Créer le contrôleur : DeliveryController
- [ ] Implémenter la logique de calcul des frais
- [ ] Ajouter la génération automatique du tracking number
- [ ] Activer Spring Boot Actuator ou créer /health endpoint
- [ ] Tester tous les endpoints avec Postman
- [ ] Vérifier la connexion avec le frontend

---

**Une fois tous ces endpoints implémentés, le module de livraison frontend fonctionnera parfaitement !** 🚀

