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

## Configuration Telegram

Le backend peut envoyer une notification Telegram après l'enregistrement réussi d'un message de contact.
Cette notification est optionnelle : si Telegram est désactivé ou mal configuré, le message reste enregistré en base.

Variables d'environnement côté backend :

```properties
TELEGRAM_NOTIFICATIONS_ENABLED=true
TELEGRAM_BOT_TOKEN=your-telegram-bot-token
TELEGRAM_CHAT_ID=your-telegram-chat-id
```

Équivalents Spring possibles :

```properties
telegram.notifications-enabled=true
telegram.bot-token=your-telegram-bot-token
telegram.chat-id=your-telegram-chat-id
```

Mise en place :

1. Créer un bot Telegram avec `@BotFather`.
2. Copier le token du bot.
3. Envoyer un message au bot depuis votre compte Telegram.
4. Récupérer votre chat ID, par exemple via `getUpdates` ou un bot dédié à l'identification du chat ID.
5. Configurer `TELEGRAM_NOTIFICATIONS_ENABLED`, `TELEGRAM_BOT_TOKEN` et `TELEGRAM_CHAT_ID` dans l'environnement backend.

Ne jamais mettre le token Telegram dans le frontend.

## Configuration Discord

Le backend peut aussi envoyer une notification Discord après l'enregistrement réussi d'un message de contact.
Cette notification est optionnelle : si Discord est désactivé ou mal configuré, le message reste enregistré en base.

Variables d'environnement côté backend :

```properties
DISCORD_NOTIFICATIONS_ENABLED=true
DISCORD_WEBHOOK_URL=your-discord-webhook-url
```

Équivalents Spring possibles :

```properties
discord.notifications-enabled=true
discord.webhook-url=your-discord-webhook-url
```

Mise en place :

1. Créer un serveur Discord privé ou choisir un serveur privé existant.
2. Créer un canal privé pour les notifications de contact.
3. Créer un webhook entrant pour ce canal.
4. Configurer `DISCORD_NOTIFICATIONS_ENABLED` et `DISCORD_WEBHOOK_URL` dans l'environnement backend.

Ne jamais mettre l'URL du webhook Discord dans le frontend.

