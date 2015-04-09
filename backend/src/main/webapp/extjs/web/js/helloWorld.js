Ext.application({
    name   : 'MyApp',

    launch : function() {

        Ext.define('Employee', {
            extend: 'Ext.data.Model',
            fields : [ 'id', 'name', 'content', 'dailyTime','createDate'  ]
        });


        var   tableJson=[
            {name:"威廉",age:"15",votes:"55",credits:"10"},
            {name:"玛丽",age:"25",votes:"33",credits:"22"},
            {name:"乔治",age:"25",votes:"22",credits:"33"},
            {name:"彼得",age:"35",votes:"15",credits:"15"}
        ];

        var store = Ext.create('Ext.data.Store', {
            // destroy the store if the grid is destroyed
            autoDestroy: true,
            model: 'Employee',
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
            title    : '标题',
            width    : 650,
            height   : 300,
            plugins  : 'rowediting',
            store    : store,
            tbar: [{
                id:'show-btn',
                text: '新增框',
                //iconCls: 'employee-add',
                handler : function() {

                    if (!win) {
                        win = Ext.create('widget.window', {
                            title: '编辑',
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
                                    fieldLabel: 'id',
                                    name: 'id'
                                }, {
                                    fieldLabel: 'name',
                                    name: 'name'
                                }, {
                                    fieldLabel: 'content',
                                    name: 'content'
                                }, {
                                    fieldLabel: 'dailyTime',
                                    name: 'dailyTime'
                                }, {
                                    fieldLabel: 'createdDate',
                                    name: 'createdDate'
                                }, {
                                    text:'提交',
                                    xtype:'button',
                                    name: 'btn-confirm'
                                }]
                            }]
                        });
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
                text: '新增',
                //iconCls: 'employee-add',
                handler : function() {
                    rowEditing.cancelEdit();

                    // Create a model instance
                    var r = Ext.create('Employee', {
                        id: '123',
                        name: '',
                        content: '',
                        dailyTime: '',
                        createDate:''
                    });

                    store.insert(0, r);
                    rowEditing.startEdit(0, 0);
                }
            }, {
                text: '删除',
                //iconCls: 'employee-remove',
                handler: function() {
                    var sm = grid.getSelectionModel();
                    rowEditing.cancelEdit();
                    store.remove(sm.getSelection());
                    if (store.getCount() > 0) {
                        sm.select(0);
                    }
                },
                disabled: false
            }],
            columns: {
                defaults: {
                    editor : 'numberfield',
                    width  : 120
                },
                items: [
                    { text: 'id', dataIndex: 'id', flex: 1, editor: 'textfield' },
                    { text: 'name', dataIndex: 'name' },
                    { text: 'content', dataIndex: 'content' },
                    { text: 'dailyTime', dataIndex: 'dailyTime' },
                    { text: 'createdDate', dataIndex: 'createdDate' }
                ]
            }
        })


        button = Ext.get('show-btn')
    }
});

Ext.Ajax.request( {
    url : 'http://localhost:8080/dailyDiscovery/retrieveTopic?size=10&page=&join=false',
    method : 'get',
    success : function(response, options) {
        var o = Ext.util.JSON.decode(response.responseText);

        var   tableJson=[
            {name:"威廉",age:"15",votes:"55",credits:"10"},
            {name:"玛丽",age:"25",votes:"33",credits:"22"},
            {name:"乔治",age:"25",votes:"22",credits:"33"},
            {name:"彼得",age:"35",votes:"15",credits:"15"}
        ];

        Ext.getCmp("myPanel").getStore().setData(o.list);
        //alert(o.msg);
        console.log(o);
    },
    failure : function(data) {
    }
});