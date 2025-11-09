# 🚀 Ajout Rapide de l'Endpoint /health au Backend

Pour que le frontend puisse vérifier la connexion, ajoutez ce simple contrôleur dans votre backend :

## Étape 1 : Créer HealthController.java

Créez le fichier dans : `src/main/java/com/b2b/controller/HealthController.java`

```java
package com.b2b.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("message", "Backend is running");
        return ResponseEntity.ok(response);
    }
}
```

## Étape 2 : Redémarrer le backend

Redémarrez votre application Spring Boot dans IntelliJ.

## Étape 3 : Tester

```bash
curl http://localhost:8082/api/health
```

Réponse attendue :
```json
{
  "status": "UP",
  "timestamp": "2025-11-09T03:55:00",
  "message": "Backend is running"
}
```

✅ C'est tout ! Le frontend pourra maintenant se connecter au backend.

