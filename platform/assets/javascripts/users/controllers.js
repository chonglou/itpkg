usersApp.controller('UserFormCtrl', ['$scope', '$http', '$translate', '$modal',
    function ($scope, $http, $translate, $modal) {
        $scope.login = function (user) {
            console.log(user);
        };
        $scope.register = function (user) {

            if (user) {
                $http.post('/users/register', user).success(function (result) {
                    $modal.open({
                        templateUrl: '/widgets/message_dlg.html',
                        controller: 'ModalInstanceCtrl',
                        resolve: {
                            title: function () {
                                return $translate.instant(result.ok ? 'titles.ok' : 'titles.fail');
                            },
                            items: function () {
                                return result.ok ? result.data : result.errors;
                            }
                        }
                    });
                });
            } else {
                $modal.open({
                    templateUrl: '/widgets/message_dlg.html',
                    controller: 'ModalInstanceCtrl',
                    resolve: {
                        title: function () {
                            return $translate.instant('titles.fail');
                        },
                        items: function () {
                            return [$translate.instant('empty_form')];
                        }
                    }
                });
            }
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
