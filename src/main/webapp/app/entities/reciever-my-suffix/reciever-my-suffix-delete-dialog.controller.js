(function() {
    'use strict';

    angular
        .module('rfr3App')
        .controller('RecieverMySuffixDeleteController',RecieverMySuffixDeleteController);

    RecieverMySuffixDeleteController.$inject = ['$uibModalInstance', 'entity', 'Reciever'];

    function RecieverMySuffixDeleteController($uibModalInstance, entity, Reciever) {
        var vm = this;

        vm.reciever = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Reciever.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
