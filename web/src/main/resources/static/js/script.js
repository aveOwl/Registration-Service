// keeping function from global namespace
(function () {
    // define module
    var application = angular.module('application', []);

    // define controller
    var controller = function ($scope, $http) {

        $scope.user = {
            email: $scope.email,
            password: $scope.password
        };

        $scope.showRegister = true;

        $scope.toForm = function () {
            $scope.showRegister = false;
            $scope.showForm = true;
        };

        $scope.submit = function () {
            $http({
                method: 'POST',
                url: '/registration',
                data: $scope.user
            }).then(function (res) {
                $scope.result = res.data;
                if ($scope.result.success) {
                    $scope.showForm = false;
                    $scope.showConfirm = true;
                }
            });
        }
    };

    // register controller
    application.controller('controller', ["$scope", "$http", controller]);
}());
