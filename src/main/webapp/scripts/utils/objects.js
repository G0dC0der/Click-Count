var Objects = null;

$(function(){
    Objects = {};

    Objects.isDefined = function(object){
        return typeof object !== 'undefined' && object != null;
    };

    Objects.typed = function(string){
        return Objects.isDefined(string) && string.length > 0;
    };
});