$(document).on('change', '#roleId', function() {
	var role = $("#roleId").val();

	if (role === "REGISTRATOR") {
		$('#myModal').modal('show');
		
		$('#submit').click(function() {
			var json = {
				"login" : $("#login").val(),
				"resource_number" : $("#resource_number").val(),
				"identifier" : $("#identifier").val(),
				"registrator_number" : $("#registrator_number").val()
			};
			$.ajax({
				type : "POST",
				url : "modal-window",
				dataType : "text",
				data : JSON.stringify(json),
				contentType : 'application/json; charset=utf-8',
				
				success: function() {
					$('#myModal').modal('hide');
				},
				error: function(xhr, ajaxOptions, thrownError) {
				    bootbox.alert("Реєстраційний номер реєстратора є унікальним або вже закріплений за іншим реєстратором");
				}
			
			});
		})
		
	}
	event.preventDefault();

});
