(function() {
    'use strict';

    angular
        .module('rfr3App')
        .controller('FunctionalGroupMySuffixDetailController', FunctionalGroupMySuffixDetailController);

    FunctionalGroupMySuffixDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'FunctionalGroup', 'Reciever'];

    function FunctionalGroupMySuffixDetailController($scope, $rootScope, $stateParams, previousState, entity, FunctionalGroup, Reciever) {
        var vm = this;

        vm.functionalGroup = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('rfr3App:functionalGroupUpdate', function(event, result) {
            vm.functionalGroup = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
