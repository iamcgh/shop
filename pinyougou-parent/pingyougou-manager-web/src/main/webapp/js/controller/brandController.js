app.controller('brandController', function($scope,$controller,brandService) {
		
	//实现伪继承
		$controller('baseController',{$scope:$scope});
		

		/*//查询品牌列表
		$scope.findAll = function() {
			$http.get('../brand/findAll.do').success(function(response) {
				$scope.list = response;
			});
		}*/

		//分页查询
		/*$scope.findPage = function(page,rows){
			$http.get('../brand/findPage.do?page='+page+'&rows='+rows).success(
					function(response){
						$scope.list = response.rows;
						$scope.paginationConf.totalItems = response.total;//更新总记录条数
					}
			)
		}*/
		
		//保存
		$scope.save = function(){
			var obj = null;
			if($scope.entity.id!=null){//如果有ID
				obj = brandService.update($scope.entity); 
			}else {
				obj = brandService.add($scope.entity);
			}	
			obj.success(
					function(response){
						if(response.success){
							   //重新查询 
					            $scope.reloadList();//重新加载
						}else{
							   alert(response.message);
						 }
					}		
			);				

		}
		
		//根据id获取实体
		$scope.findOne=function(id){
			brandService.findOne(id).success(
					function(response){
						$scope.entity= response;					
				     }
			);				
		}
		
		 
		//批量删除 
		$scope.dele=function(){			
				//获取选中的复选框			
				brandService.dele($scope.selectIds).success(
						function(response){
							if(response.success){
									$scope.reloadList();//刷新列表
							}else{
								alert(response.message);
							}						
						}		
				);				
		}
		
		$scope.searchEntity={};//定义搜索对象 			
		//条件查询 
		$scope.search=function(page,rows){
			brandService.search(page,rows,$scope.searchEntity).success(
				function(response){
						$scope.paginationConf.totalItems=response.total;//总记录数 
						$scope.list=response.rows;//给列表变量赋值
						//$scope.reloadList();//刷新列表
				}		
			);				
		}



	});