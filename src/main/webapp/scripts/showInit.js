function showOnload(json){
    var $container = $('#input-div');
    $container.html(showPage.submitForm(Objects.isDefined(json) ? json : {}).content);
    $container.find('button').click(function(){

        Dialog.loading({
            id: 'loading-section',
            text: 'Fetching data, please wait...',
            opacity: 0.2
        });

        Ajax.POST({
            url: Constants.Rest.VIEW_ALL,
            data:{
                groupName: $container.find("[name='group']").val(),
                password: $container.find("[name='password']").val(),
                captcha: $container.find("[name='captcha']").val()
            },
            success:function(data, textStatus, jqXHR){
                postFetch(data);
                $container.find('.error-text').remove();
            },
            error:function(jqXHR, textStatus, errorThrown){
                var data = $.parseJSON(jqXHR.responseText);
                $('#loading-section').remove();
                showOnload({
                    groupName: data.groupName,
                    groupError: data.groupError,
                    password: data.password,
                    captcha: "",
                    captchaError: data.captchaError
                });
            }
        });
    });
    var $link = $container.find('a');
    var $img = $container.find('img');
    var $input = $container.find("[name='captcha']");
    Captcha.init($link, $img);
    Captcha.link($link, $input);

    submitOnEnter("add-container");
}

function postFetch(data){
    var $dataContainer = $('#result-div');
    var $addContainer = $('#add-container');

    $dataContainer.html(showPage.showStatistics({ arr: format(data) }).content);
    $dataContainer.prepend('<hr>');
    $dataContainer.find('button').click(function(){
        var $button = $(this);
        var $elContainer = $button.parent();
        var urlName = $elContainer.find(".result-data").first().html();
        urlName = urlName.substring(urlName.lastIndexOf("/") + 1);

        Ajax.POST({
            url: Constants.Rest.DELETE,
            data: {
                urlName: urlName,
                groupName: $addContainer.find("[name='group']").val(),
                password: $addContainer.find("[name='password']").val()
            },
            success:function(data, textStatus, jqXHR){
                $elContainer.remove();
            },
            error:function(jqXHR, textStatus, errorThrown){
                $elContainer.find('.error-text').html(jqXHR.responseText);
            }
        });
    });
    $('#loading-section').remove();
}

function format(data){
    var urls = [];
    var now = new Date();

    for(var i in data.urls){
        var el = data.urls[i];

        var json = {
            url: Constants.DOMAIN + Constants.REST + "/" + el.urlName,
            link: el.link,
            totalClicks: el.clicks.length
        };

        if(el.clicks.length){
            el.clicks.sort(function(a, b){
                return a - b;
            });

            var oldest = new Date(el.clicks[0]);
            var daysDiff = Math.round((now-oldest)/(1000*60*60*24));
            var monthsDiff = (now.getFullYear() - oldest.getFullYear())*12 + (now.getMonth() - oldest.getMonth());

            json.firstClick = oldest.getFullYear() + "-" + (oldest.getMonth() + 1) + "-" + oldest.getDate();
            json.dailyClicks = el.clicks.length / parseFloat(daysDiff);
            //json.monthlyClicks = el.clicks.length /parseFloat(monthsDiff);
        }

        urls.push(json);
    }
    return urls;
}