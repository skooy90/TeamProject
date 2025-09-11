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

@WebServlet("/material/search")
public class MatSearchController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private MatDAO matDAO;
    
    public MatSearchController() {
        super();
        matDAO = new MatDAO();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 한글깨짐방지
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8;");
        
        try {
            String searchType = request.getParameter("searchType");
            String searchKeyword = request.getParameter("searchKeyword");
            String tab = request.getParameter("tab");
            
            // 탭 파라미터 처리 (기본값: total)
            if (tab == null || tab.isEmpty()) {
                tab = "total";
            }
            
            List<MaterialDTO> lotList;
            List<MaterialDTO> totalList;
            
            if (searchKeyword == null || searchKeyword.trim().isEmpty()) {
                // 검색어가 없으면 전체 목록
                lotList = matDAO.selectMaterialsByLot();
                totalList = matDAO.selectMaterialsTotalByProduct();
            } else if ("all".equals(searchType)) {
                // "all" 선택 + 검색어 있으면 모든 필드에서 검색
                lotList = matDAO.searchMaterialsByLot(searchKeyword);
                totalList = matDAO.searchMaterialsTotalByProduct(searchKeyword);
            } else {
                // "code" 또는 "name" 선택 + 검색어 있으면 해당 타입으로 검색
                lotList = matDAO.searchMaterialsByLotByType(searchType, searchKeyword);
                totalList = matDAO.searchMaterialsTotalByProductByType(searchType, searchKeyword);
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
            request.setAttribute("searchType", searchType);
            request.setAttribute("searchKeyword", searchKeyword);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "검색 중 오류가 발생했습니다: " + e.getMessage());
        }
        
        // 페이지 이동
        RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/MATERIAL/Mat_list.jsp");
        dispatcher.forward(request, response);
    }
}