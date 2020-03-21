"use strict";
use(function () {
    var info = {};

    info.row = resource.properties["row"] ? resource.properties["row"] : 0;
    info.column = resource.properties["column"] ? resource.properties["column"] : 0;
    info.style = 'grid-template-columns: repeat('+info.column+', 1fr)';

     return info;
});