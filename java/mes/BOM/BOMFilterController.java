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
                // 전체 라우팅 목록
                bomList = bomDAO.selectRoutingBOMs();
            } else {
                // 라우팅 타입별 필터링
                bomList = bomDAO.selectRoutingBOMs();
                // 클라이언트 사이드에서 필터링 (JSP에서 처리)
            }
            
            // 통계 정보
            int totalCount = bomDAO.getBOMRoutingCount();
            int semiBOMCount = bomDAO.getBOMRoutingCountByType("SEMI");
            int finishBOMCount = bomDAO.getBOMRoutingCountByType("FINISH");
            
            request.setAttribute("bomList", bomList);
            request.setAttribute("totalCount", totalCount);
            request.setAttribute("semiBOMCount", semiBOMCount);
            request.setAttribute("finishBOMCount", finishBOMCount);
            request.setAttribute("filterType", type);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "BOM 필터링 중 오류가 발생했습니다: " + e.getMessage());
        }
        
        
        // 페이지 이동
        RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/BOM/Bom_list.jsp");
        dispatcher.forward(request, response);
    }
}
