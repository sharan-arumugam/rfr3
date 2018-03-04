(function() {
    'use strict';

    angular
        .module('rfr3App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('functional-group-my-suffix', {
            parent: 'entity',
            url: '/functional-group-my-suffix',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'FunctionalGroups'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/functional-group-my-suffix/functional-groupsmySuffix.html',
                    controller: 'FunctionalGroupMySuffixController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('functional-group-my-suffix-detail', {
            parent: 'functional-group-my-suffix',
            url: '/functional-group-my-suffix/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'FunctionalGroup'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/functional-group-my-suffix/functional-group-my-suffix-detail.html',
                    controller: 'FunctionalGroupMySuffixDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'FunctionalGroup', function($stateParams, FunctionalGroup) {
                    return FunctionalGroup.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'functional-group-my-suffix',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('functional-group-my-suffix-detail.edit', {
            parent: 'functional-group-my-suffix-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/functional-group-my-suffix/functional-group-my-suffix-dialog.html',
                    controller: 'FunctionalGroupMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FunctionalGroup', function(FunctionalGroup) {
                            return FunctionalGroup.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('functional-group-my-suffix.new', {
            parent: 'functional-group-my-suffix',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/functional-group-my-suffix/functional-group-my-suffix-dialog.html',
                    controller: 'FunctionalGroupMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                imt: null,
                                imt1: null,
                                imt2: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('functional-group-my-suffix', null, { reload: 'functional-group-my-suffix' });
                }, function() {
                    $state.go('functional-group-my-suffix');
                });
            }]
        })
        .state('functional-group-my-suffix.edit', {
            parent: 'functional-group-my-suffix',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/functional-group-my-suffix/functional-group-my-suffix-dialog.html',
                    controller: 'FunctionalGroupMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FunctionalGroup', function(FunctionalGroup) {
                            return FunctionalGroup.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('functional-group-my-suffix', null, { reload: 'functional-group-my-suffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('functional-group-my-suffix.delete', {
            parent: 'functional-group-my-suffix',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/functional-group-my-suffix/functional-group-my-suffix-delete-dialog.html',
                    controller: 'FunctionalGroupMySuffixDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['FunctionalGroup', function(FunctionalGroup) {
                            return FunctionalGroup.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('functional-group-my-suffix', null, { reload: 'functional-group-my-suffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
