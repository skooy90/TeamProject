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

@WebServlet("/bom")
public class BOMListController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private BOMDAO bomDAO;
    
    public BOMListController() {
        super();
        bomDAO = new BOMDAO();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 한글깨짐방지
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8;");
        
        try {
            // 라우팅별 BOM 목록 조회
            List<BOMDTO> bomList = bomDAO.selectRoutingBOMs();
            
            // 통계 정보
            int totalCount = bomDAO.getBOMRoutingCount();
            int semiBOMCount = bomDAO.getBOMRoutingCountByType("SEMI");
            int finishBOMCount = bomDAO.getBOMRoutingCountByType("FINISH");
            
            request.setAttribute("bomList", bomList);
            request.setAttribute("totalCount", totalCount);
            request.setAttribute("semiBOMCount", semiBOMCount);
            request.setAttribute("finishBOMCount", finishBOMCount);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "BOM 목록 조회 중 오류가 발생했습니다: " + e.getMessage());
        }
        
        // 페이지 이동
        RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/BOM/Bom_list.jsp");
        dispatcher.forward(request, response);
    }
}