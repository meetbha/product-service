# Product Management Application

The Product Management Application is a backend service for managing different kind of product. It provides RESTful APIs to perform CRUD operations on product.

## Features

- Create new product with attributes like name, description and price.

- Retrieve list of all products.

- Retrieve product by ID.

- Update product details.

- Delete products by ID.

## Setup Instructions

1. Clone the Repository

```shell script  
https://github.com/meetbha/product-service.git
```

2. Configure the Database

- Install and start PostgreSQL.

- Create a database:

```shell script
CREATE DATABASE ecommerce;
```

3. Go to the src/main/resources/ and edit the application.properties to configure PostgreSQL Database name and credentials.

```shell script
quarkus.datasource.db-kind=postgresql
quarkus.datasource.username=postgres
quarkus.datasource.password=test
quarkus.datasource.reactive.url=postgresql://localhost:5432/ecommerce
```

4. Run the Application

```shell script 
./mvnw compile quarkus:dev
```


## API Endpoints

### 1. Create a New Product

### Request:

##### POST /api/product
##### Content-Type: application/json

```shell script
{
    "name": "Samsung 28 L Convection Microwave Oven",
    "description": "Black, Pre heat, Eco Mode, Power Defrost, Auto Cook, Wire Rack, 10 Year Warranty on Ceramic Enamel Cavity",
    "price": "13000.00"
}
```

### Response:

```shell script
{
    "id": "b266f329-d8bb-48c0-b51e-e9748b392bc0",
    "name": "Samsung 28 L Convection Microwave Oven",
    "description": "Black, Pre heat, Eco Mode, Power Defrost, Auto Cook, Wire Rack, 10 Year Warranty on Ceramic Enamel Cavity",
    "price": "13000.00",
    "createdAt": "2024-12-02T16:34:39.293+00:00"
}
```

### 2. Retrieve All Products


### Request:

#### GET /api/products


### Response:

```shell script
[
    {
        "id": "7dc1979f-34e8-4c14-8608-4ea922104fe6",
        "name": "LG AC 1.5 ton",
        "description": "5-In-1 Convertible Cooling, Inverter Split AC",
        "price": 55000.00,
        "createdAt": "2024-12-02T16:31:18.105+00:00"
    },
    {
        "id": "9ca19c28-cda6-4846-8464-597f022aa135",
        "name": "Samsung 183L Fridge",
        "description": "Digital Inverter, Direct-Cool Single Door Refrigerator",
        "price": 25000.00,
        "createdAt": "2024-12-02T16:31:18.105+00:00"
    },
    {
        "id": "e96e4b71-856a-4ded-9e5a-5da8e83149e1",
        "name": "VW 80 cm (32 inches) TV",
        "description": "HD Ready Android Smart LED TV",
        "price": 15000.00,
        "createdAt": "2024-12-02T16:31:18.105+00:00"
    }
]
```


### 3. Retrieve a Product by ID


### Request:



#### GET /api/products/7dc1979f-34e8-4c14-8608-4ea922104fe6


### Response:

```shell script
{
    "id": "7dc1979f-34e8-4c14-8608-4ea922104fe6",
    "name": "LG AC 1.5 ton",
    "description": "5-In-1 Convertible Cooling, Inverter Split AC",
    "price": 55000.00,
    "createdAt": "2024-12-02T16:31:18.105+00:00"

}
```

### 4. Update a Product


### Request:


#### PUT /api/products/7dc1979f-34e8-4c14-8608-4ea922104fe6?description="4-In-1 Convertible Cooling, Inverter Split AC"
#### Content-Type: application/json


Response:

```shell script
{
    "id": "7dc1979f-34e8-4c14-8608-4ea922104fe6",
    "name": "LG AC 1.5 ton",
    "description": "5-In-1 Convertible Cooling, Inverter Split AC",
    "price": 55000.00,
    "createdAt": "2024-12-02T16:31:18.105+00:00"

}
```


### 5. Delete a Product

### Request:

#### DELETE /api/products/4e1681ac-b8ae-477a-b1a0-ed8b4739f831


### Response:

```shell script
204 No Content
```

