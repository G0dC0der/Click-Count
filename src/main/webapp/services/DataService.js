angular.module("ClickCount").factory('DataService', ['$q', function($q) {

    return {
        add: function (addEntry) {
            var deferred = $q.defer();

            $.ajax({
                type: "POST",
                url: 'service/add',
                data: JSON.stringify(addEntry),
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function (data) {
                    deferred.resolve(data.message);
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    deferred.reject(JSON.parse(jqXHR.responseText));
                }
            });

            return deferred.promise;
        }
    };
}]);