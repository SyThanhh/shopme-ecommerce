/**
 * 
 */

$(document)
   .ready(
           function() {
               $("#btnCancel").on("click", function() {
                   window.location = moduleURL;
               });

               $("#fileImage").change(function() {
                     if(!checkFileSize(this)) {
						return;
					 } 
					showImageThumbnail(this);
					 
					
               });
           });

		   
		// check file size
		function checkFileSize(fileInput) {
			fileSize = fileInput.files[0].size;
             if (fileSize > MAX_FILE_SIZE) { // Kiểm tra xem kích thước tệp có lớn hơn 1MB (1048576 bytes) không
                 fileInput.setCustomValidity("You must choose an image less than " + MAX_FILE_SIZE + " bytes !"); // Đặt thông báo lỗi tùy chỉnh cho đầu vào tệp
                 fileInput.reportValidity(); // Hiển thị thông báo lỗi cho người dùng
             } else {
                
				 return true;
             }
		}
       // lúc tạo
       function showImageThumbnail(fileInput) {
           var file = fileInput.files[0]; // Lấy tệp đầu tiên từ input file
           var reader = new FileReader(); // Tạo một đối tượng FileReader
           console.log(file);

           reader.onload = function(e) { // Đặt một hàm xử lý sự kiện khi đọc tệp hoàn thành

               $("#thumbnail").attr("src", e.target.result); // Đặt thuộc tính src của phần tử hình ảnh có id là "thumbnail" bằng dữ liệu base64 của tệp
           };

           reader.readAsDataURL(file); // Đọc tệp dưới dạng URL data (base64)
       }
	   
	   
   function showModalDialog(title, message) {
              $("#modalTitle").text(title);
              $("#modalBody").text(message);
        		jQuery.noConflict(); /* fix lỗi modal is not function do xung đột jquery */
              $('#modalDialog').modal('show');
	}
        
  	 function showErrorModal(message) {
          showModalDialog("Error", message)
      }
      
      function showWarningModal(message) {
          showModalDialog("Warning", message);
      }   
   