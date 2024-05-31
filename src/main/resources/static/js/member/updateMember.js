document.querySelector("#passwordConfirmation").onblur = (e) => {
    const password = document.querySelector("#memberPwd");
    const passwordConfirmation = e.target;
    if (password.value !== passwordConfirmation.value) {
        alert("패스워드가 일치하지 않습니다.");
        password.select();
    }
};

const existingUsername = $('.form-control').data('member-id');

document.memberCreateFrm.onsubmit = (e) => {
    const frm = e.target;
    const username = frm.memberLoginId;
    const password = frm.memberPwd;
    const idDuplicateCheck = frm.idDuplicateCheck;
    const passwordConfirmation = frm.passwordConfirmation;
    const email = frm.memberEmail;
    const name = frm.memberName;

    if (username.value !== existingUsername && !/^\w{4,}$/.test(username.value)) {
        alert("아이디는 영문자, 숫자, _ 4자리이상 입력하세요.");
        username.focus();
        return false;
    }
    if(username.value !== existingUsername && idDuplicateCheck.value == 0) {
        alert("사용 가능 한 아이디를 입력해주세요.")
        memberLoginId.select();
        return false;
    }

    if (password.value !== passwordConfirmation.value) {
        alert("패스워드가 일치하지 않습니다.");
        password.select();
        return false;
    }
    if (!/^[\w가-힣]{2,}$/.test(name.value)) {
        alert("이름을 2글자 이상 입력하세요.");
        name.select();
        return false;
    }
    if (!/^[\w]{4,}@[\w]+(\.[\w]+){1,3}$/.test(email.value)) {
        alert("유효한 이메일을 작성해주세요.");
        email.select();
        return false;
    }

    if (!selectedGenre) {
        alert("영화 장르를 선택해주세요.");
        return false;
    }
};

/**
 * 아이디 중복검사
 */
document.querySelector("#memberLoginId").onkeyup = (e) => {
    const username = e.target;
    const guideOk = document.querySelector(".guide.ok");
    const guideError = document.querySelector(".guide.error");
    const idDuplicateCheck = document.querySelector("#idDuplicateCheck");
    const memberId = document.querySelector("#memberId").value; // 현재 회원의 ID를 가져옴

    if(!/^\w{4,}$/.test(username.value.trim())) {
        guideError.style.display = "none";
        guideOk.style.display = "none";
        idDuplicateCheck.value = 0;
        return;
    }

    $.ajax({
        url : `${contextPath}member/existingCheckIdDuplicate.do`,
        method : 'post',
        headers : {
            [csrfHeaderName] : csrfToken
        },
        data : {
            username : username.value.trim(),
            memberId : memberId // 현재 회원의 ID도 전송
        },
        success(response){
            const {available} = response;
            if(available) {
                guideError.style.display = "none";
                guideOk.style.display = "inline";
                idDuplicateCheck.value = 1;
            } else {
                guideError.style.display = "inline";
                guideOk.style.display = "none";
                idDuplicateCheck.value = 0;
            }
        }
    })
};

/**
 * 기존 선호 장르 표시
 */
document.addEventListener('DOMContentLoaded', function() {

    $.ajax({
        url: `${contextPath}member/preferredGenres`,
        type: 'get',
        headers : {
            [csrfHeaderName] : csrfToken
        },
        success: function(response) {
            displaySelectedGenres(response);
        },
        error: function(xhr, status, error) {
            console.error('Error fetching preferred genres:', error);
        }
    });
});

// 선호 장르 표시 함수
function displaySelectedGenres(preferredGenres) {
    // 모든 장르 버튼을 숨깁니다.
    // document.querySelectorAll('[name="genres"]').forEach(function(genreButton) {
    //     genreButton.parentElement.style.display = 'none';
    // });

    // 서버로부터 받은 선호 장르로 라디오 버튼 표시
    preferredGenres.forEach(function(genre) {
        const genreButton = document.querySelector(`[value="${genre.genreName}"]`);
        if (genreButton) {
            genreButton.parentElement.style.display = 'flex';
            genreButton.checked = true; // 해당 장르를 선택합니다.
        }
    });
}
