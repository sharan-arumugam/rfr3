(function() {
    'use strict';

    angular
        .module('rfr3App')
        .controller('RfrController', RfrController);

    RfrController.$inject = ['$filter','$scope','Upload','$timeout','Principal', 'Rfr', 'ParseLinks', 'AlertService', '$state', 'pagingParams', 'paginationConstants'];

    function RfrController($filter,$scope, Upload, $timeout, Principal, Rfr, ParseLinks, AlertService, $state, pagingParams, paginationConstants) {
        var vm = this;

        vm.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        vm.currentAccount = null;
        vm.languages = null;
        vm.loadAll = loadAll;
        vm.setActive = setActive;
        vm.rfrs = [];
        vm.page = 1;
        vm.totalItems = null;
        vm.clear = clear;
        vm.links = null;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.transition = transition;
        vm.uploadFiles = uploadFiles;
        vm.date = new Date();
        vm.deleteAll = deleteAll;
        vm.downloadXLS = downloadXLS;
        vm.filterText = filterText;
        
        vm.rfrs2;

        vm.loadAll();
        
        Principal.identity().then(function(account) {
            vm.currentAccount = account;
        });

        function setActive (rfr, isActivated) {
            rfr.activated = isActivated;
            Rfr.update(rfr, function () {
                vm.loadAll();
                vm.clear();
            });
        }
        
        function filterText () {
        		vm.rfrs = vm.rfrs2.filter(rfr =>
        				   (rfr.requestId+'').includes(vm.search)
        				|| (rfr.requestTitle).includes(vm.search)
        				|| (rfr.status).includes(vm.search)
        				|| (rfr.skills && (rfr.skills+'').includes(vm.search))
        				|| (rfr.fulfillment && (rfr.fulfillment).includes(vm.search))
        				);
        }

        function loadAll () {
            Rfr.query({}, onSuccess, onError);
        }
        
        function downloadXLS () {
            $.fileDownload('/api/rfr/export',{
                httpMethod : "GET",
            })
            .done(function(e, response){
            })
            .fail(function(e, response){
            });
        }

        function deleteAll () {
            Rfr.deleteAll();
            vm.rfrs = [];
        }

        function onSuccess(data, headers) {
            vm.rfrs = data;
            vm.rfrs2 = vm.rfrs;
        }

        function onError(error) {
            AlertService.error(error.data.message);
        }

        function clear () {
            vm.rfr = {
                id: null, login: null, firstName: null, lastName: null, email: null,
                activated: null, langKey: null, createdBy: null, createdDate: null,
                lastModifiedBy: null, lastModifiedDate: null, resetDate: null,
                resetKey: null, authorities: null
            };
        }

        function sort () {
            var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
            if (vm.predicate !== 'id') {
                result.push('id');
            }
            return result;
        }

        function loadPage (page) {
            vm.page = page;
            vm.transition();
        }

        function transition () {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }
        
        function uploadFiles(file, errFiles) {
            vm.f = file;
            vm.errFile = errFiles && errFiles[0];
            if (file) {
            		vm.date = new Date();
                file.upload = Upload.upload({
                    url: '/api/upload/rfr',
                    data: {rfrFile: file}
                });

                file.upload.then(function (response) {
                    $timeout(function () {
                        file.result = response.data;
                        vm.errorMsg = response.statusText;
                        vm.loadAll();
                    });
                }, function (response) {
                    if (response.status > 0) {
                        vm.errorMsg = response.statusText;
                    }
                }, function (evt) {
                    file.progress = Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
                });
            }   
        }
    }
})();
