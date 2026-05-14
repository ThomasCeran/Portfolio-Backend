⚠️ Ce projet est en cours de développement, certaines fonctionnalités sont encore en construction.

# 🌟 Portfolio Backend | Spring Boot + React

Bienvenue dans mon projet de **portfolio full-stack**, développé avec **Spring Boot** pour le backend et **React.js** pour le frontend. L'objectif est de créer une plateforme qui présente mes compétences et mes projets en tant que développeur backend, avec une interface moderne et intuitive.

---

## 🚀 Technologies utilisées

### **Backend** (Spring Boot)
- Java + Spring Boot
- API REST sécurisée avec **JWT**
- Base de données **PostgreSQL**
- Gestion des utilisateurs et rôles
- Tests unitaires avec **JUnit & Mockito**
- Documentation API avec **Swagger (SpringDoc OpenAPI)**
- Déploiement prévu avec **Docker & CI/CD**

## Configuration reCAPTCHA

Le endpoint public `POST /api/messages` attend le token reCAPTCHA v2 checkbox dans le champ JSON `recaptcha`.
Le backend vérifie ce token côté serveur auprès de Google avant d'enregistrer le message.

En production, définir la clé secrète serveur avec l'une de ces configurations :

```properties
GOOGLE_RECAPTCHA_SECRET=your-google-recaptcha-secret
```

ou :

```properties
recaptcha.secret=your-google-recaptcha-secret
```

Ne jamais exposer cette clé au frontend.

