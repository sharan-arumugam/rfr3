(function() {
    'use strict';

    angular
        .module('rfr3App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('rfr', {
            parent: 'admin',
            url: '/rfr',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'RFR'
            },
            views: {
                'content@': {
                    templateUrl: 'app/admin/rfr/rfr.html',
                    controller: 'RfrController',
                    controllerAs: 'vm'
                }
            },            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                }
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort)
                    };
                }]
            }        })
        .state('rfr.new', {
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/admin/rfr/rfr-dialog.html',
                    controller: 'RfrDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null, login: null, firstName: null, lastName: null, email: null,
                                activated: true, langKey: null, createdBy: null, createdDate: null,
                                lastModifiedBy: null, lastModifiedDate: null, resetDate: null,
                                resetKey: null, authorities: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('rfr', null, { reload: true });
                }, function() {
                    $state.go('rfr');
                });
            }]
        })
        .state('rfr.edit', {
            url: '/{requestId}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/admin/rfr/rfr-dialog.html',
                    controller: 'RfrDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Rfr', function(Rfr) {
                            return Rfr.get({requestId : $stateParams.requestId}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('rfr', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('rfr-detail', {
            parent: 'rfr',
            url: '/{requestId}',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'rfr3'
            },
            views: {
                'content@': {
                    templateUrl: 'app/admin/rfr/rfr-detail.html',
                    controller: 'RfrDetailController',
                    controllerAs: 'vm'
                }
            }
        })
        .state('rfr.delete', {
            url: '/{login}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/admin/rfr/rfr-delete-dialog.html',
                    controller: 'RfrDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Rfr', function(Rfr) {
                            return Rfr.query({}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('rfr', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }
})();
