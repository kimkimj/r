// 상품 수량 변경하기
function count(type) {
}

// 전체 선택
function selectAll(selectAll)  {
    const checkboxes
        = document.getElementsByName('item');

    checkboxes.forEach((checkbox) => {
        checkbox.checked = selectAll.checked;
    })
    getCheckPrice();
}

// 총 결제 예정 금액 구하기
function getCheckPrice() {
    console.log('getCheckPrice() 실행');
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