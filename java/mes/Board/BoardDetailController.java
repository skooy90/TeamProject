package mes.Controller.Board;

import mes.DAO.BoardManagementDAO;
import mes.DTO.BoardDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/boardDetail")
public class BoardDetailController extends HttpServlet {
    private BoardManagementDAO boardDAO = new BoardManagementDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
    	request.setCharacterEncoding("UTF-8");
    	response.setContentType("text/html; charset=utf-8");
        
        String postNo = request.getParameter("postNo");
        
        if (postNo == null || postNo.trim().isEmpty()) {
            request.setAttribute("error", "게시글 번호가 올바르지 않습니다.");
            request.getRequestDispatcher("/jsp/BOARD/board_list.jsp").forward(request, response);
            return;
        }
        
        try {
            BoardDTO board = boardDAO.selectBoardByPostNo(postNo);
            
            if (board != null) {
                request.setAttribute("board", board);
                request.getRequestDispatcher("/jsp/BOARD/board_detail.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "게시글을 찾을 수 없습니다.");
                request.getRequestDispatcher("/jsp/BOARD/board_list.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "게시글을 불러오는 중 오류가 발생했습니다.");
            request.getRequestDispatcher("/jsp/BOARD/board_list.jsp").forward(request, response);
        }
    }
}


