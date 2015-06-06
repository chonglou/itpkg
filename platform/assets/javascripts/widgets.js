itpkgApp.controller('ModalInstanceCtrl', [
    '$scope', '$modalInstance', 'title', 'items',
    function ($scope, $modalInstance, title, items) {
        $scope.title = title;
        $scope.items = items;

        $scope.ok = function () {
            $modalInstance.dismiss('cancel');
        };
    }]);



itpkgApp.directive('wdAlertDlg', function () {
    return {
        restrict: 'EA',
        templateUrl: 'widgets/alert_dlg.html?v=@version@'
    };
}).directive('wdForm', function () {
    return {
        restrict: 'EA',
        replace: true,
        //scope: {
        //    form: '=form'
        //},
        templateUrl: 'widgets/form.html?v=@version@'
    };
});