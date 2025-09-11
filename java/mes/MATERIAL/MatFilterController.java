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

@WebServlet("/material/filter")
public class MatFilterController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private MatDAO matDAO;
    
    public MatFilterController() {
        super();
        matDAO = new MatDAO();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 한글깨짐방지
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8;");
        
        try {
            String type = request.getParameter("type");
            String tab = request.getParameter("tab");
            
            // 탭 파라미터 처리 (기본값: total)
            if (tab == null || tab.isEmpty()) {
                tab = "total";
            }
            
            // 한글 파라미터를 DB 값으로 변환
            if ("원자재".equals(type)) {
                type = "RAW";
            } else if ("반제품".equals(type)) {
                type = "SEMI";
            } else if ("완제품".equals(type)) {
                type = "FINISH";
            }
            
            List<MaterialDTO> lotList;
            List<MaterialDTO> totalList;
            
            if (type == null || type.trim().isEmpty() || "all".equals(type)) {
                // 전체 목록
                lotList = matDAO.selectMaterialsByLot();
                totalList = matDAO.selectMaterialsTotalByProduct();
            } else {
                // 타입별 필터링
                lotList = matDAO.selectMaterialsByLot();
                totalList = matDAO.selectMaterialsTotalByProduct();
                
                // 클라이언트 사이드에서 필터링 (JSP에서 처리)
                // lotList와 totalList에서 해당 타입만 필터링
            }
            
            // 통계 정보 (새로운 메서드 사용)
            int totalCount = matDAO.getTotalLotCount();
            int lowStockCount = matDAO.getLowStockLotCount(50);
            int rawMaterialCount = matDAO.getLotCountByType("RAW");
            int semiProductCount = matDAO.getLotCountByType("SEMI");
            int finishedProductCount = matDAO.getLotCountByType("FINISH");
            
            // JSP로 데이터 전달
            request.setAttribute("lotList", lotList);
            request.setAttribute("totalList", totalList);
            request.setAttribute("activeTab", tab);
            request.setAttribute("totalCount", totalCount);
            request.setAttribute("lowStockCount", lowStockCount);
            request.setAttribute("rawMaterialCount", rawMaterialCount);
            request.setAttribute("semiProductCount", semiProductCount);
            request.setAttribute("finishedProductCount", finishedProductCount);
            request.setAttribute("filterType", type);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "필터링 중 오류가 발생했습니다: " + e.getMessage());
        }
        
        // 페이지 이동
        RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/MATERIAL/Mat_list.jsp");
        dispatcher.forward(request, response);
    }
}
