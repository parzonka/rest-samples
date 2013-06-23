define([ "knockout", "jquery" ], function(ko, $) {

  // this has been inspired by knockout validation
  // https://github.com/Knockout-Contrib/Knockout-Validation

  ko.bindingHandlers['validationCore'] = (function() {

    return {

      init : function(element, valueAccessor, allBindingsAccessor, viewModel,
          bindingContext) {

        if (ko.isObservable(valueAccessor())) {

          var validationMessageElement = document.createElement('span');
          element.parentNode.insertBefore(validationMessageElement,
              element.nextSibling);
          validationMessageElement.setAttribute("class", "help-inline");

          // add (for now empty) message to the observable
          if (!valueAccessor().message) {
            valueAccessor().message = ko.observable();
          }

          // computed flag to show span and to show error class
          var show = ko.computed(function() {
            if (valueAccessor().message() && valueAccessor().message() != "") {
              return true;
            } else {
              return false;
            }
          }, valueAccessor().message);

          ko.applyBindingsToNode(validationMessageElement, {
            text : valueAccessor().message,
            visible : show
          });

          var parentNode = element.parentNode;

          // find an element with control-group class and attach error class
          // conditionally.
          while (parentNode) {
            if (parentNode.className == "control-group") {
              ko.applyBindingsToNode(parentNode, {
                css : {
                  error : show
                }
              });
            }
            parentNode = parentNode.parentNode;
          }

        }
      },

      update : function(element, valueAccessor, allBindingsAccessor, viewModel,
          bindingContext) {
        // hook for future extensibility
      }
    };

  }());

  // override for KO's default 'value' binding
  (function() {
    var init = ko.bindingHandlers['value'].init;

    ko.bindingHandlers['value'].init = function(element, valueAccessor,
        allBindingsAccessor, viewModel, bindingContext) {

      init(element, valueAccessor, allBindingsAccessor);

      return ko.bindingHandlers['validationCore'].init(element, valueAccessor,
          allBindingsAccessor, viewModel, bindingContext);
    };
  }());

  ko.bindingHandlers['message'] = (function() {

    return {

      init : function(element, valueAccessor, allBindingsAccessor, viewModel,
          bindingContext) {

        var validationMessageElement = document.createElement('SPAN');
        element.appendChild(validationMessageElement);
        validationMessageElement.setAttribute("class", "help-inline");

        // add (for now empty) message to the observable
        if (!valueAccessor().message) {
          valueAccessor().message = ko.observable();
        }

        // computed flag to show span and to show error class
        var show = ko.computed(function() {
          if (valueAccessor().message() && valueAccessor().message() != "") {
            return true;
          } else {
            return false;
          }
        }, valueAccessor().message);

        ko.applyBindingsToNode(validationMessageElement, {
          text : valueAccessor().message,
          visible : show
        });

        var parentNode = element;

        // find an element with control-group class and attach error class
        // conditionally.
        while (parentNode) {
          if (parentNode.className == "control-group") {
            ko.applyBindingsToNode(parentNode, {
              css : {
                error : show
              }
            });
          }
          parentNode = parentNode.parentNode;
        }

      },

      update : function(element, valueAccessor, allBindingsAccessor, viewModel,
          bindingContext) {
        // hook for future extensibility
      }
    };

  }());

  ko.clearMessages = function(target) {
    for ( var i in target) {
      if (typeof (target[i]) === 'function' && $.isArray(target[i]())) {
        var array = target[i]();
        for ( var j = 0; j < array.length; ++j) {
          clearMessages(array[j]);
        }
      } else {
        if (target[i].message) {
          target[i].message("");
        }
      }
    }
  };

  ko.mergeMessages = function(target, messages) {
    ko.clearMessages();
    for ( var i in messages) {
      if ($.isArray(messages[i])) {
        var array = messages[i];
        for ( var j = 0; j < array.length; ++j) {
          if (array[j] != null) {
            mergeMessages(target[i]()[j], array[j]);
          }
        }
      } else if (i == "message") {
        if (target.message) {
          target.message(messages.message);
        } else {
          target.message = ko.observable(messages.message);
        }
      } else {
        if (!target[i]) {
          console.log("can't find " + i);
        } else if (target[i].message) {
          target[i].message(messages[i].message);
        } else {
          target[i].message = ko.observable(messages[i].message);
        }
      }
    }
  };

});