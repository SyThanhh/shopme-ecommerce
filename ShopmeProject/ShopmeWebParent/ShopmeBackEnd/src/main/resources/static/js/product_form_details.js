$(document).ready(function(){
    $("#btnAddDetailSeciton").click(() => {
        addNextDetailSection();
    });
});

function addNextDetailSection() {
    allDivDetails = $("[id^='divDetail']");
    divDetailsCount = allDivDetails.length;

    htmlDetailSection = `<div class="form-row mt-3" id="divDetail${divDetailsCount}">
				<input type="hidden" name="detailIDs" value="${detail.id}"/>
                <div class="col-4">
                  <input type="text" class="form-control" placeholder="Name" name="detailNames" maxlength="255">
                </div>
                <div class="col-4">
                  <input type="text" class="form-control" placeholder="Value" name="detailValues" maxlength="255">
                </div>
                <div class="col-4">
                    <a class="btn fas fa-minus icon-dark remove-btn-detail" title="Remove this detail" style="color:gray; cursor:pointer;"
                    data-id="${divDetailsCount}"></a>
                </div>
             </div>`;

    $("#divProductDetails").append(htmlDetailSection);
}

$(document).on('click', '.remove-btn-detail', function() {
    var detailElementId = $(this).data('id');
    removeDetailSectionById(detailElementId);
});

function removeDetailSectionById(element) {
    $("#divDetail" + element).remove();
}
