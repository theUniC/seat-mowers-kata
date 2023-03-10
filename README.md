<div align="center">

<img src="https://raw.githubusercontent.com/theUniC/seat-mowers-kata/main/logo.png" alt="logo">

# Seat Mowers Kata

An example application to test out some JVM-related tecnologies

<p align="center">
  <a href="#instructions">Instructions</a> •
  <a href="#tech-stack">Tech Stack</a> •
  <a href="#getting-started">Getting started</a> •
  <a href="#accessing-the-application">Accessing the application</a> •
  <a href="#design-considerations">Design considerations</a>
</p>

</div>

## Instructions

<details>
<summary>Instructions for the challenge</summary>
<br>
SEAT:CODE has been asked for a really important project. We need to develop an application that helps in controlling brand new mowers
from the SEAT Martorell Factory.

SEAT Martorell factory has a lot of green spaces but for the MVP, we will consider only one single green grass plateau to simply the problem.

A green grass plateau, which is curiously rectangular, must be navigated by the mowers.

A mower’s position and location are represented by a combination of X and Y coordinates and a letter representing one of the four cardinal compass
points (N, E, S, W). The plateau is divided up into a grid to simplify navigation. An example position might be 0, 0, N, which means the mower is in
the bottom left corner and facing North.

In order to control a mower, SEAT Maintenance Office sends a simple string of letters. The possible letters are “L”, “R” and ”M”. “L” and “R” make the
mower spin 90 degrees left or right respectively, without moving from its current spot. “M” means to move forward one grid point and maintain the same Heading.
Assume that the square directly North from (X, Y) is (X, Y + 1).

### Input

The first line of input is the upper-right coordinates of the plateau,
the bottom-left coordinates are assumed to be 0, 0.
The rest of the input is information pertaining to the mowers that have been deployed.

Each mower has two lines of input.
The first line gives the mower’s position, and the second line is a series of instructions telling the mower how to explore the plateau.

The position is made up of two integers and a letter separated by spaces, corresponding to the X and Y coordinates and the mower’s orientation.
Each mower will be finished sequentially, which means that the second mower won’t start
to move until the first one has finished moving.

### Output

The output for each mower should be its final coordinates and heading.

Input Test Case #1:
```
5 5
1 2 N
LMLMLMLMM
3 3 E
MMRMMRMRRM
```

Output Test Case #2:
```
1 3 N
5 1 E
```
</details>

## Tech Stack

* Runtime 👉 JRE 17.0.5
* Language 👉 Kotlin 1.8.0
* Delivery Mechanism 👉 Spring Boot 2.7.7
* Database 👉 MySQL 8.0.31

## Getting Started

This repository supports two ways to run the application: locally or fully dockerized. Anyway this is what you should do to get started 👇

### Clone the code

    git clone https://github.com/theUniC/seat-mowers-kata.git
    cd seat-mowers-kata

### Running the application locally

#### Requirements

* [Docker](https://www.docker.com/products/docker-desktop/)
* [SDKMan](https://sdkman.io/install)

#### Install the required version of Java and gradle 

For this SDKMan comes in very handy

    sdk env

#### Start the database

    docker compose up -d

#### Create an .env file

This application uses dotenv files to handle the environment-depedent configuration.

    cp .env.example .env

#### Run database migrations

    ./gradlew flywayMigrate -i

#### Run the application

    ./gradlew build bootRun

#### Run the unit tests

    ./gradlew test

### Running the application fully dockerized

#### Requirements

To run the application fully dockerized you only need to have [Docker](https://www.docker.com/products/docker-desktop/) installed

#### Start the database and run migrations

    docker compose up -d

Wait a few seconds until MySQL is ready and then

    docker compose run --profile db-migrations run flyway

#### Run the application

The application container is defined in the `docker-compose.override.yaml.dist` so you will need to copy it with a new file name of `docker-compose.override.yaml`

    cp docker-compose.override.yaml.dist docker-compose.override.yaml

And then start services up again

    docker-compose up -d

## Accessing the application

The application has several endpoints of interest

* Web frontend
* [REST and OpenAPI](#rest-and-openapi)
* [GraphQL](#graphql)

### Web frontend

The application has a web frontend made specifically to run instructions in the from described in the [instructions section](#instructions). It can be accessed at

**http://127.0.0.1:8080/api/instructions**

Additionally executions are saved to later check them again at

**http://127.0.0.1:8080/api/executions**

### REST and OpenAPI

To access the REST API there is a swagger UI endpoint at

**http://127.0.0.1:8080/api/swagger-ui/index.html**

And the OpenAPI document can be accessed

**http://127.0.0.1:8080/api/v3/api-docs**

Additionally an exported Postman collection is provided [here](.postman/seat-mowers.postman_collection.json).

### GraphQL

The application also supports GraphQL in this endpoint

**http://127.0.0.1:8080/api/graphql**

And can be queried using GraphiQL at this URL

**http://127.0.0.1:8080/api/graphiql**

## Design considerations

* This application uses Tactical DDD to better implement the *Ubiquitous Language* defined at the [instructions section](#instructions).
* As application architecture it is using Event Sourcing + CQRS, as it's using [Axon Framework](https://docs.axoniq.io/reference-guide/axon-framework/introduction) to handle domain messages and the default mode of this framework is Event Sourcing.
* When a mower tries to move to an occupied position, then an exception is thrown. It can be changed to do nothing pretty easily.
* There's just a single Aggregate – [Plateau](src/main/kotlin/code/seat/seatmowers/domainmodel/plateau/Plateau.kt) – which receives all the Commands. That is it acts both as an Aggregate and as a Command Handler. This has been done that way because we need inmediate consistency between both Plateau and deployed mowers.
