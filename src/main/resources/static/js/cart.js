var cartList;

// 장바구니 상품 객체 -> 객체 배열에 담기
$(document).ready(function () {
    $.ajax({
        url: "/carts",
        type: "GET",
        success : function (data) {
            console.log(data.json().result);
        },
        error: function (error) {
            console.log(error);
        }
    });
    console.log(cartList);
})


// 상품 배열 생성


// IndexedDB 지원여부 확인
if(!window.indexedDB) {
    window.alert("해당 브라우저는 IndexedDB를 지원하지 않습니다.");
} else {
    var db;
    var request = window.indexedDB.open('myCartDB');

    request.onerror = function(event) {
        console.log(event);
        alert('failed');
    }
    request.onsuccess = function(event) {
        console.log(event);
        db = request.result;
    }
}

// 상품 수량 변경하기
function count(type) {
    var request = window.indexedDB.open('myCartDB');

    request.onerror = function (event) {
        alert('Database Error: ' + event.target.errorCode);
    }
    // request.onsuccess = function(event) {
    //     var transaction =db.transaction(['cartItem'], 'readwrite');
    //
    // }
}

// 총 결제 예정 금액 구하기
function getCheckPrice() {
    console.log('getCheckPrice');
    const checkValue = 'input[name="item"]:checked';
    const selectElements = document.querySelectorAll(checkValue);

    let result = 0;
    selectElements.forEach((el) => {
        result += parseInt(el.value);
        console.log(result);
    });

    let deliveryPrice = 3000;
    if(result >= 50000) {  // 50000원 이상은 무료배송
        deliveryPrice = 0;
    }
    document.getElementsByClassName('delivery-price')[0].innerText = deliveryPrice;

    document.getElementsByClassName('order-price')[0].innerText = result;
    console.log(Number(result) + Number(deliveryPrice));
    document.getElementsByClassName('total-price')[0].innerText = parseInt(result) + deliveryPrice;
}