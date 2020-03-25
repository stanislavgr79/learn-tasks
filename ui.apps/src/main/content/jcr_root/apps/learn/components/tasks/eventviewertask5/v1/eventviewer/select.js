"use strict";

use(function (){
var type = properties["searchType"];
var text = properties["searchEvent"];

if(type == 'singleEventViewer' && text != null){

    return {
	single: "true",
    all: "false"
	};

} else if (type == 'allEventsViewer'){

    return {
	single: "false",
    all: "true"
	};

  }

});