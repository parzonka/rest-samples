define([ 'knockout', 'jquery', 'mapping', 'hasher', 'crossroads', 'menu',
// starting here: only loading, passing as parameter
'knockout.hotkeys', 'knockout-jquery-ui.extensions' ], function(ko, $, mapping,
    hasher, crossroads, menu) {

  function SightingViewModel() {
    // Data
    var self = this;

    var NewSighting = null;

    self.sightingList = ko.observableArray(null);
    self.sighting = ko.observable();
    self.vessels = ko.observableArray();
    self.searchSighting = ko.observable();
    self.timezones = ko.observableArray();

    $.post("rest/sighting/new", function(data) {
      NewSighting = data;
      self.resetSearch();
    });

    function toViewModel(data) {
      data = jQuery.extend(true, {}, data);

      if (data.vessel) {
        self.vessels([ data.vessel ]);
        data.vessel = data.vessel.vesselId;
      }

      if (data.timezone) {
        self.timezones([ {
          timezoneName : data.sightingTimezone
        } ]);
      }

      $.getJSON("rest/vessel", function(data) {
        self.vessels(data);
      });
      $.getJSON("rest/timezone", function(data) {
        self.timezones(data);
      });
      return data;
    }

    function fromViewModel(data) {
      data = jQuery.extend(true, {}, data);

      if (data.vessel) {
        data.vessel = {
          vesselId : data.vessel
        };
      }

      return data;
    }

    function isEmptyObject(obj) {
      for ( var prop in obj) {
        if (Object.prototype.hasOwnProperty.call(obj, prop)) {
          return false;
        }
      }
      return true;
    }

    function _trimSearch(data) {
      $.each(data, function(name, value) {
        if (value && typeof (value) === 'object') {
          _trimSearch(value);
        }
        if ($.trim(value) == "") {
          delete data[name];
        }
        if (value == {}) {
          delete data[name];
        }
      });
      return data;
    }

    function trimSearch(data) {
      data = jQuery.extend(true, {}, data);
      return _trimSearch(data);
    }

    self.resetSearch = function() {
      self.searchSighting(toViewModel(mapping.fromJS(NewSighting)));
    };

    // Operations
    self.saveSighting = function() {
      var v = $('#editForm').validate();

      if (!$('#editForm').valid()) {
        v.lastActive = null;
        v.focusInvalid();
        return;
      }

      var obj = fromViewModel(ko.toJS(self.sighting));
      if (obj.sightingId == null) { // INSERT
        $.ajax({
          type : "POST",
          url : "rest/sighting",
          data : mapping.toJSON(obj),
          contentType : 'application/json'
        }).error(
            function(error) {
              ko.mergeMessages(self.sighting(), ko.utils
                  .parseJson(error.responseText));
            }).success(function() {
          self.goToMain();
        });
      } else { // UPDATE
        $.ajax({
          type : "PUT",
          url : "rest/sighting/" + obj.sightingId,
          data : mapping.toJSON(obj),
          contentType : 'application/json'
        }).error(
            function(error) {
              ko.mergeMessages(self.sighting(), ko.utils
                  .parseJson(error.responseText));
            }).success(function() {
          self.goToMain();
        });
      }
    };

    self.search = function() {
      var obj = trimSearch(fromViewModel(mapping.toJS(self.searchSighting)));

      $.ajax({
        type : "POST",
        url : "rest/sighting/qbe",
        data : ko.toJSON(obj),
        contentType : 'application/json'
      }).error(function() {
        alert("error");
      }).success(function(data, textStatus, jqXHR) {
        if (jqXHR.status == 204) { // no content
          self.sightingList([]);
        } else {
          self.sightingList(data);
        }
      });
    };

    self.deleteSighting = function(sighting) {
      $.ajax({
        type : "DELETE",
        url : "rest/sighting/" + sighting.sightingId
      }).error(function() {
        alert("error");
      }).success(function() {
        self.sightingList.remove(sighting);
      });
    };

    self.goToMain = function() {
      hasher.setHash('sighting/main');
    };
    self.goToEdit = function(sighting) {
      hasher.setHash('sighting/edit/' + sighting.sightingId);
    };
    self.goToAdd = function() {
      hasher.setHash('sighting/add');
    };

    self.menu = [ {
      name : 'Sichtungen suchen/pflegen',
      link : '#/sighting/main'
    }, {
      name : 'Neue Sichtung',
      link : '#/sighting/add'
    } ];

    crossroads.addRoute(/sighting\//, function(id) {
      if (menu.subfolders() != self.menu) {
        menu.subfolders(self.menu);
      }
    });

    crossroads.addRoute(/sighting\/add$/, function() {
      self.sightingList(null);
      if (NewSighting == null) {
        $.post("rest/sighting/new", function(data) {
          NewSighting = data;
          self.sighting(mapping.fromJS(toViewModel(NewSighting)));
        });
      } else {
        self.sighting(mapping.fromJS(toViewModel(NewSighting)));
      }
    });

    crossroads.addRoute(/sighting\/edit\/(.+)$/, function(id) {
      $.get("rest/sighting/" + id, function(data) {
        self.sighting(mapping.fromJS(toViewModel(data)));
        self.sightingList(null);
      });
    });

    crossroads.addRoute(/sighting\/main/, function() {
      $.get("rest/sighting", function(data, textStatus, jqXHR) {
        if (jqXHR.status == 204) { // no content
          self.sightingList([]);
        } else {
          self.sightingList(data);
        }
        self.sighting(null);
      });
    });

  }
  ;

  return SightingViewModel;
});
