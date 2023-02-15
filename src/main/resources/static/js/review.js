function updateReview(idx) {

    const recipeIdElement = document.getElementById('recipe-id');
    const reviewIdElement = document.getElementsByClassName('individual-review-id')[idx];
    const inputTag = document.getElementsByClassName('individual-content-input')[idx];

    const reviewUpdateRes = {
        content : inputTag.value
    }

    const recipeId = recipeIdElement.innerText;
    const reviewId = reviewIdElement.innerText;
    const editable = inputTag.readOnly;
    if(editable) {  // readOnly = true 일 때
        inputTag.readOnly = !editable;
        inputTag.style.outline = "1px solid #DDF7F8";
        // 값이 비어있지 않은 경우 커서 마지막에 위치
        inputTag.focus();
        inputTag.value = '';
        inputTag.value = reviewUpdateRes.content;
        document.getElementsByClassName('edit-btn')[idx].innerText = '수정완료';
    } else {  // readOnly = false 일 때
        if(!inputTag.value.trim()) {
            alert("리뷰 내용은 반드시 등록해야합니다.");
            inputTag.focus();
            inputTag.value = '';
        } else {
            $.ajax({
                url: `/api/v1/recipes/${recipeId}/reviews/${reviewId}`,
                method: "PUT",
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify(reviewUpdateRes),
            }).done((data) => {
                alert(data.result.message);
                inputTag.readOnly = !editable;
                inputTag.style.border = "none";
                inputTag.style.outline = "none";
                document.getElementsByClassName('edit-btn')[idx].innerText = '수정';
            }).fail((error) => {
                if(error.result) {
                    alert(error.result);
                }
            }).always(() => {
            })
        }
    }
}

function deleteReview(idx) {
    const recipeId = document.getElementById('recipe-id').innerText;
    const reviewId = document.getElementsByClassName('individual-review-id')[idx].innerText;
    let confirm_massage = confirm("해당 리뷰를 정말 삭제하시겠습니까?");
    if(confirm_massage) {
        const formHtml = `
            <form id="deleteForm" action="/recipes/${recipeId}/reviews/${reviewId}" method="post">
                <input type="hidden" id="recipeId" name="recipeId" value="${recipeId}" />
                <input type="hidden" id="reviewId" name="reviewId" value="${reviewId}" />
            </form>
            `;
        const doc = new DOMParser().parseFromString(formHtml, 'text/html');
        const form = doc.body.firstChild;
        document.body.append(form);
        document.getElementById('deleteForm').submit();
    }
}
