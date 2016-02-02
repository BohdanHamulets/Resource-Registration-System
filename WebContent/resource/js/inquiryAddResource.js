
$(document).on("click","#outputInquiry",function(){
	$.ajax({
		url: 	baseUrl.toString() +'/inquiry/add/outputInquiry',
		type: 	'GET',
		success: function(response){
			$('#target').html(response);
			
			$('#registratorLogin').change(function(){
				$('#form').submit();								
			})			
		}
	})	
})

$(document).on("click","#deleteResource",function(){
	bootbox.confirm(jQuery.i18n.prop('msg.confirmDelete'),function(result){
		if (result) {
			window.location = baseUrl.toString() +'/registrator/resource/delete/'+ $('#resourceIdentifierCopy').val();			
		}
	})
})




/*$(document).on("click","#outputInquiry",function(){
	$.ajax({
		url: 	baseUrl.toString() +'/inquiry/add/outputInquiry',
		type: 	'POST',
		success: function(response){
			$('#target').html(response);
			
			$('#registratorLogin').change(function(){
				$.ajax({
					url: 	baseUrl.toString() +'/inquiry/add/addOutputInquiry',
					type: 	'POST',
					data: {
						resourceIdentifier : $('#resourceIdentifier').text(),
						registratorLogin : $('#registratorLogin').val(),
						success: function(){
							window.location.href=baseUrl.toString() +'/inquiry/add/listInqUserOut'
						}
					}					
				})								
			})			
		}
	})	
})
*/