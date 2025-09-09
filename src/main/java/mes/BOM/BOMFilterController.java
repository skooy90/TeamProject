package mes.BOM;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mes.DTO.BOMDTO;

@WebServlet("/bom/filter")
public class BOMFilterController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private BOMDAO bomDAO;
    
    public BOMFilterController() {
        super();
        bomDAO = new BOMDAO();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 한글깨짐방지
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8;");
        
        try {
            String type = request.getParameter("type");
            
            List<BOMDTO> bomList;
            
            if (type == null || type.trim().isEmpty() || "all".equals(type)) {
                // 전체 목록
                bomList = bomDAO.selectAllBOMs();
            } else {
                // 제품 유형별 필터링
                bomList = bomDAO.selectBOMsByType(type);
            }
            
            // 통계 정보
            int totalCount = bomDAO.getTotalBOMCount();
            int rawBOMCount = bomDAO.getBOMCountByType("RAW");
            int semiBOMCount = bomDAO.getBOMCountByType("SEMI");
            int finishBOMCount = bomDAO.getBOMCountByType("FINISH");
            int totalMaterialCount = bomDAO.getTotalMaterialCount();
            
            request.setAttribute("bomList", bomList);
            request.setAttribute("totalCount", totalCount);
            request.setAttribute("rawBOMCount", rawBOMCount);
            request.setAttribute("semiBOMCount", semiBOMCount);
            request.setAttribute("finishBOMCount", finishBOMCount);
            request.setAttribute("totalMaterialCount", totalMaterialCount);
            request.setAttribute("filterType", type);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "BOM 필터링 중 오류가 발생했습니다: " + e.getMessage());
        }
        
        // 페이지 이동
        RequestDispatcher dispatcher = request.getRequestDispatcher("/src/jsp/BOM/Bom_list.jsp");
        dispatcher.forward(request, response);
    }
}
