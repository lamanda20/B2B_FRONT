# 🎯 Guide d'Intégration des Modules - Plateforme B2B

## 📋 Vue d'ensemble

Ce projet utilise un **shell de navigation centralisé** où chaque membre de l'équipe peut intégrer son module facilement.

---

## 🏗️ Architecture du Shell

### Structure principale
```
main.fxml (Shell)
  ├── Header (Navigation)
  ├── ContentArea (Zone dynamique pour les modules)
  └── Footer (Informations système)
```

### Modules disponibles
1. **🏠 Accueil** - Page d'accueil (intégrée)
2. **📦 Produits** - Module Produits (à intégrer)
3. **🛒 Panier** - Module Panier (à intégrer)
4. **📋 Commandes** - Module Commandes (à intégrer)
5. **💳 Paiements** - Module Paiements (à intégrer)
6. **🚚 Livraisons** - ✅ **Module actif** (déjà intégré)
7. **🔔 Notifications** - Module Notifications (à intégrer)

---

## 🚀 Comment intégrer votre module ?

### Étape 1 : Créer votre fichier FXML

Créez votre interface dans `src/main/resources/com/example/b2bfront/`

**Exemple pour le module Produits :**
```
src/main/resources/com/example/b2bfront/products.fxml
```

### Étape 2 : Créer votre contrôleur

Créez votre contrôleur dans `src/main/java/com/example/b2bfront/controller/`

**Exemple :**
```java
package com.example.b2bfront.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ProductsController {
    
    @FXML
    private TableView<Product> productsTable;
    
    @FXML
    private void initialize() {
        System.out.println("ProductsController initialized");
        // Votre logique d'initialisation
    }
    
    // Vos méthodes...
}
```

### Étape 3 : Intégrer dans MainController

Ouvrez `MainController.java` et trouvez la méthode correspondant à votre module :

**Pour le module Produits :**
```java
@FXML
private void onShowProducts() {
    System.out.println("Navigation: Produits");
    // Décommentez et modifiez cette ligne :
    loadModule("/com/example/b2bfront/products.fxml", "Produits");
    // Supprimez cette ligne :
    // showModuleNotAvailable("Produits");
}
```

### Étape 4 : Compiler et tester

```bash
mvn clean compile
mvn javafx:run
```

---

## 📝 Exemple complet : Module Produits

### 1. products.fxml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<?import javafx.scene.control.*?>
<?import javafx.scene.layout.*?>

<BorderPane xmlns="http://javafx.com/javafx/20" 
            xmlns:fx="http://javafx.com/fxml/1"
            fx:controller="com.example.b2bfront.controller.ProductsController">
    
    <top>
        <HBox spacing="10" style="-fx-padding: 20; -fx-background-color: #16a085;">
            <Label text="📦 Module Produits" 
                   style="-fx-font-size: 24; -fx-text-fill: white; -fx-font-weight: bold;"/>
        </HBox>
    </top>
    
    <center>
        <TableView fx:id="productsTable">
            <columns>
                <TableColumn text="ID" prefWidth="50"/>
                <TableColumn text="Nom" prefWidth="200"/>
                <TableColumn text="Prix" prefWidth="100"/>
            </columns>
        </TableView>
    </center>
    
</BorderPane>
```

### 2. ProductsController.java
```java
package com.example.b2bfront.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;

public class ProductsController {
    
    @FXML
    private TableView<?> productsTable;
    
    @FXML
    private void initialize() {
        System.out.println("✅ Module Produits chargé");
        // Charger les données...
    }
}
```

### 3. Intégration dans MainController.java
```java
@FXML
private void onShowProducts() {
    System.out.println("Navigation: Produits");
    loadModule("/com/example/b2bfront/products.fxml", "Produits");
}
```

---

## 🔗 Utiliser les services existants

### ApiService - Pour les appels backend

```java
import com.example.b2bfront.service.ApiService;

public class ProductsController {
    private ApiService apiService;
    
    @FXML
    private void initialize() {
        apiService = new ApiService();
        loadProducts();
    }
    
    private void loadProducts() {
        executorService.submit(() -> {
            try {
                String response = apiService.get("/products");
                // Traiter la réponse...
                Platform.runLater(() -> {
                    // Mettre à jour l'UI
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
```

### Constants - Endpoints API

Ajoutez vos endpoints dans `Constants.java` :

```java
// Dans Constants.java
public static final String ENDPOINT_PRODUCTS = "/products";
```

---

## 🎨 Design et Style

### Couleurs des modules
- **Produits** : #16a085 (vert turquoise)
- **Panier** : #27ae60 (vert)
- **Commandes** : #f39c12 (orange)
- **Paiements** : #e74c3c (rouge)
- **Livraisons** : #9b59b6 (violet) ✅
- **Notifications** : #e67e22 (orange foncé)

### Styles recommandés
```css
/* Header de module */
-fx-background-color: #VOTRE_COULEUR;
-fx-padding: 20;

/* Titre */
-fx-font-size: 24;
-fx-font-weight: bold;
-fx-text-fill: white;
```

---

## 📡 Intégration avec le Backend

### Endpoints backend requis

Chaque module doit avoir ses propres endpoints :

**Produits :**
- `GET /api/products` - Liste des produits
- `POST /api/products` - Créer un produit
- `PUT /api/products/{id}` - Modifier un produit
- `DELETE /api/products/{id}` - Supprimer un produit

**Panier :**
- `GET /api/cart` - Contenu du panier
- `POST /api/cart/add` - Ajouter au panier
- `DELETE /api/cart/remove/{id}` - Retirer du panier

... etc.

---

## ✅ Checklist d'intégration

Pour intégrer votre module :

- [ ] Créer le fichier FXML dans `resources/com/example/b2bfront/`
- [ ] Créer le contrôleur dans `controller/`
- [ ] Créer les modèles nécessaires dans `model/`
- [ ] Créer le service pour les appels API dans `service/`
- [ ] Ajouter les endpoints dans `Constants.java`
- [ ] Modifier la méthode correspondante dans `MainController.java`
- [ ] Tester la navigation vers votre module
- [ ] Tester les appels API avec le backend
- [ ] Documenter votre module dans un README

---

## 🧪 Tester votre module

### Lancement
```bash
mvn clean compile
mvn javafx:run
```

### Navigation
1. L'application s'ouvre sur l'accueil
2. Cliquez sur le bouton de votre module dans le header
3. Votre module doit se charger dans la zone centrale

### Debug
- Les logs s'affichent dans la console
- Vérifiez les messages "Module X chargé avec succès"
- En cas d'erreur, un message apparaît dans l'UI

---

## 📚 Modules de référence

### ✅ Module Livraisons (Référence complète)

Consultez ces fichiers pour voir un exemple complet :
- `delivery.fxml` - Interface FXML
- `DeliveryController.java` - Contrôleur
- `DeliveryService.java` - Service API
- `Delivery.java`, `DeliveryStatus.java` - Modèles

### 📖 Documentation
- `DELIVERY_MODULE_README.md` - Documentation complète
- `DELIVERY_INTEGRATION_GUIDE.md` - Guide d'intégration

---

## 🤝 Collaboration

### Branches Git recommandées
```
main
├── feature/module-products
├── feature/module-cart
├── feature/module-orders
├── feature/module-payments
├── feature/module-delivery ✅
└── feature/module-notifications
```

### Workflow
1. Créez votre branche : `git checkout -b feature/module-VOTRE_MODULE`
2. Développez votre module
3. Testez l'intégration
4. Push : `git push origin feature/module-VOTRE_MODULE`
5. Créez une Pull Request

---

## 💬 Support

Pour toute question :
- Consultez le module Livraisons comme référence
- Vérifiez les fichiers README dans le projet
- Demandez de l'aide à l'équipe

---

**Version** : 1.0  
**Date** : Novembre 2025  
**Module de référence** : Livraisons ✅

