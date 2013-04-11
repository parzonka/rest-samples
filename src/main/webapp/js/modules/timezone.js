define([ 'knockout', 'jquery', 'mapping', 'hasher', 'crossroads', 'menu',
// others
'knockout.validation' ], function(ko, $, mapping, hasher, crossroads, menu) {

  function TimezoneViewModel() {
    // Data
    var self = this;

    var NewTimezone;

    self.timezoneList = ko.observableArray(null);
    self.timezone = ko.observable();

    // Operations
    self.saveTimezone = function() {
      var v = $('#editForm').validate();

      if (!$('#editForm').valid()) {
        v.lastActive = null;
        v.focusInvalid();
        return;
      }

      var obj = ko.toJS(self.timezone);
      if (obj.timezoneId == null) { // INSERT
        $.ajax({
          type : "POST",
          url : "rest/timezone",
          data : mapping.toJSON(obj),
          contentType : 'application/json'
        }).error(
            function(error) {
              ko.mergeMessages(self.timezone(), ko.utils
                  .parseJson(error.responseText));
            }).success(function() {
          self.goToMain();
        });
      } else { // UPDATE
        $.ajax({
          type : "PUT",
          url : "rest/timezone/" + obj.timezoneId,
          data : mapping.toJSON(obj),
          contentType : 'application/json'
        }).error(
            function(error) {
              ko.mergeMessages(self.timezone(), ko.utils
                  .parseJson(error.responseText));
            }).success(function() {
          self.goToMain();
        });
      }
    };
    self.deleteTimezone = function(timezone) {
      $.ajax({
        type : "DELETE",
        url : "rest/timezone/" + timezone.timezoneId
      }).error(function() {
        alert("error");
      }).success(function() {
        self.timezoneList.remove(timezone);
      });
    };

    self.goToMain = function() {
      hasher.setHash('timezone/main');
    };
    self.goToEdit = function(timezone) {
      hasher.setHash('timezone/edit/' + timezone.timezoneId);
    };
    self.goToAdd = function() {
      hasher.setHash('timezone/add');
    };

    self.menu = [ {
      name : 'Zeitzone suchen/pflegen',
      link : '#/timezone/main'
    }, {
      name : 'Neue Zeitzone',
      link : '#/timezone/add'
    } ];

    crossroads.addRoute(/timezone\//, function(id) {
      if (menu.subfolders() != self.menu) {
        menu.subfolders(self.menu);
      }
    });

    crossroads.addRoute(/timezone\/add$/, function() {
      self.timezoneList(null);
      if (NewTimezone) {
        self.timezone(mapping.fromJS(NewTimezone));
      } else {
        $.post("rest/timezone/new", function(data) {
          NewTimezone = data;
          self.timezone(mapping.fromJS(NewTimezone));
        });
      }
    });

    crossroads.addRoute(/timezone\/edit\/(.+)$/, function(id) {
      $.get("rest/timezone/" + id, function(data) {
        self.timezone(mapping.fromJS(data));
        self.timezoneList(null);
      });
    });

    crossroads.addRoute(/timezone\/main/, function() {
      $.get("rest/timezone", function(data, textStatus, jqXHR) {
        if (jqXHR.status == 204) { // no content
          self.timezoneList([]);
        } else {
          self.timezoneList(data);
        }
        self.timezone(null);
      });
    });

  }
  ;

  return TimezoneViewModel;
});
