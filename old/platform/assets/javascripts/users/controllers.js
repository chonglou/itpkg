usersApp.controller('UserFormCtrl', ['$scope', '$http', '$translate', 'formHelper',
    function ($scope, $http, $translate, formHelper) {
        $scope.login = function (user) {
            console.log(user);
        };
        $scope.register = function (user) {
            formHelper.submit('/users/register', user);
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
