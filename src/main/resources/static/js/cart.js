// 상품 수량 변경하기
async function count(type, idx)  {
    const idElements = document.getElementsByClassName('individual_id_input');
    const cntElements = document.getElementsByClassName('item-cnt');
    const stockElements = document.getElementsByClassName('individual_stock_input');
    const priceElements = document.getElementsByClassName('individual_price_input');

    const item = {
        cartItemId: parseInt(idElements[idx].value), // 장바구니에 담긴 아이템 id ( != 상품 아이디)
        cartItemCnt: parseInt(cntElements[idx].value)  // 장바구니에 담은 수량 (변경한 수량)
    }
    const stock = parseInt(stockElements[idx].value);
    const price = parseInt(priceElements[idx].innerText);

    // 더하기/빼기
    if(type === 'plus') {
        if(item.cartItemCnt >= 10) {
            alert("수량은 10개 이하이어야 합니다.");
            item.cartItemCnt = 10;
            return;
        }  else if (item.cartItemCnt >= stock) {
            alert("재고가 부족합니다.");
            return;
        }
        item.cartItemCnt += 1;
    }else if(type === 'minus')  {
        if(item.cartItemCnt <= 1) {
            alert("수량은 1개 이상이어야 합니다.");
            item.cartItemCnt = 1;
            return;
        }
        item.cartItemCnt -= 1;
    }

    console.log(item);
    console.log(item.cartItemId);
    console.log(JSON.stringify(item));
    $.ajax({
        url: `/api/v1/carts`,
        method: "PUT",
        dataType: "json",
        contentType: 'application/json;charset=utf-8',
        data: JSON.stringify(item)
    }).done((data) => {
        // console.log(data);
        // alert(data.result);
    }).fail((error) => {
        // console.log(error);
        alert(error.result);
    }).always(() => {
        console.log("장바구니 아이템 수정 기능 호출 완료");
    })

    // document.getElementsByClassName('form-check-input')[idx].value = price * item.cartItemCnt;
    document.getElementsByClassName('form-check-input')[idx].setAttribute('cnt', item.cartItemCnt);
    document.getElementsByClassName('form-check-input')[idx].setAttribute('price', price);

    await getCheckPrice();
    document.getElementsByClassName('item-cnt')[idx].value = item.cartItemCnt;
    // 결과 출력
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
        = document.getElementsByClassName('form-check-input');

    for(var checkbox of checkboxes) {
       checkbox.checked = selectAll.checked;
    }

    getCheckPrice();
}

// 총 결제 예정 금액 구하기
function getCheckPrice() {
    console.log('getCheckPrice() 실행');
    const checkValue = 'input[class="form-check-input"]:checked';
    const selectElements = document.querySelectorAll(checkValue);
    let result = 0;
    console.log(typeof(result));
    selectElements.forEach((el) => {
        console.log(el);
        const cnt = parseInt(el.getAttribute('cnt'));
        const price = parseInt(el.getAttribute('price'));

        result += (cnt * price);
        console.log("총합 = " + result);
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

function saveOrder() {
    console.log('purchaseOrder() 실행');
    const checkCnt = $(".form-check-input:checked").length;
    if(checkCnt == 0) {
        console.log(checkCnt);
        alert("상품을 1개 이상 선택해주세요");
        return;
    }

    var items = $(".form-check-input");
    var orderItems = [];

    for(var [idx, selectElement] of Object.entries(items)) {
        console.log(isNaN(idx));
        const cartItemId = $(selectElement).attr("cartItemId");
        const checked = selectElement.checked;
        if(!isNaN(idx)) {
            console.log("orderItems에 push합니다");
            orderItems.push({
                id: cartItemId,
                isChecked: checked
            })
        }
    }

    $.ajax({
        url: "/api/v1/carts/checkOrder",
        method: "POST",
        contentType: 'application/json;charset=utf-8',
        data: JSON.stringify(orderItems)
    }).done((data) => {
        console.log(data);
        location.href = "/carts/order";
    }).fail((error) => {
        console.log(error);
        if(error.result === 'NOT_ENOUGH_STOCK, 재고 수량이 없습니다') {
            alert('재고 수량이 부족한 상품이 있습니다 \n 상품의 재고 수량을 확인해주세요');
        }
    }).always(() => {
        console.log("주문 ajax 실행 완료");
    })
}

$(document).ready(function() {
    getCheckPrice();
})