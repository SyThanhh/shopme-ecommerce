var confirmText;
var confirmModalDialog;
var confirmButton;
var btnClose;
var iconNames = {
	'PICKED': 'fa-people-carry',
	'SHIPPING': 'fa-shipping-fast',
	'DELIVERED': 'fa-box-open',
	'RETURNED': 'fa-undo'
};
$(document).ready(function() {
	confirmText = $("#confirmText");
	confirmModalDialog = $("#confirmModal");
	confirmButton = $("#yesBtn");
	btnClose = $("#btnClose");
	
	$(".linkUpdateStatus").on("click", function(e) {
		e.preventDefault();
		const link =  $(this);
		const url = link.attr("href");
		confirmButton.attr("href", link.attr("href"));
		
		showUpdateConfirmModal(link);
	});
	
	handleConfirmButton();
});

function handleConfirmButton() {
	confirmButton.click(function(e) {
		e.preventDefault();
		
		sendRequestUpdate($(this));
	});
}

function sendRequestUpdate(button) {
	const requestURL = button.attr("href");
	
	$.ajax({
			type: 'POST',
			url: requestURL,
			beforeSend: function(xhr) {
				xhr.setRequestHeader(csrfHeaderName, csrfValue);
			}
		}).done(function(response) {
			updateStatusIconColor(response.orderId, response.status);
			showMessageModal("Order Updated Status Successfully");
			
		}).fail(function(err) {
			showMessageModal("Error updating order status");
		});
}
function showUpdateConfirmModal(link) {
	btnClose.text("NO");
	confirmButton.show();
	
	const orderId = link.attr("orderId");
	const status = link.attr("status");
	
	confirmText.text("Are you sure you want to update status of the order ID #" + orderId +
		" to " + status +"?"
	);
	
	confirmModalDialog.modal();
}

function updateStatusIconColor(orderId, status) {
	link = $("#link" + status + orderId);
	link.replaceWith("<i class='fas " + iconNames[status] + " fa-2x icon-green'></i>");
}

function showMessageModal(message) {
	btnClose.text('Close');
	confirmButton.hide();
	confirmText.text(message);
}
