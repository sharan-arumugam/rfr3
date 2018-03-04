(function() {
    'use strict';

    angular
        .module('rfr3App')
        .controller('EmailAddressDialogController', EmailAddressDialogController);

    EmailAddressDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'EmailAddress', 'FunctionalGroup'];

    function EmailAddressDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, EmailAddress, FunctionalGroup) {
        var vm = this;

        vm.emailAddress = entity;
        vm.clear = clear;
        vm.save = save;
        vm.functionalgroups = FunctionalGroup.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.emailAddress.id !== null) {
                EmailAddress.update(vm.emailAddress, onSaveSuccess, onSaveError);
            } else {
                EmailAddress.save(vm.emailAddress, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('rfr3App:emailAddressUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
