(function() {
    'use strict';

    angular
        .module('rfr3App')
        .controller('EmailAddressDetailController', EmailAddressDetailController);

    EmailAddressDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'EmailAddress', 'FunctionalGroup'];

    function EmailAddressDetailController($scope, $rootScope, $stateParams, previousState, entity, EmailAddress, FunctionalGroup) {
        var vm = this;

        vm.emailAddress = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('rfr3App:emailAddressUpdate', function(event, result) {
            vm.emailAddress = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
