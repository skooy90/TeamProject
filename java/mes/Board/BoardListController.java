package mes.Controller.Board;

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

@WebServlet("/boardList")
public class BoardListController extends HttpServlet {
    private BoardManagementDAO boardDAO = new BoardManagementDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        
        try {
            // 파라미터 받기
            String searchType = request.getParameter("searchType");
            String keyword = request.getParameter("keyword");
            String category = request.getParameter("category");
            String page = request.getParameter("page");
            
            // 페이지 번호 설정 (기본값: 1)
            int currentPage = 1;
            if (page != null && !page.trim().isEmpty()) {
                try {
                    currentPage = Integer.parseInt(page);
                } catch (NumberFormatException e) {
                    currentPage = 1;
                }
            }
            
            List<BoardDTO> boardList = null;
            
            // 검색 조건에 따른 게시글 조회
            if (keyword != null && !keyword.trim().isEmpty()) {
                if (searchType == null || searchType.isEmpty()) {
                    searchType = "all";
                }
                boardList = boardDAO.searchBoards(searchType, keyword);
            } else if (category != null && !category.isEmpty()) {
                boardList = boardDAO.selectBoardsByCategory(category);
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
            request.setAttribute("currentPage", currentPage);
            
            // JSP로 포워드
            request.getRequestDispatcher("/jsp/BOARD/board_list.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "게시판 목록을 불러오는 중 오류가 발생했습니다.");
            request.getRequestDispatcher("/jsp/BOARD/board_list.jsp").forward(request, response);
        }
    }
}


