/*******************************************************************************

 ******************************************************************************/
/* global Granite, Coral */
(function(document, $, Coral) {
    "use strict";

    var selectors = {
        dialogContent: ".cmp-event__editor",
        typeField: "[data-cmp-event-dialog-edit-hook='typeField']",
        typeRadio: "[data-cmp-event-dialog-edit-hook='typeField'] coral-radio",
        singleField: "[data-cmp-event-dialog-edit-hook='singleField']",
        allField: "[data-cmp-event-dialog-edit-hook='allField']",
    };

    var registry = $(window).adaptTo("foundation-registry");
    var typeField;
    var typeRadios;
    var foundationFieldSelectors;
    var singleField;
    var allField;

     var type = {
            SINGLE: "single",
            ALL: "all"
        };


    $(document).on("dialog-loaded", function(event) {
        var $dialog = event.dialog;

        if ($dialog.length) {
            var dialogContent = $dialog[0].querySelector(selectors.dialogContent);

            if (dialogContent) {
                typeField = dialogContent.querySelector(selectors.typeField);
                typeRadios = typeField.querySelectorAll(selectors.typeRadio);
                foundationFieldSelectors = getFoundationFieldSelectors();
                singleField = dialogContent.querySelector(selectors.singleField);
                allField = dialogContent.querySelector(selectors.allField);

                var hasCheckedTypeRadio = false;

               if (typeRadios.length) {
                                  for (var i = 0; i < typeRadios.length; i++) {
                                      var typeRadio = typeRadios[i];

                                      Coral.commons.ready(typeRadio, function(element) {
                                          var value = element.value;
                                          var showHideTarget = getShowHideTarget(typeField);

                                          if (element.checked) {
                                              toggleShowHideTargets(showHideTarget, value);
                                              hasCheckedTypeRadio = true;
                                          }

                                          element.on("change", function() {
                                              toggleShowHideTargets(showHideTarget, value);


                                          });
                                      });
                                  }

                    Coral.commons.nextFrame(function() {
                        if (!hasCheckedTypeRadio) {
                            typeRadios[0].checked = true;
                            typeRadios[0].trigger("change");
                        }
                    });

                    if (allField) {
                        Coral.commons.ready(allField, function(element) {
                            var showHideTarget = getShowHideTarget(element);

                            toggleShowHideTargets(showHideTarget, element.value);


                        });
                    }

                }
            }
        }
    });

    /**
     * Toggles the disabled state and visibility of elements matching the target.
     * Targets that match the provided value are enabled / shown, otherwise they are disabled / hidden.
     *
     * @param {String} target Comma separated list of targets to toggle
     * @param {String} value The value of the target to enable and show
     */
    function toggleShowHideTargets(target, value) {
        var showHideTargets = document.querySelectorAll(target);

        for (var i = 0; i < showHideTargets.length; i++) {
            var showHideTarget = showHideTargets[i];
            var showHideTargetValue = getShowHideTargetValue(showHideTarget);

            if (showHideTargetValue === value) {
                toggleTarget($(showHideTarget), true);
            } else {
                toggleTarget($(showHideTarget), false);
            }
        }
    }

    /**
     * Toggles a dialog toggle target, setting its disabled state, validity and visibility.
     *
     * @param {jQuery} $element The target
     * @param {Boolean} show true to disable and hide the target, false otherwise
     */
    function toggleTarget($element, show) {
        var field = $element.adaptTo("foundation-field");
        var toggleable = $element.parent(".foundation-toggleable").adaptTo("foundation-toggleable");

        if (show) {
            if (toggleable) {
                toggleable.show();
            } else {
                $element.show();
            }
            if (field) {
                field.setDisabled(false);
            }
        } else {
            if (toggleable) {
                toggleable.hide();
            } else {
                $element.hide();
            }

            if (field) {
                field.setDisabled(true);
                setFieldValid($element);
            }

        }

        toggleTargetChildren($element, show);
    }

    /**
     * Toggles child fields of a dialog toggle target, setting their disabled and valid states.
     *
     * @param {jQuery} $element The target
     * @param {Boolean} show true to disable and hide the target, false otherwise
     */
    function toggleTargetChildren($element, show) {
        var $childFoundationFields = $element.find(foundationFieldSelectors);
        $childFoundationFields.each(function(index, element) {
            var field = $(element).adaptTo("foundation-field");
            if (field) {
                if (show) {
                    field.setDisabled(false);
                } else {
                    field.setDisabled(true);
                    setFieldValid($(element));
                }
            }
        });
    }

    /**
     * Gets any registered foundation field selectors from the foundation registry.
     *
     * @returns {String} comma-separated foundation field selectors
     */
    function getFoundationFieldSelectors() {
        var fieldSelectors = registry.get("foundation.adapters").filter(function(adapter) {
            return adapter.type === "foundation-field" || adapter.type === "foundation-field-mixed";
        }).map(function(adapter) {
            return adapter.selector;
        });
        return fieldSelectors.join(",");
    }

    /**
     * Sets a field as valid and updates the validation UI.
     *
     * @param {jQuery} $element The field to set as valid
     */
    function setFieldValid($element) {
        var field = $element.adaptTo("foundation-field");
        var validation = $element.adaptTo("foundation-validation");
        if (field && validation) {
            field.setInvalid(false);
            validation.checkValidity();
            validation.updateUI();
        }
    }


    function getShowHideTarget(element) {
        return element.dataset["cmpEventDialogEditShowhidetarget"];
    }

    function getShowHideTargetValue(element) {
        return element.dataset["cmpEventDialogEditShowhidetargetvalue"];
    }


})(document, Granite.$, Coral);
