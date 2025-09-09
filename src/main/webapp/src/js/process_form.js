// 공정 등록/수정 폼 JavaScript

document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('processForm');
    const isEditMode = '${mode}' === 'update';
    
    // 폼 제출 이벤트
    if (form) {
        form.addEventListener('submit', function(e) {
            if (!validateForm()) {
                e.preventDefault();
                return false;
            }
            
            // 로딩 상태 표시
            const submitBtn = form.querySelector('button[type="submit"]');
            submitBtn.classList.add('loading');
            submitBtn.disabled = true;
        });
    }
    
    // 실시간 유효성 검사
    const inputs = form.querySelectorAll('input, select, textarea');
    inputs.forEach(input => {
        input.addEventListener('blur', function() {
            validateField(this);
        });
        
        input.addEventListener('input', function() {
            clearFieldError(this);
        });
    });
    
    // 공정 순서 입력 시 안내 메시지
    const prOrderInput = document.getElementById('prOrder');
    if (prOrderInput) {
        prOrderInput.addEventListener('input', function() {
            const value = parseInt(this.value);
            const orderInfo = document.querySelector('.order-info');
            
            if (value >= 1 && value <= 4) {
                orderInfo.style.borderColor = '#ffc107';
                orderInfo.style.backgroundColor = '#fff3cd';
            } else if (value >= 5 && value <= 8) {
                orderInfo.style.borderColor = '#28a745';
                orderInfo.style.backgroundColor = '#d4edda';
            } else {
                orderInfo.style.borderColor = '#bbdefb';
                orderInfo.style.backgroundColor = '#e3f2fd';
            }
        });
    }
    
    // 제품코드 선택 시 공정 순서 자동 설정
    const standardCodeSelect = document.getElementById('standardCode');
    const prOrderInput = document.getElementById('prOrder');
    
    if (standardCodeSelect && prOrderInput && !isEditMode) {
        standardCodeSelect.addEventListener('change', function() {
            const selectedOption = this.options[this.selectedIndex];
            const standardCode = selectedOption.value;
            
            if (standardCode) {
                // 제품 유형에 따라 기본 순서 설정
                if (standardCode.startsWith('SE')) {
                    prOrderInput.value = '1';
                } else if (standardCode.startsWith('FI')) {
                    prOrderInput.value = '5';
                }
            }
        });
    }
});

// 폼 유효성 검사
function validateForm() {
    let isValid = true;
    
    const requiredFields = [
        { id: 'standardCode', message: '제품코드를 선택해주세요.' },
        { id: 'prDescription', message: '공정 설명을 입력해주세요.' },
        { id: 'prType', message: '공정 유형을 선택해주세요.' },
        { id: 'prOrder', message: '공정 순서를 입력해주세요.' }
    ];
    
    requiredFields.forEach(field => {
        const element = document.getElementById(field.id);
        if (element && !validateField(element, field.message)) {
            isValid = false;
        }
    });
    
    return isValid;
}

// 개별 필드 유효성 검사
function validateField(field, customMessage = null) {
    const value = field.value.trim();
    const fieldGroup = field.closest('.form-group');
    const errorElement = fieldGroup.querySelector('.error-message');
    
    // 필수 필드 검사
    if (field.hasAttribute('required') && !value) {
        showFieldError(field, customMessage || '필수 입력 항목입니다.');
        return false;
    }
    
    // 공정 순서 검사
    if (field.id === 'prOrder' && value) {
        const order = parseInt(value);
        if (isNaN(order) || order < 1 || order > 8) {
            showFieldError(field, '공정 순서는 1-8 사이의 숫자여야 합니다.');
            return false;
        }
    }
    
    // 공정 설명 길이 검사
    if (field.id === 'prDescription' && value && value.length < 5) {
        showFieldError(field, '공정 설명은 5자 이상 입력해주세요.');
        return false;
    }
    
    // 공정 설명 길이 검사 (최대)
    if (field.id === 'prDescription' && value && value.length > 500) {
        showFieldError(field, '공정 설명은 500자 이하로 입력해주세요.');
        return false;
    }
    
    clearFieldError(field);
    return true;
}

// 필드 에러 표시
function showFieldError(field, message) {
    const fieldGroup = field.closest('.form-group');
    const errorElement = fieldGroup.querySelector('.error-message');
    
    fieldGroup.classList.add('error');
    errorElement.textContent = message;
    errorElement.style.display = 'block';
}

// 필드 에러 제거
function clearFieldError(field) {
    const fieldGroup = field.closest('.form-group');
    const errorElement = fieldGroup.querySelector('.error-message');
    
    fieldGroup.classList.remove('error');
    errorElement.style.display = 'none';
}

// 공정 이미지 파일명 검증
function validateImageFileName(fileName) {
    if (!fileName) return true; // 선택사항이므로 빈 값 허용
    
    const validExtensions = ['.png', '.jpg', '.jpeg', '.gif', '.bmp'];
    const hasValidExtension = validExtensions.some(ext => 
        fileName.toLowerCase().endsWith(ext)
    );
    
    if (!hasValidExtension) {
        return '이미지 파일은 png, jpg, jpeg, gif, bmp 형식만 허용됩니다.';
    }
    
    return null;
}

// 폼 리셋
function resetForm() {
    const form = document.getElementById('processForm');
    if (form) {
        form.reset();
        
        // 에러 상태 초기화
        const errorGroups = form.querySelectorAll('.form-group.error');
        errorGroups.forEach(group => {
            group.classList.remove('error');
            const errorElement = group.querySelector('.error-message');
            errorElement.style.display = 'none';
        });
    }
}

// 취소 확인
function confirmCancel() {
    if (confirm('작성 중인 내용이 사라집니다. 정말 취소하시겠습니까?')) {
        window.location.href = '${pageContext.request.contextPath}/process';
    }
}