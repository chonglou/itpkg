var usersApp = angular.module(
  'usersApp',
  ['ngRoute', 'pascalprecht.translate'],
  ['$translateProvider', function ($translateProvider){
    $translateProvider.translations('en', {
      'user':{
        'sign_in': 'Sign In',
        'email': 'Email',
        'password': 'Password',
        'remember_me': 'Remember me',
        'title':{
          'sign_in': 'Welcome to sign in'
        }
      }
    });

    $translateProvider.translations('zh_CN', {
      'user':{
        'sign_in': '登录',
        'email': '邮箱',
        'password': '密码',
        'remember_me': '记住我',
        'title': {
          'sign_in': '欢迎登录'
        }
      }
    });
  }]
  );

usersApp.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/users/sign_in', {
        templateUrl: 'users/sign_in.html',
        controller: 'UserFormCtrl'
      }).
      when('/users/register', {
        templateUrl: 'users/register.html',
        controller: 'UserRegisterCtrl'
      }).
      when('/users/unlock', {
        templateUrl: 'users/unlock.html',
        controller: 'UserUnlockCtrl'
      });
  }]);
