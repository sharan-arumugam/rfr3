(function() {
    'use strict';

    angular
        .module('rfr3App')
        .controller('RfrController', RfrController);

    RfrController.$inject = ['Upload','$timeout','Principal', 'Rfr', 'ParseLinks', 'AlertService', '$state', 'pagingParams', 'paginationConstants'];

    function RfrController(Upload, $timeout, Principal, Rfr, ParseLinks, AlertService, $state, pagingParams, paginationConstants) {
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
        vm.exportXls = exportXls;

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

        function loadAll () {
            Rfr.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
        }
        
        function exportXls () {
            Rfr.exportXls();
        }

        function onSuccess(data, headers) {
            //vm.links = ParseLinks.parse(headers('link'));
            //vm.totalItems = headers('X-Total-Count');
            //vm.queryCount = vm.totalItems;
           // vm.page = pagingParams.page;
            vm.rfrs = data;
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
