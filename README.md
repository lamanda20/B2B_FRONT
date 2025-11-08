# B2B Frontend - JavaFX Application

## Description
Application frontend JavaFX pour communiquer avec le backend Spring Boot B2B.

## Structure du projet
```
B2B_FRONT/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── module-info.java
│   │   │   └── com/example/b2bfront/
│   │   │       ├── MainApp.java              # Point d'entrée de l'application
│   │   │       ├── controller/
│   │   │       │   └── MainController.java   # Contrôleur principal
│   │   │       ├── model/
│   │   │       │   └── User.java             # Modèle User
│   │   │       ├── service/
│   │   │       │   ├── ApiService.java       # Service HTTP pour API calls
│   │   │       │   └── UserService.java      # Service pour gestion des users
│   │   │       └── util/
│   │   │           └── Constants.java        # Constantes de l'application
│   │   └── resources/
│   │       └── com/example/b2bfront/
│   │           └── main.fxml                 # Interface FXML principale
```

## Technologies
- **JavaFX 20** - Framework UI
- **OkHttp 4.11.0** - Client HTTP
- **Gson 2.10.1** - Parsing JSON
- **Maven** - Gestion des dépendances

## Configuration
L'URL du backend est configurée dans `Constants.java`:
```java
public static final String API_BASE_URL = "http://localhost:8080/api";
```

## Lancer l'application

### Avec Maven
```bash
mvn clean javafx:run
```

### Ou compiler et exécuter
```bash
mvn clean compile
mvn javafx:run
```

## Fonctionnalités
- Interface graphique JavaFX
- Communication avec le backend Spring Boot via API REST
- Gestion des utilisateurs (CRUD)
- Appels HTTP asynchrones pour ne pas bloquer l'interface

## Prérequis
- Java 17 ou supérieur
- Maven 3.6+
- Backend Spring Boot lancé sur http://localhost:8080

