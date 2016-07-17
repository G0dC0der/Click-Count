function ViewController($scope, ViewService) {

    $scope.fetchData = function(){
        $scope.error = null;
        $scope.group = null;
        $scope.loading = true;
        $scope.urls = [];

        ViewService.get({ groupName: $scope.viewData.groupName, password: $scope.viewData.password }).then(function (viewData) {
            $scope.urls = viewData.urls;
            $scope.group = viewData.groupName;
            $scope.totalClicks = 0;
            
            $scope.urls.forEach(function (el) {
                $scope.totalClicks += el.clicks;
            });
        }).then(undefined, function (errorEntry) {
            $scope.error = errorEntry;
        }).finally(function () {
            $scope.loading = false;
        });
    };
}

angular.module('ClickCount').controller('ViewController', [
    '$scope',
    'ViewService',
    ViewController]);