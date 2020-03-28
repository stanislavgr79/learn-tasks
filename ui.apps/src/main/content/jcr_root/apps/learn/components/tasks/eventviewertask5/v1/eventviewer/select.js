"use strict";

use(function (){
var type = properties["type"];
var text = properties["searchEvent"];

if(type == 'single' && text != null){

    return {
	single: "true",
    all: "false"
	};

} else if (type == 'all'){

    return {
	single: "false",
    all: "true"
	};

  }

});