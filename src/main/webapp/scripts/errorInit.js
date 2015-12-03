$(function(){
    $('body').html(dialogs.errorDiv({
        status: Util.queryParam('status'),
        url: Util.queryParam('url'),
        group: Util.queryParam('group'),
        description: decodeURIComponent(Util.queryParam('description'))
    }).content);
});
