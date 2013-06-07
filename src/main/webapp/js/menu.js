define([ 'knockout', 'crossroads', 'knockout.i18n' ], function(ko, crossroads) {
  var Menu = function() {
    // Data
    var self = this;

    // START SNIPPET: i18next3
    self.folders = ko.observableArray([ {
      name : ko.i18n('vessel.main'),
      link : '#/vessel/main'
    }, {
      name : ko.i18n('sighting.main'),
      link : '#/sighting/main'
    }, {
      name : ko.i18n('timezone.main'),
      link : '#/timezone/main'
    } ]);
    // END SNIPPET: i18next3

    self.folder = ko.observable();

    crossroads.addRoute(/^([a-z]*)\//, function(id) {
      self.folder('#' + id + '/main');
    });

    self.subfolders = ko.observableArray();

    self.subfolder = ko.observable();

    self.isCurrentSubfolder = function(link) {
      if ("#/" + self.subfolder() == link) {
        return true;
      } else {
        return false;
      }
    };

    // START SNIPPET: i18next7
    self.language = ko.observable("de");

    /* subscribe to the variable above. When it is changed, change the language
     * in i18next as well. Only after the language resources have been loaded, the observable
     * for the language computeds is updated.
     */
    self.language.subscribe(function(newLanguage) {
      $.i18n.setLng(newLanguage, function() {
        ko.language(newLanguage);
      });
    });
    // END SNIPPET: i18next7

  };

  return new Menu();

});
