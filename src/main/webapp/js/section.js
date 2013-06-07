define([ "knockout", "crossroads", "hasher", "i18next" ], function(ko,
    crossroads, hasher, i18next) {
  return function(name, template, style) {
    var self = this;
    this.name = name;
    this.template = template || name;
    this.data = ko.observable();
    this.style = style;
    this.loaded = false;
    this.activate = function() {
      // load view model from the server
      if (!this.loaded) {
        // START SNIPPET: i18next4
        /*
         * load the translation namespace before loading the module itself.
         * Otherwise translations might be evaluated before the resource is
         * available.
         */
        i18next.loadNamespace(self.template, function() {
          // END SNIPPET: i18next4
          // START SNIPPET: requirejs5
          require([ "modules/" + name,
              "text!../templates/" + self.template + ".html" ], function(
              Module, template) {
            ko.templates[self.template] = template;
            self.data(typeof Module === "function" ? new Module() : Module);
            self.loaded = true;
            crossroads.resetState();
            crossroads.parse(hasher.getHash());
          });
          // END SNIPPET: requirejs5
        });
      }
    };
  };
});