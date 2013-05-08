define([ "hasher", "sinon", "knockout", "jasmine-jquery", ], function(hasher,
    sinon, ko) {
  return function() {
    describe("Manage Vessels", function() {

      var server = null;


      // START SNIPPET: jasminesinon2
      var vesselList = ko.toJSON(getJSONFixture('vesselList.json'));
      var vessel = ko.toJSON(getJSONFixture('vessel.json'));
      var vesselNew = ko.toJSON(getJSONFixture('vesselNew.json'));
      // END SNIPPET: jasminesinon2

      beforeEach(function() {

        runs(function() {

          // to go the main page
          hasher.setHash("");

          // expect the main page to be shown
          expect($("#welcome")).toExist();

          // START SNIPPET: jasminesinon3
          // create a fake server with some responses
          server = sinon.fakeServer.create();

          // for each request define the expected response
          server.respondWith("GET", "rest/vessel", [ 200, {
            "Content-Type" : "application/json"
          }, vesselList ]);

          // also empty responses are possible
          server.respondWith("DELETE", "rest/vessel/1", [ 204, null, "" ]);

          server.respondWith("GET", "rest/vessel/1", [ 200, {
            "Content-Type" : "application/json"
          }, vessel ]);

          server.respondWith("PUT", "rest/vessel/1", [ 204, null, "" ]);

          server.respondWith("POST", "rest/vessel/new", [ 200, {
            "Content-Type" : "application/json"
          }, vesselNew ]);

          // all other requests will return a 404 error
          // END SNIPPET: jasminesinon3

          // go to this use case
          hasher.setHash("vessel/main");

        });

        // START SNIPPET: jasminesinon4
        /*
         * wait for the use case to appear. There might be several async
         * responses going on; therefore call server.respond() repeatedly.
         */
        waitsFor(function() {
          server.respond();
          return $("#vesselList").length != 0;
        }, "the list didn't appear", 1000);
        // END SNIPPET: jasminesinon4

        waitsFor(function() {
          return $("#vesselList > tbody > tr").length == 2;
        }, "the list wasn't filled", 1000);

      });

      afterEach(function() {
        // disable fake server
        server.restore();
      });

      it("shows a list of two vessels at the start", function() {

        expect($("#vesselList")).toBeVisible();
        expect($("#vesselList > tbody > tr")[0]).toContainHtml("Vessel 1");
        expect($("#vesselList > tbody > tr")[1]).toContainHtml("Vessel 2");

      });

      it("has the first menu item highlighed at the start", function() {

        expect($("#subfolder0")).toHaveClass('active');

      });

      // START SNIPPET: jasminesinon5
      it("removes a vessel when clicking on delete", function() {

        // starting with two vessels
        expect($("#vesselList > tbody > tr").length).toBe(2);

        // click on the first one
        $("a[name=delete]").first().click();

        // now make the server respond
        server.respond();

        // expect only the second to be left over
        expect($("#vesselList > tbody > tr").length).toBe(1);
        expect($("#vesselList > tbody > tr")[0]).toContainHtml("Vessel 2");

      });
      // END SNIPPET: jasminesinon5

      // START SNIPPET: jasminebasic3

      // this is what we want to describe with our test
      describe("Vessel Add", function() {

        // this is how we prepare the use case to be in a defined state
        beforeEach(function() {

          expect($("#vesselList > tbody > tr").length).toBe(2);

          expect($("a[name=edit]").length).toBe(2);

          expect($("#addButton")).toBeVisible();

          $("#addButton").click();
          server.respond();

        });

        // one behaviour after another is tested here
        it("we can add a vessel with add", function() {

          expect($("#vesselName0")).toBeVisible();

          // now the second menu item should be active
          expect($("#subfolder1")).toHaveClass('active');

        });

        // one behaviour after another is tested here
        it("we can remove and add a language", function() {

          // start with the language visible
          expect($("#removeLanguage0")).toBeVisible();

          // let's remove the language
          $("#removeLanguage0").click();

          // now the second menu item should be active
          expect($("#removeLanguage0")).not.toExist();

          // add the language again
          $("#addLanguage").click();

          // but should have re-appeared
          expect($("#removeLanguage0")).toExist();

        });

        // after each test a cleanup is performed - none is needed here.
        afterEach(function() {
          // nothing for now
        });

      });

      // END SNIPPET: jasminebasic3

      describe("Vessel edit", function() {

        beforeEach(function() {

          server.autoRespond = false;

          expect($("#vesselList > tbody > tr").length).toBe(2);

          expect($("a[name=edit]").length).toBe(2);

          $("a[name=edit]").first().click();

          server.respond();

        });

        it("show a vessel on editing", function() {

          expect($("#vesselName0")).toBeVisible();
          expect($("#vesselName0")).toHaveValue("Vessel 1 en");

        });

        it("returns on save to the list", function() {

          expect($("#save")).toBeVisible();

          $("#save").click();
          server.respond();

          expect($("#vesselList > tbody > tr").length).toBe(2);

        });

      });
    });
  };
});
