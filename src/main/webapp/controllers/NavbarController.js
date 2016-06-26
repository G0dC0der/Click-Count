function NavbarController($scope, $location, $http, $compile) {
    $scope.navbarItems = [
        {
            url: 'home.html',
            icon:  'home',
            label: 'Home'
        },
        {
            url: 'add.html',
            icon:  'add',
            label: 'Add'
        },
        {
            url: 'view.html',
            icon:  'remove_red_eye',
            label: 'View'
        },
        {
            url: 'terms.html',
            icon:  'account_balance',
            label: 'Terms of Use'
        },
        {
            url: 'contact.html',
            icon:  'email',
            label: 'Contact'
        }
    ];

    $scope.nav = function (url) {
        $location.url(url);
        pasteTemplate(url);
    };

    var pasteTemplate = function (url) {
        $http.get(url, {cache: true}).then(function (template) {
            $('#view-div').html($compile(template.data)($scope));
        });
    };

    (function() {
        var currentPage = $location.url();
        for (var i in $scope.navbarItems) {
            var item = $scope.navbarItems[i];

            if ('/' + item.url === currentPage) {
                $scope.currentNavItem = 'page' + ++i;
                pasteTemplate(item.url);
                return;
            }
        }
        $scope.currentNavItem = "page1";
        pasteTemplate($scope.navbarItems[0].url);
    }());
}

angular.module('ClickCount').controller('NavbarController', [
    '$scope',
    '$location',
    '$http',
    '$compile',
    NavbarController]);