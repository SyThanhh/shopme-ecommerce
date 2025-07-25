let trackRecordCount;

$(document).ready(function () {
	trackRecordCount = $(".hiddenTrackId").length;

	// Xóa dòng tracking
	$("#trackList").on("click", ".linkRemoveTrack", function (e) {
		e.preventDefault();
		deleteTrack($(this));
		updateTrackCountNumbers();
	});

	// Thêm dòng tracking mới
	$("#track").on("click", "#linkAddTrack", function (e) {
		e.preventDefault();
		addNewTrackRecord();
	});

	// Tự động cập nhật ghi chú khi thay đổi trạng thái
	$("#trackList").on("change", ".dropDownStatus", function () {
		const statusDropdown = $(this);
		const rowNumber = statusDropdown.attr("rowNumber");
		const selectedOption = $("option:selected", statusDropdown);
		const defaultNote = selectedOption.attr("defaultDescription");

		$("#trackNote" + rowNumber).text(defaultNote);
	});
});

function deleteTrack(link) {
	const rowNumber = link.attr("rowNumber");
	$("#rowTrack" + rowNumber).remove();
	$("#emptyLine" + rowNumber).remove();
}

function updateTrackCountNumbers() {
	$(".divCountTrack").each(function (index, element) {
		element.innerHTML = "" + (index + 1);
	});
}

function addNewTrackRecord() {
	const htmlCode = generateTrackCode();
	$("#trackList").append(htmlCode);
}

function generateTrackCode() {
	const nextCount = ++trackRecordCount;
	const rowId = "rowTrack" + nextCount;
	const emptyLineId = "emptyLine" + nextCount;
	const trackNoteId = "trackNote" + nextCount;
	const currentDateTime = formatCurrentDateTime();
 
	let htmlCode = `
		<div class="row border rounded p-1" id="${rowId}">
			<input type="hidden" name="trackId" value="0" class="hiddenTrackId" />
			<div class="col-2">
				<div class="divCountTrack">${nextCount}</div>
				<div class="mt-1">
					<a class="fas fa-trash icon-dark linkRemoveTrack" href="#" rowNumber="${nextCount}"></a>
				</div>					
			</div>				
				
			<div class="col-10">
				<div class="form-group row">
					<label class="col-form-label">Time:</label>
					<div class="col">
						<input type="datetime-local" name="trackDate" value="${currentDateTime}" class="form-control" required style="max-width: 300px"/>						
					</div>
				</div>					

				<div class="form-group row">  
					<label class="col-form-label">Status:</label>
					<div class="col">
						<select name="trackStatus" class="form-control dropDownStatus" required style="max-width: 150px" rowNumber="${nextCount}">
	`;

	htmlCode += $("#trackStatusOptions").clone().html();
	

	htmlCode += `
						</select>						
					</div>
				</div>

				<div class="form-group row">
					<label class="col-form-label">Notes:</label>
					<div class="col">
						<textarea rows="2" cols="10" class="form-control" name="trackNotes" id="${trackNoteId}" style="max-width: 300px" required></textarea>
					</div>
				</div>
			</div>				
		</div>	
		<div id="${emptyLineId}" class="row">&nbsp;</div>
	`;

	return htmlCode;
}

function formatCurrentDateTime() {
	const date = new Date();
	const year = date.getFullYear();
	const month = String(date.getMonth() + 1).padStart(2, '0');
	const day = String(date.getDate()).padStart(2, '0');
	const hour = String(date.getHours()).padStart(2, '0');
	const minute = String(date.getMinutes()).padStart(2, '0');
	const second = String(date.getSeconds()).padStart(2, '0');

	return `${year}-${month}-${day}T${hour}:${minute}:${second}`;
}
