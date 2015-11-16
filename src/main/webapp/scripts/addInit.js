$(function(){
    $('body').html(addPage.createForm({captchaImg:"img/questionmark.png"}).content);
    initAddField();
});

function initAddField(){
    setSubmitEvent($('#add-link'));
    var createDiv = $('#create-div');
    setCaptchaRefresh(createDiv.find('a'), createDiv.find('img'));
}

function setSubmitEvent($container){
    $container.find('button').click(function(){
        $.ajax({
            type: 'POST',
            url: 'rest/add',
            data:{
                url: $container.find("[name='url']").val(),
                link: $container.find("[name='link']").val(),
                group: $container.find("[name='group']").val(),
                password: $container.find("[name='password']").val(),
                captcha: $container.find("[name='captcha']").val()
            },
            success:function(data, textStatus, jqXHR){
                console.log(data);
                console.log(textStatus);
                console.log(jqXHR);
            },
            error:function(jqXHR, textStatus, errorThrown){
                console.log("Error!");
            }
        });
    });
}

function setCaptchaRefresh($el, $img){
    $el.click(function(){
        var xhr = new XMLHttpRequest();
        xhr.open('GET', 'rest/captcha', true);
        xhr.responseType = 'arraybuffer';
        xhr.onload = function(e) {
            if (this.response) {
                var byteArray = new Uint8Array(this.response);
                $img.prop('src', "data:image/jpeg;base64," + btoa(String.fromCharCode.apply(null, byteArray)));
            } else {
                $img.prop('src', "img/questionmark.png");
            }
        };
        xhr.send();
    });
}
