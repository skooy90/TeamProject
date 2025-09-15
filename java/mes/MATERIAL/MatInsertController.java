package mes.MATERIAL;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mes.DTO.MaterialDTO;

@WebServlet("/material/insert")
public class MatInsertController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private MatDAO matDAO;
    
    public MatInsertController() {
        super();
        matDAO = new MatDAO();
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 한글깨짐방지
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8;");
        
        try {
            // 파라미터 받기
            String qualityNo = request.getParameter("qualityNo");
            String employeeNo = request.getParameter("employeeNo");
            
            // 유효성 검사
            if (qualityNo == null || qualityNo.trim().isEmpty()) {
                request.getSession().setAttribute("error", "검사번호를 선택해주세요.");
                response.sendRedirect(request.getContextPath() + "/material/form");
                return;
            }
            
            if (employeeNo == null || employeeNo.trim().isEmpty()) {
                request.getSession().setAttribute("error", "담당자를 선택해주세요.");
                response.sendRedirect(request.getContextPath() + "/material/form");
                return;
            }
            
            // 품질관리 정보로 재고 등록
            int result = matDAO.insertMaterialFromQuality(qualityNo, employeeNo);
            
            String message;
            if (result > 0) {
                message = "재고가 성공적으로 등록되었습니다.";
            } else {
                message = "재고 등록에 실패했습니다. 해당 검사번호가 존재하지 않거나 이미 등록되었을 수 있습니다.";
            }
            
            // JavaScript로 알림창 띄우고 페이지 이동
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().println("<script>");
            response.getWriter().println("alert('" + message + "');");
            response.getWriter().println("window.location.href='" + request.getContextPath() + "/material';");
            response.getWriter().println("</script>");
            return;
            
        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = "재고 등록 중 오류가 발생했습니다: " + e.getMessage();
            
            // JavaScript로 오류 알림창 띄우고 페이지 이동
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().println("<script>");
            response.getWriter().println("alert('" + errorMessage + "');");
            response.getWriter().println("window.location.href='" + request.getContextPath() + "/material/form';");
            response.getWriter().println("</script>");
            return;
        }
    }
}