(function() {
    'use strict';
    angular
        .module('rfr3App')
        .factory('Reciever', Reciever);

    Reciever.$inject = ['$resource'];

    function Reciever ($resource) {
        var resourceUrl =  'api/recievers/:id';

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
