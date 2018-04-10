(function() {
    'use strict';

    angular
        .module('rfr3App')
        .controller('RecieverMySuffixDialogController', RecieverMySuffixDialogController);

    RecieverMySuffixDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Reciever', 'FunctionalGroup'];

    function RecieverMySuffixDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Reciever, FunctionalGroup) {
    	
    		Array.prototype.flatMap = function(lambda) {
                return Array.prototype.concat.apply([], this.map(lambda)); 
    		};
    		
        var vm = this;

        vm.reciever = entity;
        vm.clear = clear;
        vm.save = save;
        vm.mail = mail;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            
            if (vm.reciever.id !== null) {
            		Reciever.update(vm.reciever, onSaveSuccess, onSaveError);
            } else {
            		Reciever.save(vm.reciever, onSaveSuccess, onSaveError);
            }
        }
        
        function mail () {
            vm.isMailing = true;
            Reciever.mail(vm.reciever, onSaveSuccess, onSaveError);
        }

        function onSaveSuccess (result) {
            $scope.$emit('rfr3App:recieverUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }
        
        if(vm.reciever.selectTree && vm.reciever.selectTree.length) {
        		$scope.data = vm.reciever.selectTree;
        
        } else {
	        FunctionalGroup.query(function (jsonResponse) {
	    			$scope.data = angular.copy(jsonResponse);
	        });
        }
        
        $scope.CustomCallback = function (item, selectedItems) {
            if (selectedItems !== undefined && selectedItems.length >= 80) {
                return false;
            } else {
            		vm.reciever.groups = selectedItems.flatMap(group => group.name);
            		//vm.reciever.groups = selectedItems;
                return true;
            }
        };
        
    }
})();
