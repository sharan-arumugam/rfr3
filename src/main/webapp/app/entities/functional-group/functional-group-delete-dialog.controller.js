(function() {
    'use strict';

    angular
        .module('rfr3App')
        .controller('FunctionalGroupDeleteController',FunctionalGroupDeleteController);

    FunctionalGroupDeleteController.$inject = ['$uibModalInstance', 'entity', 'FunctionalGroup'];

    function FunctionalGroupDeleteController($uibModalInstance, entity, FunctionalGroup) {
        var vm = this;

        vm.functionalGroup = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            FunctionalGroup.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
