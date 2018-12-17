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
    <script src="/console/datasync/js/bootbox.min.js"></script>
    <script src="/console/datasync/js/layer/layer.js"></script>
    <script src="/console/shared/bootstrap-toastr/toastr.js"></script>
    <link rel="stylesheet" type="text/css" href="/console/shared/bootstrap-3.3.7/css/bootstrap.css">
    <link rel="stylesheet" type="text/css" href="/console/datasync/js/layer/layer.css" />
    <link rel="stylesheet" type="text/css" href="/console/shared/bootstrap-3.3.7/css/bootstrap-table.min.css">
    <link rel="stylesheet" type="text/css" href="/console/datasync/css/createTask.css" />
    <link rel="stylesheet" type="text/css" href="/console/datasync/css/style.min.css" />
    <link rel="stylesheet" type="text/css" href="/console/shared/bootstrap-toastr/toastr.css" />
    <style>
        .alert-info {
            background-color: #f0f6fe;
            border-color: #f0f6fe;
        }
        .table_div{
            /*height: 326px;*/
            margin-left: -16px;
            margin-right: -16px;
        }
        .table th, .table td {
            text-align: center;
            vertical-align: middle!important;
        }
        .table {
            font-size:12px;
        }
        .progress {
            height: 18px !important;
            margin-bottom: 0px !important;
        }

        /*.fixed-table-container thead th {*/
             /*line-height: 0px !important;*/
        /*}*/
        .fixed-table-container{
            /*height: 435px !important;*/
        }
        .sr-only {
            position: relative !important;
        }

    </style>
</head>
<body style="overflow: auto;">
<div>

    <div class="alert alert-info" role="alert" style="margin:0  3px;height: 66px;">
        <!--查询条件 -->
        <div class="row">
            <form class="form-inline">
                <div class="form-group" style="margin-left: 10px;">
                    <label >任务标识</label>
                    <input type="text" id="SearchDataTaskName" class="form-control sqlStatements" style="width: 160px;"/>
                </div>
                <div class="form-group" style="margin-left: 10px;">
                    <label >数据类型</label>
                    <select  id="dataSourceList" class="form-control" style="width: 150px">
                        <option value="">请选择...</option>
                        <option value="db">关系数据库</option>
                        <option value="file">文件数据库</option>
                    </select>
                </div>
                <div class="form-group" style="margin-left: 10px;">
                    <label>任务状态</label>
                    <select  id="dataStatusList" class="form-control" style="width: 150px">
                        <option value="">请选择...</option>
                        <option value="1">上传完成</option>
                        <option value="0">未上传</option>
                    </select>
                </div>
                <button type="button" class="btn blue" style="margin-left: 20px" onclick="searchDataBySql()">查询</button>
                <button type="button" class="btn green" style="margin-left: 10px" onclick="relCreateTask('settingtask/settingTask.jsp')">新建任务</button>
            </form>
        </div>
        <div class="table_div">
            <table id="dataTaskTableID" class="table table-bordered" style="text-align: center;data-striped:true;">

            </table>
        </div>
    </div>
</div>
<input type="hidden" id="taskIdHidden"/>

<script type="text/javascript" src="/console/shared/bootstrap-3.3.7/js/bootstrap-table.js"></script>
<script>
    searchDataBySql();
    //查询任务列表
    function searchDataBySql(){
        var SearchDataTaskName=$("#SearchDataTaskName").val();//任务标识
        var dataSourceList=$("#dataSourceList  option:selected")[0].value;//数据类型
        var dataStatusList=$("#dataStatusList").val();//任务状态

        $.ajax({
            type:"POST",
            url:"/searchDataTaskList.do",
            cache: false,
            data:{
                SearchDataTaskName:SearchDataTaskName,
                dataSourceList:dataSourceList,
                dataStatusList:dataStatusList
            },
            success:function (data) {
                var DataList = JSON.parse(data);
                loadDataTaskList(DataList.dataTasks);
            },
            error:function () {
                console.log("请求失败")
            }
        })
    };

    //装载bootstracptable
    function loadDataTaskList(dataList){//加载任务列表
        $('#dataTaskTableID').bootstrapTable('destroy');
        $('#dataTaskTableID').bootstrapTable({
            striped: true,
            pagination: true,
            sortable: true,
            sortOrder: "asc",
            pageNumber: 1,
            pageSize: 10,                       //每页的记录行数（*）
           // pageList: [ 25, 50, 100],
            minimumCountColumns: 5,
            search:false,
            showRefresh:false,
            uniqueId: "no",
            locale: "zh-CN",
            searchOnEnterKey:true,
            detailView: false,
            height:$(window).height() - 60,
            columns:[ {
                field: 'dataTaskId',
                title: 'id'
            }, {
                field: 'dataSo',
                title: '任务编号'
            }, {
                field: 'dataTaskName',
                title: '任务标识'
            }, {
                field: 'dataTaskType',
                title: '数据来源类型'
            }, {
                field: 'dataSrc.dataSourceName',
                title: '数据源'
            }, {
                field: 'createTime',
                title: '创建时间',
                formatter: function (value, row, index) {
                //——修改——获取日期列的值进行转换
                    return new Date(value).toLocaleDateString().replace(/\//g, "-")+"   "+new Date(value).toLocaleTimeString().replace(/\//g, "-");
                }
            }, {
                field: 'status',
                title: '上传进度',
                formatter: processFormatter
            }, {
                field: 'status',
                title: '状态',
                formatter: function (value, row, index) {
                   if(value==0){
                    return "未上传";
                   }else{
                       return "上传完成";
                   }
                }
            }, {
                field: 'Desc',
                title: '操作',
                width:'29%',
                formatter: operateFormatter //自定义方法，添加操作按钮
            } ]
        });
        $('#dataTaskTableID').bootstrapTable('load', dataList);
        $('#dataTaskTableID').bootstrapTable('hideColumn', 'dataTaskId');
        $('#dataTaskTableID').bootstrapTable('hideColumn', 'dataSo');
        $('#dataTaskTableID').bootstrapTable('resetView');

        // $("#dataTaskTableID").bootstrapTable('resetView');
    };


    //为操作添加按钮
    function operateFormatter(value, row, index) {//赋予的参数
        if(row.status==0){//未上传
            return [
                '<button class="btn btn-default details btn-xs" value="'+row.dataTaskId+','+row.status+'" onclick="ftpUpload(value)"><a>上传</a></button>&nbsp;',
                '<button class="btn btn-default details btn-xs" value="'+row.dataTaskId+'" onclick="viewDtails(value)"><a>查看</a></button>&nbsp;',
                '<button class="btn btn-default delete btn-xs" onclick="deleteThis(this)" data-id="'+row.dataTaskId+'"><a>删除</a></button>&nbsp;',
                '<button class="btn btn-default details btn-xs" value="'+row.dataTaskId+'" onclick="editTaskDtails(value)"><a>编辑</a></button>&nbsp;',
                // '<button class="btn btn-default delete btn-xs" onclick="" data-id="'+row.dataTaskId+'">'+
                //  '<a href="/console/datasync/logFile/'+row.dataTaskId+'log.txt" download="'+row.dataTaskName+'Log.txt">日志</a>'+
                // '</button>'
            ].join('');
        }else{
            return [
                '<button class="btn btn-default details btn-xs" value="'+row.dataTaskId+','+row.status+'" onclick="ftpUpload(value)"><a>上传</a></button>&nbsp;',
                '<button class="btn btn-default details btn-xs" value="'+row.dataTaskId+'" onclick="viewDtails(value)"><a>查看</a></button>&nbsp;',
                '<button class="btn btn-default delete btn-xs" onclick="deleteThis(this)" data-id="'+row.dataTaskId+'"><a>删除</a></button>&nbsp;',
                '<button class="btn btn-default delete btn-xs" onclick="" data-id="'+row.dataTaskId+'">'+
                  '<a href="/console/datasync/logFile/'+row.dataTaskId+'log.txt" download="'+row.dataTaskName+'Log.txt">日志</a>'+
                '</button>'
            ].join('');
        }
    }

    //创建任务按钮-调用父类方法
    function relCreateTask(params){
        parent.goToPage(params);

    }

    //导出-上传按钮
    function ftpUpload(value){
        var array=value.split(',');
        var taskId;//taskId
        var status;//status--状态
        if(array.length>=2){
            taskId=array[0];//taskId
            status=array[1];//status--状态
        }
        if(status==1){//已经上传的文件
            bootbox.confirm("<span style='font-size: 16px'>确认需要重新上传吗?</span>",function (r) {
                if (r) {
                    startFtpUpload(taskId);
                } else {
                    return;
                }
            })
        }else{
            startFtpUpload(taskId);
        }
    };

    function startFtpUpload(taskId) {//执行上传ftp
        $("#"+taskId+"")[0].style.width="0%";
        $("#"+taskId+"Text")[0].textContent="0%";
        var souceID = taskId;
        var keyID = souceID + new Date().getTime();
        $.ajax({
            type:"POST",
            url:"/ftpLocalUpload.do",
            data:{taskId:taskId},
            success:function(data){
                if(data=="" || data==1){
                    searchDataBySql();
                }else {
                    alert(data);
                }
            },
            error:function () {
                console.log("请求失败")
            }
        })
        getProcess(keyID,souceID);//获取上传进度
    }

    //查看详情按钮
    function viewDtails(value){
        $("#taskIdHidden").val(value);
        var fatherBody = $(window.top.document.body);
        var id = 'pages';
        var dialog = $('#' + id);
        if (dialog.length == 0) {
            dialog = $('<div class="modal fade" role="dialog" id="' + id + '"/>');
            //  dialog.empty()
            dialog.appendTo(fatherBody);
        }
        dialog.load("/console/datasync/datatask/viewTaskDetailsWin.html", function() {
            dialog.modal({
                backdrop: false
            });

        });
        fatherBody.append("<div id='backdropId' class='modal-backdrop fade in'></div>");
    }

    //删除
    function deleteThis(el) {
        bootbox.confirm("<span style='font-size: 16px'>确认要删除此条记录吗?</span>",function (r) {
            if (r) {
                el.parentNode.parentNode.remove(true);
                var id = $(el).attr("data-id");//任务id
                $.ajax({
                    type: "POST",
                    url: "/deleteTaskById.do",
                    data: {
                        taskId: id
                    },
                    async: "false",
                    success: function (data) {
                        toastr["success"]("删除成功");
                        searchDataBySql();

                    },
                    error: function () {
                        console.log("请求失败")
                    }
                })
            } else {

            }
        })
    };

    //进度条
    var processFormatter = function (value, row, index) {
        if(value==0){
            var process = "<div class=\"progress progress-striped active\" >\n" +
                "\t<div id=\""+row.dataTaskId+"\" class=\"progress-bar progress-bar-success\" role=\"progressbar\"\n" +
                "\t\t aria-valuenow=\"60\" aria-valuemin=\"0\" aria-valuemax=\"100\"\n" +
                "\t\t style=\"width: 0%;\">\n" +
                 "\t\t<span class=\"sr-only\" id='"+row.dataTaskId+"Text'></span>\n" +
                "\t</div>\n" +
                "</div>";
            return process;
        }else{
            var process = "<div class=\"progress progress-striped active\" >\n" +
                "\t<div id=\""+row.dataTaskId+"\" class=\"progress-bar progress-bar-success\" role=\"progressbar\"\n" +
                "\t\t aria-valuenow=\"60\" aria-valuemin=\"0\" aria-valuemax=\"100\"\n" +
                "\t\t style=\"width: 100%;\">\n" +
                "\t\t<span class=\"sr-only\" id='"+row.dataTaskId+"Text'>100%</span>\n" +
                "\t</div>\n" +
                "</div>";
            return process;
        }
    };

    //获取上传进度
    function getProcess(keyID,souceID) {
        var setout= setInterval(function () {
            $.ajax({
                url:"/ftpUploadProcess.do",
                type:"POST",
                async:true,
                data:{
                    processId:souceID
                },
                success:function (dataReult) {
                    var data=dataReult.replace(/[\r\n]/g,"");
                    if(data >= 100){
                        clearInterval(setout);
                        return;
                    }

                     $("#"+souceID+"")[0].style.width=data+"%";
                     $("#"+souceID+"Text")[0].textContent=data+"%";
                }
            })
        },1000)

    }


    //编辑任务
    function editTaskDtails(taskId){
            $("#taskIdHidden").val(taskId);
            var fatherBody = $(window.top.document.body);
            var id = 'pages';
            var dialog = $('#' + id);
            if (dialog.length == 0) {
                dialog = $('<div class="modal fade" role="dialog" id="' + id + '"/>');
                //  dialog.empty()
                dialog.appendTo(fatherBody);
            }
            dialog.load("/console/datasync/datatask/editorDataTask.html", function() {
                dialog.modal({
                    backdrop: false
                });

            });
            fatherBody.append("<div id='backdropId' class='modal-backdrop fade in'></div>");
    }

</script>

</body>
</html>
