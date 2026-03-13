# Compte Rendu - Application de Gestion de Tickets de Support

## Architecture Choisie

L'application est développée en utilisant JavaFX pour l'interface utilisateur, avec une architecture basée sur le modèle MVC (Modèle-Vue-Contrôleur). 

- **Modèle** : Représenté par la classe `SupportTicket`, qui encapsule les données d'un ticket de support (ID, titre, description, statut, etc.).
- **Vue** : Composée des fichiers FXML (`primary.fxml`, `secondary.fxml`) et du fichier CSS (`ticket-persistence.css`) pour la présentation et l'interaction utilisateur.
- **Contrôleur** : Les classes `PrimaryController` et `SecondaryController` gèrent la logique d'interaction entre la vue et le modèle.

Pour la persistance des données, nous utilisons SQLite comme base de données, avec un pattern DAO (Data Access Object) pour abstraire les opérations de base de données. Un service (`TicketPersistenceService`) orchestre les opérations CRUD et gère la logique métier.

L'architecture suit les principes de séparation des préoccupations, facilitant la maintenance et l'extensibilité.

## Rôle du DAO

Le DAO (Data Access Object) est un pattern de conception qui fournit une interface abstraite pour accéder aux données persistées. Dans cette application :

- L'interface `TicketDao` définit les méthodes CRUD (Create, Read, Update, Delete) pour les tickets de support.
- L'implémentation `SQLiteTicketDao` utilise JDBC pour interagir avec la base de données SQLite, exécutant les requêtes SQL correspondantes.

Le rôle principal du DAO est d'isoler la logique d'accès aux données du reste de l'application, permettant de changer facilement le système de stockage (par exemple, passer de SQLite à MySQL) sans modifier le code métier.

## Fonctionnement Global du CRUD

Le CRUD (Create, Read, Update, Delete) est implémenté à travers le service `TicketPersistenceService`, qui utilise le DAO pour effectuer les opérations :

- **Create** : Ajouter un nouveau ticket via `addTicket()`. Le service valide les données et appelle le DAO pour insérer dans la base.
- **Read** : Récupérer tous les tickets via `getAllTickets()` ou un ticket spécifique via `getTicketById()`. Le DAO exécute des requêtes SELECT.
- **Update** : Modifier un ticket existant via `updateTicket()`. Le service met à jour les données et le DAO exécute une requête UPDATE.
- **Delete** : Supprimer un ticket via `deleteTicket()`. Le DAO exécute une requête DELETE.

Le service gère également l'initialisation de la base de données via `DatabaseManager` et fournit des méthodes pour exporter les tickets (via `TicketExporter`).

L'interface utilisateur (JavaFX) permet à l'utilisateur d'interagir avec ces opérations via des boutons et formulaires, relayés par les contrôleurs.

## Ce Qui Pourrait Être Amélioré

- **Gestion des Erreurs** : Ajouter une gestion d'erreurs plus robuste, avec des messages utilisateur informatifs en cas d'échec des opérations CRUD ou de connexion à la base de données.
- **Validation des Données** : Implémenter une validation côté client et serveur pour les champs des tickets (par exemple, vérifier que le titre n'est pas vide).
- **Sécurité** : Pour une application en production, utiliser des requêtes préparées pour éviter les injections SQL, et chiffrer les données sensibles si nécessaire.
- **Tests** : Étendre les tests unitaires et d'intégration, notamment pour couvrir les scénarios d'erreur et les interactions UI.
- **Performance** : Optimiser les requêtes SQL et ajouter une pagination pour la lecture de gros volumes de données.
- **Extensibilité** : Rendre le DAO plus générique pour supporter d'autres entités, ou ajouter une couche de cache pour améliorer les performances.
- **Interface Utilisateur** : Améliorer l'UX avec des animations, une meilleure gestion des états (chargement, erreurs), et supporter plusieurs langues.
- **Configuration** : Externaliser la configuration de la base de données (URL, credentials) dans un fichier de propriétés pour faciliter les déploiements.