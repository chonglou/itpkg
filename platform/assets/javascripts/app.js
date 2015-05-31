var itpkgApp = angular.module(
  'itpkgApp',
  [
    'ngRoute', 'ui.bootstrap','pascalprecht.translate',
    'usersApp'
  ],
  ['$translateProvider', function ($translateProvider){    
    $translateProvider.preferredLanguage('en');
  }]
);

itpkgApp.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/main', {
        templateUrl: 'partials/main.html',
        //controller: 'MainCtrl'
      }).
      otherwise({
        redirectTo: '/main'
      });
  }]);
