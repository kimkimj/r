// 상품 수량 변경하기
function count(type, idx)  {
    console.log('count() 실행');
    console.log(idx);
    let number = parseInt(document.getElementsByClassName('item-cnt')[0].value);
    // 상품 재고
    let stock = parseInt(document.getElementsByClassName('individual_stock_input')[0].value);
    console.log('현재 수량 = ' + number);
    console.log('재고 = ' + stock);
    // 더하기/빼기
    if(type === 'plus') {
        console.log("plus");
        number = number + 1;
        if(number > 10) {
            alert("수량은 10개 이하이어야 합니다.");
            document.getElementsByClassName('item-cnt')[0].value = 10;
            return;
        }  else if (number > stock) {
            alert("구매할 수 있는 개수를 초과하였습니다.");
            return;
        }
    }else if(type === 'minus')  {
        console.log("minus");
        number = number - 1;
        if(number <=0) {
            alert("수량은 1개 이상이어야 합니다.");
            document.getElementsByClassName('item-cnt')[0].value = 1;
            return;
        }
    }

    console.log("number = " + number);

    // 결과 출력
    document.getElementsByClassName('item-cnt')[0].value = number;
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