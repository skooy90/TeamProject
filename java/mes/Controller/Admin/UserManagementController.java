package mes.Controller.Admin;

import mes.DAO.UserManagementDAO;
import mes.DTO.UsersDTO;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/users")
public class UserManagementController extends HttpServlet {
    private UserManagementDAO userDAO = new UserManagementDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        
        try {
            String action = request.getParameter("action");
            List<UsersDTO> userList = null;
            
            if ("search".equals(action)) {
                // 검색 기능
                String searchType = request.getParameter("searchType");
                String keyword = request.getParameter("keyword");
                
                if (searchType != null && keyword != null && !keyword.trim().isEmpty()) {
                    userList = userDAO.searchUsers(searchType, keyword);
                    request.setAttribute("searchType", searchType);
                    request.setAttribute("keyword", keyword);
                } else {
                    userList = userDAO.selectAllUsers();
                }
            } else if ("filter".equals(action)) {
                // 필터 기능
                String authority = request.getParameter("authority");
                String position = request.getParameter("position");
                String passwordStatus = request.getParameter("passwordStatus");
                
                if (authority != null && !authority.isEmpty()) {
                    userList = userDAO.selectUsersByAuthority(authority);
                    request.setAttribute("filterAuthority", authority);
                } else if (position != null && !position.isEmpty()) {
                    userList = userDAO.selectUsersByPosition(position);
                    request.setAttribute("filterPosition", position);
                } else {
                    userList = userDAO.selectAllUsers();
                }
            } else {
                // 기본 목록 조회
                userList = userDAO.selectAllUsers();
            }
            
            // 통계 정보 조회
            Map<String, Integer> statistics = userDAO.getUserStatistics();
            
            request.setAttribute("userList", userList);
            request.setAttribute("statistics", statistics);
            
            // JSP로 포워드
            RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/ADMIN/user_management.jsp");
            dispatcher.forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "사용자 목록 조회 중 오류가 발생했습니다: " + e.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/ADMIN/user_management.jsp");
            dispatcher.forward(request, response);
        }
    }
}

