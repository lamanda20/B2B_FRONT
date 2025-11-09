# Guide d'Intégration - Module Livraison

## 🔗 Pour les autres développeurs

Ce document explique comment intégrer le module de livraison avec les autres modules du projet B2B.

---

## 📦 Pour le Module Commandes (Personne 4)

### Après création d'une commande

Lorsqu'une commande est créée et payée, vous pouvez créer automatiquement une livraison :

```java
import com.example.b2bfront.service.DeliveryService;
import com.example.b2bfront.model.Delivery;
import com.example.b2bfront.model.ShippingAddress;
import com.example.b2bfront.model.DeliveryStatus;

// Après création de la commande
Long orderId = createdOrder.getId();

// Créer l'adresse de livraison
ShippingAddress address = new ShippingAddress();
address.setRecipientName("Nom du client");
address.setFullAddress("123 Rue Example");
address.setCity("Casablanca");
address.setPostalCode("20000");
address.setPhoneNumber("0612345678");

// Créer la livraison
Delivery delivery = new Delivery();
delivery.setOrderId(orderId);
delivery.setShippingAddress(address);
delivery.setCarrier("Maroc Poste");
delivery.setStatus(DeliveryStatus.PENDING);

// Appel API
DeliveryService deliveryService = new DeliveryService(apiService);
Delivery created = deliveryService.createDelivery(delivery);

System.out.println("Livraison créée: " + created.getTrackingNumber());
```

### Afficher les livraisons d'une commande

```java
// Dans votre interface de détails de commande
Long orderId = 123L;
List<Delivery> deliveries = deliveryService.getDeliveriesByOrderId(orderId);

// Afficher dans votre UI
for (Delivery delivery : deliveries) {
    System.out.println("Statut: " + delivery.getStatus().getDisplayName());
    System.out.println("Tracking: " + delivery.getTrackingNumber());
}
```

---

## 💳 Pour le Module Paiement (Personne 5)

### Calculer les frais de livraison avant paiement

```java
import com.example.b2bfront.service.DeliveryService;

// Avant de finaliser le paiement
String city = "Rabat"; // Ville de livraison
DeliveryService deliveryService = new DeliveryService(apiService);

// Calculer les frais
Double shippingCost = deliveryService.calculateShippingCost(city);

// Ajouter au total
Double orderTotal = 500.0; // Total des articles
Double totalWithShipping = orderTotal + shippingCost;

System.out.println("Total commande: " + orderTotal + " MAD");
System.out.println("Frais de livraison: " + shippingCost + " MAD");
System.out.println("Total à payer: " + totalWithShipping + " MAD");
```

### Afficher les frais dans votre interface

```java
// Dans votre FXML ou contrôleur de paiement
@FXML
private Label lblShippingCost;

// Appel asynchrone
executorService.submit(() -> {
    try {
        Double cost = deliveryService.calculateShippingCost(selectedCity);
        Platform.runLater(() -> {
            lblShippingCost.setText(String.format("%.2f MAD", cost));
        });
    } catch (Exception e) {
        e.printStackTrace();
    }
});
```

---

## 🔔 Pour le Module Notifications (Personne 7)

### Écouter les changements de statut

Vous devez surveiller les changements de statut pour envoyer des notifications :

```java
// Méthode à appeler après chaque mise à jour de statut
public void onDeliveryStatusChanged(Delivery delivery, DeliveryStatus oldStatus, DeliveryStatus newStatus) {
    String message = "";
    
    switch (newStatus) {
        case PENDING:
            message = "Votre livraison est en attente de traitement";
            break;
        case IN_PROGRESS:
            message = "Votre colis est en cours de livraison! Tracking: " + delivery.getTrackingNumber();
            break;
        case DELIVERED:
            message = "Votre colis a été livré avec succès!";
            break;
        case RETURNED:
            message = "Votre colis a été retourné. Contactez le service client.";
            break;
        case CANCELLED:
            message = "La livraison a été annulée.";
            break;
    }
    
    // Envoyer la notification
    notificationService.sendNotification(delivery.getOrderId(), message);
}
```

### Notifications recommandées

1. **Création de livraison** : "Votre commande #X est en cours de préparation"
2. **En transit** : "Votre colis est en route! Suivi: [tracking]"
3. **Proche de la livraison** : "Votre colis arrive aujourd'hui!"
4. **Livré** : "Colis livré! Merci pour votre commande"
5. **Problème** : "Un problème est survenu avec votre livraison"

---

## 🎨 Intégrer l'interface dans votre application

### Option 1: Intégration complète dans MainApp

Modifier `MainApp.java` pour inclure un menu de navigation :

```java
// Ajouter un menu dans main.fxml
<MenuBar>
    <Menu text="Modules">
        <MenuItem text="Livraisons" onAction="#onOpenDeliveryModule"/>
        <MenuItem text="Commandes" onAction="#onOpenOrderModule"/>
        <MenuItem text="Paiements" onAction="#onOpenPaymentModule"/>
    </Menu>
</MenuBar>

// Dans MainController.java
@FXML
private void onOpenDeliveryModule() {
    try {
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/com/example/b2bfront/delivery.fxml")
        );
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root, 1200, 700));
        stage.setTitle("Module Livraison");
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```

### Option 2: Intégration dans un TabPane

```xml
<!-- Dans main.fxml -->
<TabPane>
    <Tab text="Accueil">
        <fx:include source="home.fxml"/>
    </Tab>
    <Tab text="Commandes">
        <fx:include source="orders.fxml"/>
    </Tab>
    <Tab text="Livraisons">
        <fx:include source="delivery.fxml"/>
    </Tab>
    <Tab text="Paiements">
        <fx:include source="payment.fxml"/>
    </Tab>
</TabPane>
```

### Option 3: Utilisation du lanceur dédié

Pour tester indépendamment :
```bash
mvn javafx:run -Djavafx.mainClass=com.example.b2bfront.DeliveryApp
```

---

## 📡 Endpoints API à connaître

### Depuis votre module vers Livraison

```
POST   /api/deliveries                    # Créer une livraison
GET    /api/deliveries/order/{orderId}    # Livraisons d'une commande
GET    /api/deliveries/calculate-shipping # Calculer frais
POST   /api/deliveries/{id}/status        # Changer statut
```

### Format des requêtes

**Créer une livraison :**
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

**Changer le statut :**
```json
{
    "status": "IN_PROGRESS"
}
```

---

## 🔄 Workflow complet d'une commande

```
1. [Module Commandes] - Création de la commande
   ↓
2. [Module Paiement] - Calcul des frais de livraison
   ↓ (calculateShippingCost)
3. [Module Paiement] - Paiement effectué
   ↓
4. [Module Livraison] - Création automatique de la livraison
   ↓ (createDelivery)
5. [Module Notifications] - Notification "Commande confirmée"
   ↓
6. [Module Livraison] - Mise à jour du statut (PENDING → IN_PROGRESS)
   ↓ (updateDeliveryStatus)
7. [Module Notifications] - Notification "En cours de livraison"
   ↓
8. [Module Livraison] - Mise à jour du statut (IN_PROGRESS → DELIVERED)
   ↓ (updateDeliveryStatus)
9. [Module Notifications] - Notification "Livraison effectuée"
```

---

## 🛠️ Classes utilitaires à utiliser

### DeliveryService - Méthodes principales

```java
// Récupérer toutes les livraisons
List<Delivery> getAllDeliveries()

// Récupérer une livraison par ID
Delivery getDeliveryById(Long id)

// Livraisons d'une commande
List<Delivery> getDeliveriesByOrderId(Long orderId)

// Filtrer par statut
List<Delivery> getDeliveriesByStatus(DeliveryStatus status)

// Créer une livraison
Delivery createDelivery(Delivery delivery)

// Mettre à jour le statut
Delivery updateDeliveryStatus(Long deliveryId, DeliveryStatus newStatus)

// Calculer les frais
Double calculateShippingCost(String city)

// Tracker une livraison
Delivery trackDelivery(String trackingNumber)
```

---

## ⚠️ Points d'attention

1. **Thread Safety** : Toujours utiliser `Platform.runLater()` pour mettre à jour l'UI
2. **Gestion d'erreurs** : Toutes les méthodes peuvent lancer `IOException`
3. **Token Auth** : Assurez-vous que le token est configuré dans `ApiService`
4. **Validation** : Validez les données avant de créer une livraison
5. **Backend** : Le backend doit être démarré sur le port 8082

---

## 🧪 Exemples de tests

```java
// Test de création de livraison
@Test
public void testCreateDelivery() throws IOException {
    ApiService apiService = new ApiService();
    DeliveryService deliveryService = new DeliveryService(apiService);
    
    ShippingAddress address = new ShippingAddress();
    address.setRecipientName("Test User");
    address.setCity("Casablanca");
    address.setFullAddress("123 Test St");
    address.setPhoneNumber("0600000000");
    
    Delivery delivery = new Delivery();
    delivery.setOrderId(1L);
    delivery.setShippingAddress(address);
    delivery.setCarrier("Maroc Poste");
    delivery.setStatus(DeliveryStatus.PENDING);
    
    Delivery created = deliveryService.createDelivery(delivery);
    assertNotNull(created.getId());
    assertNotNull(created.getTrackingNumber());
}
```

---

## 📞 Support

Pour toute question ou problème d'intégration, contactez le responsable du module Livraison.

**Endpoints backend** : Vérifiez que votre backend implémente tous les endpoints listés  
**Documentation API** : Consultez le README du backend pour plus de détails

---

**Version** : 1.0  
**Dernière mise à jour** : Novembre 2025

