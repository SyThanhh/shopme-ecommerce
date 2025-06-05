$(document).ready(function() {
	
	$("#btnAdd2Cart").on("click", function() {
		addToCart();
	});
});

function addToCart() {
	
	quantity = $("#quantity" + productId).val();
	
	url = contextPath + "cart/add/" + productId + "/" + quantity;
	
	$.ajax({
		type : "POST",
		url : url,
		beforeSend : function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
	}).done(function(response) {
		showModalDialog("Giỏ hàng", response);
	}).fail(function(response) {
		showErrorModal("Thêm sản phẩm vào giỏ hàng thất bại !");
	});
}