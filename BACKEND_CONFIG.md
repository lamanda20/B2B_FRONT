# Configuration de la connexion au Backend

## Configuration par défaut
Par défaut, le frontend se connecte à : `http://localhost:8082/api`

## Méthodes de configuration

### 1. Variable d'environnement (Recommandé pour production)
```bash
# Windows PowerShell
$env:BACKEND_URL="http://192.168.1.100:8082/api"
mvn javafx:run

# Windows CMD
set BACKEND_URL=http://192.168.1.100:8082/api
mvn javafx:run

# Linux/Mac
export BACKEND_URL=http://192.168.1.100:8082/api
mvn javafx:run
```

### 2. Propriété système Java
```bash
mvn javafx:run -Dbackend.url=http://192.168.1.100:8082/api
```

### 3. Modifier le fichier Constants.java
Éditer `src/main/java/com/example/b2bfront/util/Constants.java`:
```java
private static final String DEFAULT_API_BASE_URL = "http://VOTRE_IP:8082/api";
```

## Ordre de priorité
1. Variable d'environnement `BACKEND_URL` (priorité la plus haute)
2. Propriété système `-Dbackend.url`
3. URL par défaut dans Constants.java

## Endpoints disponibles
- `/health` - Vérifier la santé du backend
- `/auth/login` - Connexion
- `/auth/register` - Inscription
- `/users` - Gestion des utilisateurs

## Logs
L'application affiche automatiquement:
- L'URL du backend utilisée au démarrage
- Chaque requête HTTP (URL + méthode)
- Les réponses et erreurs

## Exemple d'utilisation

```bash
# Lancer avec backend local
mvn javafx:run

# Lancer avec backend distant
mvn javafx:run -Dbackend.url=http://192.168.1.50:8082/api

# Ou avec variable d'environnement
$env:BACKEND_URL="http://monserveur.com/api"
mvn javafx:run
```

## Test de connexion
Cliquez sur le bouton "Ping Backend" dans l'interface pour tester la connexion.
- ✓ Vert = Connexion réussie
- ✗ Rouge = Erreur de connexion
