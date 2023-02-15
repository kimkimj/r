var duplicateIdCheck = false;
var duplicateEmailCheck = false;
$(document).ready(function () {
    const specialRule = /[`~!@#$%^&*()_|+\-=?;:'"<>\{\}\[\]\\\/ ]/gim;

    $("#id-input").keyup(function () {
        console.log("id 검사중");
        let ckid = $("#id-input").val();
        if (ckid.length <= 0 || ckid.length > 8) {
            $("#id-result").text("아이디는 8자이하여야 합니다").removeClass('text-primary').removeClass('text-dark').addClass('text-danger');
            duplicateIdCheck = false;
        }else if(specialRule.test(ckid)) {
            $("#id-result").text("아이디는 한글, 영문 대소문자, 숫자로만 이루어져야 합니다").removeClass('text-primary').removeClass('text-dark').addClass('text-danger');
            duplicateIdCheck = false;
        }else {
            $("#id-result").text("아이디 중복 검사를 해주세요.").removeClass('text-danger').removeClass('text-dark').addClass('text-danger');
            duplicateIdCheck = false;
        }
    });

    $(".id-btn").on("click", function () {
        let sellername = $('#id-input').val();
        if (sellername.trim().length == 0) {
            $("#id-result").text("아이디를 입력해주세요").removeClass('text-primary').removeClass('text-dark').addClass('text-danger');
            return;
        }
        $.ajax({
            url: "/api/v1/seller/check-sellerName",
            method: "GET",
            data: {
                sellerName: sellername
            },
            success: function (message) {
                alert(message);
                if(message === '사용 가능한 아이디 입니다.') {
                    duplicateIdCheck = true;
                    $("#id-result").text("사용 가능한 아이디 입니다").removeClass('text-danger').removeClass('text-dark').addClass('text-primary');
                }
            }
        })
    })

    $("#pw-input").keyup(function () {
        console.log("pw 검사중");
        let ckpw = $("#pw-input").val();
        if (ckpw.length < 8 || ckpw.length > 24) {
            $("#pw-result").text("비밀번호는 8자이상 24자이하여야 합니다").removeClass('text-primary').removeClass('text-dark').addClass('text-danger');
        }else if(ckpw.search(/\W|\s/g) > -1) {
            $("#pw-result").text("비밀번호는 한글, 영문 대소문자, 숫자로만 이루어져야 합니다").removeClass('text-primary').removeClass('text-dark').addClass('text-danger');
        }else {
            $("#pw-result").text("사용 가능한 비밀번호 입니다").removeClass('text-danger').removeClass('text-dark').addClass('text-primary');
        }
    });

    $("#name-input").keyup(function () {
        console.log("company 검사중");
        let ckname = $("#name-input").val();
        if (ckname.length < 2 || ckname.length > 20) {
            $("#name-result").text("회사명은 2자이상 16자이하여야 합니다").removeClass('text-primary').removeClass('text-dark').addClass('text-danger');
        }else {
            $("#name-result").empty();
        }
    });

    $("#email-input").keyup(function () {
        let emailCheck = new RegExp('[a-z0-9]+@[a-z]+\.[a-z]{2,3}');
        console.log("email 검사중");
        let ckemail = $("#email-input").val();
        if (ckemail.trim().length < 0) {
            $("#email-result").text("이메일을 입력해주세요").removeClass('text-primary').removeClass('text-dark').addClass('text-danger');
            duplicateEmailCheck = false;
        }else if(!emailCheck.test(ckemail)){
            $("#email-result").text("이메일을 올바른 형식으로 적어주세요.").removeClass('text-primary').removeClass('text-dark').addClass('text-danger');
            duplicateEmailCheck = false;
        }else {
            $("#email-result").text("이메일 중복검사를 진행해주세요.").removeClass('text-primary').removeClass('text-dark').addClass('text-danger');
            duplicateEmailCheck = false;
        }
    });

    $(".email-btn").on("click", function () {
        let emailCheck =new RegExp('[a-z0-9]+@[a-z]+\.[a-z]{2,3}');
        let ckemail = $('#email-input').val();
        if (ckemail.trim().length === 0 || ckemail == 'undefined') {
            $("#email-result").text("이메일을 입력해주세요").removeClass('text-primary').removeClass('text-dark').addClass('text-danger');
            return;
        }else if(!emailCheck.test(ckemail) || ckemail.match(/\s/g)){
            $("#email-result").text("이메일을 올바른 형식으로 적어주세요.").removeClass('text-primary').removeClass('text-dark').addClass('text-danger');
            return;
        }

        $.ajax({
            url: "/api/v1/seller/check-email",
            method: "GET",
            contentType: 'application/json',
            data: {
                email: ckemail
            },
            success: function (message) {
                alert(message);
                if(message === '사용가능한 이메일 입니다.') {
                    duplicateEmailCheck = true;
                    $("#email-result").text("사용 가능한 이메일 입니다").removeClass('text-danger').removeClass('text-dark').addClass('text-primary');
                }
            }
        })
    });

    $("#phone-input").keyup(function () {
        console.log("phone 검사중");
        let ckphone = $("#phone-input").val();
        let phoneCheck =new RegExp('(01[016789])(\\d{3,4})(\\d{4})');
        if (ckphone.length <= 0) {
            $("#phone-result").text("핸드폰 번호를 입력해주세요").removeClass('text-primary').removeClass('text-dark').addClass('text-danger');
            return;
        }else if(ckphone.search(/\W|\s/g) > -1) {
            $("#phone-result").text("핸드폰 번호는 특수기호, 공백 없이 숫자만 입력해주세요").removeClass('text-primary').removeClass('text-dark').addClass('text-danger');
            return;
        }else if(!phoneCheck.test(ckphone)){
            $("#phone-result").text("010xxxxxxxx형식으로 입력해주세요").removeClass('text-primary').removeClass('text-dark').addClass('text-danger');
            return;
        }else {
            $("#phone-result").empty();
            return;
        }
    });

    $("#businessnum-input").keyup(function () {
        let businessnumCheck =new RegExp('(\\d)');
        console.log("businessnum 검사중");
        let ckbuisinessnum = $("#businessnum-input").val();
        if (ckbuisinessnum.trim().length === 0) {
            $("#businessnum-result").text("사업자등록번호를 입력해주세요").removeClass('text-primary').removeClass('text-dark').addClass('text-danger');
            return;
        }else if(!businessnumCheck.test(ckbuisinessnum) || ckbuisinessnum.trim().length != 10) {
            $("#businessnum-result").text("사업자등록번호를 다시 확인해주세요").removeClass('text-primary').removeClass('text-dark').addClass('text-danger');
            return;
        }else {
            $("#businessnum-result").empty();
            return;
        }
    });

});

function submitForm() {
    console.log("submit() 실행");
    const specialRule = /[`~!@#$%^&*()_|+\-=?;:'"<>\{\}\[\]\\\/ ]/gim;
    const emailCheck =new RegExp('[a-z0-9]+@[a-z]+\.[a-z]{2,3}');
    const phoneCheck =new RegExp('(01[016789])(\\d{3,4})(\\d{4})');
    const businessnumCheck =new RegExp('(\\d)');
    let ckid = $("#id-input").val();
    let ckpw = $("#pw-input").val();
    let ckname = $("#name-input").val();
    let ckaddrno = $("#address-no-input").val();
    let ckaddress = $("#address-input").val();
    let ckdetail = $("#detail-input").val();
    let ckemail = $('#email-input').val();
    let ckphone = $("#phone-input").val();
    let ckbusinessnum = $("#businessnum-input").val();

    console.log(ckid);
    console.log(ckpw);
    console.log(ckname);
    console.log(ckaddrno);
    console.log(ckaddress);
    console.log(ckdetail);
    console.log(ckemail);
    console.log(ckphone);
    console.log(ckbusinessnum);

    if (ckid.length <= 0 || ckid.length > 8 || specialRule.test(ckid)) {
        alert("아이디를 다시 확인해주세요");
        return;
    }else if(!duplicateIdCheck){
        alert("아이디 중복체크를 해주세요");
        return;
    }else if(ckpw.length < 8 || ckpw.length > 24 || ckpw.search(/\W|\s/g) > -1) {
        alert("비밀번호를 다시 확인해주세요");
        return;
    }else if(ckname.length < 2 || ckname.length > 16) {
        alert("이름을 다시 확인해주세요");
        return;
    }else if(ckaddrno.length <= 0) {
        alert("우편번호 찾기를 진행해주세요");
        return;
    }else if(ckdetail.trim().length <= 0) {
        alert("상세주소를 다시 확인해주세요");
        return;
    }else if(ckemail.trim().length <= 0 || !emailCheck.test(ckemail)) {
        console.log(ckaddrno);
        console.log(ckaddrno.length);
        console.log(ckdetail);
        console.log(ckdetail.length);
        alert("이메일을 다시 확인해주세요");
        return;
    } else if(!duplicateEmailCheck) {
        alert("이메일 중복체크를 해주세요");
        return;
    } else if(ckphone.length <= 0 || ckphone.search(/\W|\s/g) > -1 || !phoneCheck.test(ckphone)) {
        alert("핸드폰 번호를 다시 확인해주세요");
        return;
    }else if(ckbusinessnum.trim().length < 10 || !businessnumCheck.test(ckbusinessnum)) {
        alert("사업자등록번호를 다시 확인해주세요");
        return;
    } else {
        const obj = {
            sellerName: ckid,
            password: ckpw,
            companyName: ckname,
            address: ckaddress + ' ' + ckdetail,
            email: ckemail,
            phoneNum: ckphone,
            businessRegNum: ckbusinessnum
        }
        $.ajax({
            url: "/api/v1/seller/join",
            method: "POST",
            dataType: "json",
            contentType: 'application/json',
            data: JSON.stringify(obj),
            success: function (data) {
                alert(data.result.message);
                location.href = "/seller/login";
            }
        });
    }
}

function sample6_execDaumPostcode() {
    $('#address-input').attr('readonly', false);
    new daum.Postcode({
        oncomplete: function(data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

            // 각 주소의 노출 규칙에 따라 주소를 조합한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var addr = ''; // 주소 변수
            var extraAddr = ''; // 참고항목 변수

            //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
            if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                addr = data.roadAddress;
            } else { // 사용자가 지번 주소를 선택했을 경우(J)
                addr = data.jibunAddress;
            }

            // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
            if(data.userSelectedType === 'R'){
                // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                    extraAddr += data.bname;
                }
                // 건물명이 있고, 공동주택일 경우 추가한다.
                if(data.buildingName !== '' && data.apartment === 'Y'){
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                if(extraAddr !== ''){
                    extraAddr = ' (' + extraAddr + ')';
                }
                // 조합된 참고항목을 해당 필드에 넣는다.
                // document.getElementById("sample6_extraAddress").value = extraAddr;

            }
            // else {
            //     document.getElementById("sample6_extraAddress").value = '';
            // }

            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            document.getElementById('address-no-input').value = data.zonecode;
            document.getElementById("address-input").value = addr + extraAddr;
            // 커서를 상세주소 필드로 이동한다.
            document.getElementById("detail-input").focus();
        }
    }).open();
    $('#address-input').attr("readonly", true);
}