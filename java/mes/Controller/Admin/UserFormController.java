package mes.Controller.Admin;

import mes.DAO.UserManagementDAO;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/admin/users/form")
public class UserFormController extends HttpServlet {
    private UserManagementDAO userDAO = new UserManagementDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        
        try {
            // 다음 사원번호 생성 (K0001, K0002, ...)
            String nextEmployeeNo = generateNextEmployeeNo();
            request.setAttribute("nextEmployeeNo", nextEmployeeNo);
            
            // JSP로 포워드
            RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/ADMIN/user_form.jsp");
            dispatcher.forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "사용자 등록 폼 로드 중 오류가 발생했습니다: " + e.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/ADMIN/user_form.jsp");
            dispatcher.forward(request, response);
        }
    }
    
    /**
     * 다음 사원번호 생성 (K0001, K0002, ...)
     */
    private String generateNextEmployeeNo() {
        try {
            // 현재 최대 사원번호 조회
            String maxEmployeeNo = userDAO.getMaxEmployeeNo();
            
            if (maxEmployeeNo == null || maxEmployeeNo.isEmpty()) {
                return "K0001";
            }
            
            // K0001 -> 1, K0002 -> 2, ...
            String numberPart = maxEmployeeNo.substring(1);
            int nextNumber = Integer.parseInt(numberPart) + 1;
            
            // 4자리 숫자로 포맷팅
            return String.format("K%04d", nextNumber);
            
        } catch (Exception e) {
            e.printStackTrace();
            return "K0001";
        }
    }
}

