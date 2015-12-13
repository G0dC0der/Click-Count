var Captcha = null;

$(function(){
    Captcha = {};

    Captcha.init = function($el, $img) {
        $el.click(function(){
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
    };

    Captcha.link = function($el, $input) {
        $el.click(function(){
            $input.val('');
        });
    };
});