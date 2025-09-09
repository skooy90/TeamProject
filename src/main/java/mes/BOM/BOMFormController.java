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
import mes.DTO.StandardDTO;

@WebServlet("/bom/form")
public class BOMFormController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private BOMDAO bomDAO;
    
    public BOMFormController() {
        super();
        bomDAO = new BOMDAO();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 한글깨짐방지
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8;");
        
        try {
            String bomCodeParam = request.getParameter("code");
            boolean isEditMode = (bomCodeParam != null && !bomCodeParam.isEmpty());
            
            BOMDTO bom = null;
            List<Map<String, Object>> materials = null;
            
            if (isEditMode) {
                // 수정 모드: 기존 BOM 데이터 조회
                bom = bomDAO.selectBOMByNo(bomCodeParam);
                if (bom == null) {
                    response.sendRedirect(request.getContextPath() + "/bom");
                    return;
                }
                // 해당 BOM의 자재 목록 조회
                materials = bomDAO.getBOMMaterials(bomCodeParam);
            }
            
            // 제품 목록 조회 (드롭다운용)
            List<StandardDTO> standards = bomDAO.selectAllStandards();
            
            // 모든 자재 목록 조회 (드롭다운용)
            List<Map<String, Object>> allMaterials = bomDAO.getBOMMaterials(null);
            
            request.setAttribute("bom", bom);
            request.setAttribute("materials", materials);
            request.setAttribute("standards", standards);
            request.setAttribute("allMaterials", allMaterials);
            request.setAttribute("mode", isEditMode ? "update" : "insert");
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "BOM 폼 로드 중 오류가 발생했습니다: " + e.getMessage());
        }
        
        // 페이지 이동
        RequestDispatcher dispatcher = request.getRequestDispatcher("/src/jsp/BOM/Bom_form.jsp");
        dispatcher.forward(request, response);
    }
}