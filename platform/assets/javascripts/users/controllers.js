usersApp.controller('UserFormCtrl', ['$scope', function ($scope) {
    $scope.login = function (user) {
        console.log(user);
    };
    $scope.register = function (user) {
        console.log(user);
    };
    $scope.password = function (user) {
        console.log(user);
    };
    $scope.confirm = function (user) {
        console.log(user);
    };
    $scope.unlock = function (user) {
        console.log(user);
    };
    $scope.reset = function () {
        $scope.user = {};
    }
}]);
