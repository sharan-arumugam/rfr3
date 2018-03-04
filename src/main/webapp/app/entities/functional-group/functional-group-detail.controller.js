(function() {
    'use strict';

    angular
        .module('rfr3App')
        .controller('FunctionalGroupDetailController', FunctionalGroupDetailController);

    FunctionalGroupDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'FunctionalGroup'];

    function FunctionalGroupDetailController($scope, $rootScope, $stateParams, previousState, entity, FunctionalGroup) {
        var vm = this;

        vm.functionalGroup = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('rfr3App:functionalGroupUpdate', function(event, result) {
            vm.functionalGroup = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
