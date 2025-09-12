package mes.Controller.Admin;

import mes.DAO.UserManagementDAO;
import mes.DTO.UsersDTO;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/admin/users/action")
public class UserActionController extends HttpServlet {
    private UserManagementDAO userDAO = new UserManagementDAO();
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        
        String action = request.getParameter("action");
        
        try {
            if ("create".equals(action)) {
                // 사용자 등록
                createUser(request, response);
            } else if ("update".equals(action)) {
                // 사용자 수정
                updateUser(request, response);
            } else if ("resetPassword".equals(action)) {
                // 비밀번호 초기화
                resetPassword(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "작업 중 오류가 발생했습니다: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/admin/users");
        }
    }
    
    private void createUser(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String employeeNo = request.getParameter("employeeNo");
        String usName = request.getParameter("usName");
        String usPosition = request.getParameter("usPosition");
        String usAuthority = request.getParameter("usAuthority");
        String usPassword = request.getParameter("usPassword");
        
        // 유효성 검사
        if (usName == null || usName.trim().isEmpty()) {
            request.getSession().setAttribute("error", "이름을 입력해주세요.");
            response.sendRedirect(request.getContextPath() + "/admin/users/form");
            return;
        }
        
        if (usPosition == null || usPosition.trim().isEmpty()) {
            request.getSession().setAttribute("error", "직급을 선택해주세요.");
            response.sendRedirect(request.getContextPath() + "/admin/users/form");
            return;
        }
        
        if (usAuthority == null || usAuthority.trim().isEmpty()) {
            request.getSession().setAttribute("error", "권한을 선택해주세요.");
            response.sendRedirect(request.getContextPath() + "/admin/users/form");
            return;
        }
        
        // 기본 비밀번호 설정 (사원번호로)
        if (usPassword == null || usPassword.trim().isEmpty()) {
            usPassword = employeeNo;  // 사원번호를 기본 비밀번호로 설정
        }
        
        UsersDTO user = new UsersDTO();
        user.setEmployeeNo(employeeNo);
        user.setUsName(usName);
        user.setUsPosition(usPosition);
        user.setUsAuthority(usAuthority);
        user.setUsPassword(usPassword);
        user.setUsPsUpStatus(0); // 비밀번호 변경 필요
        
        int result = userDAO.insertUser(user);
        
        if (result > 0) {
            request.getSession().setAttribute("success", "사용자가 성공적으로 등록되었습니다.");
        } else {
            request.getSession().setAttribute("error", "사용자 등록에 실패했습니다.");
        }
        
        response.sendRedirect(request.getContextPath() + "/admin/users");
    }
    
    private void updateUser(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String employeeNo = request.getParameter("employeeNo");
        String usName = request.getParameter("usName");
        String usPosition = request.getParameter("usPosition");
        String usAuthority = request.getParameter("usAuthority");
        
        UsersDTO user = new UsersDTO();
        user.setEmployeeNo(employeeNo);
        user.setUsName(usName);
        user.setUsPosition(usPosition);
        user.setUsAuthority(usAuthority);
        
        int result = userDAO.updateUser(user);
        
        if (result > 0) {
            request.getSession().setAttribute("success", "사용자 정보가 성공적으로 수정되었습니다.");
        } else {
            request.getSession().setAttribute("error", "사용자 정보 수정에 실패했습니다.");
        }
        
        response.sendRedirect(request.getContextPath() + "/userList");
    }
    
    private void resetPassword(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String employeeNo = request.getParameter("employeeNo");
        
        int result = userDAO.resetPassword(employeeNo);
        
        if (result > 0) {
            request.getSession().setAttribute("success", "비밀번호가 초기화되었습니다. (새 비밀번호: " + employeeNo + ")");
        } else {
            request.getSession().setAttribute("error", "비밀번호 초기화에 실패했습니다.");
        }
        
        response.sendRedirect(request.getContextPath() + "/admin/users");
    }
}
