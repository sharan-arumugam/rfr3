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
        .filter('custom', function() {
		  return function(input, search) {
		    if (!input) return input;
		    if (!search) return input;
		    var expected = ('' + search).toLowerCase();
		    var result = {};
		    angular.forEach(input, function(value, key) {
		      var actual = ('' + value).toLowerCase();
		      if (actual.indexOf(expected) !== -1) {
		        result[key] = value;
		      }
		    });
		    return result;
		  }
		})
        .run(run);

    run.$inject = ['stateHandler'];

    function run(stateHandler) {
        stateHandler.initialize();
    }
})();
