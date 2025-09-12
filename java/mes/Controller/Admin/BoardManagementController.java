package mes.Controller.Admin;

import mes.DAO.BoardManagementDAO;
import mes.DTO.BoardDTO;
import mes.DAO.BoardManagementDAO.BoardStatistics;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/boards")
public class BoardManagementController extends HttpServlet {
    private BoardManagementDAO boardDAO = new BoardManagementDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            // 파라미터 받기
            String searchType = request.getParameter("searchType");
            String keyword = request.getParameter("keyword");
            String category = request.getParameter("category");
            String writer = request.getParameter("writer");
            
            List<BoardDTO> boardList = null;
            
            // 검색 조건에 따른 게시글 조회
            if (keyword != null && !keyword.trim().isEmpty()) {
                if (searchType == null || searchType.isEmpty()) {
                    searchType = "all";
                }
                boardList = boardDAO.searchBoards(searchType, keyword);
            } else if (category != null && !category.isEmpty()) {
                boardList = boardDAO.selectBoardsByCategory(category);
            } else if (writer != null && !writer.isEmpty()) {
                boardList = boardDAO.selectBoardsByWriter(writer);
            } else {
                boardList = boardDAO.selectAllBoards();
            }
            
            // 통계 정보 조회
            BoardStatistics stats = boardDAO.getBoardStatistics();
            
            // JSP로 전달할 데이터 설정
            request.setAttribute("boardList", boardList);
            request.setAttribute("stats", stats);
            request.setAttribute("searchType", searchType);
            request.setAttribute("keyword", keyword);
            request.setAttribute("category", category);
            request.setAttribute("writer", writer);
            
            // JSP로 포워드
            request.getRequestDispatcher("/jsp/ADMIN/board_management.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "게시판 목록을 불러오는 중 오류가 발생했습니다.");
            request.getRequestDispatcher("/jsp/ADMIN/board_management.jsp").forward(request, response);
        }
    }
}

