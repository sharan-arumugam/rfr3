(function() {
    'use strict';

    angular
        .module('rfr3App')
        .controller('RegionMySuffixController', RegionMySuffixController);

    RegionMySuffixController.$inject = ['Region'];

    function RegionMySuffixController(Region) {

        var vm = this;

        vm.regions = [];

        loadAll();

        function loadAll() {
            Region.query(function(result) {
                vm.regions = result;
                vm.searchQuery = null;
            });
        }
    }
})();
