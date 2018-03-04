(function() {
    'use strict';

    angular
        .module('rfr3App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('email-address', {
            parent: 'entity',
            url: '/email-address',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'EmailAddresses'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/email-address/email-addresses.html',
                    controller: 'EmailAddressController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('email-address-detail', {
            parent: 'email-address',
            url: '/email-address/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'EmailAddress'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/email-address/email-address-detail.html',
                    controller: 'EmailAddressDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'EmailAddress', function($stateParams, EmailAddress) {
                    return EmailAddress.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'email-address',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('email-address-detail.edit', {
            parent: 'email-address-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/email-address/email-address-dialog.html',
                    controller: 'EmailAddressDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EmailAddress', function(EmailAddress) {
                            return EmailAddress.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('email-address.new', {
            parent: 'email-address',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/email-address/email-address-dialog.html',
                    controller: 'EmailAddressDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                psNumber: null,
                                name: null,
                                appleMail: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('email-address', null, { reload: 'email-address' });
                }, function() {
                    $state.go('email-address');
                });
            }]
        })
        .state('email-address.edit', {
            parent: 'email-address',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/email-address/email-address-dialog.html',
                    controller: 'EmailAddressDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EmailAddress', function(EmailAddress) {
                            return EmailAddress.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('email-address', null, { reload: 'email-address' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('email-address.delete', {
            parent: 'email-address',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/email-address/email-address-delete-dialog.html',
                    controller: 'EmailAddressDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['EmailAddress', function(EmailAddress) {
                            return EmailAddress.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('email-address', null, { reload: 'email-address' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
