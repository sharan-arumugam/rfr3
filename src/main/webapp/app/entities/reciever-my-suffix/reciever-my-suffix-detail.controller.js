(function() {
    'use strict';

    angular
        .module('rfr3App')
        .controller('RecieverMySuffixDetailController', RecieverMySuffixDetailController);

    RecieverMySuffixDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Reciever', 'FunctionalGroup'];

    function RecieverMySuffixDetailController($scope, $rootScope, $stateParams, previousState, entity, Reciever, FunctionalGroup) {
        var vm = this;

        vm.reciever = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('rfr3App:recieverUpdate', function(event, result) {
            vm.reciever = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
