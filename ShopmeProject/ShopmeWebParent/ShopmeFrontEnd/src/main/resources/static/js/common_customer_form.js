
var dropDownCountry;
var dataListState;
var fieldState;
$(document).ready(function() {
	dropDownCountry = $("#country");
	dataListState = $("#listStates");
	fieldState = $('#state');

	dropDownCountry.on("change", function() {
		loadStatesForCountry();
	});
});

function loadStatesForCountry() {
	selectedCountry = $("#country option:selected");
	countryId = selectedCountry.val();
	url = contextPath + "settings/list_states_by_country/" + countryId;
	
	$.get(url, function(responseJSON) {
		dataListState.empty();
		console.log("response : " , responseJSON);
		$.each(responseJSON, function(index, state) {
			$("<option>").val(state.name).text(state.name).appendTo(dataListState);
		});

	}).fail(function() {
		alert('failed to connect to the server!');
	});
}
// check password
function checkPasswordMatch(confirmPassword) {
	if(confirmPassword.value != $("#password").val()) {
		confirmPassword.setCustomValidity("Passwords do not match !");
	} else {
		confirmPassword.setCustomValidity("");
	}
}

// check email duplicated
function checkEmailUnique(form) {
	url = contextPath + "customers/check_unique_email";
	customerEmail = $("#email").val();
	csrfValue = $("input[name='_csrf']").val();

	params = {email: customerEmail, _csrf: csrfValue};

	$.post(url, params, function (response) {
		if (response == "OK") {
			form.submit();
		} else if (response == "Duplicated") {
			showWarningModal("There is another cusotmer having the email " + customerEmail);
		} else {
			showErrorModal("Unknown response from server");
		}
	}).fail(function () {
		showErrorModal("Could not connect to the server");
	});

	return false;
}



