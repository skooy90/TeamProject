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
            String bomNo = request.getParameter("code");
            
            if (bomNo == null || bomNo.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/bom");
                return;
            }
            
            // BOM 상세 정보 조회
            BOMDTO bom = bomDAO.selectBOMByNo(bomNo);
            
            if (bom == null) {
                response.sendRedirect(request.getContextPath() + "/bom");
                return;
            }
            
            // BOM 관련 자재 목록 조회
            List<Map<String, Object>> materials = bomDAO.getBOMMaterials(bomNo);
            
            request.setAttribute("bom", bom);
            request.setAttribute("materials", materials);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "BOM 상세 조회 중 오류가 발생했습니다: " + e.getMessage());
        }
        
        // 페이지 이동
        RequestDispatcher dispatcher = request.getRequestDispatcher("/src/jsp/BOM/BOM_detail.jsp");
        dispatcher.forward(request, response);
    }
}