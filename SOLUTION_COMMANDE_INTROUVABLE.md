# 🔍 SOLUTION : Erreur HTTP 500 - Commande introuvable

## ❌ Problème Identifié

Le backend retourne HTTP 500 avec l'erreur :
```
Commande introuvable avec l'ID: 1234
```

**Cause** : Vous essayez de créer une livraison pour une commande (ID: 1234) qui n'existe pas dans la base de données.

---

## ✅ SOLUTION 1 : Créer une commande de test dans le backend (RECOMMANDÉ)

### Dans IntelliJ, créez un script SQL ou utilisez l'API backend pour créer une commande de test :

```sql
-- 1. Créer un utilisateur de test (si pas encore fait)
INSERT INTO users (nom, email, password, adresse, ville, telephone, role)
VALUES ('Test User', 'test@test.com', 'password123', 
        '123 Rue Test', 'Casablanca', '0612345678', 'ACHETEUR');

-- 2. Créer une commande de test
INSERT INTO commandes (ref_commande, statut, date_commande, total, user_id)
VALUES ('CMD-001', 'EN_ATTENTE', NOW(), 100.00, 
        (SELECT id FROM users WHERE email = 'test@test.com'));

-- 3. Vérifier l'ID de la commande créée
SELECT id, ref_commande, statut FROM commandes ORDER BY id DESC LIMIT 1;
```

**Ensuite, utilisez cet ID dans le frontend !**

---

## ✅ SOLUTION 2 : Créer commande + livraison via l'API (POUR TEST)

### Étape 1 : Créer une commande via Postman ou curl

```bash
# Créer une commande
curl -X POST http://localhost:8082/api/commandes \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "items": [
      {
        "productId": 1,
        "quantity": 2,
        "price": 50.00
      }
    ],
    "total": 100.00
  }'
```

### Étape 2 : Utiliser l'ID de la commande retournée

La réponse vous donnera un ID (ex: `"id": 5`). Utilisez cet ID dans le frontend.

---

## ✅ SOLUTION 3 : Modifier le backend pour créer automatiquement une commande (TEMPORAIRE)

Si vous voulez tester rapidement, modifiez temporairement le backend pour créer une commande si elle n'existe pas.

**Dans `DeliveryService.java`, méthode `createDeliveryForOrder` :**

```java
public DeliveryDTO createDeliveryForOrder(Long orderId, String carrier,
                                          Map<String, Object> shippingAddress,
                                          Double shippingCost) {
    // ✅ SOLUTION TEMPORAIRE : Créer une commande de test si elle n'existe pas
    Commande commande = commandeRepository.findById(orderId)
            .orElseGet(() -> {
                // Créer une commande de test
                Commande newCommande = new Commande();
                newCommande.setRefCommande("CMD-TEST-" + orderId);
                newCommande.setStatut(StatutCommande.EN_ATTENTE);
                newCommande.setDateCommande(LocalDate.now());
                newCommande.setTotal(0.0);
                
                // Trouver un utilisateur de test ou en créer un
                User testUser = userRepository.findById(1L)
                        .orElseThrow(() -> new RuntimeException("Créez d'abord un utilisateur dans la BD"));
                newCommande.setUser(testUser);
                
                return commandeRepository.save(newCommande);
            });

    // ... reste du code existant
}
```

---

## ✅ SOLUTION 4 : Lister les commandes disponibles dans le frontend (RECOMMANDÉ)

Je vais modifier le frontend pour afficher une liste déroulante des commandes disponibles au lieu d'un champ texte libre.

**Avantages :**
- L'utilisateur choisit une commande réelle
- Pas d'erreur "commande introuvable"
- Plus professionnel

Voulez-vous que je modifie le frontend pour :
1. Charger la liste des commandes disponibles
2. Afficher un ComboBox au lieu d'un TextField pour le N° Commande
3. Ne permettre la création que si une commande valide est sélectionnée

---

## 🚀 SOLUTION RAPIDE POUR TESTER MAINTENANT

### 1. Dans IntelliJ (Backend), ouvrez la console H2 ou votre DB :

```sql
-- Vérifier les commandes existantes
SELECT id, ref_commande, statut FROM commandes;

-- Si aucune commande, créez-en une :
INSERT INTO commandes (ref_commande, statut, date_commande, total, user_id)
VALUES ('CMD-TEST-001', 'EN_ATTENTE', CURRENT_DATE, 100.00, 1);
```

### 2. Récupérez l'ID de la commande créée :

```sql
SELECT id FROM commandes ORDER BY id DESC LIMIT 1;
```

### 3. Dans le frontend, utilisez cet ID

Par exemple, si l'ID est `3`, entrez `3` dans le champ "N° Commande".

---

## 📊 État Actuel

✅ **Calcul des frais** : Fonctionne parfaitement  
✅ **Envoi des données** : Le frontend envoie tout correctement  
✅ **Backend** : Le code est correct  
❌ **Commande** : La commande avec l'ID saisi n'existe pas dans la BD  

---

## 🎯 Recommandation

**Créez d'abord 2-3 commandes de test dans votre base de données**, puis testez la création de livraison.

**OU** dites-moi si vous voulez que je modifie le frontend pour charger automatiquement les commandes disponibles dans un ComboBox (liste déroulante) !


