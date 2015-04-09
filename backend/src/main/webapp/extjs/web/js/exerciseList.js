Ext.application({
    name   : 'MyApp',

    launch : function() {

        Ext.define('Exercise', {
            extend: 'Ext.data.Model',
            fields : [ 'id', 'name', 'remark', 'url' ]
        });


        var store = Ext.create('Ext.data.Store', {
            // destroy the store if the grid is destroyed
            autoDestroy: true,
            model: 'Exercise',
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
            title    : '练习大套',
            width    : 1200,
            height   : 300,
            plugins  : [{
                ptype: 'cellediting',
                clicksToEdit: 0
            }],
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
                                    fieldLabel: 'remark',
                                    name: 'remark'
                                }, {
                                    fieldLabel: 'url',
                                    name: 'url',
                                    xtype:'fileuploadfield'
                                }, {
                                    text:'提交',
                                    xtype:'button',
                                    name: 'btn-confirm',
                                    handler:function(){

                                    }
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
            },{
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
                disabled: true
            }],
            columns: {
                defaults: {
                    editor: 'textfield',
                    width  : 120
                },
                items: [
                    { text: 'id', dataIndex: 'id'},
                    { text: 'name', dataIndex: 'name' },
                    { text: 'remark', dataIndex: 'remark' },
                    { text: 'url', dataIndex: 'url' }
                ]
            }
        })


        button = Ext.get('show-btn')
    }
});

Ext.Ajax.request( {
    url : prevUrl+'/exercise/retrieveExercise?size=10&page=',
    method : 'get',
    success : function(response, options) {
        var o = Ext.util.JSON.decode(response.responseText);


        Ext.getCmp("myPanel").getStore().setData(o.list);
        //alert(o.msg);
        console.log(o);
    },
    failure : function(data) {
    }
});

function addNewItem(){
    Ext.Ajax.request({
        url : prevUrl+'/exercise/generateExercise',
        method : 'post',
        success: function(response){
            var text = response.responseText;
            // process server response here
        }
    });
}