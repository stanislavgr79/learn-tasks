$(document).ready(function(){

    $('#searchForm').keyup(function(){
	    var searchText = $("#search").val();
        console.log('searchText: ' + searchText);
        var formURL = $(this).attr("action");
        var method = $(this).attr("method");

        var form_data = $(this).serialize();
        var jsonData = {search1: searchText};
        var params1 = {
            search1: searchText,
            search2: $("#search").val()
        };

		var displayResources = $("#result");

        // declare the object
		var params2 = {};

        // throw some data into the object
        params2.search1 = searchText;
        params2.search2 = $("#search").val();

        // convert the object into a json string
//        var data_json = JSON.stringify(data4);
//        console.log('json string: ' + data_json);
//        data_json = encodeURIComponent(data_json)

//		var obj = { name: "John", age: 30, city: "New York" };
//		var myJSON = JSON.stringify(obj);

        if(searchText != ''){
             $.ajax({

                        type:method,
                 		cache: false,
                        url:formURL,
                        contentType: 'application/json',
                        data: params2,
                        success: function(response){

								var output ="";

                               $.each(response, function(key, value){

                                    output +=
                                          "<a href='\#' class='list-group-item list-group-item-action border-1'>" + value + "</a>";

                                        });

									$('#show-list').html(output);

                                }

                    });

                         }
        				 else {
                             $('#show-list').html('');
							 $('#result').html('');
                         }
		});

    $(document).on('click','a',function(){
        $("#search").val($(this).text());
        $("#show-list").html('');
    });
});


$(function() {
    $('#searchForm').submit(function(e) {

        e.preventDefault(); //STOP default action

        var formURL = $(this).attr("action");
        var method = $(this).attr("method");
        var searchText = $('#search').val();

        var form_data = $(this).serialize();
        var jsonData = { search: search};

        var params1 = {
            search1: searchText,
            search2: $("#search").val()
        };


 if(searchText != ''){

        $.ajax({

         type:method,
            url:formURL,
            contentType: 'application/json',
            data: params1,
            success: function(response){

      	var output ="";

                $.each(response, function(key, value){

                   output +="<a href=" + value + " class='list-group-item list-group-item-action border-1'>" + value + "</a>";

                });

             $('#result').html(output);

            }

        });
  }
      else {
         $('#result').html('');
      }


    });


});

