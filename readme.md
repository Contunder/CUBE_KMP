
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

- Entry point : /api/auth/register
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

- Entry point : /api/auth/login
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

- Entry point : /api/user/actual
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

## GET USER BY EMAIL TO API

- Entry point : /api/user/email/{email}
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

## GET USER BY ID TO API

- Entry point : /api/user/id/{id}
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

## GET ALL USER TO API

- Entry point : /api/user/all
- All user except actual user
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

- Entry point : /api/friend/user
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

## GET FRIENDS REQUEST TO API

- Entry point : /api/friend/request
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
"active": false
},

## GET ACTIVE FRIENDS TO API

- Entry point : /api/friend/user/active
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
"active": true 
},

## GET FRIENDS BY RELATION TO API

- Entry point : /api/friend/relation/{relation}
- With Bearer token

#### Response if {relation} = 'Famille':

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
"active": true  
},

## POST FRIENDS ADD TO API

- Entry point : /api/friend/add/{email}/{relation}
- With Bearer token

#### Response :

>

## POST ACCEPT REQUEST FRIENDS TO API

- Entry point : /api/friend/request/add/{email}/{relation}
- With Bearer token

#### Response :

> 

## POST CATALOGUE TO API

- Entry point  : /api/catalogue
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

## GET CATALOGUE BY ID TO API

- Entry point : /api/catalogue/{id}
- In JSON (example at bottom)
- With Bearer token has an Admin

#### Response if {id} = 1:

>{  
"id": 1,  
"category": "Test"  
}

## GET ALL CATALOGUES TO API

- Entry point : /api/catalogue
- In JSON (example at bottom)
- With Bearer token has an Admin

#### Response :

>{  
"id": 1,  
"category": "Test"  
}

## PUT CATALOGUE WITH ID TO API

- Entry point : /api/catalogue/{id}
- In JSON (example at bottom)
- With Bearer token has an Admin

#### Exemple if {id} = 1 :

>{  
"category":"Test"  
}

#### Response :

>{  
"id": 1,  
"category": "Test"  
}

## DELETE CATALOGUE WITH ID TO API

- Entry point : /api/catalogue/{id}
- In JSON (example at bottom)
- With Bearer token has an Admin

#### Exemple if {id} = 1 :

>{  
"category":"Test"  
}

#### Response :

>categrory deleted


## POST RESOURCE TO API

- Entry point : /api/resources/add/{catalogueId}
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

## GET ALL RESOURCE TO API

- Entry point : /api/resources
- You can change RequestPram for pagination 
- pageNo / pageSize / sortBy / sortDir
- With Bearer token

#### Response :

>{  
"id": 1,  
"access": "Public",  
"value": "Test de plus de 10 charactére",  
"comments": null,  
"catalogue": null  
}

## GET RESOURCE BY ID TO API

- Entry point : /api/resources/{id}
- With Bearer token

#### Response if {id} = 1 :

>{  
"id": 1,  
"access": "Public",  
"value": "Test de plus de 10 charactére",  
"comments": null,  
"catalogue": null  
}

## GET RESOURCES BY CATALOGUE ID TO API

- Entry point : /api/resources/catalogue/{id}
- With Bearer token

#### Response if {id} = 1 :

>{  
"id": 1,  
"access": "Public",  
"value": "Test de plus de 10 charactére",  
"comments": null,  
"catalogue": null  
}

## PUT RESOURCE BY ID TO API

- Entry point : /api/resources/{id}/{catalogueId}
- Has an admin
- With Bearer token

#### Response if {id} = 1 and {catalogueId} = 1 :

>{  
"id": 1,  
"access": "Public",  
"value": "Test de plus de 10 charactére",  
"comments": null,  
"catalogue": null  
}

## DELETE RESOURCE BY ID TO API

- Entry point : /api/resources/{id}
- Has an Admin
- With Bearer token

#### Response if {id} = 1 :

>

## POST COMMENT BY RESSOURCE ID TO API

- Entry point : /api/resource/{id}/comments
- With Bearer token

#### Response if {id} = 1 :

>

## GET COMMENT BY RESSOURCE ID TO API

- Entry point : /api/resource/{id}/comments
- With Bearer token

#### Response if {id} = 1 :

>

## GET COMMENT BY RESSOURCE ID AND COMMENT ID TO API

- Entry point : /api/resource/{resourceId}/comments/{id}
- With Bearer token

#### Response if {id} = 1 :

>

## GET COMMENT BY RESSOURCE ID AND COMMENT ID TO API

- Entry point : /api/resource/{resourceId}/comments/{id}
- With Bearer token

#### Response if {id} = 1 :

>

## PUT COMMENT BY RESSOURCE ID AND COMMENT ID TO API

- Entry point : /api/resource/{resourceId}/comments/{id}
- With Bearer token

#### Response if {id} = 1 :

>

## DELETE COMMENT BY RESSOURCE ID AND COMMENT ID TO API

- Entry point : /api/resource/{resourceId}/comments/{id}
- With Bearer token

#### Response if {id} = 1 :

>

Reste à dev : Activity / Analitycs 

http://localhost:8080/api/resource/1/comments

{
"value":"Test de plus de 10 charactére"
}