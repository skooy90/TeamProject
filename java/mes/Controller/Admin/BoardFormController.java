package mes.Controller.Admin;

import mes.DAO.BoardManagementDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/boards/form")
public class BoardFormController extends HttpServlet {
    private BoardManagementDAO boardDAO = new BoardManagementDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            // 다음 게시글 번호 생성
            String nextPostNo = boardDAO.getNextPostNo();
            
            // JSP로 전달할 데이터 설정
            request.setAttribute("nextPostNo", nextPostNo);
            
            // JSP로 포워드
            request.getRequestDispatcher("/jsp/ADMIN/board_form.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "게시글 등록 폼을 불러오는 중 오류가 발생했습니다.");
            request.getRequestDispatcher("/jsp/ADMIN/board_form.jsp").forward(request, response);
        }
    }
}

