package mes.MATERIAL;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/material/delete")
public class MatDeleteController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private MatDAO matDAO;
    
    public MatDeleteController() {
        super();
        matDAO = new MatDAO();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 한글깨짐방지
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8;");
        
        try {
            String materialCode = request.getParameter("code");
            
            if (materialCode == null || materialCode.trim().isEmpty()) {
                request.getSession().setAttribute("error", "재고코드가 필요합니다.");
                response.sendRedirect(request.getContextPath() + "/material");
                return;
            }
            
            int result = matDAO.deleteMaterial(materialCode);
            
            if (result > 0) {
                request.getSession().setAttribute("success", "재고가 성공적으로 삭제되었습니다.");
            } else {
                request.getSession().setAttribute("error", "재고 삭제에 실패했습니다.");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "재고 삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
        
        response.sendRedirect(request.getContextPath() + "/material");
    }
}
