var productCost;
var subtotal;
var shippingCost;
var tax;
var total;

$(document).ready(function() {
	productCost = $("#productCost");
	subtotal = $("#subtotal");
	shippingCost = $("#shippingCost");
	tax = $("#tax");
	total = $("#total");


	formatOrderAmounts();
	formatProductAmounts();
	
	$("#productList").on('change', '.quantity-input', function(e) {
		e.preventDefault();
		updateSubtotalQuantityChanged($(this)); // calculate subtoal when quantity change
		updateOrderAmounts();
	});
	
	$("#productList").on("change", ".price-input", function(e) {
			updateSubtotalPriceChanged($(this));
			updateOrderAmounts();
	});
	
	$("#productList").on("change", ".cost-input", function(e) {
			updateOrderAmounts();
	});
	
	$("#productList").on("change", ".ship-input", function(e) {
			updateOrderAmounts();
	});
});
function getNumberValueRemovedThousandSeparator(fieldRef) {
	fieldValue = fieldRef.val().replace(",", "");
	return parseFloat(fieldValue);
}
// UPDATE TAB OVERVIEW
function updateOrderAmounts() {
	totalCost = 0.0;

	$(".cost-input").each(function(e) {
		costInputField = $(this);
		rowNumber = costInputField.attr("rowNumber");
		quantityValue = $("#quantity" + rowNumber).val();


		productCost = getNumberValueRemovedThousandSeparator(costInputField);
		totalCost += productCost * parseInt(quantityValue);
	});

	setAndFormatNumberForField("productCost", totalCost);

	orderSubtotal = 0.0;

	$(".subtotal-output").each(function(e) {
		productSubtotal = getNumberValueRemovedThousandSeparator($(this));
		orderSubtotal += productSubtotal;
	});

	setAndFormatNumberForField("subtotal", orderSubtotal);

	shippingCost = 0.0;

	$(".ship-input").each(function(e) {
		productShip = getNumberValueRemovedThousandSeparator($(this));
		shippingCost += productShip;
	});

	setAndFormatNumberForField("shippingCost", shippingCost);

	taxFiled = getNumberValueRemovedThousandSeparator(tax);
	orderTotal = orderSubtotal + taxFiled + shippingCost;
	setAndFormatNumberForField("total", orderTotal);
}

function setAndFormatNumberForField(fieldId, fieldValue) {
	formattedValue = $.number(fieldValue, 2);
	$("#" + fieldId).val(formattedValue);
}
function updateSubtotalPriceChanged(input) {
	const priceValue = parseInt(input.val());
	const rowNumber = input.attr("rowNumber");
	const quantityValue = parseFloat($("#quantity" + rowNumber).val());

	const newSubtotal = priceValue * quantityValue;

	
	setAndFormatNumberForField("subtotal" + rowNumber, newSubtotal);
}
function updateSubtotalQuantityChanged(input) {
	const quantityValue = parseInt(input.val());
	const rowNumber = input.attr("rowNumber");
	const priceValue = parseFloat($("#price" + rowNumber).val());

	const newSubtotal = priceValue * quantityValue;

	setAndFormatNumberForField("subtotal" + rowNumber, newSubtotal);
}

function formatOrderAmounts() {
	formatNumberForField(productCost);
	formatNumberForField(subtotal);
	formatNumberForField(shippingCost);
	formatNumberForField(tax);
	formatNumberForField(total);
}

function formatProductAmounts() {
	$(".cost-input").each(function(e) {
		formatNumberForField($(this));
	});

	$(".price-input").each(function(e) {
		formatNumberForField($(this));
	});

	$(".subtotal-output").each(function(e) {
		formatNumberForField($(this));
	});

	$(".ship-input").each(function(e) {
		formatNumberForField($(this));
	});
}
function formatNumberForField(fieldRef) {
	fieldRef.val($.number(fieldRef.val(), 2));
}