package mes.BOM;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mes.DTO.BOMDTO;

@WebServlet("/bom/update")
public class BOMUpdateController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private BOMDAO bomDAO;
    
    public BOMUpdateController() {
        super();
        bomDAO = new BOMDAO();
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 한글깨짐방지
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8;");
        
        try {
            // 폼 데이터 받기
            String bomNo = request.getParameter("bomNo");
            String standardCode = request.getParameter("standardCode");
            String bomDescription = request.getParameter("bomDescription");
            String bomType = request.getParameter("bomType");
            String bomOrderStr = request.getParameter("bomOrder");
            String bomImage = request.getParameter("bomImage");
            
            // 유효성 검사
            if (bomNo == null || bomNo.trim().isEmpty() ||
                standardCode == null || standardCode.trim().isEmpty() ||
                bomDescription == null || bomDescription.trim().isEmpty() ||
                bomType == null || bomType.trim().isEmpty() ||
                bomOrderStr == null || bomOrderStr.trim().isEmpty()) {
                
                response.sendRedirect(request.getContextPath() + "/bom/form?code=" + bomNo);
                return;
            }
            
            int bomOrder;
            try {
                bomOrder = Integer.parseInt(bomOrderStr);
                if (bomOrder < 1) {
                    response.sendRedirect(request.getContextPath() + "/bom/form?code=" + bomNo);
                    return;
                }
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/bom/form?code=" + bomNo);
                return;
            }
            
            // BOMDTO 생성
            BOMDTO bom = new BOMDTO();
            bom.setBomNo(bomNo);
            bom.setStandardCode(standardCode);
            bom.setBomDescription(bomDescription);
            bom.setBomType(bomType);
            bom.setBomOrder(bomOrder);
            bom.setBomImage(bomImage != null ? bomImage : "default.png");
            
            // DB에 수정
            int result = bomDAO.updateBOM(bom);
            
            if (result > 0) {
                response.sendRedirect(request.getContextPath() + "/bom");
            } else {
                response.sendRedirect(request.getContextPath() + "/bom/form?code=" + bomNo);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/bom");
        }
    }
}
