define([ "knockout", "jquery" ], function(ko, $) {

  ko.bindingHandlers.fadeVisible = {
    init : function(element, valueAccessor) {
      // Start visible/invisible according to initial value
      var shouldDisplay = valueAccessor();
      $(element).toggle(shouldDisplay);
    },
    update : function(element, valueAccessor) {
      // On update, fade in/out
      var shouldDisplay = valueAccessor();
      shouldDisplay ? $(element).fadeIn() : $(element).toggle(false);
    }
  };

  ko.bindingHandlers.checkedRadioToBool = {
    init : function(element, valueAccessor, allBindingsAccessor) {
      var observable = valueAccessor(), interceptor = ko.computed({
        read : function() {
          return observable().toString();
        },
        write : function(newValue) {
          observable(newValue === "true");
        },
        owner : this
      });
      ko.applyBindingsToNode(element, {
        checked : interceptor
      });
    }
  };

  // http://knockoutjs.com/documentation/extenders.html

  ko.extenders.numeric = function(target, precision) {
    // create a writeable computed observable to intercept writes to our
    // observable
    var result = ko.computed({
      read : function() {
        /*
         * I don't want to convert the string to a number as I don't want to
         * lose precision. Therefore I use this rather crude mechanism.
         */
        var current = target();
        if (current) {
          current = current.replace(".", ",");
          if (current.indexOf(",") == -1 && precision != 0) {
            current = current + ",";
          }
          while (current.length - current.indexOf(",") <= precision) {
            current = current + "0";
          }
        }
        return current;
      },
      write : function(newValue) {
        var current = target();

        valueToWrite = newValue.replace(".", "").replace(",", ".");

        // only write if it changed
        if (valueToWrite !== current) {
          target(valueToWrite);
        } else {
          // if the rounded value is the same, but a different value was
          // written, force a notification for the current field
          if (newValue !== current) {
            target.notifySubscribers(valueToWrite);
          }
        }
      }
    });

    // return the new computed observable
    return result;
  };

});