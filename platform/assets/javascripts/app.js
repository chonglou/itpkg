var itpkgApp = angular.module(
  'itpkgApp',
  [
    'ngRoute', 'ui.bootstrap','pascalprecht.translate',
    'usersApp'
  ],
  ['$translateProvider', function ($translateProvider){
    $translateProvider.translations('en', {
      'links':{
        'back_to_home': 'Back to home'
      }
    });

    $translateProvider.translations('zh_CN', {
      'links':{
        'back_to_home': '返回主页'
      }
    });
    $translateProvider.preferredLanguage('en');
    $translateProvider.useSanitizeValueStrategy('escaped');
  }]
);

itpkgApp.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/main', {
        templateUrl: 'partials/main.html?v=@version@'
      }).
      otherwise({
        redirectTo: '/main'
      });
  }]
);


itpkgApp.controller('RootCtrl', ['$scope', '$http', 'Page',
  function($scope, $http, Page){
    $http.get("/index.json").success(function(data){
      $scope.topNav = {};
      $scope.site = data;
      $scope.Page = Page;
      Page.setTitle(data.title);
      Page.setKeywords(data.keywords);
      Page.setDescription(data.description);
    });
  }]
);

itpkgApp.factory('Page', function(){
  var title = '';
  var keywords = '';
  var description = '';
  return {
    description: function(){
      return description;
    },
    setDescription: function(nd){
      description = nd;
    },
    keywords: function(){
      return keywords;
    },
    setKeywords: function(nk){
      keywords = nk;
    },
    title: function(){
      return title;
    },
    setTitle: function(nt){
      title = nt;
    }
  };
});
