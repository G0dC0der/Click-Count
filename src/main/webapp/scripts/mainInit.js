function mainOnload(){
    Ajax.GET({
        url: Constants.Rest.TOTAL_DATA,
        success:function(data, textStatus, jqXHR){
            console.log(data);
            $('#total-data').html(indexPage.totalInfo({
                totalUrls: data.totalUrls,
                totalClicks: data.totalClicks
            }).content);
        },
        error:function(jqXHR, textStatus, errorThrown){
            console.log(jqXHR);
            //TODO:
        }
    });
}

