function mainOnload(){
    Ajax.GET({
        url: Constants.Rest.TOTAL_DATA,
        success:function(data, textStatus, jqXHR){
            printData({
                totalUrls: data.totalUrls,
                totalClicks: data.totalClicks
            });
        },
        error:function(jqXHR, textStatus, errorThrown){
            printData({
                totalUrls: -1,
                totalClicks: -1
            });
        }
    });
}

function printData(json) {
    $('#total-data').html(indexPage.totalInfo(json).content);
}
