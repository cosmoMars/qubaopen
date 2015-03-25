Ext.application({
    name   : 'MyApp',

    launch : function() {

        Ext.widget({
            id:"asd",
            renderTo : "myPanel",
            xtype    : 'grid',
            title    : '标题',
            width    : 650,
            height   : 300,
            plugins  : 'rowediting',
            store    : {
                fields : [ 'name', 'age', 'votes', 'credits' ],
                data   : [
                    [ 'Bill', 35, 10, 427 ],
                    [ 'Fred', 22, 4, 42 ]
                ]
            },
            columns: {
                defaults: {
                    editor : 'numberfield',
                    width  : 120
                },
                items: [
                    { text: '姓名', dataIndex: 'name', flex: 1, editor: 'textfield' },
                    { text: '年龄', dataIndex: 'age' },
                    { text: '票数', dataIndex: 'votes' },
                    { text: '信誉', dataIndex: 'credits' }
                ]
            }
        })
    }
});

Ext.Ajax.request( {
    url : 'http://localhost:8080/dailyDiscovery/retrieveTopic?size=10&page=&join=false',
    method : 'get',
    success : function(response, options) {
        var o = Ext.util.JSON.decode(response.responseText);
        alert(o.msg);
    },
    failure : function(data) {
    }
});