function updateRole(idx) {
    var id = $('.user-id').eq(idx).html();
    var userName = $('.username').eq(idx).html();
    var role = $('.role').eq(idx);
    var answer = confirm("정말 " + userName + "의 권한을 ADMIN으로 올리시겠습니까?");

    if(answer) {
        if(role.html() === 'ADMIN') {
        } else if(role.html() === 'USER') {
        }
        $.ajax({
            url: `/api/v1/admin/role/${id}`,
            method: "PUT",
            dataType: "json",
            contentType: 'application/json;charset=utf-8',
            data: JSON.stringify(role)
        }).done((data) => {
            if(data.resultCode === 'SUCCESS') {
                var resultRole = data.result.userRole;
                alert('권한을 ' + resultRole + '로 변경하였습니다.');
                role.html(resultRole);
            }
        }).fail((error) => {
        }).always(() => {
        })
    }
}

function deleteRole(idx) {
    var id = $('.user-id').eq(idx).html();
    var userName = $('.username').eq(idx).html();
    var answer = confirm("정말 " + userName + "을(를) 삭제하시겠습니까?");

    if(answer) {
        $.ajax({
            url: `/api/v1/admin/user/${id}`,
            method: "DELETE",
            dataType: "json",
            contentType: 'application/json;charset=utf-8',
        }).done((data) => {
            if(data.resultCode === 'SUCCESS') {
                alert("삭제가 완료되었습니다.");
                $('.user-info').eq(idx).remove();
            }
        }).fail((error) => {
        }).always(() => {
        })
    }
}

function updateSeller(idx, status) {
    var id = $('.seller-id').eq(idx).html();
    var company = $('.company-name').eq(idx).html();
    var message = status === 'ok' ? 'SELLER' : 'REJECT'
    const seller = {
        id: parseInt(id),
        status: status
    }

    $.ajax({
        url: `/api/v1/admin/seller`,
        method: "PUT",
        dataType: "json",
        contentType: 'application/json;charset=utf-8',
        data: JSON.stringify(seller)
    }).done((data) => {
        if(data.resultCode === 'SUCCESS') {
            alert("판매자를 " + status + "로 변경하였습니다");
            $('.btn-group').eq(idx).remove();
            var change = data.result.userRole;
            $('.seller-status').eq(idx).html(change);
        }
    }).fail((error) => {
    }).always(() => {
    })
}

function deleteSeller(idx) {
    var id = $('.seller-id').eq(idx).html();
    var company = $('.company-name').eq(idx).html();
    var answer = confirm("정말 " + company + "을(를) 삭제하시겠습니까?");

    if(answer) {
        $.ajax({
            url: `/api/v1/admin/seller/${id}`,
            method: "DELETE",
            contentType: 'application/json;charset=utf-8',
        }).done((data) => {
            if(data.resultCode === 'SUCCESS') {
                alert("삭제가 완료되었습니다.");
                $('.seller-info').eq(idx).remove();
            }
        }).fail((error) => {
        }).always(() => {
        })
    }
}