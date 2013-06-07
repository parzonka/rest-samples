// START SNIPPET: requirejs2
require.config({
  shim : {
    jquery : {
      exports : "jQuery"
    },
    // knockout has internal checks to make it work with jQuery, see i.e.
    // registerEventHandler
    // therefore jQuery needs to be loaded before.
    knockout : {
      deps : [ 'jquery' ]
    },
    // END SNIPPET: requirejs2
    'knockout.validation' : {
      deps : [ 'knockout' ]
    },
    mapping : {
      deps : [ 'knockout' ]
    },
    bootstrap : {
      deps : [ 'jquery' ],
      exports : "bootstrap"
    },
    'knockout-jquery-ui.extensions' : {
      deps : [ 'jquery', 'knockout' ]
    },
    'jquery-ui-timepicker-addon' : {
      deps : [ 'jquery-ui' ]
    },
    'jquery-ui' : {
      deps : [ 'jquery' ]
    },
    'jquery.validate' : {
      deps : [ 'jquery' ]
    },
    'jasmine' : {
      exports : "jasmine"
    },
    'jasmine-junit' : {
      deps : [ 'jasmine' ]
    },
    'sinon' : {
      exports : "sinon"
    },
    'jasmine-html' : {
      deps : [ 'jasmine' ]
    },
    'jasmine-jquery' : {
      deps : [ 'jasmine' ]
    },
    'less' : {
      exports : 'less'
    }
  },

  // START SNIPPET: requirejs3
  paths : {
    jquery : 'libs/jquery.min',
    // END SNIPPET: requirejs3
    'jquery.validate' : 'libs/jquery.validate',
    hasher : 'libs/hasher',
    crossroads : 'libs/crossroads',
    knockout : 'libs/knockout.min',
    mapping : 'libs/knockout.mapping',
    bootstrap : 'libs/bootstrap',
    signals : 'libs/signals.min',
    text : 'libs/text',
    'jquery-ui' : 'libs/jquery-ui.min',
    'jquery-ui-timepicker-addon' : 'libs/jquery-ui-timepicker-addon',
    less : 'libs/less.min',
    jasmine : 'libs/jasmine',
    sinon : 'libs/sinon',
    moment : 'libs/moment',
    i18next : 'libs/i18next.amd.withJQuery-1.6.3',
    "jasmine-jquery" : 'libs/jasmine-jquery',
    "jasmine-junit" : 'libs/jasmine.junit_reporter',
    "jasmine-console" : 'libs/jasmine.console_reporter'
  }
});

less = {
  env : "development"
};

require([ "knockout", "menu", "hasher", "crossroads", "section", "test",
// starting here: only loading, passing as parameter
"stringTemplateEngine", "text", "bootstrap", "knockout.extensions", "less",
    "jquery.validate", "knockout.validation", "knockout.i18n" ],
    function(ko, menu, hasher, crossroads, Section, test) {

      // call this from the console to enable automatic refreshing of less
      // styles
      // less.watch();

      function ViewModel() {
        // Data
        var self = this;

        self.sections = {
          index : new Section("index")
        };

        self.index = ko.observable("index");

        self.currentSection = ko.computed(function() {
          var newSection = self.sections[self.index()];
          newSection.activate();
          return newSection;
        });

        self.menu = menu;

      }

      var m = new ViewModel();

      crossroads.addRoute(/^$/, function() {
        m.index("index");
      });

      crossroads.addRoute(/^([a-z]+)/, function(section) {
        if (!m.sections[section]) {
          m.sections[section] = new Section(section);
        }
        m.index(section);
      });

      // setup hasher
      function parseHash(newHash, oldHash) {
        crossroads.parse(newHash);
        m.menu.subfolder(newHash);
      }

      // crossroads.routed.add(console.log, console);
      crossroads.bypassed.add(function(request) {
        console.log('bypassed: ' + request);
      });
      crossroads.greedy = true;

      hasher.initialized.add(parseHash); // parse initial hash
      hasher.changed.add(parseHash); // parse hash changes
      hasher.init(); // start listening for history change

      $.validator.messages = {
        required : "Bitte einen Wert eingeben.",
        remote : "Bitte Wert korrigieren.",
        email : "Bitte eine gültige E-Mail-Adresse eingeben.",
        url : "Bitte eine gültige URL eingeben.",
        date : "Bitte gültiges Datum eingeben.",
        dateISO : "Bitte gültiges Datum eingeben (ISO).",
        number : "Bitte eine gültige Zahl eingeben.",
        digits : "Bitte nur Zahlen eingeben.",
        creditcard : "Bitte eine gültige Kreditkartennummer eingeben.",
        equalTo : "Bitte den wiederholten Wert korrigieren.",
        maxlength : $.validator
            .format("Bitte nicht mehr als {0} Buchstaben eingeben."),
        minlength : $.validator
            .format("Bitte mindestens {0} Buchstaben eingeben."),
        rangelength : $.validator
            .format("Bitte zwischen {0} und {1} Buchstaben eingeben."),
        range : $.validator
            .format("Bitte einen Wert zwischen {0} und {1} eingeben."),
        max : $.validator.format("Bitte einen Wert kleiner {0} eingeben."),
        min : $.validator.format("Bitte einen Wert größer {0} eingeben.")
      };

      $.validator.defaults.highlight = function(element) {
        $(element).closest('.control-group').removeClass('success').addClass(
            'error');
      };

      $.validator.defaults.errorClass = 'help-inline';

      $.validator.defaults.success = function(element) {
        element.text('').removeClass('error').addClass('valid').hide().closest(
            '.control-group').removeClass('error'); // .addClass('success');
      };

      ko.applyBindings(m);
      test();

    });
