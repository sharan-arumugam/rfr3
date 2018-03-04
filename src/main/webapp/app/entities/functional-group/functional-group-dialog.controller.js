(function() {
    'use strict';

    angular
        .module('rfr3App')
        .controller('FunctionalGroupDialogController', FunctionalGroupDialogController);

    FunctionalGroupDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'FunctionalGroup'];

    function FunctionalGroupDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, FunctionalGroup) {
        var vm = this;

        vm.functionalGroup = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.functionalGroup.id !== null) {
                FunctionalGroup.update(vm.functionalGroup, onSaveSuccess, onSaveError);
            } else {
                FunctionalGroup.save(vm.functionalGroup, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('rfr3App:functionalGroupUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
