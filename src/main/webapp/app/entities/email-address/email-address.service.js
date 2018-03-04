(function() {
    'use strict';
    angular
        .module('rfr3App')
        .factory('EmailAddress', EmailAddress);

    EmailAddress.$inject = ['$resource'];

    function EmailAddress ($resource) {
        var resourceUrl =  'api/email-addresses/:id';

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
