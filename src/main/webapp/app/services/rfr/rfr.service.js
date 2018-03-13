(function () {
    'use strict';

    angular
        .module('rfr3App')
        .factory('Rfr', Rfr);

    Rfr.$inject = ['$resource'];

    function Rfr ($resource) {
        var service = $resource('api/rfr/:requestId', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'save': { method:'POST' },
            'update': { method:'PUT' },
            'delete':{ method:'DELETE'},
            'exportXls':{ method:'PATCH'}
        });

        return service;
    }
})();
