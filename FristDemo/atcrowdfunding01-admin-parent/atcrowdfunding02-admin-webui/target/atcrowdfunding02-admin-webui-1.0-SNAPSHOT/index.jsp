<%--
  Created by IntelliJ IDEA.
  User: 1
  Date: 2024/2/28
  Time: 13:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<base href="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">
<%--http://localhost:8080/atcrowdfunding02_admin_webui/test/ssm.html--%>
<script type="text/javascript" src="jquery/jquery-2.1.1.min.js"></script>
<script type="text/javascript" src="layer/layer.js"></script>
<script type="text/javascript">
    $(function (){
        $("#btn1").click(function (){
            $.ajax({
                "url":"send/array/one.html",          //请求目标资源地址
                "type":"post",                      //请求方式
                "data":{"array":[5,8,12]},                    //请求数据
                "datatype":"text",                  //如何对待服务器端返回数据
                "success":function (response){      //服务器处理请求成功的回调函数

                },
                "error":function (response){           //服务器处理请求失败的回调函数

                }
            })
        });
    });

    $(function (){
        $("#btn2").click(function (){
            $.ajax({
                "url":"send/array/two.html",          //请求目标资源地址
                "type":"post",                      //请求方式
                "data":{
                    "array[0]":5,
                    "array[1]":8,
                    "array[2]":12,
                },                    //请求数据
                "datatype":"text",                  //如何对待服务器端返回数据
                "success":function (response){      //服务器处理请求成功的回调函数

                },
                "error":function (response){           //服务器处理请求失败的回调函数

                }
            })
        });
    });
    $(function (){
        $("#btn3").click(function (){
            // 准备要发送的数组
            var array = [5,8,12];
            // 将 JSON 数组转换成 JSON 字符串
            // "[5,8,12]"
            var RequestBody = JSON.stringify(array);
            $.ajax({
                "url":"send/array/three.html",          //请求目标资源地址
                "type":"post",                      //请求方式
                "data": RequestBody,                    //请求数据
                "contentType":"application/json;charset=UTF-8",// 告诉服务器端当前请求的请求体是JSON 格式
                "datatype":"text",                  //如何对待服务器端返回数据
                "success":function (response){      //服务器处理请求成功的回调函数

                },
                "error":function (response){           //服务器处理请求失败的回调函数

                }
            })
        });
    });

    $(function (){
        $("#btn4").click(function (){
            // 准备要发送的 JSON 数据
            var student = {
                "stuId": 999,
                "stuName": "tom",
                "stuAge": 23,
                "address": {
                    "province": "shandong",
                    "city": "heze",
                    "street": "hello"
                },
                "subjectList": [
                    {
                        "subjectName": "subjectOne", "subjectScore": 500
                    }, {
                        "subjectName": "subjectTwo", "subjectScore": 1000
                    }, {
                        "subjectName": "subjectThree", "subjectScore": 2000
                    }],
                "map": {
                    "yingyu": 100, "shuxue": 80, "wuli": 90
                }
            }
            // 将 JSON 对象转换为 JSON 字符串
            var requestBody = JSON.stringify(student);
            // 发送 Ajax 请求
            $.ajax({ "url": "send/compose/object.json",
                "type": "post",
                "data":requestBody,
                "contentType":"application/json;charset=UTF-8",
                "dataType":"json",
                "success":function(response){

                },"error":function(response){

                }
            });
        });
    });
    $(function (){
        $("#btn5").click(function (){
            layer.msg("Layer");
        });
    });
</script>
<head>
    <title>Title</title>
</head>
<body>
    <a href="test/ssm.html">测试SSM整合环境</a>
<br/>
    <br/>
    <br/>
    <br/>
    <br/>
<button id="btn1">Send [5,8,12] One</button>
    <br/>
    <br/>
    <br/>
    <br/>
    <br/>
    <button id="btn2">Send [5,8,12] Two</button>

    <br/>
    <br/>
    <br/>

    <button id="btn3">Send [5,8,12] Three</button>

    <br/>
    <br/>
    <br/>

    <button id="btn4">Send compose object</button>

    <br/>
    <br/>
    <br/>
    <button id="btn5">点我弹框</button>

</body>
</html>
