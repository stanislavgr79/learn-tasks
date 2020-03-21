$(document).ready(function() {

   var root = document.querySelector(':root');
   var row1 = $('#grid').attr("rowattr");
   var column1 = $('#grid').attr("columnattr");

   root.style.setProperty('--my-rowjs', row1);
   root.style.setProperty('--my-columnjs', column1);

});