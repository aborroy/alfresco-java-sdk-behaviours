# Alfresco Java Event API - Behaviour policies coverage

Sample application using the pure Spring Integration approach for the Alfresco Java Event API mapping existing behaviour policies to the use of this new API.


## Usage

### Pre-Requisites

To properly build and run the project in a local environment it is required to have installed some tools.

* Java 11:
```bash
$ java -version

openjdk version "11.0.1" 2018-10-16
OpenJDK Runtime Environment 18.9 (build 11.0.1+13)
OpenJDK 64-Bit Server VM 18.9 (build 11.0.1+13, mixed mode)
```

* [Maven](https://maven.apache.org/install.html) version 3.3 or higher:
```bash
$ mvn -version

Apache Maven 3.6.1 (d66c9c0b3152b2e69ee9bac180bb8fcc8e6af555; 2019-04-04T21:00:29+02:00)
```

* [Docker](https://docs.docker.com/install/) version 1.12 or higher:
```bash
$ docker -v

Docker version 20.10.2, build 2291f61
```

* [Docker compose](https://docs.docker.com/compose/install/):
```bash
$ docker-compose -v

docker-compose version 1.27.4, build 40524192
```

### Build and run

Start Docker Compose.

```
$ docker-compose up --build --force-recreate
```

Build the project.

```
$ mvn clean package -Dlicense.skip=true
```

Run the application

```
$ java -jar target/alfresco-java-sdk-behaviours-5.0.0-SNAPSHOT.jar
```

## Supported behaviour policies

ContentServicePolicies
* onContentPropertyUpdate
* onContentUpdate

CopyServicePolicies
* onCopyNode (listening to `cm:copiedfrom` aspect addition)

NodeServicePolicies
* onAddAspect
* onCreateAssociation
* onCreateChildAssociation
* onCreateNode
* onDeleteAssociation
* onDeleteChildAssociation
* onDeleteNode
* onMoveNode
* onRemoveAspect
* onSetNodeType
* onUpdateNode
* onUpdateProperties

VersionServicesPolicies
* afterCreateVersion
* onCreateVersion

## Unsupported behaviour policies

ContentServicePolicies
* onContentRead

CopyServicePolicies
* beforeCopy
* onCopyComplete

NodeServicePolicies
* beforeAddAspect
* beforeArchiveNode
* beforeCreateNode
* beforeCreateStore
* beforeDeleteAssociation
* beforeDeleteChildAssociation
* beforeDeleteNode
* beforeMoveNode
* beforeRemoveAspect
* beforeSetNodeType
* beforeUpdateNode

VersionServicesPolicies
* beforeCreateVersion
* calculateVersionLabel
