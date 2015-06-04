itpkgApp.directive('wdAlertDlg', function () {
    return {
        restrict: 'EA',
        templateUrl: '/widgets/alert_dlg.html?v=@version@'
    };
}).directive('wdForm', function () {
    return {
        restrict: 'EA',
        replace: true,
        //scope: {
        //    form: '=form'
        //},
        templateUrl: '/widgets/form.html?v=@version@'
    };
});