# Chatop

Chatop est une application de locations immobilières composée d'un front-end Angular, d'une API Spring Boot et d'une base de données MySQL.

## Structure du dépôt

- `front-end/` : application Angular.
- `chatopAPI/` : API Java/Spring Boot.
- `ressources/sql/script.sql` : création des tables MySQL.
- [`ressources/postman/rental.postman_collection.json`](ressources/postman/rental.postman_collection.json) : collection Postman pour tester les routes de l'API.
- [`ressources/mockoon/rental-oc.json`](ressources/mockoon/rental-oc.json) : configuration de l'API simulée avec Mockoon.

Les étapes ci-dessous installent et démarrent l'application avec le backend Spring Boot et MySQL. Mockoon n'est pas requis.

## Prérequis

- Git.
- Node.js 16.10 ou supérieur
- Java JDK 17
- MySQL Server et le client en ligne de commande `mysql` (MySQL 8 recommandé).

Vérifier les outils :

```sh
node --version
npm --version
java -version
mysql --version
```

## 1. Récupérer le projet

```sh
git clone <URL_DU_DEPOT>
cd <NOM_DU_DEPOT>
```

Remplacer `<URL_DU_DEPOT>` par l'URL Git du projet et `<NOM_DU_DEPOT>` par le dossier créé par `git clone`. Si le projet est déjà présent sur la machine, ouvrir un terminal à sa racine, au même niveau que ce README.

## 2. Installer et initialiser MySQL

Démarrer le service MySQL, puis créer la base et un compte dédié. Dans un terminal, ouvrir une session administrateur MySQL :

```sh
mysql -u root -p
```

Entrer le mot de passe administrateur MySQL, puis exécuter dans l'invite MySQL (remplacer le mot de passe d'exemple par un mot de passe local) :

```sql
CREATE DATABASE chatop_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'chatop_user'@'localhost' IDENTIFIED BY 'MotDePasseLocalAChanger';
GRANT ALL PRIVILEGES ON chatop_db.* TO 'chatop_user'@'localhost';
EXIT;
```

Depuis la racine du dépôt, importer le schéma fourni :

```sh
mysql -u chatop_user -p chatop_db < ressources/sql/script.sql
```

Entrer le mot de passe défini lors de `CREATE USER`. Le script crée les tables `USERS`, `RENTALS` et `MESSAGES` ainsi que leurs index et clés étrangères. Il ne crée aucun compte ni aucune location ; la base est vide après l'import.

## 3. Configurer et démarrer l'API Spring Boot

La configuration de l'API est dans `chatopAPI/src/main/resources/application.properties` :

- MySQL : `localhost:3306/chatop_db`.
- Port HTTP : `3001`.
- Identifiants MySQL : variables `DB_USERNAME` et `DB_PASSWORD`.
- Clé JWT : variable obligatoire `JWT_KEY`.
- URL publique : `APP_URL`, optionnelle, par défaut `http://localhost:3001`.
- Dossier d'images : `APP_UPLOAD_DIR`, optionnel, par défaut `uploads` sous le répertoire courant du processus.

Dans un nouveau terminal, depuis la racine du dépôt, définir les variables et lancer le backend.

### macOS ou Linux

```sh
export DB_USERNAME='chatop_user'
export DB_PASSWORD='MotDePasseLocalAChanger'
export JWT_KEY='CleDeDeveloppementLocaleAChanger-32-caracteres-minimum'
export APP_URL='http://localhost:3001'
export APP_UPLOAD_DIR='uploads'
cd chatopAPI
./mvnw spring-boot:run
```

### Windows PowerShell

```powershell
$env:DB_USERNAME = 'chatop_user'
$env:DB_PASSWORD = 'MotDePasseLocalAChanger'
$env:JWT_KEY = 'CleDeDeveloppementLocaleAChanger-32-caracteres-minimum'
$env:APP_URL = 'http://localhost:3001'
$env:APP_UPLOAD_DIR = 'uploads'
Set-Location chatopAPI
.\mvnw.cmd spring-boot:run
```

OU 

### IntelJ idea

Aller dans Run>EditConfiguration et y ajouter les variables d'environnement

La clé ci-dessus est uniquement un exemple pour le développement local. Utiliser une clé privée longue et aléatoire, et conserver la même clé tant que des JWT émis par ce backend doivent rester valides. Ne pas publier de vrais secrets dans Git.

## 4. Lancement de l'application

### Backend (Spring Boot)

#### Via IntelliJ IDEA (recommandé)

1. Ouvrir le projet `chatopAPI` dans IntelliJ IDEA.
2. Dans la configuration d'exécution, renseigner les variables d'environnement décrites à l'étape 3 (`DB_USERNAME`, `DB_PASSWORD` et `JWT_KEY`, ainsi que les variables facultatives).
3. Lancer la classe principale `ChatopApplication.java` avec le bouton **Run**.

#### Via Maven

Depuis le dossier `chatopAPI`, après avoir configuré les mêmes variables d'environnement :

```sh
mvn spring-boot:run
```

Avec le Maven Wrapper fourni, on peut aussi exécuter `./mvnw spring-boot:run` (macOS/Linux) ou `mvnw.cmd spring-boot:run` (Windows).

L'API sera accessible à l'adresse [http://localhost:3001](http://localhost:3001).

### Frontend (Angular)

Dans un autre terminal, depuis la racine du dépôt :

```sh
cd front-end
npm install
npm run start
```

Le frontend sera accessible à l'adresse [http://localhost:4200](http://localhost:4200). Laisser le backend actif : le proxy Angular transmet les requêtes `/api/*` au backend sur le port `3001`.

## 5. Créer un compte et des locations de démonstration

Comme le script SQL ne charge pas de données, il faut créer un compte avant d'utiliser les routes protégées. On peut s'inscrire depuis l'écran d'inscription du front ou utiliser Postman.

Dans Postman, envoyer la requête suivante :

```http
POST http://localhost:3001/api/auth/register
Content-Type: application/json
```

Corps JSON :

```json
{
  "name": "test Test",
  "email": "test@test.com",
  "password": "test!31"
}
```

La réponse contient un JWT sous la forme `{"token":"..."}`. Pour les requêtes protégées, utiliser l'onglet **Authorization** de Postman, choisir **Bearer Token** et coller la valeur du champ `token`. Pour se reconnecter, envoyer `POST http://localhost:3001/api/auth/login` avec ce corps :

```json
{
  "email": "demo@example.com",
  "password": "DemoPassword123!"
}
```

Pour ajouter une location de démonstration, envoyer `POST http://localhost:3001/api/rentals` avec le Bearer Token récupéré lors de la précedente commande. Dans **Body → form-data**, ajouter les champs suivants :

| Clé | Type | Exemple |
| --- | --- | --- |
| `name` | Text | Appartement de démonstration |
| `surface` | Text | `50` |
| `price` | Text | `120` |
| `description` | Text | Location de démonstration |
| `picture` | File | Sélectionner une image locale |

Postman définit lui-même l'en-tête `multipart/form-data` et sa frontière ; ne pas saisir manuellement cet en-tête. Les images téléversées sont enregistrées dans `chatopAPI/uploads` par défaut.

La liste s'obtient avec `GET http://localhost:3001/api/rentals` et le token Bearer. La réponse attendue est un objet contenant la liste dans la propriété `rentals` :

```json
{
  "rentals": [
    {
      "id": 1,
      "name": "Appartement de démonstration",
      "surface": 50.0,
      "price": 120.0,
      "picture": "http://localhost:3001/api/images/...",
      "description": "Location de démonstration",
      "owner_id": 1,
      "created_at": "2026/11/26",
      "updated_at": "2026/11/26"
    }
  ]
}
```

Après inscription ou connexion dans le front, l'application redirige vers la liste des locations. Une base fraîche ne montrera aucune location tant qu'aucune n'a été créée.

## Routes principales

| Méthode | Route | Authentification | Description |
| --- | --- | --- | --- |
| `POST` | `/api/auth/register` | Non | Créer un utilisateur et recevoir un JWT. |
| `POST` | `/api/auth/login` | Non | Se connecter et recevoir un JWT. |
| `GET` | `/api/auth/me` | Bearer JWT | Obtenir le compte connecté. |
| `GET` | `/api/rentals` | Bearer JWT | Lister les locations. |
| `GET` | `/api/rentals/{id}` | Bearer JWT | Obtenir une location. |
| `POST` | `/api/rentals` | Bearer JWT | Créer une location avec un formulaire multipart. |
| `PUT` | `/api/rentals/{id}` | Bearer JWT | Modifier une location avec un formulaire multipart. |
| `POST` | `/api/messages` | Bearer JWT | Envoyer un message au format JSON. |

Les images accessibles par `/api/images/{nom-de-fichier}` ne nécessitent pas de JWT.

## Documentation API (Swagger)

L'API est documentée avec Swagger UI via Springdoc OpenAPI.

Une fois le backend lancé, la documentation interactive est accessible à l'adresse suivante : [http://localhost:3001/api/docs](http://localhost:3001/api/docs).

## Ressources Mockoon et Postman

- **Postman** : importer [`ressources/postman/rental.postman_collection.json`](ressources/postman/rental.postman_collection.json) dans Postman pour retrouver les requêtes de l'API.
- **Mockoon** : importer [`ressources/mockoon/rental-oc.json`](ressources/mockoon/rental-oc.json) dans Mockoon pour charger la configuration du serveur simulé.

## Arrêt et redémarrage

- Arrêter chaque serveur avec `Ctrl+C` dans son terminal.
- Le service MySQL peut rester actif.
- Au prochain démarrage, relancer le backend avec les mêmes variables d'environnement, puis `npm start` dans `front-end`.

## Dépannage

### Le backend ne démarre pas

- Vérifier `java -version` : le projet requiert Java 17.
- Vérifier que MySQL est démarré et écoute sur le port `3306`.
- Vérifier que la base s'appelle `chatop_db`, que le compte et mot de passe correspondent aux variables `DB_USERNAME` et `DB_PASSWORD`, puis vérifier que le script SQL a été importé.
- Vérifier que `JWT_KEY` est définie dans le terminal qui lance le backend.
- Consulter la première exception dans la console Spring Boot.

### Le front affiche une erreur réseau ou HTTP 500

- Vérifier que les deux serveurs sont lancés : Angular sur `4200`, API sur `3001`.
- Vérifier `GET http://localhost:3001/api/rentals` dans Postman avec un Bearer Token valide.
- Une réponse `401` indique généralement un token absent, invalide ou expiré ; se reconnecter pour en obtenir un autre.
- Une base nouvellement installée ne contient pas de locations : créer une location via le front ou Postman.
- Pour un `500`, consulter la console du backend ; le détail utile se trouve dans l'exception Spring, pas dans la trace Angular.

### Le front ne peut pas joindre le backend via le proxy

Vérifier que `npm start` a démarré Angular avec la configuration de proxy déclarée dans `angular.json`, et que l'API écoute sur `localhost:3001`. Le navigateur affiche généralement une requête à `localhost:4200/api/...` ; c'est Angular qui la relaie au port `3001`.
