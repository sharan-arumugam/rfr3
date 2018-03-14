(function() {
    'use strict';

    angular
        .module('rfr3App')
        .controller('RfrDeleteController', RfrDeleteController);

    RfrDeleteController.$inject = ['$uibModalInstance', 'entity', 'Rfr'];

    function RfrDeleteController ($uibModalInstance, entity, Rfr) {
        var vm = this;

        vm.rfr = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete () {
        		Rfr.deleteAll({}, () => $uibModalInstance.close(true));
        }
    }
})();
