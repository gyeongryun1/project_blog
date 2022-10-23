const target1 = document.getElementById("btn-save");
const target2 = document.getElementById("btn-update");

let index = {


      init:function(){

          if(target1 != null){
          target1.addEventListener("click", () => {
            this.save();
          });
          }

          if(target2 != null){
          target2.addEventListener("click", () => {
            this.update();
          });
          }
      },


//      save: function(){
//      let data={
//        username : document.getElementById("username").value,
//        password : document.getElementById("password").value,
//        email : document.getElementById("email").value
//         };
//         console.log(data);
//
//      //ajax 호출시 default가 비동기 호출
//      $.ajax({
//      type: "POST",
//      url:"/auth/joinProc",
//      data: JSON.stringify(data), //http body 데이터
//      contentType:"application/json; charset=utf-8", //body data가 어떤 타입인지
//      dataType: "json"//요청을 서버로 해서 응답이 왔을 때 기본적으로 모든것이 String(생긴게 json이라면 javascript 오브젝트로 변경 )
//      }).done(function(resp){
//      if(resp.status === 500){
//            alert("회원가입에 실패했습니다")
//            } else{
//        alert("회원가입이 완료 되었습니다");
//        location.href = "/";
//        }
//      }).fail(
//      function(error){
//
//      alert("회원가입에 실패했습니다");
//        location.href = "/auth/joinForm2";
//        console.log(resp)
//        console.log(JSON.stringify(error))
//      }
//      ); //3개의 데이터를 json으로 변경하여 insert 요청
//
//      },

      update: function(){
      let data={
        id : document.getElementById("id").value,
        username : document.getElementById("username").value,
        password : document.getElementById("password").value,
        email : document.getElementById("email").value
         };
         console.log(data);

      $.ajax({
      type: "PUT",
      url:"/member",
      data: JSON.stringify(data), //http body 데이터
      contentType:"application/json; charset=utf-8", //body data가 어떤 타입인지
      dataType: "json"//요청을 서버로 해서 응답이 왔을 때 기본적으로 모든것이 String(생긴게 json이라면 javascript 오브젝트로 변경 )
      }).done(function(resp){
        alert("회원수정이 완료 되었습니다");
        location.href = "/";
        console.log(resp)
      }).fail(function(error){
      alert("회원수정에 실패했습니다");
        location.href = "/";
//        console.log(resp)
        console.log(JSON.stringify(error))
      }); //3개의 데이터를 json으로 변경하여 insert 요청

      },

      block: function(memberId){
      $.ajax({
      type: "get",
      url:`/admin/block/${memberId}`,
      dataType: "json"
      }).done(function(resp){
      alert("차단 성공");
      location.href = `/admin/member`;
      console.log(resp)
      }).fail(function(error){
      alert("차단 실패");
      location.href = `/admin/member`;
      console.log(JSON.stringify(error))
      });
      },

      unBlock: function(memberId){
      $.ajax({
      type: "get",
      url:`/admin/unBlock/${memberId}`,
      dataType: "json"
      }).done(function(resp){
      alert("차단 해제 성공");
      location.href = `/admin/member`;
      console.log(resp)
      }).fail(function(error){
      alert("차단 해제 실패");
      location.href = `/admin/member`;
      console.log(JSON.stringify(error))
      });
      }

    };
      index.init();



