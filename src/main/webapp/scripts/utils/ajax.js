var Ajax = null;

$(function(){
    Ajax = {};

    /**
     * json, A json containing data, url, success, headers and error.
     */
    Ajax.POST = function(json){
        ajaxInternal("POST", json);
    };

    Ajax.GET = function(json){
        ajaxInternal("GET", json);
    };

    function ajaxInternal(type, json) {
        $.ajax({
            type: type,
            url: json.url,
            contentType: typeof json.contentType === 'undefined' ? 'application/json' : json.contentType,
            headers: json.headers,
            data: JSON.stringify(json.data),
            success: json.success,
            error: json.error
        });
    }
});

