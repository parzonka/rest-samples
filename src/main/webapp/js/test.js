define([ 'knockout' ], function(ko) {

  return function() {
    if (location.search.indexOf('?spec=') != -1) {

      require([ "jasmine", "jasmine-html", "sinon" ], function(jasmine) {

        var link = document.createElement("link");
        link.type = "text/css";
        link.rel = "stylesheet";
        link.href = "css/jasmine.css";
        document.getElementsByTagName("head")[0].appendChild(link);

        var jasmineEnv = jasmine.getEnv();
        jasmineEnv.updateInterval = 1000;

        var htmlReporter = new jasmine.TrivialReporter(); // HtmlReporter();

        jasmineEnv.addReporter(htmlReporter);

        jasmineEnv.specFilter = function(spec) {
          return htmlReporter.specFilter(spec);
        };

        // we will be faking only REST calls
        sinon.FakeXMLHttpRequest.useFilters = true;
        sinon.FakeXMLHttpRequest.addFilter(function(method, url, async,
            username, password) {
          if (/^rest\//.test(url)) {
            return false;
          }
          return true;
        });

        /*
         * this seems to be a bug in !text -> only works here with relative path
         * we need to require these, so they are available later.
         */
        require([ "modules/tests/vesselTest", "modules/tests/sightingTest" ], function(vesselTest, sightingTest) {
          vesselTest();
          sightingTest();
          sinon.log = function(data) {
            console.log(data);
          };
          jasmineEnv.execute();
        });

        // clear

        // jasmine.currentEnv_ = null;
        // $('#TrivialReporter').remove();

        // jasmineEnv.execute();
      });

    }
  };
});