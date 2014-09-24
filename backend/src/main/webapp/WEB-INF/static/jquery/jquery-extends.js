$.extend({
			alert : function(message, title) {
				$("<div></div>").dialog({
							buttons : {
								"确定" : function() {
									$(this).dialog("close");
								}
							},
							close : function(event, ui) {
								$(this).remove();
							},
							height : 300,
							resizable : false,
							title : title,
							modal : true
						}).text(message);
			},
			confirm : function(message, title, onConfirm) {
				$("<div></div>").dialog({
							buttons : {
								"确定" : function() {
									$(this).dialog("close");
									onConfirm();
								},
								"取消" : function() {
									$(this).dialog("close");
								}
							},
							close : function(event, ui) {
								$(this).remove();
							},
							height : 300,
							resizable : false,
							title : title,
							modal : true
						}).text(message);
			}
		});

function validate_date(date) {
	var pattern = new RegExp(/\b\d{4}[\/-]\d{2}[\/-]\d{2}\b/);
	return pattern.test(date);
}