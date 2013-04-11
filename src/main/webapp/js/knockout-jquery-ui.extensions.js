// http://stackoverflow.com/questions/7537002/autocomplete-combobox-with-knockout-js-template-jquery

//jqAuto -- main binding (should contain additional options to pass to autocomplete)
//jqAutoSource -- the array of choices
//jqAutoValue -- where to write the selected value
//jqAutoSourceLabel -- the property that should be displayed in the possible choices
//jqAutoSourceInputValue -- the property that should be displayed in the input box
//jqAutoSourceValue -- the property to use for the value
define(
    [ "knockout", "jquery",
    // other libs
    "jquery-ui", "jquery-ui-timepicker-addon", "moment" ],
    function(ko, $) {

      ko.bindingHandlers.jqAuto = {
        init : function(element, valueAccessor, allBindingsAccessor, viewModel) {
          var options = valueAccessor() || {}, allBindings = allBindingsAccessor(), unwrap = ko.utils.unwrapObservable, modelValue = allBindings.jqAutoValue, source = allBindings.jqAutoSource, valueProp = allBindings.jqAutoSourceValue, inputValueProp = allBindings.jqAutoSourceInputValue
              || valueProp, labelProp = allBindings.jqAutoSourceLabel
              || valueProp;

          // function that is shared by both select and change event handlers
          function writeValueToModel(valueToWrite) {
            if (ko.isWriteableObservable(modelValue)) {
              modelValue(valueToWrite);
            } else { // write to non-observable
              if (allBindings['_ko_property_writers']
                  && allBindings['_ko_property_writers']['jqAutoValue'])
                allBindings['_ko_property_writers']['jqAutoValue']
                    (valueToWrite);
            }
          }

          // on a selection write the proper value to the model
          options.select = function(event, ui) {
            writeValueToModel(ui.item ? ui.item.actualValue : null);
          };

          // on a change, make sure that it is a valid value or clear out the
          // model
          // value
          options.change = function(event, ui) {
            var currentValue = $(element).val();
            var matchingItem = ko.utils.arrayFirst(unwrap(source), function(
                item) {
              return unwrap(item[inputValueProp]) === currentValue;
            });

            if (!matchingItem) {
              writeValueToModel(null);
            }
          }

          // handle the choices being updated in a DO, to decouple value updates
          // from source (options) updates
          var mappedSource = ko.dependentObservable(function() {
            mapped = ko.utils.arrayMap(unwrap(source), function(item) {
              var result = {};
              result.label = labelProp ? unwrap(item[labelProp]) : unwrap(item)
                  .toString(); // show
              // in
              // pop-up
              // choices
              result.value = inputValueProp ? unwrap(item[inputValueProp])
                  : unwrap(item).toString(); // show
              // in
              // input
              // box
              result.actualValue = valueProp ? unwrap(item[valueProp]) : item; // store
              // in
              // model
              return result;
            });
            return mapped;
          });

          // whenever the items that make up the source are updated, make sure
          // that
          // autocomplete knows it
          mappedSource.subscribe(function(newValue) {
            $(element).autocomplete("option", "source", newValue);
          });

          options.source = mappedSource();

          // initialize autocomplete
          $(element).autocomplete(options);
        },
        update : function(element, valueAccessor, allBindingsAccessor,
            viewModel) {
          // update value based on a model change
          var allBindings = allBindingsAccessor(), unwrap = ko.utils.unwrapObservable, modelValue = unwrap(allBindings.jqAutoValue)
              || '', valueProp = allBindings.jqAutoSourceValue, inputValueProp = allBindings.jqAutoSourceInputValue
              || valueProp;

          // if we are writing a different property to the input than we are
          // writing
          // to the model, then locate the object
          if (valueProp && inputValueProp !== valueProp) {
            var source = unwrap(allBindings.jqAutoSource) || [];
            var modelValue = ko.utils.arrayFirst(source, function(item) {
              return unwrap(item[valueProp]) === modelValue;
            }) || {}; // probably don't need the || {}, but just protect against
            // a
            // bad value
          }

          // update the element with the value that should be shown in the input
          $(element)
              .val(
                  modelValue && inputValueProp !== valueProp ? unwrap(modelValue[inputValueProp])
                      : modelValue.toString());
        }
      };

      ko.bindingHandlers.jqAutoCombo = {
        init : function(element, valueAccessor) {
          var autoEl = $("#" + valueAccessor());

          $(element).click(function() {
            // close if already visible
            if (autoEl.autocomplete("widget").is(":visible")) {
              console.log("close");
              autoEl.autocomplete("close");
              return;
            }

            // autoEl.blur();
            autoEl.autocomplete("option", "minLength", 0);
            autoEl.autocomplete("search", "");
            autoEl.focus();

          });

        }
      }

      // http://stackoverflow.com/questions/6612705/knockout-with-jquery-ui-datepicker

      ko.bindingHandlers.datepicker = {
        init : function(element, valueAccessor, allBindingsAccessor) {
          // initialize datepicker with some optional options
          var options = allBindingsAccessor().datepickerOptions || {};
          $(element).datepicker(options);

          // handle the field changing
          ko.utils.registerEventHandler(element, "change", function() {
            var observable = valueAccessor();
            observable($.datepicker.formatDate('yy-mm-dd', $(element)
                .datepicker("getDate")));
          });

          // handle disposal (if KO removes by the template binding)
          ko.utils.domNodeDisposal.addDisposeCallback(element, function() {
            $(element).datepicker("destroy");
          });

        },
        update : function(element, valueAccessor) {
          var value = ko.utils.unwrapObservable(valueAccessor());

          if (value) {
            value = $.datepicker.parseDate('yy-mm-dd', value);
          }

          current = $(element).datepicker("getDate");

          if (value - current !== 0) {
            $(element).datepicker("setDate", value);
          }
        }
      };

      /* German translation for the jQuery Timepicker Addon */
      /* Written by Marvin */
      (function($) {
        $.timepicker.regional['de'] = {
          timeOnlyTitle : 'Zeit Wählen',
          timeText : 'Zeit',
          hourText : 'Stunde',
          minuteText : 'Minute',
          secondText : 'Sekunde',
          millisecText : 'Millisekunde',
          timezoneText : 'Zeitzone',
          currentText : 'Jetzt',
          closeText : 'Fertig',
          timeFormat : 'HH:mm',
          amNames : [ 'vorm.', 'AM', 'A' ],
          pmNames : [ 'nachm.', 'PM', 'P' ],
          isRTL : false
        };
        $.timepicker.setDefaults($.timepicker.regional['de']);
      })(jQuery);

      /* German initialisation for the jQuery UI date picker plugin. */
      /* Written by Milian Wolff (mail@milianw.de). */
      jQuery(function($) {
        $.datepicker.regional['de'] = {
          clearText : 'löschen',
          clearStatus : 'aktuelles Datum löschen',
          closeText : 'schließen',
          closeStatus : 'ohne Änderungen schließen',
          prevText : '&#x3c;zurück',
          prevStatus : 'letzten Monat zeigen',
          nextText : 'Vor&#x3e;',
          nextStatus : 'nächsten Monat zeigen',
          currentText : 'heute',
          currentStatus : '',
          monthNames : [ 'Januar', 'Februar', 'März', 'April', 'Mai', 'Juni',
              'Juli', 'August', 'September', 'Oktober', 'November', 'Dezember' ],
          monthNamesShort : [ 'Jan', 'Feb', 'Mär', 'Apr', 'Mai', 'Jun', 'Jul',
              'Aug', 'Sep', 'Okt', 'Nov', 'Dez' ],
          monthStatus : 'anderen Monat anzeigen',
          yearStatus : 'anderes Jahr anzeigen',
          weekHeader : 'Wo',
          weekStatus : 'Woche des Monats',
          dayNames : [ 'Sonntag', 'Montag', 'Dienstag', 'Mittwoch',
              'Donnerstag', 'Freitag', 'Samstag' ],
          dayNamesShort : [ 'So', 'Mo', 'Di', 'Mi', 'Do', 'Fr', 'Sa' ],
          dayNamesMin : [ 'So', 'Mo', 'Di', 'Mi', 'Do', 'Fr', 'Sa' ],
          dayStatus : 'Setze DD als ersten Wochentag',
          dateStatus : 'Wähle D, M d',
          dateFormat : 'dd.mm.yy',
          firstDay : 1,
          initStatus : 'Wähle ein Datum',
          isRTL : false
        };
        $.datepicker.setDefaults($.datepicker.regional['de']);
      });

      ko.bindingHandlers.datetimepicker = {
        init : function(element, valueAccessor, allBindingsAccessor) {
          // initialize datepicker with some optional options
          var options = allBindingsAccessor().datepickerOptions || {};
          $(element).datetimepicker(options);

          // handle the field changing
          ko.utils.registerEventHandler(element, "change", function() {
            var observable = valueAccessor();
            var date = $(element).datetimepicker("getDate");
            if (date != null) {
              observable($.datepicker.formatDate("yy-mm-dd", date) + 'T'
                  + $.datepicker.formatTime("HH:mm:ss.l", {
                    hour : date.getHours(),
                    minute : date.getMinutes(),
                    second : date.getSeconds()
                  }));
            } else {
              observable(null);
            }
          });

          // handle disposal (if KO removes by the template binding)
          ko.utils.domNodeDisposal.addDisposeCallback(element, function() {
            $(element).datetimepicker("destroy");
          });

        },
        update : function(element, valueAccessor) {
          var value = ko.utils.unwrapObservable(valueAccessor());

          // handle date data coming via json from Microsoft
          if (String(value).indexOf('/Date(') == 0) {
            value = new Date(parseInt(value
                .replace(/\/Date\((.*?)\)\//gi, "$1")));
          } else if (!(value instanceof Date)) {
            /*
             * use moment here to parse the date, as otherwise chrome would
             * switch time zones and wouldn't show it 1:1. Do nothing if value
             * is null.
             */
            if (value) {
              value = moment(value).toDate();
            }
          }

          current = $(element).datetimepicker("getDate");

          if (value - current !== 0) {
            $(element).datetimepicker("setDate", value);
          }
        }
      };

    });