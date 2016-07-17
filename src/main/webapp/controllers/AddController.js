function AddController($scope, $element, DataService) {
    $scope.urls = [];
    $scope.addInData = {};
    $scope.addOutData = {};

    $scope.submitAdd = function () {
        $scope.loading  = true;
        $scope.addOutData = {};
        
        DataService.add($scope.addInData).then(function (url) {
            $scope.urls.push(url);
            $.notify("Success!",  { position:"top center", className:'success' });
        }).then(undefined, function (errorEntry) {
            $scope.addOutData = errorEntry;
            $.notify("Failed to add the URL.",  { position:"top center", className:'error' });
        }).finally(function () {
            $scope.loading = false;
        });
    };
    
    $scope.copyToClipboard = function (url) {
        url.showToolTip = true;
        //TODO:
    };
}

angular.module('ClickCount').controller('AddController', [
    '$scope',
    '$element',
    'DataService',
    AddController]);