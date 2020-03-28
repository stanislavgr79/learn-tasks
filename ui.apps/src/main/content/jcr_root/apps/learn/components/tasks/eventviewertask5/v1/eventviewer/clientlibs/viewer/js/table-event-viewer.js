$(document).ready(function(){
    type = $('.event-Form').attr("search-type");

    if(type == 'single' && $('#single-event').attr("search-value") != null){
		getSingleEventViewer();
    } else if (type == 'all'){
		getAllEventsViewer();
    } else {
			   var out = "";
	 		   out +="<table id='table-viewer-single'>";
               out +="<caption id='caption-single-nofound'> No selected property in Event viewer-component</caption>";
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
  } else {
         $('#single-event').html('');
  }
}

function getAllEventsViewer(){
    console.log('run all');
    var path_eventForm = $('#eventForm').attr("path-eventForm");
    var method_event = $('#eventForm').attr("method-event");

    var params = {
            current_table_page: $("#all-events").data("tb-current-page")
        };


    $.ajax({
            type: method_event,
            url: path_eventForm + '.all.json',
        	data: params,
            contentType: 'application/json',
         	success: function (response, status, request) {

         var total_event_count = request.getResponseHeader('totalEvents');

         $("#all-events").data("total-event-count", total_event_count);

            var output ="";

            if(response != null ) {
                output +="<table id='table-viewer-all'>";
                output +="<caption id='caption-col-single'>Events:</caption>";
                output +="<tr>";
                output +="<th id='th-col-single' scope='col-single'>Title</th>";
                output +="<th id='th-col-single' scope='col-single'>Date</th>";
                output +="<th id='th-col-single' scope='col-single'>Description</th>";
                output +="<th id='th-col-single' scope='col-single'>Place</th>";
                output +="<th id='th-col-single' scope='col-single'>Topic</th>";
                output +="</tr>";

                $.each(response, function(key, value){
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

				changeCssVisibleButton();

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

function changeCssVisibleButton(){

    if($("#all-events").data("tb-current-page") == 1){
        $('.btn-page-first').attr('disabled', 'disabled');
		$('.btn-page-first').addClass('disabled');
		$('.btn-page-prev').attr('disabled', 'disabled');
		$('.btn-page-prev').addClass('disabled');
    }else{
		$('.btn-page-first').removeAttr('disabled');
		$('.btn-page-first').removeClass('disabled');
		$('.btn-page-prev').removeAttr('disabled');
        $('.btn-page-prev').removeClass('disabled');
    }

	var tb_total_events = $("#all-events").data("total-event-count");
    var tb_events_pageSize = $("#all-events").data("tb-pagination");
    var tb_events_lastPage = tb_total_events / tb_events_pageSize;

	if($("#all-events").data("tb-current-page") == Math.ceil(tb_events_lastPage)){

        $('.btn-page-last').attr('disabled', 'disabled');
		$('.btn-page-last').addClass('disabled');
		$('.btn-page-next').attr('disabled', 'disabled');
		$('.btn-page-next').addClass('disabled');
    }else{
		$('.btn-page-last').removeAttr('disabled');
		$('.btn-page-last').removeClass('disabled');
		$('.btn-page-next').removeAttr('disabled');
		$('.btn-page-next').removeClass('disabled');
    }

}



$('.tb-page-first').click(function(){
	$("#all-events").data("tb-current-page", 1);
    getAllEventsViewer();
})

$('.tb-page-last').click(function(){
	var tb_total_events = $("#all-events").data("total-event-count");
    var tb_events_pageSize = $("#all-events").data("tb-pagination");
    var tb_events_lastPage = tb_total_events / tb_events_pageSize;

	$("#all-events").data("tb-current-page", Math.ceil(tb_events_lastPage));
    getAllEventsViewer();

})

$('.tb-page-prev').click(function(){
	$("#all-events").data("tb-current-page", $("#all-events").data("tb-current-page")-1);
    getAllEventsViewer();
})

$('.tb-page-next').click(function(){
	$("#all-events").data("tb-current-page", $("#all-events").data("tb-current-page")+1);
    getAllEventsViewer();
})


