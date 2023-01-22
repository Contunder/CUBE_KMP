
# CUBE KMP API

## Config Database

- In /src/main/resources/application.properties
- Change spring.datasource with your config 
- The Database will be automaticly create at first launch
- Add Role in the table

>ROLE_USER  
>ROLE_ADMIN

- If you have any problems you can't change acces in SecurityConfig 

> .anyRequest().authenticated() -> .anyRequest().permitAll()

## REGISTER TO API

- Entry point for login : /api/auth/register
- In JSON (example at bottom)
- By default your hare a simply user for change in Admin for this time you have to change value in database user_role

#### Exemple :

>{  
"name":"",  
"lastName":"",  
"birthday":"1998-10-06",  Format de date à respecter  
"address":"",  
"zipCode":,  
"city":"",  
"email":"",  
"password":""  
}

#### Response :

>User registered successfully!.

## LOGIN TO API

- Entry point for login : /api/auth/login
- In JSON (example at bottom)
- Use Bearer token and past the api key 

#### Exemple :

>{  
"userEmail":"",  
"password":""  
}  

#### Response :

>{  
"accessToken": "eyJhbGciOiJI2jrskm5NGk4Bky8Xzr7C74YLt_TmHs9I7VG-WV",  
"tokenType": "Bearer"  
}

## GET USER TO API

- Entry point for login : /api/user/actual
- With Bearer token

#### Response :

>{  
"id": 2,  
"name": "Test",  
"lastName": "Test",  
"birthday": "1998-10-06",  
"address": "Test",  
"zipCode": "59000",  
"city": "Lille",  
"email": "test@yopmail.com",  
"profilPicture": null  
}  

## GET FRIENDS TO API

- Entry point for login : /api/friend/user
- With Bearer token

#### Response :

>[  
{  
"id": 0,  
"friend":  
{  
"id": 2,   
"name": "Test",  
"lastName": "Test",  
"birthday": "1998-10-06",  
"address": "Test",  
"zipCode": "59000",  
"city": "Lille",  
"email": "test@yopmail.com",  
"password": null,  
"profilePicture": null,  
"verified": false,  
"disabled": false,  
"roles": null,  
"friends": null  
},  
"relation": "Famille",  
"active": true -> si a true à accepter la demande  
},  

>{  
"id": 0,  
"friend":  
> {  
"id": 3,  
"name": "Test2",  
"lastName": "Test2",  
"birthday": "1998-10-06",  
"address": "Test2",  
"zipCode": "59000",  
"city": "Lille",  
"email": "test2@yopmail.com",  
"password": null,  
"profilePicture": null,  
"verified": false,  
"disabled": false,  
"roles": null,  
"friends": null  
},  
"relation": "Test",  
"active": false -> si à false n'a pas accepter la demande  
}  
]  

## POST CATALOGUE TO API

- Entry point for login : /api/catalogue
- In JSON (example at bottom)
- With Bearer token has an Admin

#### Exemple :

>{  
"category":"Test"  
}

#### Response :

>{  
"id": 1,  
"category": "Test"  
}

## POST RESOURCE TO API

- Entry point for login : /api/resources/add/{catalogueId}
- In JSON (example at bottom)
- With Bearer token

#### Exemple :

>{  
"access":"Public",  
"value":"Test de plus de 10 charactére"  
}  

#### Response :

>{  
"id": 1,  
"access": "Public",  
"value": "Test de plus de 10 charactére",  
"comments": null,  
"catalogue": null  
}

Même si catalogue est null il est bien associées petit bug à fix  

Je n'est pas fini le readme pour les points d'entrer regarder dans les controller  

Reste à dev : Friend / Activity / Analitycs 
