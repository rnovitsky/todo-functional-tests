# "TODO" App functional tests

The repository contains automated functional tests for a simple web app for managing list of TODOs.

## Prerequisites
- Docker (native for Linux, Docker Desktop for Windows)
- Java 17
- Gradle 8.x

## Application under test
The image of the tested application is located in [app](/app) folder.

Before running tests the image should be loaded to Docker using the following command:

`docker load -i ./app/todo-app.tar`

To start a container locally:

`docker run -d -p 8080:4242 --name todo-app todo-app:latest`

Application will be available at http://localhost:8080

## Run tests
To run all the tests run the following command:

`./gradlew clean test`

### Test parameters
Additional test parameters can be passed via environment variables.

List of available variables:
- `TODO_USERNAME` - username for authorization. Default: admin
- `TODO_PASSWORD` - password for authorization. Default: admin
- `LOG_LEVEL` - minimal logging level to show in stdout. Default: INFO
- `USE_TESTCONTAINERS` - start application using Testcontainers and run tests against it. Default: 1 (i.e., enabled)

To run tests without using Testcontainers use command (in this case a standalone container with app should be running, see [Application under test](#application-under-test)):

`./gradlew clean test -DUSE_TESTCONTAINERS=0`

Testcontainers requires the image to be loaded to the local docker installation, load the image before running tests (see [Application under test](#application-under-test))

## Test reports
Test execution reports can be found in `/build/reports/tests/test/index.html`.

When running on CI test reports are published to [GitHub Pages](https://rnovitsky.github.io/todo-functional-tests).
