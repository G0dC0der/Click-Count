$(function(){
    $('#add-section').html(addPage.addForm({}).content);
    var $container = $('#add-container');
    setSubmitEvent($container);
    setCaptchaRefresh($container.find('a'), $container.find('img'));
});

function postInit(){
    var $container = $('#add-container');
    setSubmitEvent($container);
    setCaptchaRefresh($container.find('a'), $container.find('img'));
}

/**
 * $container, The container containing the data generated by addPage.addForm.
 */
function setSubmitEvent($container){
    $container.find('button').on('click', function(){
        $(this).prop('disabled', true);
        Dialog.loading({
            id: 'loading-section',
            text: 'Adding URL, please wait...',
            opacity: 0.2
        });

        Ajax.POST({
            url: Constants.Rest.ADD,
            data:{
                url: $container.find("[name='url']").val(),
                link: $container.find("[name='link']").val(),
                group: $container.find("[name='group']").val(),
                password: $container.find("[name='password']").val(),
                captcha: $container.find("[name='captcha']").val()
            },
            success:function(data, textStatus, jqXHR){
                postAdd(data, true);
            },
            error:function(jqXHR, textStatus, errorThrown){
                postAdd($.parseJSON(jqXHR.responseText), false);
            }
        });
    });
}

/**
 * $el, The element to trigger the refresh action
 * $img, The img element to paste the image on.
 */
function setCaptchaRefresh($el, $img){
    $el.on('click', function(){
        var xhr = new XMLHttpRequest();
        xhr.open('GET', Constants.Rest.CAPTCHA, true);
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

function postAdd(response, success){
    $('#loading-section').remove();

    $('#add-section').html(addPage.addForm({
        url: response.url,
        urlError: response.urlError,
        link: response.link,
        linkError: response.linkError,
        group: response.group,
        groupError: response.groupError,
        password: response.password,
        passwordError: response.passwordError,
        captcha: response.captcha,
        captchaError: response.captchaError
    }).content);

    postInit();

    if(success){
        if(!$('#info-table').length){
            $('#info-section').html(addPage.infoHeader({}).content);
        }

        $('#info-table').append(addPage.infoRow({
            url: Constants.DOMAIN + Constants.REST + (Objects.typed(response.group) ? "/" + response.group : "") + "/" + response.url,
            link: (response.link.indexOf('http') == 0 ? response.link : "http://" + response.link),
            group: response.group
        }).content);

        Dialog.popup({
            text: "The link was successfully added!",
            img: "img/success.gif",
            life: 3000
        });
    } else {
        Dialog.popup({
            text: "Failed to add the link.",
            img: "img/fail.png",
            life: 3000
        });
    }
}
