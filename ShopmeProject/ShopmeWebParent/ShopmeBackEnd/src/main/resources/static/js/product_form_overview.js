dropdownBrands = $("#brand");
 dropdownCategories = $("#category");
   

    $(document).ready(() => {
		jQuery.noConflict(); // fix lỗi xung đột jquery
    	$("#shortDescription").richText();
    	$("#fullDescription").richText();
    	
        dropdownBrands.change(() => {
        	dropdownCategories.empty();
            getCategories();
        });
        
        getCategoriesForNewForm(); // khởi tạo lại status ban đầu
        
     // check dulicateName
   	    $("#productForm").submit(function(e) {
               e.preventDefault(); // Ngăn chặn form submit mặc định
               checkunique(this); // Gọi hàm checkUnique
         });
		 
	
    });
	
	function getCategoriesForNewForm() {
		catIdField = $("#categoryId");
	
		editMode = false;
		
		if(catIdField.length) {
			editMode = true;
		}
		if(!editMode) getCategories();
	}

    function getCategories() {
        let brandId = dropdownBrands.val();
      
        let url = brandModuleURL + "/" + brandId + "/categories";
        
        $.get(url, (responseJson) => {
        	$.each(responseJson, (index, category)=>{
        		 $("<option>").val(category.id).text(category.name).appendTo(dropdownCategories);
        	});
          
        });
    }
	
	
    
    function checkunique(form) {
   	
   		productId = $("#id").val();
   		productName = $("#name").val();
   		
   		csrfValue = $("input[name='_csrf']").val(); // Lấy giá trị CSRF token từ input ẩn
           params = {
               id : productId,
               name : productName,
               _csrf : csrfValue
           }; 
           $.post(
               checkUniqueURL,
               params,
               function(response) { // Thực hiện POST request
                   if (response == "OK") {
                       form.submit();
                   } else if (response == "Duplicate") {
                       showModalDialog("Warning",
                               "There is another Product having same name  "
                                       + productName); // Hiển thị phản hồi từ server
                   } else {
                       showModalDialog("Error",
                               "Unknow response from server");
                   } 
                   
               }).fail(function() { // khi sai đường dẫn
            	   showErrorModal("Could not connect to the server"); 
          });

           return false; // Ngăn form submit mặc định
       }