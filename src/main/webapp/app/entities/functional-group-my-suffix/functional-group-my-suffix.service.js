(function() {
    'use strict';
    angular
        .module('rfr3App')
        .factory('FunctionalGroup', FunctionalGroup);

    FunctionalGroup.$inject = ['$resource'];

    function FunctionalGroup ($resource) {
        var resourceUrl =  'api/functional-groups/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
