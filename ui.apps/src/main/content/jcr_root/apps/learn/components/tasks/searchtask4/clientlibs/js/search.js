$(document).ready(function(){

    $('#searchForm').keyup(function(){
		var path_page = $(this).attr("path-page");
        console.log('path_page: ' + path_page);

        var search_type = $(this).attr("search-type");
        console.log('search_type: ' + search_type);

	    var searchText = $("#search").val();
        console.log('searchText: ' + searchText);

        var method = $(this).attr("method");

        var form_data = $(this).serialize();
        var jsonData = {search1: searchText};
        var params = {
            search1: searchText
        };

		var displayResources = $("#result");

        if(searchText != ''){

             $.ajax({

                        type:method,
                 		cache: false,
                        url : path_page + '.data.json',
                        data: params,
                        contentType: 'application/json',
                        success: function(response){

								var output ="";

                               $.each(response, function(key, value){

                 console.log("key: " + key);
                                    output +=
                                          "<a href=" + value + ".html" + " class='list-group-item list-group-item-action border-1'>" + key + "</a>";

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

});


$(function() {
    $('#searchForm').submit(function(e) {

        e.preventDefault(); //STOP default action

        var servlet = $(this).attr("action");
        var method = $(this).attr("method");
        var searchText = $('#search').val();
        var path = $(location).attr("href");

        var form_data = $(this).serialize();
        var jsonData = { search: search};

       var params = {
            param1: searchText,
            param2: path,
			param3: search_type
        };


 if(searchText != ''){

        $.ajax({

            type:method,
            url:servlet,
            contentType: 'application/json',
            data: params,
            success: function(response){

      	var output ="";

                $.each(response, function(key, value){

                   output +="<a href=" + value + " class='list-group-item list-group-item-action border-1'>" + key + "</a>";

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

