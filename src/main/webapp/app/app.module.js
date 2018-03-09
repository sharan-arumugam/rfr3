(function() {
    'use strict';

    angular
        .module('rfr3App', [
            'ngStorage',
            //'ngTable',
            'ngResource',
            'ngCookies',
            'ngAria',
            'ngCacheBuster',
            'ngFileUpload',
            'ui.bootstrap',
            'ui.bootstrap.datetimepicker',
            'ui.router',
            'infinite-scroll',
            // jhipster-needle-angularjs-add-module JHipster will add new module here
            'angular-loading-bar',
            'multiselect-searchtree'
        ])
        .run(run);

    run.$inject = ['stateHandler'];

    function run(stateHandler) {
        stateHandler.initialize();
    }
})();
