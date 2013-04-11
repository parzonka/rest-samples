define(
    [ 'knockout', 'jquery', 'mapping', 'hasher', 'crossroads', 'menu' ],
    function(ko, $, mapping, hasher, crossroads, menu) {

      function toViewModel(data) {
        data = jQuery.extend(true, {}, data);

        var text = [];
        $.each(data.vesselName, function(key, value) {
          text.push({
            textLanguage : key,
            textString : value
          });
        });
        data.vesselName = text;

        return data;
      }

      function fromViewModel(data) {
        data = jQuery.extend(true, {}, data);

        var text = {};
        data.vesselName.forEach(function(element) {
          text[element.textLanguage] = element.textString;
        });
        data.vesselName = text;

        return data;
      }

      function VesselViewModel() {
        // Data
        var self = this;

        var NewVessel;

        self.vesselList = ko.observableArray(null);
        self.vessel = ko.observable();

        // Operations
        self.saveVessel = function() {
          var obj = fromViewModel(ko.toJS(self.vessel));
          if (obj.vesselId == null) { // INSERT
            $.ajax({
              type : "POST",
              url : "rest/vessel",
              data : mapping.toJSON(obj),
              contentType : 'application/json'
            }).error(function() {
              alert("error");
            }).success(function() {
              self.goToMain();
            });
          } else { // UPDATE
            $.ajax({
              type : "PUT",
              url : "rest/vessel/" + obj.vesselId,
              data : mapping.toJSON(obj),
              contentType : 'application/json'
            }).error(function(error) {
              alert("error");
            }).success(function() {
              self.goToMain();
            });
          }
        };

        self.deleteLanguage = function(language) {
          self.vessel().vesselName.remove(language);
        };

        self.deleteVessel = function(vessel) {
          $.ajax({
            type : "DELETE",
            url : "rest/vessel/" + vessel.vesselId
          }).error(function() {
            alert("error");
          }).success(function() {
            self.vesselList.remove(vessel);
          });
        };

        self.addLanguage = function() {
          self.vessel().vesselName.push(mapping.fromJS({
            textLanguage : "",
            textString : ""
          }));
        };

        self.goToMain = function() {
          hasher.setHash('vessel/main');
        };
        self.goToEdit = function(vessel) {
          hasher.setHash('vessel/edit/' + vessel.vesselId);
        };
        self.goToAdd = function() {
          hasher.setHash('vessel/add');
        };

        self.menu = [ {
          name : 'Schiffstyp suchen/pflegen',
          link : '#/vessel/main'
        }, {
          name : 'Neuer Schiffstyp',
          link : '#/vessel/add'
        } ];

        crossroads.addRoute(/vessel\//, function(id) {
          if (menu.subfolders() != self.menu) {
            menu.subfolders(self.menu);
          }
        });

        crossroads.addRoute(/vessel\/add$/, function() {
          self.vesselList(null);
          if (NewVessel) {
            self.vessel(mapping.fromJS(toViewModel(NewVessel)));
          } else {
            $.post("rest/vessel/new", function(data) {
              NewVessel = data;
              self.vessel(mapping.fromJS(toViewModel(NewVessel)));
            });
          }
        });

        crossroads.addRoute(/vessel\/edit\/(.+)$/, function(id) {
          $.get("rest/vessel/" + id, function(data) {
            self.vessel(mapping.fromJS(toViewModel(data)));
            self.vesselList(null);
          });
        });

        crossroads.addRoute(/vessel\/main/, function() {
          $.get("rest/vessel", function(data, textStatus, jqXHR) {
            if (jqXHR.status == 204) { // no content
              self.vesselList([]);
            } else {
              self.vesselList(data);
            }
            self.vessel(null);
          });
        });

      }

      return VesselViewModel;
    });
