var app = angular.module('leonteqApp', ['ngMaterial']);

app.controller('leonteqController', ['$scope', '$http', '$mdToast', function($scope, $http, $mdToast) {
	var testUrl = /(https?:\/\/)?(www\.)?[-a-zA-Z0-9@:%._\+~#=]{2,256}\.[a-z]{2,4}\b([-a-zA-Z0-9@:%_\+.~#?&\/\/=]*)/g;
	$scope.restPath = {
			basic : 'rest/leonteq',
			short : '/short',
			unshort : '/unshort'
	}
	
	$scope.shortUrl = function(){
		var url = $scope.urlToShortLoad;
		if ($scope.testValidUrl(url)){
			var urlEncoded = encodeURIComponent(url);
			$http.get($scope.restPath.basic+$scope.restPath.short+'/?uri='+urlEncoded)
				.then(function(response){
					if (response.data.result == "error"){
						$mdToast.show($mdToast.simple().textContent('Error! ' + response.data.data));
					}else{
						$scope.urlShortedLoad = response.data.data; 
					}
				},function(response){
					$mdToast.show($mdToast.simple().textContent('Generic Error!'));
				});
		}
	}
	
	$scope.unshortUrl = function(){
		var url = $scope.urlShortedLoad;
		if ($scope.testValidUrl(url)){
			var urlEncoded = encodeURIComponent(url);
			$http.get($scope.restPath.basic+$scope.restPath.unshort+'/?uri='+urlEncoded)
				.then(function(response){
					if (response.data.result == "error"){
						$mdToast.show($mdToast.simple().textContent('Error! ' + response.data.data));
					}else{
						$scope.urlToShortLoad = response.data.data; 
					}
				},function(response){
					$mdToast.show($mdToast.simple().textContent('Generic Error!'));
				});
		}
	}
	
	$scope.testValidUrl = function(url){
		var testUrl = /(https?:\/\/)?(www\.)?[-a-zA-Z0-9@:%._\+~#=]{2,256}\.[a-z]{2,4}\b([-a-zA-Z0-9@:%_\+.~#?&\/\/=]*)/g;
		return url && url.length > 10 && testUrl.test(url);
	}
	
}]);