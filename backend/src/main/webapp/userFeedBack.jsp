<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>用户使用情况</title>
<style>
table {
	background: black;
}

table tr:nth-child(even) {
	background: white;
}

table tr:nth-child(odd) {
	background: #dddddd;
}

table tr:nth-child(1) {
	background: gray;
}
</style>

<script type="text/javascript" src="static/jquery/jquery-1.8.2.min.js"></script>
<script>
	function getflag() {
		var SearchString = window.location.search.substring(1);
		var VariableArray = SearchString.split('&');
		for ( var i = 0; i < VariableArray.length; i++) {
			var KeyValuePair = VariableArray[i].split('=');
			if (KeyValuePair[0] == "flag") {
				return KeyValuePair[1];
			}
		}
	}

	$(document)
			.ready(
					function() {

						var jsonSent = '{"flag":"' + getflag() + '"}';
						console.log(getflag());
						$
								.ajax({
									url : "userFeedBack/checkData.htm",
									type : "POST",
									data : {
										"json" : jsonSent
									},
									dataType : "json",
									success : function(data, textStatus, jqXHR) {

										var result = data.success;
										if (result == 1) {
											var qTitle = data.title;
											var qRow = data.row;
											var tableStr = '<tr> <th colspan='+qTitle.length+'>'
													+ data.tabletitle
													+ '</th></tr>';
											tableStr += '<tr>';

											for ( var i = 0; i < qTitle.length; i++) {
												tableStr += '<td style="text-align: center" width="200">'
														+ qTitle[i] + '</td>';
											}
											tableStr = tableStr + '</tr>';

											for ( var i = 0; i < qRow.length; i++) {
												tableStr = tableStr + '<tr>';
												for ( var j = 0; j < qRow[i].shuju.length; j++) {
													if (qRow[i].shuju[j]
															.match("data:image/jpeg;base64,")) {
														tableStr += '<td style="text-align: center"> <img width="96" height="118" src="'+qRow[i].shuju[j]+'"/></td>';
													} else {
														tableStr += '<td style="text-align: center">'
																+ qRow[i].shuju[j]
																+ '</td>';
													}
												}
												tableStr = tableStr + '</tr>';
											}

											$("#checkData").append(tableStr);

										}

										if (result == 0) {
											var msg = data.message;
											alert(msg);
										}

									}
								});

					});
</script>
</head>
<body>
	<table id='checkData'></table>
</body>
</html>