function AddController($scope, $element, DataService) {
    var randomURL = function () {
        return Math.random().toString(36).substring(17);
    };
    
    $scope.addInData = {
        urlName: randomURL()
    };

    $scope.submitAdd = function () {
        $scope.loading  = true;
        
        DataService.add($scope.addInData).then(function (url) {
            console.log(url);
        }).catch(function (errorEntry) {
            console.log(errorEntry);
        }).finally(function () {
            $scope.loading = false;
        });
    };
}

angular.module('ClickCount').controller('AddController', [
    '$scope',
    '$element',
    'DataService',
    AddController]);