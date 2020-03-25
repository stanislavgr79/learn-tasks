$(document).ready(function(){
    type = $('.event-Form').attr("search-type");
    console.log("type: " + type);

    if(type == 'singleEventViewer' && $('#single-event').attr("search-value") != null){
		getSingleEventViewer();
    } else if (type == 'allEventsViewer'){
		getAllEventsViewer();
    } else {
			   var out = "";
        console.log('!!!!!!!!!!!!!!!');
	 		   out +="<table id='table-viewer-single'>";
               out +="<caption id='caption-single-nofound'> No selected propety in Event viewer-component</caption>";
			   out +="</table>";
			   $('#no-content').html(out);
    }

})

$('#single-event').onpropertychange = function() {
    if (event.propertyName == "search-value") getSingleEventViewer();
  }


function getSingleEventViewer(){
  console.log('run single');
  var path_eventForm = $('#eventForm').attr("path-eventForm");
  var method_event = $('#eventForm').attr("method-event");
  var searchValue = $('#single-event').attr("search-value");

  console.log('search by: ' + searchValue);

  var params = {
              searchEvent: searchValue
          };

 if(type != ''){

        $.ajax({
            type: method_event,
            cache: false,
            url: path_eventForm + '.single.json',
            data: params,
            contentType: 'application/json',
            success: function(response){

            console.log("response single: " + response);
			var output ="";

            if(response != null ) {

			output +="<table id='table-viewer-single'>";
			output +="<caption id='caption-col-single'>Event: "  + response.title + "</caption>";
     		output +="<tr id >";
            output +="<th id='th-col-single' scope='col-single'>Title</th>";
     		output +="<th id='th-col-single' scope='col-single'>Date</th>";
     		output +="<th id='th-col-single' scope='col-single'>Place</th>";
     		output +="<th id='th-col-single' scope='col-single'>Topic</th>";
			output +="</tr>";
			output +="<tr>";
			output +=" <td id='td-col-single'>"+response.title+"</td><td id='td-col-single'>"+response.eventStartDate+"</td><td id='td-col-single'>"+response.place+"</td><td id='td-col-single'>"+response.topic+"</td>";
			output +="</tr>";
			output +="<tr>";
			output +="<td id='description-viewer-single' colspan='4'>Description: " + response.description + "</td>";
			output +="</tr>";
            output +="</table>";

             $('#single-event').html(output);

        } else {
			   output +="<table id='table-viewer-single'>";
               output +="<caption id='caption-single-nofound'>"  + "No found Event by Title: " + $('#single-event').attr("search-value") +"</caption>";
			   output +="</table>";
			   $('#single-event').html(output);

               }

            }
        });
  }
      else {
         $('#single-event').html('');
      }


}

function getAllEventsViewer(){
    console.log('run all');
    var path_eventForm = $('#eventForm').attr("path-eventForm");
    var method_event = $('#eventForm').attr("method-event");


obj333 = {
    name:  'qqqq333',
    totalScore: 'zzzz333',
    gamesPlayed: 'xxxx333'
  };

obj444 = {
    name:  'qqqq444',
    totalScore: 'zzzz444',
    gamesPlayed: 'xxxx444'
  };

obj555 = {
    name:  'qqqq555',
    totalScore: 'zzzz555',
    gamesPlayed: 'xxxx555'
  };

let set = new Set([obj333, obj444]);
set.add(obj555);
console.log('set: set([obj333, obj444]);' + set);
console.log('set: set([obj333, obj444]);' + set);
console.log('set.size : ' + set.size);

set.forEach(function(key, value){
    console.log('!!!!!!!!value set element: ' + value.name);
	console.log('stringify set element: ' + JSON.stringify(value));
});

        $.ajax({
            type: method_event,
            url: path_eventForm + '.all.json',
            contentType: 'application/json',
            success: function(response){

            var output ="";

            if(response != null ) {

            output +="<table id='table-viewer-single'>";
			output +="<caption id='caption-col-single'>Events:</caption>";
     		output +="<tr id >";
            output +="<th id='th-col-single' scope='col-single'>Title</th>";
     		output +="<th id='th-col-single' scope='col-single'>Date</th>";
     		output +="<th id='th-col-single' scope='col-single'>Description</th>";
            output +="<th id='th-col-single' scope='col-single'>Place</th>";
     		output +="<th id='th-col-single' scope='col-single'>Topic</th>";
			output +="</tr>";

             $.each(response, function(key, value){
			console.log('aaaaaaaaaaaaaaaaaaaakey: simple' + key + ', value' + value.title);

                      output += "<tr>" +
                                "<td class='td-col' id='td-col-all-title'>"+value.title+"</td>"+
            					"<td class='td-col' id='td-col-all-eventStartDate'>"+value.eventStartDate+"</td>"+
                                "<td class='td-col' id='td-col-all-description'>"+value.description+"</td>"+
                                "<td class='td-col' id='td-col-all-place'>"+value.place+"</td>"+
                                "<td class='td-col' id='td-col-all-topic'>"+value.topic+"</td>"+
                                "</tr>";
                             });

                       output +="</table>";

             $('#all-events').html(output);

            } else {

			   console.log('No any Event in admin-page');
	 		   output +="<table id='table-viewer-single'>";
               output +="<caption id='caption-single-nofound'> No any Event in admin-page </caption>";
			   output +="</table>";
			   $('#all-events').html(output);

               }


            }
        });

}

