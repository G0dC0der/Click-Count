var Dialog = null;

$(function(){
    Dialog = {};

    Dialog.OK = 1;
    Dialog.CANCEL = 2;

    /**
     * json: Contains text(string), img(string) and life(int)
     */
    Dialog.popup = function(json){
        var $popup = $(dialogs.popup({
            text: json.text,
            img: json.img
        }).content);

        $popup.hide();
        $popup.fadeIn(250);
        $('body').prepend($popup);
        setTimeout(function(){
            $popup.fadeOut(250);
        }, json.life);
    };

    Dialog.formDialog = function(){
    };

    /**
     * json: Contains id(string), text(string), opacity(float)
     */
    Dialog.loading = function(json){
        var $loadingDiv = $(dialogs.loading(json).content);
        $('body').append($loadingDiv);
    };
});