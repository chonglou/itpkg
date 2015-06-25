var usersApp = angular.module(
    'usersApp',
    ['ngRoute', 'pascalprecht.translate']);


usersApp.config(['$translateProvider',
        function ($translateProvider) {
            $translateProvider.translations('en', {
                'user': {
                    'name': 'Username',
                    'email': 'Email',
                    'password': 'Password',
                    'password_re': 'Password Confirmation',
                    'remember_me': 'Remember me',
                    'buttons': {
                        'login': 'Sign In',
                        'register': 'Sign Up',
                        'password': 'Send me reset password instructions',
                        'confirm': 'Resend confirmation instructions',
                        'unlock': 'Send me unlock instructions'
                    },
                    'titles': {
                        'login': 'Sign in',
                        'register': 'Sign up',
                        'password': 'Forgot your password? ',
                        'confirm': "Didn't receive confirmation instructions? ",
                        'unlock': "Didn't receive unlock instructions?"
                    }
                }
            });

            $translateProvider.translations('zh-CN', {
                'user': {
                    'name': '用户名',
                    'email': '邮箱',
                    'password': '密码',
                    'password_re': '再次输入',
                    'remember_me': '记住我',
                    'buttons': {
                        'login': '登录',
                        'register': '注册',
                        'password': '发送重置密码邮件',
                        'confirm': '发送激活邮件',
                        'unlock': '发送解锁邮件'
                    },
                    'titles': {
                        'login': '用户登录',
                        'register': '注册新账户',
                        'password': '忘记密码？',
                        'confirm': '没有收到激活邮件？ ',
                        'unlock': '没有收到解锁邮件？'
                    }
                }
            });
        }]
);

usersApp.config(['$routeProvider',
    function ($routeProvider) {
        $routeProvider.
            when('/users/login', {
                templateUrl: 'users/login.html?v=@version@',
                controller: 'UserFormCtrl'
            }).
            when('/users/register', {
                templateUrl: 'users/register.html?v=@version@',
                controller: 'UserFormCtrl'
            }).
            when('/users/unlock', {
                templateUrl: 'users/unlock.html?v=@version@',
                controller: 'UserFormCtrl'
            }).
            when('/users/confirm', {
                templateUrl: 'users/confirm.html?v=@version@',
                controller: 'UserFormCtrl'
            }).
            when('/users/password', {
                templateUrl: 'users/password.html?v=@version@',
                controller: 'UserFormCtrl'
            }).
            when('/users/profile', {
                templateUrl: 'users/profile.html?v=@version@',
                controller: 'UserFormCtrl'
            });
    }]);
