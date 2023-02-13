function updateRole(idx) {
    console.log('updateRole() 실행');
    console.log("이 사람은 " + idx + "번째");
    var id = $('.user-id').eq(idx).html();
    var userName = $('.username').eq(idx).html();
    var role = $('.role').eq(idx);
    console.log("이 사람은 " + role.html() + "등급");
    var answer = confirm("정말 " + userName + "의 권한을 ADMIN으로 올리시겠습니까?");

    if(answer) {
        console.log(userName + "의 권한을 수정합니다.");
        if(role.html() === 'ADMIN') {
            console.log('원래 권한은 ADMIN');
        } else if(role.html() === 'USER') {
            console.log('원래 권한은 USER');
        }
        $.ajax({
            url: `/api/v1/admin/role/${id}`,
            method: "PUT",
            contentType: 'application/json;charset=utf-8',
            data: JSON.stringify(role)
        }).done((data) => {
            console.log(data);
            if(data.resultCode === 'SUCCESS') {
                var resultRole = data.result.userRole;
                console.log(typeof resultRole);
                alert('권한을 ' + resultRole + '로 변경하였습니다.');
                role.html(resultRole);
            }
        }).fail((error) => {
            console.log(error);
        }).always(() => {
            console.log("권한 변경 기능 호출 완료");
        })
    }
}

function deleteRole(idx) {
    console.log('deleteRole() 실행');
    console.log("이 사람은 " + idx + "번째");
    var id = $('.user-id').eq(idx).html();
    var userName = $('.username').eq(idx).html();
    var answer = confirm("정말 " + userName + "을(를) 삭제하시겠습니까?");

    if(answer) {
        console.log(userName + "을 삭제합니다.");
        $.ajax({
            url: `/api/v1/admin/${id}`,
            method: "DELETE",
            contentType: 'application/json;charset=utf-8',
        }).done((data) => {
            console.log(data);
            if(data.resultCode === 'SUCCESS') {
                alert("삭제가 완료되었습니다.");
                $('.user-info').eq(idx).remove();
            }
        }).fail((error) => {
            console.log(error);
        }).always(() => {
            console.log("회원 삭제 기능 호출 완료");
        })
    }
}