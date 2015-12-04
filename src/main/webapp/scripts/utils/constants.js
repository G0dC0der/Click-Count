var Constants = null;

$(function(){
    Constants = {};
    Constants.Rest = {};

    Constants.DOMAIN = 'http://localhost:9090/clicky/';
    Constants.REST = "service";
    Constants.Rest.ADD = Constants.REST + "/add";
    Constants.Rest.CAPTCHA = Constants.REST + "/captcha";
    Constants.Rest.VIEW_ALL = Constants.REST + "/view/all";
    Constants.Rest.TOTAL_DATA = Constants.REST + "/view/total";
});

