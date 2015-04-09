Ext.application({
    name   : 'MyApp',

    launch : function() {

        //"haveConsulted": false,
        //    "birthday": "1989-03-13",
        //    "sex": "Male",
        //    "treatmented": false,
        //    "doctorInfoPhone": "",
        //    "profession": "无业游民",
        //    "married": false,
        //    "sendUser": false,
        //    "haveChildren": false,
        //    "sendDoctor": false,
        //    "doctorStatus": "",
        //    "outDated": "",
        //    "sendEmail": false

        Ext.define('Booking', {
            extend: 'Ext.data.Model',
            fields : [ 'id', 'userId','name','phone', 'helpReason','consultType', 'money','quick','quickMoney','city' ,'tradeNo','createDate','lastModifiedDate',
                'time','doctorId','doctorName','doctorPhone','status','refusalReason','payTime','otherProblem','lastBookingTime','resolveType',
                'chargeId','userStatus','doctorStatus','hospitalId','hospitalName','hospitalPhone']
        });



        var store = Ext.create('Ext.data.Store', {
            // destroy the store if the grid is destroyed
            autoDestroy: true,
            model: 'Booking',
            proxy: {
                type: 'memory'
            },
            data: null,
            sorters: [{
                property: 'start',
                direction: 'DESC'
            }]
        });


        var rowEditing = Ext.create('Ext.grid.plugin.RowEditing', {
            clicksToMoveEditor: 1,
            autoCancel: false
        });
        var win,button ;

        var grid =Ext.create('Ext.grid.Panel', {
            id:"myPanel",
            renderTo : "container",
            xtype    : 'grid',
            title    : '订单',
            width    : 1200,
            height   : 600,
            region:'center',
            store    : store,
            tbar: [{
                id:'show-btn',
                text: '处理',
                //iconCls: 'employee-add',
                handler : function() {
                    var sm = grid.getSelectionModel();
                    rowEditing.cancelEdit();
                    if(sm.getSelection().length<1){
                        Ext.Msg.alert('错误', '请选择一项');
                        return;
                    }
                    var id=sm.getSelection()[0].get("id");
                    if (!win) {
                        win = Ext.create('widget.window', {
                            id:'myWindow',
                            title: '处理结果',
                            header: {
                                titlePosition: 2,
                                titleAlign: 'center'
                            },
                            closable: true,
                            closeAction: 'hide',
                            maximizable: true,
                            animateTarget: button,
                            width: 600,
                            minWidth: 350,
                            height: 350,
                            tools: [{type: 'pin'}],
                            layout: {
                                type: 'border',
                                padding: 5
                            },
                            items: [{
                                region: 'center',
                                xtype: 'panel',
                                width: 600,
                                minWidth: 350,
                                height: 350,
                                defaultType: 'textfield',
                                bodyPadding: 10,
                                defaults: {
                                    labelWidth: 100
                                },
                                items: [{
                                    id:'txt-bookingId',
                                    fieldLabel: 'id',
                                    name: 'id',
                                    editable:false,
                                    value:id
                                }, {
                                    id:'txt-assistantId',
                                    fieldLabel: '处理人',
                                    name: 'assistantId'
                                }, {
                                    id:'txt-resolved',
                                    fieldLabel: '是否解决',
                                    name: 'resolved',
                                    xtype:'combobox',
                                    editable:false,
                                    store: {
                                        fields: ['value', 'text'],
                                        data : [
                                            {"value":"0", "text":"未解决"},
                                            {"value":"1", "text":"解决"}
                                        ]
                                    },
                                    queryMode: 'local',
                                    valueField: 'value'
                                }, {
                                    id:'txt-remark',
                                    fieldLabel: 'remark',
                                    xtype:'textarea',
                                    name: 'remark'
                                }, {
                                    text:'提交',
                                    xtype:'button',
                                    name: 'btn-confirm',
                                    handler:function(){
                                        var txt1=Ext.getCmp('txt-bookingId').getValue();
                                        var txt2=Ext.getCmp('txt-assistantId').getValue();
                                        var txt3=Ext.getCmp('txt-resolved').getValue();
                                        var txt4=Ext.getCmp('txt-remark').getValue();
                                        var jsonSent={};
                                        jsonSent.bookingId=txt1;
                                        jsonSent.assistantId=txt2;
                                        jsonSent.resolved=txt3;
                                        jsonSent.remark=txt4;
                                        if(!txt2 || !txt3 || !txt4){
                                            Ext.Msg.alert('错误', '表单不能有空');
                                            return;
                                        }
                                        resolveBooking(jsonSent);

                                    }
                                }]
                            }]
                        });
                    }else{
                        Ext.getCmp("txt-bookingId").setValue(id);
                        Ext.getCmp('txt-assistantId').setValue("");
                        Ext.getCmp('txt-resolved').setValue("");
                        Ext.getCmp('txt-remark').setValue("");
                    }
                    button.dom.disabled = true;
                    if (win.isVisible()) {
                        win.hide(this, function() {
                            button.dom.disabled = false;
                        });
                    } else {
                        win.show(this, function() {
                            button.dom.disabled = false;
                        });
                    }
                }
            }, {
                id:'txt-resolveType',
                xtype:'combobox',
                store: {
                    fields: ['value', 'text'],
                    data : [
                        {"value":"1", "text":"咨询师未响应"},
                        {"value":"2", "text":"咨询师拒绝"},
                        {"value":"3", "text":"用户未响应 "},
                        {"value":"4", "text":"加急确认"},
                        {"value":"5", "text":"订单改约"},
                        {"value":"6", "text":"订单回访"},
                        {"value":"0", "text":"无需操作"}
                    ]
                },
                fieldLabel: '类型过滤',
                queryMode: 'local',
                valueField: 'value',
                editable:false,
                listeners: {
                    afterRender: function(combo) {
                            combo.setValue("1");
                    },
                    change:function(){
                        //change( this, newValue, oldValue, eOpts )
                        refreshList(arguments[1]);
                    }
                }
            }
            ],
            columns: {
                items: [
                    { text: 'id', dataIndex: 'id'},
                    { text: 'userId', dataIndex: 'userId' },
                    { text: 'name', dataIndex: 'name' },
                    { text: 'phone', dataIndex: 'phone' },
                    { text: 'helpReason', dataIndex: 'helpReason' },
                    { text: 'consultType', dataIndex: 'consultType' },
                    { text: 'money', dataIndex: 'money' },
                    { text: 'quick', dataIndex: 'quick' },
                    { text: 'quickMoney', dataIndex: 'quickMoney' },
                    { text: 'city', dataIndex: 'city' },
                    { text: 'tradeNo', dataIndex: 'tradeNo' },
                    { text: 'createDate', dataIndex: 'createDate' },
                    { text: 'lastModifiedDate', dataIndex: 'lastModifiedDate' },
                    { text: 'doctorId', dataIndex: 'doctorId' },
                    { text: 'doctorName', dataIndex: 'doctorName' },
                    { text: 'doctorPhone', dataIndex: 'doctorPhone' },
                    { text: 'status', dataIndex: 'status' },
                    { text: 'refusalReason', dataIndex: 'refusalReason' },
                    { text: 'payTime', dataIndex: 'payTime' },
                    { text: 'otherProblem', dataIndex: 'otherProblem' },
                    { text: 'lastBookingTime', dataIndex: 'lastBookingTime' },
                    { text: 'resolveType', dataIndex: 'resolveType' },
                    { text: 'chargeId', dataIndex: 'chargeId' },
                    { text: 'userStatus', dataIndex: 'userStatus' },
                    { text: 'doctorStatus', dataIndex: 'doctorStatus' },
                    { text: 'hospitalId', dataIndex: 'hospitalId' },
                    { text: 'hospitalName', dataIndex: 'hospitalName' },
                    { text: 'hospitalPhone', dataIndex: 'hospitalPhone' }
                ]
            }
        })


        button = Ext.get('show-btn')
    }
});

//根据resolveType刷新列表
function refreshList(type){
    //加载框
    var myPanel=Ext.getCmp('myPanel');
    myPanel.el.mask('Loading', 'x-mask-loading');
    Ext.Ajax.request( {
        url : 'http://localhost:8080/booking/retrieveBookings?type='+type+'&size=10&page=',
        method : 'get',
        success : function(response, options) {
            myPanel.el.unmask();
            var o = Ext.util.JSON.decode(response.responseText);
            Ext.getCmp("myPanel").getStore().setData(o.list);
            console.log(o);
            Ext.override(Ext.window.Toast, {
                slideInDuration: 500
            });
            Ext.toast({
                html: '列表刷新成功',
                width: 200,
                align: 'tl'
            });
        },
        failure : function(data) {
            myPanel.el.unmask();
        }
    });
}


//处理订单
function resolveBooking(json){

    //加载框
    var myWindow=Ext.getCmp('myWindow');
    myWindow.el.mask('Loading', 'x-mask-loading');
    Ext.Ajax.request( {
        url : prevUrl+'/booking/resolveBooking',
        method : 'post',
        params:json,
        success : function(response, options) {
            myWindow.el.unmask();
            var o = Ext.util.JSON.decode(response.responseText);
            console.log(o);
            myWindow.hide();
            refreshList(Ext.getCmp("txt-resolveType").getValue());
        },
        failure : function(data) {
            myWindow.el.unmask();
        }
    });
}

//Ext.Ajax.request( {
//    url : 'http://localhost:8080/booking/retrieveBookings?type=0&size=10&page=',
//    method : 'get',
//    success : function(response, options) {
//        var o = Ext.util.JSON.decode(response.responseText);
//
//
//        Ext.getCmp("myPanel").getStore().setData(o.list);
//        //alert(o.msg);
//        console.log(o);
//    },
//    failure : function(data) {
//    }
//});