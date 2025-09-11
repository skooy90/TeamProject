package mes.BOM;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mes.DTO.BOMDTO;

@WebServlet("/bom/detail")
public class BOMDetailController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private BOMDAO bomDAO;
    
    public BOMDetailController() {
        super();
        bomDAO = new BOMDAO();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 한글깨짐방지
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8;");
        
        try {
            String standardCode = request.getParameter("code");
            
            if (standardCode == null || standardCode.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/bom");
                return;
            }
            
            // 라우팅의 공정별 자재 조회
            List<BOMDTO> bomSteps = bomDAO.selectBOMStepsByRouting(standardCode);
            
            if (bomSteps == null || bomSteps.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/bom");
                return;
            }
            
            // 첫 번째 공정을 대표로 사용 (라우팅 정보)
            BOMDTO bom = bomSteps.get(0);
            
            // 통계 정보
            int totalCount = bomDAO.getBOMRoutingCount();
            int semiBOMCount = bomDAO.getBOMRoutingCountByType("SEMI");
            int finishBOMCount = bomDAO.getBOMRoutingCountByType("FINISH");
            
            request.setAttribute("bom", bom);
            request.setAttribute("bomSteps", bomSteps);
            request.setAttribute("totalCount", totalCount);
            request.setAttribute("semiBOMCount", semiBOMCount);
            request.setAttribute("finishBOMCount", finishBOMCount);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "BOM 상세 조회 중 오류가 발생했습니다: " + e.getMessage());
        }
        
        // 페이지 이동
        RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/BOM/BOM_detail.jsp");
        dispatcher.forward(request, response);
    }
}