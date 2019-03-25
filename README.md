

# Mini shop

Le but de ce TP est d’implémenter certains services REST d’une API pour gérer un magasin. Minishop est une api REST simplifiée pour un magasin en ligne comportant des clients, des produits, des paniers d’achats et des commandes.

Les méthodes HTTP (POST, GET, PUT, DELETE) sont utilisées pour interagir avec les différentes ressources liées au magasin.

Bon nombre de services ont déjà été implémenté. Votre mission est **d’implémenter** les services relatifs à la gestion des clients (classe **CustomerResource**) et de faire en sorte que **tous les tests** de la classe « **PersistenceServiceTest** » passent. Voici la spécification des services que vous devrez implémenter :

| Ressources | URI exemple |
| --- | --- |
| Lister tous les clients | `GET /customer` |
| Lister les informations d’un client| `GET /customer/{id}` |
| Créer un nouveau client | `POST /customer` |
| Supprimer un client | `DELETE /customer/{id}` |
| Modifier les informations d’un client | `PUT /customer/1` |

### Informations supplémentaires:

Pour la ressource `GET /customer`, il faut que la ressources retourne un tableaux d'objets JSON (MediaType: `application/json`), voici un exemple :
```
[
	{
		"customerId": 1
		"username": "user123",
		"firstName": "John",
		"lastName": "Doe",
		"email": "john@doe.com",
		"phone": "0777777777"
	},
	{
		"customerId": 2
		"username": "sophiesmith",
		"firstName": "Sophie",
		"lastName": "Smith",
		"email": "sophie@smith.com",
		"phone": "0888888888"
	}
]
```

Pour la ressource `GET /customer/1`, il faut que la ressources retourne un objet JSON (MediaType: `application/json`), voici un exemple :
```
{
	"customerId": 1
	"username": "user123",
	"firstName": "John",
	"lastName": "Doe",
	"email": "john@doe.com",
	"phone": "0777777777"
}
```

Pour la ressource `POST /customer`, il faut que vous passiez dans le corps de la requête (MediaType : `application/x-www-form-urlencoded`) les paramètres suivants:
* username
* firstname
* lastname
* email
* phone

De plus, la ressource doit retourner un objet JSON (MediaType: `application/json`) correspondant à l'objet créé, exemple: 
```
{
	"customerId": 1
	"username": "user123",
	"firstName": "John",
	"lastName": "Doe",
	"email": "john@doe.com",
	"phone": "0777777777"
}
```

Pour la ressource `PUT /customer/1`, il faut que vous passiez dans le corps de la requête (MediaType: `application/json`) un JSON object ayant les attributs suivants:
* username
* firstName
* lastName
* email
* phone

Exemple:
```
{
	"username": "user123",
	"firstName": "John",
	"lastName": "Doe",
	"email": "john@doe.com",
	"phone": "0777777777"
}
```

De plus,  la ressource doit retourner un objet JSON (MediaType: `application/json`) correspondant à l'objet mis à jour.

Vous avez à disposition la classe de services « **PersistenceService** » qui vous sera utile lors de l’implémentation des services REST. N’hésitez pas à aller regarder les services déjà implémentés, cela peut vous aider à en comprendre le fonctionnement!

Testez aussi vos services grâce à l’outil Postman.

**Livrables : le code push sur une branche « customerREST » (n’oubliez pas de faire une pull request et d’y assigner les professeurs lorsque vous avez terminé!)**
