function errorOnload(){
    $('body').append(dialogs.errorDiv({
        status: Util.queryParam('status'),
        url: Util.queryParam('url'),
        group: Util.queryParam('group'),
        description: Util.queryParam('description')
    }).content);
}