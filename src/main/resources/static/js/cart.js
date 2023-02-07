// 상품 수량 변경하기
function count(type, idx)  {
    console.log('count() 실행');
    console.log(idx);
    // 장바구니에 담긴 수량
    let number = parseInt(document.getElementsByClassName('item-cnt')[idx].value);
    // 상품 재고
    let stock = parseInt(document.getElementsByClassName('individual_stock_input')[idx].value);
    // 상품 가격
    let price = parseInt(document.getElementsByClassName('individual_price_input')[idx].innerText);
    console.log('재고 = ' + stock);
    // 더하기/빼기
    if(type === 'plus') {
        number = number + 1;
        if(number > 10) {
            alert("수량은 10개 이하이어야 합니다.");
            document.getElementsByClassName('item-cnt')[idx].value = 10;
            return;
        }  else if (number > stock) {
            alert("구매할 수 있는 개수를 초과하였습니다.");
            return;
        }
    }else if(type === 'minus')  {
        number = number - 1;
        if(number <=0) {
            alert("수량은 1개 이상이어야 합니다.");
            document.getElementsByClassName('item-cnt')[idx].value = 1;
            return;
        }
    }

    console.log("현재 담은 수량 = " + number);
    document.getElementsByClassName('form-check-input')[idx].value = price * number;

    getCheckPrice();
    // 결과 출력
    document.getElementsByClassName('item-cnt')[idx].value = number;
}

function deleteItem(idx) {
    console.log(idx);
    const itemName = document.getElementsByClassName('individual_name_input')[idx].innerText;
    const itemId = document.getElementsByClassName('delete-btn')[idx].value;
    let confirm_massage = confirm(itemName + "을(를) 정말 삭제하시겠습니까?");

    if(confirm_massage) {
        const formHtml = `
            <form id="deleteForm" action="/carts/${itemId}" method="post">
                <input type="hidden" id="id" name="id" value="${itemId}" />
            </form>
            `;
        const doc = new DOMParser().parseFromString(formHtml, 'text/html');
        const form = doc.body.firstChild;
        document.body.append(form);
        document.getElementById('deleteForm').submit();
    }
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
        console.log('el.value : ' + el.value);
        result += parseInt(el.value);
        console.log(result);
    });

    let deliveryPrice = 3000;
    if(result >= 50000) {  // 50000원 이상은 무료배송
        deliveryPrice = 0;
    }
    document.getElementsByClassName('delivery-price')[0].innerText = deliveryPrice;

    document.getElementsByClassName('order-price')[0].innerText = result;
    // console.log(Number(result) + Number(deliveryPrice));
    console.log(parseInt(result));
    if(parseInt(result) === 0)
        document.getElementsByClassName('total-price')[0].innerText = result;
    else
        document.getElementsByClassName('total-price')[0].innerText = parseInt(result) + deliveryPrice;
}

$(document).ready(function() {
    getCheckPrice();
})