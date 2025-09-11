package mes.PROCESS;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/process/delete")
public class ProcessDeleteController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private ProcessDAO processDAO;
    
    public ProcessDeleteController() {
        super();
        processDAO = new ProcessDAO();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 한글깨짐방지
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8;");
        
        try {
            String standardCode = request.getParameter("code");
            
            if (standardCode == null || standardCode.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/process");
                return;
            }
            
            // 해당 라우팅의 모든 공정 삭제
            int result = processDAO.deleteProcessesByStandardCode(standardCode);
            
            if (result > 0) {
                response.sendRedirect(request.getContextPath() + "/process");
            } else {
                response.sendRedirect(request.getContextPath() + "/process");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/process");
        }
    }
}
