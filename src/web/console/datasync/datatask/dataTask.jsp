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
        .alert-info {
            background-color: #f0f6fe;
            border-color: #f0f6fe;
        }
        .table_div{
            /*height: 326px;*/
            margin-left: -16px;
            margin-right: -12px;
        }
        .table th, .table td {
            text-align: center;
            vertical-align: middle!important;
        }
        .table {
            font-size:12px;
        }

        /*.fixed-table-container thead th {*/
             /*line-height: 0px !important;*/
        /*}*/
        .fixed-table-container{
            height: 339px !important;
        }

    </style>
</head>
<body style="overflow: hidden;">

<div>


    <div class="alert alert-info" role="alert" style="margin:0  3px;height: 66px;">
        <!--查询条件 -->
        <div class="row">
            <form class="form-inline">
                <div class="form-group" >
                    <label >数据类型</label>
                    <select  id="dataSourceList" class="form-control" style="width: 150px">
                        <option value="">全部</option>
                        <option value="db">关系数据库</option>
                        <option value="file">文件数据库</option>
                    </select>
                </div>
                <div class="form-group">
                    <label  >状态</label>
                    <select  id="dataStatusList" class="form-control" style="width: 150px">
                        <option value="">全部</option>
                        <option value="1">上传完成</option>
                        <option value="0">未上传</option>
                    </select>
                </div>
                <button type="button" class="btn blue" style="margin-left: 49px" id="seachTaskSource">查询</button>
                <button type="button" class="btn green" style="margin-left: 49px" onclick="relCreateTask('settingtask/settingTask.jsp')">新建任务</button>
            </form>
        </div>
        <div class="table_div">
            <table id="dataTaskTableID" class="table table-bordered" style="text-align: center;">

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
        $.ajax({
            type:"POST",
            url:"/searchDataTaskList.do",
            cache: false,
            data:{
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

    function loadDataTaskList(dataList){//加载任务列表
        $('#dataTaskTableID').bootstrapTable('destroy');
        $('#dataTaskTableID').bootstrapTable({
            striped: true,
            pagination: true,
            sortable: true,
            sortOrder: "asc",
            pageNumber: 1,
            pageSize: 10,                       //每页的记录行数（*）
            //pageList: [ 25, 50, 100],
            minimumCountColumns: 5,
            search:false,
            showRefresh:false,
            uniqueId: "no",
            locale: "zh-CN",
            searchOnEnterKey:true,
            detailView: false,
            height:380,
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
                field: 'dataSourceName',
                title: '数据源'
            }, {
                field: 'createTime',
                title: '创建时间'
            }, {
                field: 'status',
                title: '上传进度'
            }, {
                field: 'status',
                title: '状态'
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
    };

    //为操作添加按钮
    function operateFormatter(value, row, index) {//赋予的参数
        return [
            // '<button class="btn btn-default details btn-xs" value="'+row.id+'" onclick="detail(value)">导出</button>&nbsp;',
            '<button class="btn btn-default details btn-xs" value="'+row.dataTaskId+'" onclick="ftpUpload(value)">上传</button>&nbsp;',
            '<button class="btn btn-default details btn-xs" value="'+row.dataTaskId+'" onclick="viewDtails(value)">查看</button>&nbsp;',
            '<button class="btn btn-default delete btn-xs" onclick="deleteThis(this)" data-id="'+row.id+'">删除</button>',
        ].join('');
    }

    //创建任务按钮-调用父类方法
    function relCreateTask(params){
        parent.goToPage(params);

    }

    //导出-上传按钮
    function ftpUpload(value){
        $.ajax({
            type:"POST",
            url:"/ftpLocalUpload.do",
            data:{},
            async:"false",
            success:function(data){

            },
            error:function () {
                console.log("请求失败")
            }

        })
    };

    //查看详情按钮
    function viewDtails(value){
       // $("#fileModal").modal('show');
        openModal(value);
    }

    //打开模态框
    function openModal(value){
        $("#taskIdHidden").val(value);
        debugger
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
    };

</script>

</body>
</html>
