angular.module("ClickCount").factory('DataService', ['$q', function($q) {

    return {
        add: function (addEntry) {
            var deferred = $q.defer();

            $.ajax({
                type: "POST",
                url: 'http://localhost:9090/clicky/service/add',
                data: JSON.stringify(addEntry),
                success: function (data) {
                    deferred.resolve(data);
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    deferred.reject(jqXHR.statusText);
                }
            });

            return deferred.promise;
        }
    };
}]);