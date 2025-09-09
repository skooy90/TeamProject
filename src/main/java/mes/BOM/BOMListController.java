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
            // BOM 목록 조회
            List<BOMDTO> bomList = bomDAO.selectAllBOMs();
            
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
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "BOM 목록 조회 중 오류가 발생했습니다: " + e.getMessage());
        }
        
        // 페이지 이동
        RequestDispatcher dispatcher = request.getRequestDispatcher("/src/jsp/BOM/Bom_list.jsp");
        dispatcher.forward(request, response);
    }
}
