<%--
  Created by IntelliJ IDEA.
  User: caohq
  Date: 2018/11/16
  Time: 17:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page  language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>Insert title here</title>
    <script type="text/javascript" src="/console/shared/js/jquery-3.2.1.min.js " ></script>
    <script src="/console/shared/bootstrap-3.3.7/js/bootstrap.js"></script>
    <link rel="stylesheet" type="text/css" href="/console/shared/bootstrap-3.3.7/css/bootstrap.css">
    <link rel="stylesheet" type="text/css" href="/console/shared/bootstrap-3.3.7/css/bootstrap-table.min.css">
    <link rel="stylesheet" type="text/css" href="/console/datasync/css/createTask.css" />
    <link rel="stylesheet" type="text/css" href="/console/datasync/css/style.min.css" />
    <style>
        .fixed-table-pagination .page-list{
            display: none !important;
        }
        * {
            outline: 0;
            padding: 0;
            margin: 0;
            border: 0;
        }

        TABLE {
            font-size: 10.5pt;
        }
        /*tbody {*/
            /*display: table-row-group;*/
            /*vertical-align: middle;*/
            /*border-color: inherit;*/
        /*}*/
        /*tr {*/
            /*display: table-row;*/
            /*vertical-align: inherit;*/
            /*border-color: inherit;*/
        /*}*/
        /*.right_titleleft {*/
            /*height: 54px;*/
            /*background: url(/console/shared/images/right_title_bg.jpg) no-repeat right;*/
            /*width: 143px;*/
            /*float: left;*/
        /*}*/
        /*.right_titlemid {*/
            /*height: 54px;*/
            /*padding-top: 23px;*/
            /*padding-left: 5px;*/
            /*padding-right: 5px;*/
            /*background: url(/console/shared/images/right_titlemid_bg.jpg) repeat-x top;*/
            /*float: left;*/
        /*}*/
        /*.right_titleright {*/
            /*height: 54px;*/
            /*width: 151px;*/
            /*background: url(/console/shared/images/right_titleleft_bg.jpg) no-repeat left;*/
            /*float: left;*/
            /*margin-left: -5px;*/
        /*}*/
    </style>
</head>
<body style="overflow: auto; height: 400px;">


<div>

    <div style="width:50%;float:left;margin-left: 46px;margin-top: 20px;height:27px;">
        <span style="font-weight: 700;font-size: 24px;">数据源:&nbsp;&nbsp;&nbsp;&nbsp;</span>
        <label for="aaa" style="font-size: 16px" >数据库</label>
        <input name="ways" type="radio" checked="checked" value="database" id="aaa"/>
        <label for="bbb" style="font-size: 16px">&nbsp;&nbsp;&nbsp;&nbsp;本地上传</label>
        <input name="ways" type="radio" value="file" id="bbb"/>
    </div>

    <!--数据库-->
    <div style="width:64%;margin-top:20px;float:left;margin-left: 100px;">
        <div id="sjk" class="form-group" style="width: 100%;height: 100%;margin-left: 76px;">
            <label for="aaa" >数据库列表:</label>
            <select id="selectId" class="form-control selectpicker" style="width: 200px;display: inline !important;"></select>
            <div>
                <label id="tableLabel" style="margin-top: 10px">&nbsp;&nbsp;选择表资源</label>
                <div id="tablesDiv" style="margin-left: 40px;height:244px;overflow-y:auto;"></div>
            </div>
            <div style="width: 100%;height: 40px;margin-top: 20px; " id="sqlSearchDiv">
                <label>sql查询</label>
                <input id="sqlInputBox" class="form-control sqlStatements" style="width: 300px;display: inline !important;" type="text"/>
                <input id="createNewTableName" class="form-control sqlStatements" style="width: 100px;display: inline !important;" type="text" placeholder="请输入表名"/>
                <button type="button" class="btn blue preview" onclick="showPreviewModal()" >预览</button>
                <button type="button" class="btn green" onclick="addSql()"><span class="glyphicon glyphicon-plus"></span>sql查询</button>
                <button type="button" class="btn green" onclick="submitSqlData()">提交</button>
            </div>
        </div>

        <!--本地数据源列表-->
        <div id="bdsc" style="width: 100%;height: 100%;margin-left: 76px;">
            <div style="" class="form-group">
                <label for="aaa" >本地数据源列表:</label>
                <select id="selectBdDirID"  class="form-control selectpicker" style="width: 200px;display: inline !important;"></select>
                <div>
                    <label id="bdTableLabel">&nbsp;&nbsp;请选择资源</label>
                    <div id="bdDirDiv" style="width: 70%;margin-left: 105px;height:200px;overflow-y:auto;"></div>
                </div>
            </div>
            <div style="width: 100%;height: 40px;float: right;">
                <button type="button"  id="bdSubmitButton" class="btn blue preview" onclick="submitLocalFileData()"  style="float: right;">提交</button>
            </div>
        </div>
    </div>

    <div class="modal fade" id="createLocalFileModal" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="createFileTitle">请输入任务名称</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form>
                        <div class="form-group">
                            <label for="localFileName" class="col-form-label">任务名</label>
                            <input type="text" autofocus class="form-control" id="localFileName">
                            <span id="checkedLocalFileName" style="color: red;"></span>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" id="createLocalFileSureBut">确认创建</button>
                </div>
            </div>
        </div>
    </div>
    <div class="modal fade" id="createFileMModal" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="createLocalFileTitle">请输入任务名称</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form>
                        <div class="form-group">
                            <label for="fileName" class="col-form-label">任务名</label>
                            <input type="text" autofocus class="form-control" id="fileName">
                            <span id="checkedFileName" style="color: red;"></span>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" id="createFileSureBut">确认创建</button>
                </div>
            </div>
        </div>
    </div>

    <input type="hidden" id="sql"/>
    <input type="hidden" id="connData"/>
</div>

<script type="text/javascript" src="/console/shared/bootstrap-3.3.7/js/bootstrap-table.js"></script>
<script type="text/javascript">
    $.ajaxSettings.async=false;
    start();
    loadBdDataDir();
    function start() {
        document.getElementById("tableLabel").style.display="none";//隐藏“选择表资源标签”
        document.getElementById("bdTableLabel").style.display="none";//隐藏“选择表资源标签”
        document.getElementById("bdSubmitButton").style.display="none";//隐藏“选择表资源标签”
        document.getElementById("sqlSearchDiv").style.display="none";//隐藏“选择表资源标签”
        document.getElementById("bdsc").style.display="none";//隐藏“选择表资源标签”
        $.ajax({
            type:"POST",
            url:"/searchDataList.do",
            data:{},
            success:function (dataSession) {
                var dataSessiobArray=dataSession.substr(1,dataSession.length-4).split(',');
                $("#selectId").append("<option style='width: 300px;display: none;'>请选择...</option>");
                for(var i=0;i<dataSessiobArray.length;i++){
                    $("#selectId").append("<option style=width: 300px; value='"+ dataSessiobArray[i]+"'>"+ dataSessiobArray[i].substr(0, dataSessiobArray[i].indexOf('$'))+"</option>");
                }
            },
            error:function () {
                console.log("请求失败")
            }
        })
    };

    $("#selectId").on("change",function () {
        var connData = $("#selectId option:selected")[0].value;//获取数据库参数
        $.ajax({
            type:"POST",
            url:"/searchTables.do",
            cache: false,
            data:{
                connData:connData
            },
            success:function (data) {
                if(data.trim()=="数据库连接异常！"){
                    alert(data);
                    return;
                }
                $('#tablesDiv').empty();//清空div
                document.getElementById("tableLabel").style.display="block";//显示“选择表资源标签”
                document.getElementById("sqlSearchDiv").style.display="block";//显示“选择表资源标签”
                var json = data.substr(1,data.length-4);
                var dataArray=json.split(",");
                for(var i=0;i<dataArray.length;i++){
                    $("#tablesDiv").append("<div style='width: 200px;float:left;'><input type='checkbox' value='"+dataArray[i]+"'>"+dataArray[i]+"</input></div>");
                }
            },
            error:function () {
                console.log("请求失败")
            }
        })
    });

    //切换数据库和本地数据源radio
    $("[name='ways']").on("change",function () {
        if(this.value =="database"){
            document.getElementById("sjk").style.display="block";//隐藏“选择表资源标签”
            document.getElementById("bdsc").style.display="none";//隐藏“选择表资源标签”
        }else {
            document.getElementById("sjk").style.display="none";//隐藏“选择表资源标签”
            document.getElementById("bdsc").style.display="block";//隐藏“选择表资源标签”
        }
    });

    //加载本地数据源列表
    function loadBdDataDir() {
        $.ajax({
            type:"POST",
            url:"/searchBdDirList.do",
            data:{},
            success:function (dataSession) {
                var dataSessiobArray=dataSession.substr(1,dataSession.length-4).split(',');
                $("#selectBdDirID").append("<option style='width: 300px;display: none;'>请选择...</option>");
                for(var i=0;i<dataSessiobArray.length;i++){
                    var title=dataSessiobArray[i].substr(0, dataSessiobArray[i].indexOf('?*'));
                    var uri=dataSessiobArray[i].substr(dataSessiobArray[i].indexOf('?*')+2,dataSessiobArray[i].length);
                    $("#selectBdDirID").append("<option style=width:300px; value='"+uri+"'>"+title+"</option>");
                }
            },
            error:function () {
                console.log("请求失败")
            }
        })
    };

    //查询本地数据源文件列表
    $("#selectBdDirID").on("change",function () {
        var dirListData = $("#selectBdDirID option:selected")[0].value;//获取数据库参数
        $.ajax({
            type:"POST",
            url:"/searchBdDirListPath.do",
            cache: false,
            data:{
                dirListData:dirListData
            },
            success:function (data) {
                $('#bdDirDiv').empty();//清空div
                document.getElementById("bdTableLabel").style.display="block";//显示“选择表资源标签”
                document.getElementById("bdSubmitButton").style.display="block";//显示“选择表资源标签”
                var json = data.substr(1,data.length-4);
                var dataArray=json.split(",");
                debugger
                for(var i=0;i<dataArray.length;i++){
                    //var path=dataArray[i].substr(dataArray[i].indexOf('?')+2,dataArray[i].length);
                    $("#bdDirDiv").append("<div style='width: 300px;float:left;'><input type='checkbox' value='"+dataArray[i]+"'>"+dataArray[i]+"</input></div>");
                }
            },
            error:function () {
                console.log("请求失败")
            }
        })
    });

    //预览按钮
    function showPreviewModal(){
        var connData = $("#selectId option:selected")[0].value;//获取数据库参数
        if(connData=="请选择..." || connData==null){
            alert("请选择数据库！");
            return;
        }
        var sql=document.getElementById("sqlInputBox").value;//获取输入框中的sql语句
        if(sql=="" || sql==null){
            alert("请输入sql！");
            return;
        }
        $("#connData").val(connData);
        $("#sql").val(sql);
        openModal();
    }

    //打开模态框
    function openModal(){
        var fatherBody = $(window.top.document.body);
        var id = 'pages';
        var dialog = $('#' + id);
        if (dialog.length == 0) {
            dialog = $('<div class="modal fade" role="dialog" id="' + id + '"/>');
          //  dialog.empty()
            dialog.appendTo(fatherBody);
        }
        dialog.load("/console/datasync/settingtask/sqlPopPreviewWin.html", function() {
            dialog.modal({
                backdrop: false
            });

        });
            fatherBody.append("<div id='backdropId' class='modal-backdrop fade in'></div>");
    };

    // 修改弹出框的title, 显示弹框
    function submitSqlData(title){
        $("#createFileTitle").text(title);
        var checked= checkTaskData();
        if(checked==true){
           $('#createFileMModal').modal('show');
        }else
            return;
    }
    // 关闭弹框， 获取输入值，然后执行逻辑
    $("#createFileSureBut").click(function (){
        var connDataName = $("#selectId option:selected")[0].text;//获取数据源
        var connDataValue = $("#selectId option:selected")[0].value;//获取数据源value
        var taskName = $("#fileName").val();//获取弹出框内任务名称
        var sql=$('#sqlInputBox').val();//获取sql语句
        var createNewTableName=$('#createNewTableName').val();//获取新建表名
        var checkedValue=getChecedValue();
        // var dataSourceType=$("[name='ways']")
        if(taskName==null || taskName==""){
           $('#checkedFileName').html("请输入任务名称！");
            return;
        }
        $("#createFileMModal").modal("hide");//隐藏弹出框
        $.ajax({
            type:"POST",
            url:"/submitSqlData.do",
            data:{
                connDataName:connDataName,
                taskName:taskName,
                sql:sql,
                createNewTableName:createNewTableName,
                checkedValue:checkedValue,
                connDataValue:connDataValue
            },
            success:function (dataSession) {
                parent.goToPage("datatask/dataTask.jsp");
            },
            error:function () {
                console.log("请求失败")
            }
        })
    });

    //检测任务名称
    $("#fileName").bind("input propertychange",function(){
        //你要触发的函数内容
        if($("#fileName").val()!="" && $("#fileName").val()!=null){
            $('#checkedFileName').empty();
        }else{
            $('#checkedFileName').html("请输入任务名称！");
        }
    })

    //获取界面中所有被选中的radio
    function getChecedValue() {
        var i = 0;
        var values = '';
        var checked = $("input:checked"); //获取所有被选中的标签元素
        for (i = 0; i < checked.length; i++) { //将所有被选中的标签元素的值保存成一个字符串，以逗号隔开
            if (i < checked.length - 1 && $("input:checked")[i].type=='checkbox')
                values += checked[i].value + ',';
            else if($("input:checked")[i].type=='checkbox'){
                values += checked[i].value;
            }
        }
        return values;
    }

    //数据源（数据库）任务新建检测
    function checkTaskData() {
        var checked=true;
        var taskName = $("#fileName").val();//获取弹出框内任务名称
        var sql=$('#sqlInputBox').val();//获取sql语句
        var createNewTableName=$('#createNewTableName').val();//获取新建表名
        var checkedValue=getChecedValue();
        if(createNewTableName==null || createNewTableName==""){
            checked=false;
            alert("请输入新建表名！");
            return;
        }
        if((sql==null || sql=="")&&(checkedValue==null || checkedValue=="")){
            checked=false;
            alert("请至少输入sql语句或选择表资源！");
            return;
        }

        return checked;
    }

    //本地文件任务提交
    function submitLocalFileData(){
        $('#createLocalFileModal').modal('show');
        return;
    }

    // 关闭弹框， 获取输入值，然后执行逻辑--本地
    $("#createLocalFileSureBut").click(function (){
        var connDataName = $("#selectId option:selected")[0].text;//获取数据源
        var connDataValue = $("#selectId option:selected")[0].value;//获取数据源value
        $.ajax({
            type:"POST",
            url:"/submitFileData.do",
            data:{

            },
            success:function (dataSession) {

            },
            error:function () {
                console.log("请求失败")
            }
        })
    });


</script>

</body>
</html>
