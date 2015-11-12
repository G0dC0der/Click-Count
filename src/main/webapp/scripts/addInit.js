$(function(){
    $('body').html(add.stuff.createForm());
    setSubmitEvent($('#add-link'));
});

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