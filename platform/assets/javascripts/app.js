var itpkgApp = angular.module(
    'itpkgApp',
    [
        'ngRoute', 'ngAnimate', 'ngSanitize', 'ngCookies',
        'ui.bootstrap', 'pascalprecht.translate',
        'usersApp'
    ]);

itpkgApp.config(
    ['$translateProvider',
        function ($translateProvider) {

            $translateProvider.translations('en', {
                empty_form: 'Empty form fields',
                'titles': {
                    'ok': 'Success!',
                    'fail': 'Error!'
                },
                'buttons': {
                    'ok': 'Ok',
                    'cancel': 'Cancel',
                    'close': 'Close',
                    'submit': 'Submit',
                    'view': 'View',
                    'edit': 'Edit',
                    'update': 'Update',
                    'reset': 'Reset'
                },
                'links': {
                    'back_to_home': 'Back to home'
                }
            });

            $translateProvider.translations('zh-CN', {
                empty_form: '表单数据为空',
                'titles': {
                    'ok': '操作成功!',
                    'fail': '操作失败!'
                },
                'buttons': {
                    'ok': '确定',
                    'cancel': '取消',
                    'close': '关闭',
                    'submit': '提交',
                    'view': '查看',
                    'edit': '编辑',
                    'update': '更新',
                    'reset': '重写'
                },
                'links': {
                    'back_to_home': '返回主页'
                }
            });


            $translateProvider.useCookieStorage();
            $translateProvider.preferredLanguage('en');
            $translateProvider.useSanitizeValueStrategy('escaped');

        }]
);

itpkgApp.config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider.
                when('/main', {
                    templateUrl: '/partials/main.html?v=@version@'
                }).
                otherwise({
                    redirectTo: '/main'
                });
        }]
);

itpkgApp.directive('btnReset', function () {
    return {
        templateUrl: '/widgets/reset.html?v=@version@'
    };
});

itpkgApp.controller('RootCtrl', [
        '$translate', '$scope', '$http', 'Page',
        function ($translate, $scope, $http, Page) {
            $http.get('/index.json').success(function (data) {
                $scope.topNav = {};
                $scope.site = data;
                $scope.site.locales = ['us', 'cn'];
                $scope.Page = Page;
                $scope.setLang = function (lang) {
                    switch (lang) {
                        case 'cn':
                            lang = 'zh-CN';
                            break;
                        default:
                            lang = 'en';
                    }
                    $translate.use(lang);
                };

                Page.setTitle(data.title);
                Page.setKeywords(data.keywords);
                Page.setDescription(data.description);
            });
        }]
).controller('PostFormCtl', [
        '$scope', '$modalInstance','url','data',
        function($scope, $modalInstance, title, items){
            //todo
        }
    ])
    .controller('ModalInstanceCtrl', [
        '$scope', '$modalInstance', 'title', 'items',
        function ($scope, $modalInstance, title, items) {
            $scope.title = title;
            $scope.items = items;

            $scope.ok = function () {
                $modalInstance.dismiss('cancel');
            };
        }]);

itpkgApp.factory('Page', function () {
    var title = '';
    var keywords = '';
    var description = '';
    return {
        description: function () {
            return description;
        },
        setDescription: function (nd) {
            description = nd;
        },
        keywords: function () {
            return keywords;
        },
        setKeywords: function (nk) {
            keywords = nk;
        },
        title: function () {
            return title;
        },
        setTitle: function (nt) {
            title = nt;
        }
    };
});
