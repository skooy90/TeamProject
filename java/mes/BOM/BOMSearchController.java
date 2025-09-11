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

@WebServlet("/bom/search")
public class BOMSearchController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private BOMDAO bomDAO;
    
    public BOMSearchController() {
        super();
        bomDAO = new BOMDAO();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 한글깨짐방지
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8;");
        
        try {
            String searchType = request.getParameter("searchType");
            String searchKeyword = request.getParameter("searchKeyword");
            
            List<BOMDTO> bomList;
            
            if (searchKeyword == null || searchKeyword.trim().isEmpty()) {
                // 검색어가 없으면 전체 목록
                bomList = bomDAO.selectRoutingBOMs();
            } else if ("all".equals(searchType)) {
                // "all" 선택 + 검색어 있으면 모든 필드에서 검색
                bomList = bomDAO.searchRoutingBOMs(searchKeyword);
            } else {
                // "code" 또는 "name" 선택 + 검색어 있으면 해당 타입으로 검색
                bomList = bomDAO.searchRoutingBOMsByType(searchType, searchKeyword);
            }
            
            // 통계 정보
            int totalCount = bomDAO.getBOMRoutingCount();
            int semiBOMCount = bomDAO.getBOMRoutingCountByType("SEMI");
            int finishBOMCount = bomDAO.getBOMRoutingCountByType("FINISH");
            
            request.setAttribute("bomList", bomList);
            request.setAttribute("totalCount", totalCount);
            request.setAttribute("semiBOMCount", semiBOMCount);
            request.setAttribute("finishBOMCount", finishBOMCount);
            request.setAttribute("searchType", searchType);
            request.setAttribute("searchKeyword", searchKeyword);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "BOM 검색 중 오류가 발생했습니다: " + e.getMessage());
        }
        
        // 페이지 이동
        RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/BOM/Bom_list.jsp");
        dispatcher.forward(request, response);
    }
}