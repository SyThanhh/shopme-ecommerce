function clearFilter() {
			window.location=moduleURL;
		}

	 function showDeleteConfirmModal(link, entityName) {
		 	entityId = link.attr("entityId");
			$("#yesBtn").attr("href", link.attr("href"));
			$("#confirmBody").text("Are you sure you want to delete this "+entityName+" ID " + entityId);
			$("#confirmModal").modal();
		} 
