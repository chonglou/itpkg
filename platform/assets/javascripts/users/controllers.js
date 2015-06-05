usersApp.controller('UserFormCtrl', ['$scope', '$http', '$translate', '$modal',
    function ($scope, $http, $translate, $modal) {
        $scope.login = function (user) {
            console.log(user);
        };
        $scope.register = function (user) {

            if (user) {
                $http.post('/users/register', user).success(function (result) {
                    $modal.open({
                        templateUrl: 'widgets/message_dlg.html?v=@version@',
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
                    templateUrl: 'widgets/message_dlg.html?v=@version@',
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

    //.controller('UserLoginCtrl', ['$scope', '$http', '$translate', '$modal',
    //    function ($scope, $http, $translate, $modal) {
    //        $scope.form = {
    //            title: 'user.titles.login',
    //            submit: {
    //                click: function (item) {
    //                    console.log(item);
    //                },
    //                label: 'user.buttons.login'
    //            },
    //            fields: [
    //                {
    //                    id: 'item.email',
    //                    type: 'email',
    //                    label: 'user.email',
    //                    required: true
    //                },
    //                {
    //                    id: 'item.password',
    //                    type: 'password',
    //                    label: 'user.password',
    //                    required: true
    //                },
    //                {
    //                    id: 'item.remember',
    //                    type: 'checkbox',
    //                    label: 'user.remember_me',
    //                    required: true
    //                }
    //            ]
    //        };
    //    }]);
