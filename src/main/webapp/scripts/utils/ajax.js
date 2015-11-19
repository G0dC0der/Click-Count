var Ajax = null;

$(function(){
    Ajax = {};

    /**
     * json, A json containing data, url, success and error.
     */
    Ajax.POST = function(json){
        $.ajax({
            type: 'POST',
            url: json.url,
            contentType: typeof json.contentType === 'undefined' ? 'application/json' : json.contentType,
            data: JSON.stringify(json.data),
            success: json.success,
            error: json.error
        });
    };

    Ajax.GET = function(json){

    };
});

