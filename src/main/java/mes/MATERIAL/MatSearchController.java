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
            
            List<MaterialDTO> materialList;
            
            if (searchKeyword == null || searchKeyword.trim().isEmpty()) {
                // 검색어가 없으면 전체 목록
                materialList = matDAO.selectAllMaterials();
            } else if ("all".equals(searchType)) {
                // "all" 선택 + 검색어 있으면 모든 필드에서 검색
                materialList = matDAO.searchAllFields(searchKeyword);
            } else {
                // "code" 또는 "name" 선택 + 검색어 있으면 해당 타입으로 검색
                materialList = matDAO.searchMaterials(searchType, searchKeyword);
            }
            
            // 통계 정보
            int totalCount = matDAO.getTotalMaterialCount();
            int lowStockCount = matDAO.getLowStockCount(50);
            int rawMaterialCount = matDAO.getMaterialCountByType("RAW");
            int semiProductCount = matDAO.getMaterialCountByType("SEMI");
            int finishedProductCount = matDAO.getMaterialCountByType("FINISH");
            
            request.setAttribute("materialList", materialList);
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
        RequestDispatcher dispatcher = request.getRequestDispatcher("/src/jsp/MATERIAL/Mat_list.jsp");
        dispatcher.forward(request, response);
    }
}
