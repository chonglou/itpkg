itpkgApp.controller('ModalInstanceCtrl', [
    '$scope', '$modalInstance', 'title', 'items',
    function ($scope, $modalInstance, title, items) {
        $scope.title = title;
        $scope.items = items;

        $scope.ok = function () {
            $modalInstance.dismiss('cancel');
        };
    }]);

itpkgApp.factory('localeHelper', ['$translate', function ($translate) {
    var rt = {};
    rt.url = function (url) {
        return url + (url.split('?')[1] ? '&' : '?') + 'locale=' + $translate.use();
    };

    rt.form = function (obj) {
        obj.locale = $translate.use();
    };
    return rt;
}]);
itpkgApp.factory('formHelper', ['$http', '$translate', '$modal', 'localeHelper',
    function ($http, $translate, $modal, localeHelper) {
        var fm = {};
        fm.submit = function (url, model) {
            if (model) {
                localeHelper.form(model);

                model.locale = $translate.use();
                $http.post(url, model).success(function (result) {
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
        return fm
    }]);

itpkgApp.directive('wdAlertDlg', function () {
    return {
        restrict: 'EA',
        templateUrl: 'widgets/alert_dlg.html?v=@version@'
    };
});
/*.directive('wdForm', function () {
 return {
 restrict: 'EA',
 replace: true,
 //scope: {
 //    form: '=form'
 //},
 templateUrl: 'widgets/form.html?v=@version@'
 };
 });
 */