usersApp.controller('UserFormCtrl', ['$scope', function ($scope) {
    $scope.sign_in = function (user) {
        console.log(user);
    };
    $scope.reset = function () {
        $scope.user = {};
    }
}]);
