const target1 = document.getElementById("btn-board-save");
const target2 = document.getElementById("btn-delete");
const target3 = document.getElementById("btn-update");
const target4 = document.getElementById("btn-reply-save");
const target5 = document.getElementById("files");

            if(target5 != null){
            target5.onchange = function(){

                        let fileSizeTotal = 0;
                        var files = target5.files;

                        totalFiles = files.length;
                        console.log("file count :" + totalFiles);

                        if(totalFiles > 3){
                        alert("파일은 3개까지 첨부 가능합니다")
                        target5.value = '';
                        console.log('file clear1')
                        return;
                        }

                        for (let index = 0; index < totalFiles; index++) {
                            fileSizeTotal = fileSizeTotal + files[index].size;
                            console.log('fileSizeTotal : '+fileSizeTotal);
                            if(fileSizeTotal > 1024*1024*2){
                                alert('파일의 업로드 용량 제한은 2MB 입니다. /n/n')
                                target5.value = '';
                                console.log('file clear2')
                                return;

                            }
                                 }
                        }
            }

let index = {

    init:function(){

        if(target3 != null){
             target3.addEventListener("click", () => {
                     this.update();
                   });
           }

        if(target2 != null){
             target2.addEventListener("click", () => {
                     this.deleteById();
                 });
            }

        if(target1 != null){
             target1.addEventListener("click", () => {
                    this.save();
                 });
           }

        if(target4 != null){
             target4.addEventListener("click", () => {
                    this.replySave();
                 });
           }

      },

      save: function(){

      var formData = new FormData();
      var files = new Array();
      var totalfiles = document.getElementById('files').files.length;
         for (let index = 0; index < totalfiles; index++) {
            formData.append("files[]", document.getElementById('files').files[index]);
         }
      formData.append("title",document.getElementById("title").value),
      formData.append("content",document.getElementById("content").value),


      console.log(formData.get("title"));
      console.log(formData.get("content"));
      console.log(formData.get("files"));

      //ajax 호출시 default가 비동기 호출
      $.ajax({
      type: "POST",
      url:"/api/board",
      data: formData, //http body 데이터
      enctype: "multipart/form-data",
      processData: false, //프로세스 데이터 설정 : false 값을 해야 form data로 인식합니다
      contentType: false, //헤더의 Content-Type을 설정 : false 값을 해야 form data로 인식합니다
      }).done(function(resp){
        alert("글쓰기가 되었습니다");
        location.href = "/";
        console.log(resp)
      }).fail(function(error){
      alert("글쓰기가 실패했습니다");
        location.href = "/";
//        console.log(resp)
        console.log(JSON.stringify(error))
      }); //3개의 데이터를 json으로 변경하여 insert 요청
      },

        update: function(){

        let id = document.getElementById("id").value;
        var formData = new FormData();
        var files = new Array();
        var totalfiles = document.getElementById('files').files.length;

        for (let index = 0; index < totalfiles; index++) {
             formData.append("files[]", document.getElementById('files').files[index]);
        }

        formData.append("title",document.getElementById("title").value),
        formData.append("content",document.getElementById("content").value),
        console.log(formData.get("title"));
        console.log(formData.get("content"));
        console.log(formData.get("files"));
        $.ajax({
        type: "PUT",
        url:"/api/board/"+id,
        data: formData,
        enctype: "multipart/form-data",
        processData: false,
         contentType: false,
        }).done(function(resp){
          alert("글 수정이 완료되었습니다");
          location.href = "/";
          console.log(resp)
        }).fail(function(error){
        alert("글 수정이 실패했습니다");
          location.href = "/";
          console.log(JSON.stringify(error))
        });
        },




//      update: function(){
//
//      let id = document.getElementById("id").value;
//      console.log(id);
//      let data={
//        title : document.getElementById("title").value,
//        content : document.getElementById("content").value
//         };
//         console.log(data);
//
//      $.ajax({
//      type: "PUT",
//      url:"/api/board/"+id,
//      data: JSON.stringify(data),
//      contentType:"application/json; charset=utf-8",
//      dataType: "json"
//      }).done(function(resp){
//        alert("글 수정이 완료되었습니다");
//        location.href = "/";
//        console.log(resp)
//      }).fail(function(error){
//      alert("글 수정이 실패했습니다");
//        location.href = "/";
//        console.log(JSON.stringify(error))
//      });
//      },

      deleteById: function(){
            let id= document.getElementById("id").textContent;
            console.log(id);
            $.ajax({
            type: "DELETE",
            url:"/api/board/"+id,
            dataType: "json"
            }).done(function(resp){
              alert("삭제가 되었습니다");
              location.href = "/";
              console.log(resp)
            }).fail(function(error){
            alert("삭제가 실패했습니다");
              location.href = "/";
              console.log(JSON.stringify(error))
            });
           },

           replySave: function(){
                 let data={
                   content : document.getElementById("reply-content").value,
                   boardId : document.getElementById("boardId").value,
                   memberId : document.getElementById("memberId").value
                    };
                    console.log(data)

                 $.ajax({
                 type: "POST",
                 url:`/api/board/${data.boardId}/reply`,
                 data: JSON.stringify(data),
                 contentType:"application/json; charset=utf-8",
                 dataType: "json"
                 }).done(function(resp){
                   alert("댓글 작성이 되었습니다");
                   location.href = `/board/${data.boardId}`;
                   console.log(resp)
                 }).fail(function(error){
                 alert("글쓰기가 실패했습니다");
                   location.href = "/";
                   console.log(JSON.stringify(error))
                 });
                 },

           replyDelete: function(boardId,replyId){
                  alert("실행정상")
                  $.ajax({
                  type: "DELETE",
                  url:`/api/board/${boardId}/reply/${replyId}`,
                  dataType: "json"
                  }).done(function(resp){
                  alert("댓글 삭제 성공");
                  location.href = `/board/${boardId}`;
                  console.log(resp)
                  }).fail(function(error){
                  alert("댓글 삭제 실패");
                  location.href = `/board/${boardId}`;
                  console.log(JSON.stringify(error))
                  });
                  },

                  boardDeleteAdmin: function(boardId){
                  alert("실행정상")
                  $.ajax({
                  type: "DELETE",
                  url:`/admin/board/${boardId}`,
                  dataType: "json"
                  }).done(function(resp){
                  alert("게시물 삭제 성공");
                  location.href = `/board/boards`;
                  console.log(resp)
                  }).fail(function(error){
                  alert("게시물 삭제 실패");
                  location.href = `/admin/boards`;
                  console.log(JSON.stringify(error))
                  });
                  },

                  boardHide: function(boardId){
                  $.ajax({
                  type: "Post",
                  url:`/admin/board/hide/${boardId}`,
                  dataType: "json"
                  }).done(function(resp){
                  alert("게시물 숨기기 성공");
                  location.href = `/admin/boards`;
                  console.log(resp)
                  }).fail(function(error){
                  alert("게시물 숨기기 실패");
                  location.href = `/admin/boards`;
                  console.log(JSON.stringify(error))
                  });
           },

                  boardShow: function(boardId){
                  $.ajax({
                  type: "Post",
                  url:`/admin/board/show/${boardId}`,
                  dataType: "json"
                  }).done(function(resp){
                  alert("게시물 보이기 성공");
                  location.href = `/admin/boards`;
                  console.log(resp)
                  }).fail(function(error){
                  alert("게시물 보이기 실패");
                  location.href = `/admin/boards`;
                  console.log(JSON.stringify(error))
                  });
           }
    }
      index.init();



