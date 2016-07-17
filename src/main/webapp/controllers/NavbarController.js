function NavbarController($scope, $location) {

    $scope.navTo = function (alias) {
        $location.url(alias);
    };

    $scope.jumpTo = function (index) {
        $scope.selectedTab = index;
    };

    $scope.navbarItems = [
        {
            url: 'templates/home.html',
            alias: 'home',
            icon:  'home',
            label: 'Home'
        },
        {
            url: 'templates/add.html',
            alias: 'add',
            icon:  'add',
            label: 'Add'
        },
        {
            url: 'templates/view.html',
            alias: 'view',
            icon:  'remove_red_eye',
            label: 'View'
        },
        // {
        //     url: 'templates/stats.html',
        //     icon:  'insert_chart',
        //     label: 'Statistics'
        // },
        {
            url: 'templates/terms.html',
            alias: 'terms',
            icon:  'assignment',
            label: 'Terms of Use'
        },
        {
            url: 'templates/contact.html',
            alias: 'contact',
            icon:  'email',
            label: 'Support'
        }
    ];

    var tabIndex = (function (items) {
        var url = $location.url();
        if(url) {
            for(var i in items) {
                if('/' + items[i].alias === url)
                    return i;
            }
            return 0;
        } else {
            return 0;
        }
    })($scope.navbarItems);

    $scope.jumpTo(tabIndex);
    $scope.navTo($scope.navbarItems[tabIndex].alias);
}

angular.module('ClickCount').controller('NavbarController', [
    '$scope',
    '$location',
    NavbarController]);