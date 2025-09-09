// Chart.js 그래프 초기화
document.addEventListener('DOMContentLoaded', function() {
    const ctx = document.getElementById('comparisonChart');
    if (ctx) {
        const chart = new Chart(ctx.getContext('2d'), {
            type: 'bar',
            data: {
                labels: ['MA0001', 'MA0002', 'MA0003', 'MA0004', 'MA0005'],
                datasets: [{
                    label: '재고량 (kg)',
                    data: [25, 45, 30, 60, 40],
                    backgroundColor: [
                        '#dc3545', // 부족 (빨강)
                        '#007bff', // 정상 (파랑)
                        '#28a745', // 정상 (초록)
                        '#ffc107', // 정상 (노랑)
                        '#6c757d'  // 정상 (회색)
                    ],
                    borderColor: [
                        '#dc3545',
                        '#007bff',
                        '#28a745',
                        '#ffc107',
                        '#6c757d'
                    ],
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    title: {
                        display: true,
                        text: '같은 제품의 다른 재고 비교',
                        font: {
                            size: 16,
                            weight: 'bold'
                        }
                    },
                    legend: {
                        display: false
                    },
                    tooltip: {
                        callbacks: {
                            afterLabel: function(context) {
                                const labels = ['신선 오이 (부족)', '신선 오이 (정상)', '신선 오이 (정상)', '신선 오이 (정상)', '신선 오이 (정상)'];
                                return labels[context.dataIndex];
                            }
                        }
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        title: {
                            display: true,
                            text: '재고량 (kg)'
                        }
                    },
                    x: {
                        title: {
                            display: true,
                            text: '재고 코드'
                        }
                    }
                }
            }
        });
    }
});

// 삭제 확인
function deleteMaterial(code) {
    if (confirm('정말로 이 재고를 삭제하시겠습니까?\n\n삭제된 재고는 복구할 수 없습니다.')) {
        window.location.href = window.contextPath + '/material/delete?code=' + code;
    }
}

// 페이지 로드 시 초기화
window.onload = function() {
    console.log('재고 상세 페이지가 로드되었습니다.');
};
