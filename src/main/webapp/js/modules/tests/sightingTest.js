define([ "hasher", "sinon", "knockout", "jasmine-jquery", ], function(hasher,
    sinon, ko) {
  return function() {
    describe("Manage Sightings", function() {

      var server = null;
      // basic sighting responses
      var sightingList = ko.toJSON(getJSONFixture('sightingList.json'));
      var sighting = ko.toJSON(getJSONFixture('sighting.json'));
      var sightingNew = ko.toJSON(getJSONFixture('sightingNew.json'));
      // drop down list for the vessels
      var vesselList = ko.toJSON(getJSONFixture('vesselList.json'));
      var timezoneList = ko.toJSON(getJSONFixture('timezoneList.json'));

      beforeEach(function() {

        runs(function() {

          // to go the main page
          hasher.setHash("");

          // expect the main page to be shown
          expect($("#welcome")).toExist();

          // create a fake server with some responses
          server = sinon.fakeServer.create();

          server.respondWith("GET", "rest/sighting", [ 200, {
            "Content-Type" : "application/json"
          }, sightingList ]);

          server.respondWith("POST", "rest/sighting/qbe", [ 200, {
            "Content-Type" : "application/json"
          }, sightingList ]);

          server.respondWith("DELETE", "rest/sighting/1", [ 204, null, "" ]);

          server.respondWith("GET", "rest/sighting/1", [ 200, {
            "Content-Type" : "application/json"
          }, sighting ]);

          server.respondWith("PUT", "rest/sighting/1", [ 204, null, "" ]);

          server.respondWith("POST", "rest/sighting/new", [ 200, {
            "Content-Type" : "application/json"
          }, sightingNew ]);

          server.respondWith("POST", "rest/sighting", [ 201, null, "" ]);

          server.respondWith("GET", "rest/vessel", [ 200, {
            "Content-Type" : "application/json"
          }, vesselList ]);

          server.respondWith("GET", "rest/timezone", [ 200, {
            "Content-Type" : "application/json"
          }, timezoneList ]);

          // go to this use case
          hasher.setHash("sighting/main");

        });

        /*
         * wait for the use case to appear. There might be several async
         * responses going on; therefore call server.respond() repeatedly.
         */
        waitsFor(function() {
          server.respond();
          return $("#sightingList").length != 0;
        }, "the list didn't appear", 1000);

        waitsFor(function() {
          return $("#sightingList > tbody > tr").length == 2;
        }, "the list wasn't filled", 1000);

      });

      afterEach(function() {
        // disable fake server
        server.restore();
      });

      it("shows a list of two sightings at the start", function() {

        expect($("#sightingList")).toBeVisible();
        expect($("#sightingList > tbody > tr")[0]).toContainHtml("Vessel 1");
        expect($("#sightingList > tbody > tr")[1]).toContainHtml("Vessel 2");

      });

      it("has the first menu item highlighed at the start", function() {

        expect($("#subfolder0")).toHaveClass('active');

      });

      it("removes a sighting when clicking on delete", function() {

        // starting with two sightings
        expect($("#sightingList > tbody > tr").length).toBe(2);

        // click on the first one
        $('#sightingList a[name="delete"]').first().click();
        server.respond();

        // expect only the second to be left over
        expect($("#sightingList > tbody > tr").length).toBe(1);
        expect($("#sightingList > tbody > tr")[0]).toContainHtml("Vessel 2");

      });

      describe("sighting Add", function() {
        beforeEach(function() {

          expect($("#sightingList > tbody > tr").length).toBe(2);

          expect($("a[name=edit]").length).toBe(2);

          expect($("#addButton")).toBeVisible();

          $("#addButton").click();
          server.respond();

        });

        it("we can add a sighting with add", function() {

          expect($("#sightingMemo")).toBeVisible();

          // now the second menu item should be active
          expect($("#subfolder1")).toHaveClass('active');

        });

        it("we can save the form", function() {

          runs(function() {

            $("#vessel")[0].value = 1;
            $("#vessel").change();

            $("#sightingMemo")[0].value = "memo";
            $("#sightingMemo").change();

            expect($("#ui-datepicker-div")).not.toBeVisible();

            $("#sightingDate")[0].focus();
            // hm... date will not end up in final response
            $("#sightingDate")[0].value = "06.04.2013 10:00";
            $("#sightingDate").change();

            expect($("#ui-datepicker-div")).toBeVisible();

            $("button.ui-datepicker-close")[0].click();
          });

          waitsFor(function() {
            return $("#ui-datepicker-div:visible").length == 0;
          }, "the date picker to disappear", 500);

          runs(function() {
            expect($("#ui-datepicker-div")).not.toBeVisible();

            $("#sightingTimezone")[0].value = "Europe/Berlin";
            $("#sightingTimezone").change();

            $("#save").click();
            server.respond();
            expect($("#sightingList > tbody > tr").length).toBe(2);

          });

        });

      });

      describe("sighting edit", function() {

        beforeEach(function() {

          server.autoRespond = false;

          expect($("#sightingList > tbody > tr").length).toBe(2);

          expect($("a[name=edit]").length).toBe(2);

          $("a[name=edit]").first().click();

          server.respond();

        });

        it("show a sighting on editing", function() {

          expect($("#sightingMemo")).toBeVisible();
          expect($("#sightingMemo")).toHaveValue("strange!");

        });

        it("show a search result when searching", function() {

          /*
           * TODO: add a spy here to find out that the query by example has been
           * called. Instead do a hard removal for the moment.
           */
          $("#sightingList > tbody > tr").remove();

          expect($("#sightingList > tbody > tr").length).toBe(0);

          $("#search").click();

          server.respond();

          expect($("#sightingList > tbody > tr").length).toBe(2);

        });

        it("returns on save to the list", function() {

          expect($("#save")).toBeVisible();

          $("#save").click();
          server.respond();

          expect($("#sightingList > tbody > tr").length).toBe(2);

        });

      });
    });
  };
});
