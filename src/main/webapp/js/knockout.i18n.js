// START SNIPPET: i18next1
define([ "knockout", "jquery", "i18next" ], function(ko, $, i18next) {

  // observable for the current active language
  ko.language = ko.observable();

  /*
   * returns a computed value for a given key. As it is dependent on the current
   * active language the outcome will change when the language changes.
   */
  ko.i18n = function(key) {
    return ko.computed(function() {
      if (ko.language() != null) {
        return i18next.t(key, {
          lng : ko.language()
        });
      } else {
        return "";
      }
    }, key);
  };

  /*
   * Initialize the translation. Initial language will be German, the fallback
   * language will be English. As a callback (after the translation resources
   * have been loaded) the active language is updated (and only then the
   * re-computation of any translations is triggered)
   */
  $.i18n.init({
    lng : "de",
    fallbackLng : "en"
  }, function() {
    ko.language("de");
  });

});
// START SNIPPET: i18next1
