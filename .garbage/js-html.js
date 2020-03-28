var type = '';
var test1;
var test2 = 'aaaaaaaaaaa';

var obj333;





$(document).ready(function(){
    type = $('.event-Form').attr("search-type");
    console.log(type);

    if(type == 'singleEventViewer'){
		getSingleEventViewer();
    } else if (type == 'allEventsViewer'){
		getAllEventsViewer();
    };

})

function getSingleEventViewer(){
       console.log('run single');

        var path_eventForm = $('#eventForm').attr("path-eventForm");
            console.log('path_page: ' + path_eventForm);
        var method_event = $('#eventForm').attr("method-event");
            console.log('method_event: ' + method_event);


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

 if(type != ''){

        $.ajax({
            type: method_event,
            url: path_eventForm + '.single.json',
            contentType: 'application/json',
            success: function(response){

      	var output ="";

        //var object_as_string_as_object = JSON.parse(object_as_string);



            console.log(' input response: ' + response);
            var str = JSON.stringify(response);
             console.log('str sringify response: ' + JSON.stringify(response));
	 		var object_after_parse = JSON.parse(str);
           console.log('object_after_parse str: ' + object_after_parse);

            console.log('111111111111obj3: ' + obj333);
             console.log('alert objjjjjjj: ' + JSON.stringify(obj333));



            console.log('response.title: ' + response.title);
            console.log('response.description: ' + response.description);
            console.log('response.eventStartDate: ' + response.eventStartDate);
            console.log('response.place: ' + response.place);
            console.log('response.topic: ' + response.topic);

            var data_event = $("#single-event").attr('data-event');
            console.log('attr data-event: '  + data_event)


				var msglist = $("#msglist");
				var show = msglist.data("list-size");
				msglist.data("list-size", show+3);

				var asdf = $("#asdf");
				var show2 = asdf.data("sly-repeat");
				asdf.data("sly-repeat", set);
            console.log('asdf : ' + $("#asdf").data("sly-repeat"));

            let result1 = $("#asdf").data("sly-repeat");
			console.log('set.size result1 : ' + set.size);

        //    var object_after_parse_let = JSON.parse(result1);
		//	$("#asdf").data("sly-repeat", object_after_parse_let);

          result1.forEach(function(key, value){
	console.log('stringify result1 asdf element: ' + JSON.stringify(value));


});


            console.log('mglist ?8 :' + $("#msglist").data("list-size"));

                $.each(response, function(key, value){

				var jsonstring = value;

            console.log('key: '+ key + ', value: ' + jsonstring);

                });

             test = response;
             console.log('test = response: ' + test);

                $('#single-event').val(response);
				var getvalue = $('#single-event').val();
 				console.log('single-event val ' + getvalue);

     		 $('#single-event').attr('data-sly-use.obj3', response);

     		var obj3 = $('#single-event').attr('data-sly-use.obj3');
            console.log('single-event atrr data-sly-use.obj3 : ' + obj3);



 console.log('response.title: ' + response.title);
            console.log('response.description: ' + response.description);
            console.log('response.eventStartDate: ' + response.eventStartDate);
            console.log('response.place: ' + response.place);
            console.log('response.topic: ' + response.topic);




     		var out ="";
			out +="<table id='table-viewer-single'>";
			out +="<caption id='caption-col-single'>Event: "  + response.title + "</caption>";
     		out +="<tr id >";
            out +="<th id='th-col-single' scope='col-single'>Title</th>";
     		out +="<th id='th-col-single' scope='col-single'>Date</th>";
     		out +="<th id='th-col-single' scope='col-single'>Place</th>";
     		out +="<th id='th-col-single' scope='col-single'>Topic</th>";
			out +="</tr>";
			out +="<tr>";
			out +=" <td id='td-col-single'>"+response.title+"</td><td id='td-col-single'>"+response.eventStartDate+"</td><td id='td-col-single'>"+response.place+"</td><td id='td-col-single'>"+response.topic+"</td>";
			out +="</tr>";
			out +="<tr>";
			out +="<td id='description-viewer-single' colspan='4'>Description: " + response.description + "</td>";
			out +="</tr>";
            out +="</table>";

             $('#resultat').html(out);




			//define a json object
        var employee = { "name": "John Johnson", "street": "Oslo West 16", "phone": "555 1234567" };

        //use JSON.stringify to convert it to json string
        var jsonstring = JSON.stringify(employee);
        $("#result2").append('<p>json string: ' + jsonstring + '</p>');

        //convert json string to json object using JSON.parse function
        var jsonobject = JSON.parse(jsonstring);
        var info = '<ul><li>Name:' + jsonobject.name + '</li><li>Street:' + jsonobject.street + '</li><li>Phone:' + jsonobject.phone + '</li></ul>';

        $("#result2").append('<p>json object:</p>');
        $("#result2").append(info);


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
            console.log('path_page: ' + path_eventForm);
        var method_event = $('#eventForm').attr("method-event");
            console.log('method_event: ' + method_event);

 if(type != ''){

        $.ajax({
            type: method_event,
            url: path_eventForm + '.all.json',
            contentType: 'application/json',
            success: function(response){

      	var output ="";

                $.each(response, function(key, value){

                   output +="<a>"  + key + value + "</a>";

                });
             $('#all-events').html(output);
            }
        });
  }
      else {
         $('#all-events').html('');
      }
}


function myFun1() {

	return test1;
}

function myFun2() {

	return test2;
}

function myFun3() {

	return obj3;
}


$(function() {
    $('.tb-page-first').submit(function(e) {
        console.log('submit tb-page-first');
        e.preventDefault(); //STOP default action
		$("#all-events").data("tb-current-page", 1);

    });
})

$(function() {
    $('.tb-page-last').submit(function(e) {
        console.log('submit tb-page-last');
        e.preventDefault(); //STOP default action

		var tb_total_events = $("#all-events").data("total-event-count");
        var tb_events_pageSize = $("#all-events").data("tb-pagination");
        tb_events_lastPage = tb_total_events / tb_events_pageSize;

		$("#all-events").data("tb-current-page", Math.ceil(tb_events_lastPage));

    });
})


$(function() {
    $('.tb-page-prev').submit(function(e) {
        console.log('submit tb-page-prev');
        e.preventDefault(); //STOP default action
		$("#all-events").data("tb-current-page", $("#all-events").data("tb-current-page")-1);

    });
})

$(function() {
    $('.tb-page-next').submit(function(e) {
        console.log('submit tb-page-prev');
        e.preventDefault(); //STOP default action
        $("#all-events").data("tb-current-page", $("#all-events").data("tb-current-page")+1);
    });
})