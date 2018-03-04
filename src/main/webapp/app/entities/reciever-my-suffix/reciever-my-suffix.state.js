(function() {
    'use strict';

    angular
        .module('rfr3App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('reciever-my-suffix', {
            parent: 'entity',
            url: '/reciever-my-suffix',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Recievers'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/reciever-my-suffix/recieversmySuffix.html',
                    controller: 'RecieverMySuffixController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('reciever-my-suffix-detail', {
            parent: 'reciever-my-suffix',
            url: '/reciever-my-suffix/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Reciever'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/reciever-my-suffix/reciever-my-suffix-detail.html',
                    controller: 'RecieverMySuffixDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Reciever', function($stateParams, Reciever) {
                    return Reciever.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'reciever-my-suffix',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('reciever-my-suffix-detail.edit', {
            parent: 'reciever-my-suffix-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/reciever-my-suffix/reciever-my-suffix-dialog.html',
                    controller: 'RecieverMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Reciever', function(Reciever) {
                            return Reciever.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('reciever-my-suffix.new', {
            parent: 'reciever-my-suffix',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/reciever-my-suffix/reciever-my-suffix-dialog.html',
                    controller: 'RecieverMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                psNumber: null,
                                appleMail: null,
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('reciever-my-suffix', null, { reload: 'reciever-my-suffix' });
                }, function() {
                    $state.go('reciever-my-suffix');
                });
            }]
        })
        .state('reciever-my-suffix.edit', {
            parent: 'reciever-my-suffix',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/reciever-my-suffix/reciever-my-suffix-dialog.html',
                    controller: 'RecieverMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Reciever', function(Reciever) {
                            return Reciever.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('reciever-my-suffix', null, { reload: 'reciever-my-suffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('reciever-my-suffix.delete', {
            parent: 'reciever-my-suffix',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/reciever-my-suffix/reciever-my-suffix-delete-dialog.html',
                    controller: 'RecieverMySuffixDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Reciever', function(Reciever) {
                            return Reciever.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('reciever-my-suffix', null, { reload: 'reciever-my-suffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
