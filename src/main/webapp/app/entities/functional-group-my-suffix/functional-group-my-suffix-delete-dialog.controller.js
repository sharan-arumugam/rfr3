(function() {
    'use strict';

    angular
        .module('rfr3App')
        .controller('FunctionalGroupMySuffixDeleteController',FunctionalGroupMySuffixDeleteController);

    FunctionalGroupMySuffixDeleteController.$inject = ['$uibModalInstance', 'entity', 'FunctionalGroup'];

    function FunctionalGroupMySuffixDeleteController($uibModalInstance, entity, FunctionalGroup) {
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
