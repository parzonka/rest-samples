define([ "hasher", "sinon", "knockout", "jasmine-jquery", ], function(hasher,
    sinon, ko) {
  return function() {
    describe("Manage Timezones", function() {

      var server = null;
      // basic timezone responses
      var timezoneList = ko.toJSON(getJSONFixture('timezoneList.json'));
      var timezone = ko.toJSON(getJSONFixture('timezone.json'));
      var timezoneNew = ko.toJSON(getJSONFixture('timezoneNew.json'));

      beforeEach(function() {

        runs(function() {

          // to go the main page
          hasher.setHash("");

          // expect the main page to be shown
          expect($("#welcome")).toExist();

          // create a fake server with some responses
          server = sinon.fakeServer.create();

          server.respondWith("GET", "rest/timezone", [ 200, {
            "Content-Type" : "application/json"
          }, timezoneList ]);

          server.respondWith("DELETE", "rest/timezone/1", [ 204, null, "" ]);

          server.respondWith("GET", "rest/timezone/1", [ 200, {
            "Content-Type" : "application/json"
          }, timezone ]);

          server.respondWith("PUT", "rest/timezone/1", [ 204, null, "" ]);

          server.respondWith("POST", "rest/timezone/new", [ 200, {
            "Content-Type" : "application/json"
          }, timezoneNew ]);

          server.respondWith("POST", "rest/timezone", [ 201, null, "" ]);

          server.respondWith("GET", "rest/timezone", [ 200, {
            "Content-Type" : "application/json"
          }, timezoneList ]);

          // go to this use case
          hasher.setHash("timezone/main");

        });

        /*
         * wait for the use case to appear. There might be several async
         * responses going on; therefore call server.respond() repeatedly.
         */
        waitsFor(function() {
          server.respond();
          return $("#timezoneList").length != 0;
        }, "the list didn't appear", 1000);

        waitsFor(function() {
          return $("#timezoneList > tbody > tr").length == 2;
        }, "the list wasn't filled", 1000);

      });

      afterEach(function() {
        // disable fake server
        server.restore();
      });

      it("shows a list of two timezones at the start", function() {

        expect($("#timezoneList")).toBeVisible();
        expect($("#timezoneList > tbody > tr")[0]).toContainHtml(
            "Europe/Berlin");
        expect($("#timezoneList > tbody > tr")[1]).toContainHtml(
            "Europe/London");

      });

      it("has the first menu item highlighed at the start", function() {

        expect($("#subfolder0")).toHaveClass('active');

      });

      it("removes a timezone when clicking on delete", function() {

        // starting with two timezones
        expect($("#timezoneList > tbody > tr").length).toBe(2);

        // click on the first one
        $('#timezoneList a[name="delete"]').first().click();
        server.respond();

        // expect only the second to be left over
        expect($("#timezoneList > tbody > tr").length).toBe(1);
        expect($("#timezoneList > tbody > tr")[0]).toContainHtml("Europe/London");

      });

      describe("timezone Add", function() {
        beforeEach(function() {

          expect($("#timezoneList > tbody > tr").length).toBe(2);

          expect($("a[name=edit]").length).toBe(2);

          expect($("#addButton")).toBeVisible();

          $("#addButton").click();
          server.respond();

        });

        it("we can add a timezone with add", function() {

          expect($("#timezoneName")).toBeVisible();

          // now the second menu item should be active
          expect($("#subfolder1")).toHaveClass('active');

        });

        it("we can save the form", function() {

          $("#timezoneName")[0].value = "Honolulu";
          $("#timezoneMemo").change();

          $("#save").click();
          server.respond();
          expect($("#timezoneList > tbody > tr").length).toBe(2);

        });

      });

      describe("timezone edit", function() {

        beforeEach(function() {

          server.autoRespond = false;

          expect($("#timezoneList > tbody > tr").length).toBe(2);

          expect($("a[name=edit]").length).toBe(2);

          $("a[name=edit]").first().click();

          server.respond();

        });

        it("show a timezone on editing", function() {

          expect($("#timezoneName")).toBeVisible();
          expect($("#timezoneName")).toHaveValue("Europe/Berlin");

        });

        it("returns on save to the list", function() {

          expect($("#save")).toBeVisible();

          $("#save").click();
          server.respond();

          expect($("#timezoneList > tbody > tr").length).toBe(2);

        });

      });
    });
  };
});
