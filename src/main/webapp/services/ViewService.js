angular.module("ClickCount").factory('ViewService', ['$q', function($q) {

    return {
        get: function (viewEntry) {
            var deferred = $q.defer();

            $.ajax({
                type: "POST",
                url: 'service/view/all',
                data: JSON.stringify(viewEntry),
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function (data) {
                    deferred.resolve(data);
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    deferred.reject(JSON.parse(jqXHR.responseText).message);
                }
            });

            return deferred.promise;
        }
    };
}]);