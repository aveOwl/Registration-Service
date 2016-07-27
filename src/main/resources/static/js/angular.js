// Create Controller
angular.module('application', [])
    .controller('FormController', ['$scope', '$http', function ($scope, $http) {
        $scope.showHome=true;

        $scope.go = function () {
            $scope.showHome=false;
            $scope.showRegister=true;
        };

        $scope.formData = {};

        $scope.processForm = function() {
            $http({
                method: "POST",
                url: "/registration",
                data: $.param($scope.formData)
            })
                .success(function (response) {
                    if (response.success) {
                            $scope.showConfirm=true;
                            $scope.showRegister=false;
                        } else {
                    if (response.invalidEmail) {
                        $scope.emailError = response.emailViolationMessage;
                    }
                    if (response.invalidPassword) {
                        $scope.emailPass = response.passwordViolationMessage;
                    }
                    }
                })
        }
    }]);
