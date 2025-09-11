package mes.MATERIAL;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mes.DTO.MaterialDTO;

@WebServlet("/material")
public class MatListController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private MatDAO matDAO;
    
    public MatListController() {
        super();
        matDAO = new MatDAO();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 한글깨짐방지
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8;");
        
        // 탭 파라미터 처리 (기본값: total)
        String tab = request.getParameter("tab");
        if (tab == null || tab.isEmpty()) {
            tab = "total";
        }
        
        try {
            // LOT별 재고 조회 (담당자 포함)
            List<MaterialDTO> lotList = matDAO.selectMaterialsByLot();
            
            // 제품별 총수량 조회
            List<MaterialDTO> totalList = matDAO.selectMaterialsTotalByProduct();
            
            // JSP로 데이터 전달
            request.setAttribute("lotList", lotList);
            request.setAttribute("totalList", totalList);
            request.setAttribute("activeTab", tab);
            
            // 통계 정보 계산 (새로운 메서드 사용)
            int totalCount = matDAO.getTotalLotCount();
            int lowStockCount = matDAO.getLowStockLotCount(50); // 임계값 50
            int rawMaterialCount = matDAO.getLotCountByType("RAW");
            int semiProductCount = matDAO.getLotCountByType("SEMI");
            int finishedProductCount = matDAO.getLotCountByType("FINISH");
            
            // 통계 정보 JSP로 전달
            request.setAttribute("totalCount", totalCount);
            request.setAttribute("lowStockCount", lowStockCount);
            request.setAttribute("rawMaterialCount", rawMaterialCount);
            request.setAttribute("semiProductCount", semiProductCount);
            request.setAttribute("finishedProductCount", finishedProductCount);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "재고 목록 조회 중 오류가 발생했습니다: " + e.getMessage());
        }
        
        // 페이지 이동
        RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/MATERIAL/Mat_list.jsp");
        dispatcher.forward(request, response);
    }
}