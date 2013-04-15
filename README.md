# REST samples

This project tries provide simple and advanced examples on how to provide and consume rest services.

General ideas about this project:

- Start simple
- introduce concepts one by one
- provide them well documented
- provide something to try out
- show how automatic testing can work here
- target environment should be JBoss 7 for a start
- we will use RESTEasy for the moment as it is bundled with JBoss 7

## How to run this sample

Please install [Apache Maven](http://maven.apache.org/) version 3.0.4 or later. Then you can start the project by using the following maven target:

    mvn -DskipTests=true jboss-as:run

This will download all necessary artefacts to run this project, including JBoss 7. Afterwards you can access the project in your browser you your favourite REST client with the URL [http://localhost:8080/rest-sample/](http://localhost:8080/rest-sample/)

## How to find out more about this project

This project contains a Maven site that is extended and updated as the project evolves.

You can look at the raw documents in src/site/apt/ - or you can run the site locally by issuing

    mvn site:run

and pointing your browser at [http://localhost:8090](http://localhost:8090).

## How to develop changes for these REST samples

Please use Eclipse Juno JEE for developing these. Recommended plugins are JBoss developer tools 4.0, m2e 1.2+ for Maven support.

You should download JBoss AS 7.1.1 from [JBoss](http://www.jboss.org/as) and extract it to a folder. You can then start the server locally and log in to the [http://localhost:8080](http://localhost:8080).


## How to try out the REST elements

### Use SoapUI to test the REST services

REST is supposed to be a universal interface that can be consumed by a lot of clients. To look at some ready to go test samples, please install [SoapUI](http://www.soapui.org) (version 4.5.1 is used in these samples).

There is a project called <tt>rest-samples-soapui-project.xml</tt> in the sub-folder <tt>src\test\resources</tt>

*Hint*: when looking at the responses, there are different views. SoapUI might trick you by showing you a XML view even if you received a JSON response or an empty response. Please watch out for this and make yourself familiar - especially with the Raw view.

### Use your browser and a HTML5/jQuery app

Run the sample i.e. with maven like described above. Brose to [http://localhost:8080/rest-samples](http://localhost:8080/rest-samples).

### How to run the test cases

There are some tests (SimpleArquillianTest.java) that spawn a container first, in this case JBoss 7.

At the moment you must have a local installation of JBoss available and the environment variable JBOSS_HOME needs to point to that folder. I will try to make it work without a local installation in the future.

## Todos

1. provide a pure JAVA client for a REST service
2. **STARTED** automatic test cases missing
3. Make the arquillian tests work without installing JBoss 7 locally first.
4. Add translation JS Frontend

Use Java 32bit to use the HTML visual page editor
https://issues.jboss.org/browse/JBIDE-2720

##License

Unless otherwise specified i.e. in a library file this is all MIT license - [http://www.opensource.org/licenses/mit-license.php](http://www.opensource.org/licenses/mit-license.php)

<!-- Piwik Image Tracker -->
<img src="http://piwik.ahus1.de/piwik.php?idsite=4&amp;rec=1&amp;action_name=readmeMD" style="border:0" alt="" />
<!-- End Piwik -->
