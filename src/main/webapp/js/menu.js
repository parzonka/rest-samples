define([ 'knockout', 'crossroads' ], function(ko, crossroads) {
  var Menu = function() {
    // Data
    var self = this;

    self.folders = ko.observableArray([ {
      name : 'Schiffstypen',
      link : '#/vessel/main'
    }, {
      name : 'Sichtungen',
      link : '#/sighting/main'
    }, {
      name : 'Zeitzonen',
      link : '#/timezone/main'
    } ]);

    self.folder = ko.observable();

    crossroads.addRoute(/^([a-z]*)\//, function(id) {
      self.folder('#' + id + '/main');
    });

    self.subfolders = ko.observableArray();

    self.subfolder = ko.observable();

    self.isCurrentSubfolder = function (link) {
      if("#/" + self.subfolder() == link) {
        return true;
      } else {
        return false;
      }
    };

  };

  return new Menu();

});
