app.service("loginService",function($http){
	//读取登录人名字
	this.loginName = function(){
		return $http.get('../login/name.do');
	}
});