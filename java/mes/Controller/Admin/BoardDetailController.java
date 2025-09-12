package mes.Controller.Admin;

import mes.DAO.BoardManagementDAO;
import mes.DTO.BoardDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/boards/detail")
public class BoardDetailController extends HttpServlet {
    private BoardManagementDAO boardDAO = new BoardManagementDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String postNo = request.getParameter("postNo");
        
        if (postNo == null || postNo.trim().isEmpty()) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"success\": false, \"message\": \"게시글 번호가 올바르지 않습니다.\"}");
            return;
        }
        
        try {
            BoardDTO board = boardDAO.selectBoardByPostNo(postNo);
            
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            
            if (board != null) {
                response.getWriter().write("{\"success\": true, \"board\": {" +
                    "\"postNo\": \"" + board.getPostNo() + "\"," +
                    "\"boWriter\": \"" + board.getBoWriter() + "\"," +
                    "\"boCategory\": \"" + board.getBoCategory() + "\"," +
                    "\"boCreationDate\": \"" + board.getBoCreationDate() + "\"," +
                    "\"boTitle\": \"" + board.getBoTitle() + "\"," +
                    "\"boContent\": \"" + board.getBoContent().replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r") + "\"" +
                    "}}");
            } else {
                response.getWriter().write("{\"success\": false, \"message\": \"게시글을 찾을 수 없습니다.\"}");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"success\": false, \"message\": \"게시글 정보를 불러오는 중 오류가 발생했습니다.\"}");
        }
    }
}

