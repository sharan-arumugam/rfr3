(function() {
    'use strict';

    angular
        .module('rfr3App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('functional-group', {
            parent: 'entity',
            url: '/functional-group',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'FunctionalGroups'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/functional-group/functional-groups.html',
                    controller: 'FunctionalGroupController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('functional-group-detail', {
            parent: 'functional-group',
            url: '/functional-group/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'FunctionalGroup'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/functional-group/functional-group-detail.html',
                    controller: 'FunctionalGroupDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'FunctionalGroup', function($stateParams, FunctionalGroup) {
                    return FunctionalGroup.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'functional-group',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('functional-group-detail.edit', {
            parent: 'functional-group-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/functional-group/functional-group-dialog.html',
                    controller: 'FunctionalGroupDialogController',
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
        .state('functional-group.new', {
            parent: 'functional-group',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/functional-group/functional-group-dialog.html',
                    controller: 'FunctionalGroupDialogController',
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
                    $state.go('functional-group', null, { reload: 'functional-group' });
                }, function() {
                    $state.go('functional-group');
                });
            }]
        })
        .state('functional-group.edit', {
            parent: 'functional-group',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/functional-group/functional-group-dialog.html',
                    controller: 'FunctionalGroupDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FunctionalGroup', function(FunctionalGroup) {
                            return FunctionalGroup.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('functional-group', null, { reload: 'functional-group' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('functional-group.delete', {
            parent: 'functional-group',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/functional-group/functional-group-delete-dialog.html',
                    controller: 'FunctionalGroupDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['FunctionalGroup', function(FunctionalGroup) {
                            return FunctionalGroup.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('functional-group', null, { reload: 'functional-group' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
