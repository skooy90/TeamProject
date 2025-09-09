// 폼 유효성 검사
function validateForm() {
    let isValid = true;
    const isEditMode = window.isEditMode;
    
    // 제품코드 검사 (등록 모드에서만)
    if (!isEditMode) {
        const standardCode = document.getElementById('standardCode');
        const standardCodeError = document.getElementById('standardCodeError');
        if (!standardCode.value) {
            standardCodeError.textContent = '제품코드를 선택해주세요.';
            standardCode.style.borderColor = '#dc3545';
            isValid = false;
        } else {
            standardCodeError.textContent = '';
            standardCode.style.borderColor = '#ddd';
        }
    }
    
    // 재고량 검사
    const quantity = document.getElementById('quantity');
    const quantityError = document.getElementById('quantityError');
    if (!quantity.value || quantity.value < 0) {
        quantityError.textContent = '재고량을 입력해주세요. (0 이상)';
        quantity.style.borderColor = '#dc3545';
        isValid = false;
    } else {
        quantityError.textContent = '';
        quantity.style.borderColor = '#ddd';
    }
    
    // 담당자 검사 (등록 모드에서만)
    if (!isEditMode) {
        const employeeNo = document.getElementById('employeeNo');
        const employeeNoError = document.getElementById('employeeNoError');
        if (!employeeNo.value) {
            employeeNoError.textContent = '담당자를 선택해주세요.';
            employeeNo.style.borderColor = '#dc3545';
            isValid = false;
        } else {
            employeeNoError.textContent = '';
            employeeNo.style.borderColor = '#ddd';
        }
    }
    
    return isValid;
}

// 폼 제출 처리
document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('materialForm');
    if (form) {
        form.addEventListener('submit', function(e) {
            e.preventDefault();
            
            if (validateForm()) {
                const isEditMode = window.isEditMode;
                const action = isEditMode ? '수정' : '등록';
                
                if (confirm('재고 정보를 ' + action + '하시겠습니까?')) {
                    // 실제 폼 제출
                    this.submit();
                }
            }
        });
    }
});

// 취소 버튼
function goBack() {
    if (confirm('작성 중인 내용이 사라집니다. 정말 취소하시겠습니까?')) {
        window.location.href = window.contextPath + '/material';
    }
}

// 삭제 버튼
function deleteMaterial() {
    if (confirm('정말로 이 재고를 삭제하시겠습니까?\n삭제된 데이터는 복구할 수 없습니다.')) {
        window.location.href = window.contextPath + '/material/delete?code=' + window.materialCode;
    }
}

// 실시간 유효성 검사
document.addEventListener('DOMContentLoaded', function() {
    const isEditMode = window.isEditMode;
    
    // 제품코드 검사 (등록 모드에서만)
    if (!isEditMode) {
        const standardCode = document.getElementById('standardCode');
        if (standardCode) {
            standardCode.addEventListener('change', function() {
                if (this.value) {
                    document.getElementById('standardCodeError').textContent = '';
                    this.style.borderColor = '#ddd';
                }
            });
        }
    }
    
    // 재고량 검사
    const quantity = document.getElementById('quantity');
    if (quantity) {
        quantity.addEventListener('input', function() {
            if (this.value && this.value >= 0) {
                document.getElementById('quantityError').textContent = '';
                this.style.borderColor = '#ddd';
            }
        });
    }
    
    // 담당자 검사 (등록 모드에서만)
    if (!isEditMode) {
        const employeeNo = document.getElementById('employeeNo');
        if (employeeNo) {
            employeeNo.addEventListener('change', function() {
                if (this.value) {
                    document.getElementById('employeeNoError').textContent = '';
                    this.style.borderColor = '#ddd';
                }
            });
        }
    }
    
    // 페이지 로드 시 초기화
    console.log('Form loaded in mode:', window.mode);
});
