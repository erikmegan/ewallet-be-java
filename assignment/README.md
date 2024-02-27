# FLIP-ASSIGNMENT

Technologies:
  * Spring Boot 2 (Using <b>spring-boot-starter-webflux</b>)
  * Redis (Using <b>spring-boot-starter-data-redis</b>)
  * MySQL (Using <b>spring-boot-starter-data-jpa</b>)
  * Web (Using <b>spring-boot-starter-web</b>)
  

## Run Application
```
make infra-up
```


## REST API ENDPOINT FORMAT
```
http://localhost:8080/mocks/flip/assignment/21221391/
```

## Transaction Transfer
```
method : POST
path : /mocks/flip/assignment/21221391/transfer 
header : Authorization
request body : 
    {
      "amount": 500,
      "to_username": "username5"
    }
success response :
    200 Transfer success
failed response :
    400 Insufficient balance
    401 Unauthorized
    404 Destination user not found
```

## Transaction Balance Top Up
```
method : POST
path : /mocks/flip/assignment/21221391/balance_topup
header : Authorization
request body : 
    {
      "amount": 2000
    }
success response :
    200 Topup successful
failed response :
    400 Invalid topup amount
    401 User token not valid
```

## Top transactions for user
```
method : GET
path : /mocks/flip/assignment/21221391/top_transactions_per_user
header : Authorization
success response :
[
    {
        "username": "username1",
        "amount": 20000.0
    },
    {
        "username": "username1",
        "amount": 5000.0
    },
    {
        "username": "username1",
        "amount": 5000.0
    },
    {
        "username": "username1",
        "amount": 4600.0
    },
    {
        "username": "username1",
        "amount": 4600.0
    },
    {
        "username": "username1",
        "amount": -4500.0
    },
    {
        "username": "username1",
        "amount": 4500.0
    },
    {
        "username": "username1",
        "amount": 4500.0
    },
    {
        "username": "username1",
        "amount": 4200.0
    },
    {
        "username": "username1",
        "amount": 4200.0
    }
]

failed response:
    401 Unauthorized
```

## Top Users
```
method : GET
path : /mocks/flip/assignment/21221391/top_users 
header : Authorization
success response :
[
    {
        "username": "username1",
        "transactedValue": 17600.0
    },
    {
        "username": "username3",
        "transactedValue": 600.0
    },
    {
        "username": "username4",
        "transactedValue": 400.0
    }
]
failed response :
    401 Unauthorized

```

## Register User
```
method : POST
path : /mocks/flip/assignment/21221391/create_user
header : 
request :
    {
      "username": "username1"
    }
response : 5b3aa055-0f16-444e-8731-95ead34556db
```

## Get Token
```
method : GET
path : /mocks/flip/assignment/21221391/get_token
param : username
response : 5b3aa055-0f16-444e-8731-95ead34556db
```

## Balance Read 
```
method : GET
path : /mocks/flip/assignment/21221391/balance_read
header : Authorization
success response :
    {
      "balance": 1500
    }
failed response : 
    401 Unauthorized
```
