<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>ECharts</title>
    <!-- including ECharts file -->
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js" type="text/javascript"></script>
    <script src="http://echarts.baidu.com/gallery/vendors/echarts/echarts.min.js" ></script>
</head>
<body>
    <!-- prepare a DOM container with width and height -->
    <div id="main" style="height:450px;"></div>
    <script type="text/javascript">
        // based on prepared DOM, initialize echarts instance
        var myChart = echarts.init(document.getElementById('main'));

        var userCnt = ${userCnt};

        // 유저 목록
        var userList = ${userList};
        var legendArray = new Array();
     	$(userList).each(function(index, value) {
     		legendArray[index] = value.name;
    	});  

    	// 과목 목록
    	var subjectList = ${subjectList};
    	console.log(subjectList);
    	var subjectArray = new Array();
     	$(subjectList).each(function(index, value) {
     		subjectArray[index] = value.subjectName;
    	}); 

    	// 성적표 목록
    	var scoreList = ${scoreList};
    	var dataArray = new Array();
    	var series='[';
    	var series = '['; 
       	for(var i=1; i<=userCnt; i++) {   	   	  	   			   			
       		$(scoreList).each(function(index, value) {
           		$(value).each(function(index2, value2) {
               		if (index == i - 1) {
	               		dataArray[index2] = value2.score;
               		}
               	}); 			
       		});		
       		series += '{"name":"' + legendArray[i-1] + '", "symbol":"circle", "symbolSize":10, "type":"line", "data":[' + dataArray + ']}';
       	   	if (i != userCnt) {
       	   	   	series += ",";
       	   	}
       	 	dataArray = [];
       	 }
       	series += ']';
      	 
        // specify chart configuration item and data
		option = {
		    title: {
		        text: '성적표'
		    },
		    tooltip: {
		    	trigger: 'item',
		    	formatter: function(p1, p2) {  	 
			    	var result = p1.seriesName + "의 " +  p1.name + " 성적은 " + p1.value + "점 입니다";
			    	return result;
		    	}		       
		    },
		    legend: {
		        data: userList
		    },
		    grid: {
		        left: '3%',
		        right: '4%',
		        bottom: '3%',
		        containLabel: true
		    },
		    toolbox: {
		        feature: {
		            saveAsImage: {}
		        }
		    },
		    xAxis: {
		        type: 'category',
		        boundaryGap: false,
		        data: subjectArray
		    },
		    yAxis: {
		        type: 'value'
		    },
		    series:  JSON.parse(series)
		};


        // use configuration item and data specified to show chart
        myChart.setOption(option);
    </script>
</body>
</html>