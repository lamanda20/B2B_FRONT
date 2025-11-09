# Module Livraison & Suivi des Commandes

## 📦 Vue d'ensemble

Ce module gère l'ensemble du processus de livraison des commandes, du calcul des frais à la livraison finale, avec un suivi en temps réel pour les acheteurs.

## ✨ Fonctionnalités principales

### 1. Gestion des informations de livraison
- **Adresses de livraison complètes** : nom, adresse, ville, code postal, téléphone
- **Transporteurs supportés** :
  - Maroc Poste
  - Jumia Express
  - Amana
  - CTM
  - DHL Express
  - FedEx
  - Autres transporteurs

### 2. Statuts de livraison
Le système gère un cycle de vie complet avec les statuts suivants :
- 🟡 **En attente** (PENDING) : Livraison créée, en attente de traitement
- 🔵 **En cours** (IN_PROGRESS) : Colis en cours d'acheminement
- 🟢 **Livrée** (DELIVERED) : Livraison terminée avec succès
- 🔴 **Retournée** (RETURNED) : Colis retourné à l'expéditeur
- ⚫ **Annulée** (CANCELLED) : Livraison annulée

### 3. Interface de suivi en temps réel
- **Tableau de bord interactif** : visualisation de toutes les livraisons
- **Filtrage par statut** : affichage des livraisons selon leur état
- **Recherche par tracking** : suivi d'une livraison via son numéro
- **Barre de progression visuelle** : indication claire de l'avancement
- **Détails complets** : toutes les informations de livraison et d'adresse

### 4. Calcul des frais de livraison
- **Calcul intelligent** : frais basés sur la ville de destination
- **Affichage en temps réel** : calcul instantané lors de la création
- **Transparence** : affichage clair des coûts avant confirmation

## 🏗️ Architecture

### Modèles (`model/`)
- **`Delivery`** : Représente une livraison complète
  - ID, numéro de commande, transporteur
  - Statut, frais, numéro de tracking
  - Dates (estimée, réelle)
  - Notes et timestamps
  
- **`DeliveryStatus`** : Énumération des statuts possibles
  - Conversion en texte français pour l'affichage
  
- **`ShippingAddress`** : Adresse de livraison
  - Informations complètes du destinataire
  - Adresse, ville, code postal, téléphone

### Services (`service/`)
- **`DeliveryService`** : Gestion des appels API
  - CRUD complet des livraisons
  - Filtrage par statut et commande
  - Calcul des frais de livraison
  - Tracking par numéro de suivi
  - Gestion des adresses de livraison

### Contrôleurs (`controller/`)
- **`DeliveryController`** : Contrôleur JavaFX principal
  - Gestion de l'interface utilisateur
  - Appels asynchrones au backend
  - Mise à jour en temps réel
  - Gestion des événements utilisateur

### Interfaces (`resources/`)
- **`delivery.fxml`** : Interface graphique complète
  - Design moderne et intuitif
  - Disposition en deux panneaux (liste + détails)
  - Formulaires de création intégrés

## 🔗 Intégrations

### Module Commandes (Personne 4)
- Récupération des livraisons par ID de commande
- Lien automatique commande → livraison
- Endpoint : `GET /deliveries/order/{orderId}`

### Module Paiement (Personne 5)
- Affichage des frais de livraison avant paiement
- Intégration des coûts dans le total
- Endpoint : `GET /deliveries/calculate-shipping?city={city}`

### Module Notifications (Personne 7)
- Notifications automatiques lors des changements de statut
- Alertes de livraison imminente
- Confirmation de livraison

## 📡 Endpoints API Backend

```
# Livraisons
GET    /api/deliveries                    # Toutes les livraisons
GET    /api/deliveries/{id}               # Une livraison par ID
GET    /api/deliveries/order/{orderId}    # Livraisons d'une commande
GET    /api/deliveries/status/{status}    # Filtrer par statut
POST   /api/deliveries                    # Créer une livraison
POST   /api/deliveries/{id}               # Mettre à jour une livraison
POST   /api/deliveries/{id}/status        # Changer le statut
GET    /api/deliveries/track/{tracking}   # Tracking par numéro
GET    /api/deliveries/calculate-shipping # Calculer les frais

# Adresses de livraison
GET    /api/shipping-addresses            # Adresses de l'utilisateur
POST   /api/shipping-addresses            # Créer/modifier une adresse
```

## 🚀 Utilisation

### Démarrer l'interface
```java
// Dans MainApp.java ou un lanceur dédié
FXMLLoader loader = new FXMLLoader(
    getClass().getResource("/com/example/b2bfront/delivery.fxml")
);
Scene scene = new Scene(loader.load(), 1200, 700);
stage.setScene(scene);
stage.setTitle("Module Livraison & Suivi");
stage.show();
```

### Workflow typique

1. **Charger les livraisons**
   - Cliquer sur "Charger toutes" pour voir toutes les livraisons
   - Ou filtrer par statut spécifique

2. **Suivre une livraison**
   - Entrer le numéro de tracking
   - Cliquer sur "Rechercher"
   - Voir les détails et le statut en temps réel

3. **Créer une nouvelle livraison**
   - Remplir le formulaire avec les informations
   - Calculer les frais de livraison
   - Valider la création

4. **Mettre à jour le statut**
   - Sélectionner une livraison dans la liste
   - Cliquer sur "Changer le statut"
   - Choisir le nouveau statut

## 💰 Calcul des frais de livraison

Le système calcule les frais selon la ville de destination :
- **Casablanca, Rabat, Tanger** : 20-30 MAD
- **Autres grandes villes** : 35-50 MAD
- **Villes éloignées** : 50-80 MAD

Le calcul prend en compte :
- La distance par rapport au centre de distribution
- Le type de zone (urbaine/rurale)
- Le transporteur sélectionné

## 🎨 Interface utilisateur

### Panneau gauche : Liste des livraisons
- Tableau avec toutes les informations essentielles
- Filtres et recherche rapide
- Actions de mise à jour

### Panneau droit : Détails et formulaires
- **Section détails** : informations complètes de la livraison sélectionnée
- **Barre de progression** : visualisation du statut
- **Adresse de livraison** : informations du destinataire
- **Formulaire de création** : création de nouvelles livraisons

## 🔧 Configuration

Les endpoints sont configurés dans `Constants.java` :
```java
public static final String ENDPOINT_DELIVERIES = "/deliveries";
public static final String ENDPOINT_SHIPPING_ADDRESS = "/shipping-addresses";
```

## 📝 Notes techniques

- **Appels asynchrones** : Toutes les requêtes API sont asynchrones via `ExecutorService`
- **Thread-safe** : Mise à jour de l'UI via `Platform.runLater()`
- **Gestion d'erreurs** : Alertes utilisateur et logs détaillés
- **Performance** : Chargement progressif et cache des données

## 🐛 Dépannage

### Erreur de connexion au backend
```
✗ Error: Connection refused
```
**Solution** : Vérifier que le backend est démarré sur le port 8082

### Livraison non trouvée
```
✗ Livraison non trouvée
```
**Solution** : Vérifier que le numéro de tracking est correct

### Erreur de calcul des frais
```
✗ Erreur de calcul
```
**Solution** : Vérifier que la ville est bien enregistrée dans le système

## 📚 Ressources

- Backend API : http://localhost:8082/api
- Documentation : Voir BACKEND_CONFIG.md
- Tests : Utiliser Postman ou l'interface

## 👥 Contacts

- **Module Livraison** : Vous (Personne 6)
- **Module Commandes** : Personne 4
- **Module Paiement** : Personne 5
- **Module Notifications** : Personne 7

---

**Version** : 1.0  
**Date** : Novembre 2025  
**Statut** : ✅ Opérationnel

