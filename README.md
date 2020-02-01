
# cliv-server

Mult3 - Módulo Servidor

# Setup

- [Instale o JDK 8 Oracle](http://www.webupd8.org/2012/09/install-oracle-java-8-in-ubuntu-via-ppa.html)

- Mude para o Java 8: `sudo update-alternatives --config java`

- Instale o node e npm da distribuição: `apt install nodejs npm`\
O nvm não funciona com o Gradle e Intellij. Outra solução possível é criar links 
nos diretórios /usr/bin, /usr/local/bin, etc, para uma versão específica instalada pelo nvm.\
Ver:\
https://objectpartners.com/2016/04/14/using-self-contained-node-js-and-npm-instances-with-gradle/\
https://askubuntu.com/questions/529687/how-to-use-update-alternatives-to-manage-multiple-installed-version-of-the-sam

- [Instale o nvm](https://github.com/creationix/nvm#install-script)

- Instale a versão v8.11.3 do node pelo nvm: `nvm install v8.11.3`

- [Node.js: what is ENOSPC error and how to solve?](https://stackoverflow.com/questions/22475849/node-js-what-is-enospc-error-and-how-to-solve)

- [Delegue o build do intellij para o gradle](https://www.jetbrains.com/help/idea/gradle.html#delegate_build_gradle)

- Execute `nvm use` e depois `npm install` na raiz do projeto.

- Execute `./gradlew npmInstall` na raiz do projeto.\
Se acontecer erro `/usr/bin/env "sh\r" no such file or directory`, instale o dos2unix e execute `dos2unix gradlew`

- [Instale o PostgresSQL 9.5](https://www.postgresql.org/download/linux/ubuntu/)

## Building and Running application by docker-compose

### Build docker image
```shell script
./gradlew -Pdev bootJar jibDockerBuild
./gradlew -Pprod bootJar jibDockerBuild
```
### Running the docker-compose
```shell script
docker-compose -f src/main/docker/app.yml up -d
```


# Antigo (mover para outro arquivo)

# cliv_server
This application was generated using JHipster 5.7.0, you can find documentation and help at [https://www.jhipster.tech/documentation-archive/v5.7.0](https://www.jhipster.tech/documentation-archive/v5.7.0).

## Development

To start your application in the dev profile, simply run:

    


For further instructions on how to develop with JHipster, have a look at [Using JHipster in development][].

### Using angular-cli

You can also use [Angular CLI][] to generate some custom client code.

For example, the following command:

    ng generate component my-component

will generate few files:

    create src/main/webapp/app/my-component/my-component.component.html
    create src/main/webapp/app/my-component/my-component.component.ts
    update src/main/webapp/app/app.module.ts


## Building for production

To optimize the cliv_server application for production, run:

    ./gradlew -Pprod bootWar

To ensure everything worked, run:



Refer to [Using JHipster in production][] for more details.

## Testing

To launch your application's tests, run:

    ./gradlew test

For more information, refer to the [Running tests page][].

### Code quality

Sonar is used to analyse code quality. You can start a local Sonar server (accessible on http://localhost:9001) with:

```
docker-compose -f src/main/docker/sonar.yml up -d
```

Then, run a Sonar analysis:

```
./gradlew -Pprod clean test sonarqube
```

For more information, refer to the [Code quality page][].

## Using Docker to simplify development (optional)

You can use Docker to improve your JHipster development experience. A number of docker-compose configuration are available in the [src/main/docker](src/main/docker) folder to launch required third party services.

For example, to start a  database in a docker container, run:

    docker-compose -f src/main/docker/.yml up -d

To stop it and remove the container, run:

    docker-compose -f src/main/docker/.yml down

You can also fully dockerize your application and all the services that it depends on.
To achieve this, first build a docker image of your app by running:

    

Then run:

    docker-compose -f src/main/docker/app.yml up -d

For more information refer to [Using Docker and Docker-Compose][], this page also contains information on the docker-compose sub-generator (`jhipster docker-compose`), which is able to generate docker configurations for one or several JHipster applications.

## Continuous Integration (optional)

To configure CI for your project, run the ci-cd sub-generator (`jhipster ci-cd`), this will let you generate configuration files for a number of Continuous Integration systems. Consult the [Setting up Continuous Integration][] page for more information.

[JHipster Homepage and latest documentation]: https://www.jhipster.tech
[JHipster 5.7.0 archive]: https://www.jhipster.tech/documentation-archive/v5.7.0

[Using JHipster in development]: https://www.jhipster.tech/documentation-archive/v5.7.0/development/
[Using Docker and Docker-Compose]: https://www.jhipster.tech/documentation-archive/v5.7.0/docker-compose
[Using JHipster in production]: https://www.jhipster.tech/documentation-archive/v5.7.0/production/
[Running tests page]: https://www.jhipster.tech/documentation-archive/v5.7.0/running-tests/
[Code quality page]: https://www.jhipster.tech/documentation-archive/v5.7.0/code-quality/
[Setting up Continuous Integration]: https://www.jhipster.tech/documentation-archive/v5.7.0/setting-up-ci/




[]: https://www.jetbrains.com/help/idea/gradle.html#delegate_build_gradle
