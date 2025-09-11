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
            String standardCodeParam = request.getParameter("code");
            boolean isEditMode = (standardCodeParam != null && !standardCodeParam.isEmpty());
            
            BOMDTO bom = null;
            List<BOMDTO> bomSteps = null;
            
            if (isEditMode) {
                // 수정 모드: 기존 라우팅 데이터 조회
                bomSteps = bomDAO.selectBOMStepsByRouting(standardCodeParam);
                if (bomSteps == null || bomSteps.isEmpty()) {
                    response.sendRedirect(request.getContextPath() + "/bom");
                    return;
                }
                // 첫 번째 공정을 대표로 사용
                bom = bomSteps.get(0);
            }
            
            // 라우팅용 제품 목록 조회 (SEMI, FINISH 타입만)
            List<StandardDTO> standards = bomDAO.selectStandardsByType("SEMI,FINISH");
            
            request.setAttribute("bom", bom);
            request.setAttribute("bomSteps", bomSteps);
            request.setAttribute("standards", standards);
            request.setAttribute("mode", isEditMode ? "update" : "insert");
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "BOM 폼 로드 중 오류가 발생했습니다: " + e.getMessage());
        }
        
        // 페이지 이동
        RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/BOM/Bom_form.jsp");
        dispatcher.forward(request, response);
    }
}
