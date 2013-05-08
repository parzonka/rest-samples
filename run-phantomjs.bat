mkdir target\phantomjs-reports
cd target\phantomjs-reports
del TEST*.xml
phantomjs ..\..\src\test\js\phantomjs-testrunner.js http://localhost:8080/rest-samples/?spec=
cd ..
cd ..