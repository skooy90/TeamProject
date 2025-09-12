package mes.Controller.Board;

import mes.DAO.BoardManagementDAO;
import mes.DTO.BoardDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/boardAction")
public class BoardActionController extends HttpServlet {
    private BoardManagementDAO boardDAO = new BoardManagementDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        try {
            switch (action) {
                case "write":
                    writeBoard(request, response);
                    break;
                case "update":
                    updateBoard(request, response);
                    break;
                case "delete":
                    deleteBoard(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/boardList");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "처리 중 오류가 발생했습니다.");
            response.sendRedirect(request.getContextPath() + "/boardList");
        }
    }

    private void writeBoard(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String postNo = request.getParameter("postNo");
        String employeeNo = request.getParameter("employeeNo");
        String boCategory = request.getParameter("boCategory");
        String boTitle = request.getParameter("boTitle");
        String boContent = request.getParameter("boContent");
        String boWriter = request.getParameter("boWriter");
        
        // 유효성 검사
        if (boTitle == null || boTitle.trim().isEmpty() ||
            boContent == null || boContent.trim().isEmpty() ||
            boCategory == null || boCategory.trim().isEmpty()) {
            
            request.getSession().setAttribute("error", "필수 항목을 모두 입력해주세요.");
            response.sendRedirect(request.getContextPath() + "/boardWrite");
            return;
        }
        
        BoardDTO board = new BoardDTO();
        board.setPostNo(postNo);
        board.setEmployeeNo(employeeNo);
        board.setBoCategory(boCategory);
        board.setBoTitle(boTitle);
        board.setBoContent(boContent);
        board.setBoWriter(boWriter);
        
        int result = boardDAO.insertBoard(board);
        
        if (result > 0) {
            request.getSession().setAttribute("success", "게시글이 등록되었습니다.");
            response.sendRedirect(request.getContextPath() + "/boardList");
        } else {
            request.getSession().setAttribute("error", "게시글 등록에 실패했습니다.");
            response.sendRedirect(request.getContextPath() + "/boardWrite");
        }
    }

    private void updateBoard(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String postNo = request.getParameter("postNo");
        String boCategory = request.getParameter("boCategory");
        String boTitle = request.getParameter("boTitle");
        String boContent = request.getParameter("boContent");
        String boWriter = request.getParameter("boWriter");
        
        // 유효성 검사
        if (boTitle == null || boTitle.trim().isEmpty() ||
            boContent == null || boContent.trim().isEmpty() ||
            boCategory == null || boCategory.trim().isEmpty()) {
            
            request.getSession().setAttribute("error", "필수 항목을 모두 입력해주세요.");
            response.sendRedirect(request.getContextPath() + "/boardDetail?postNo=" + postNo);
            return;
        }
        
        BoardDTO board = new BoardDTO();
        board.setPostNo(postNo);
        board.setBoCategory(boCategory);
        board.setBoTitle(boTitle);
        board.setBoContent(boContent);
        board.setBoWriter(boWriter);
        
        int result = boardDAO.updateBoard(board);
        
        if (result > 0) {
            request.getSession().setAttribute("success", "게시글이 수정되었습니다.");
        } else {
            request.getSession().setAttribute("error", "게시글 수정에 실패했습니다.");
        }
        
        response.sendRedirect(request.getContextPath() + "/boardDetail?postNo=" + postNo);
    }

    private void deleteBoard(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String postNo = request.getParameter("postNo");
        
        if (postNo == null || postNo.trim().isEmpty()) {
            request.getSession().setAttribute("error", "게시글 번호가 올바르지 않습니다.");
            response.sendRedirect(request.getContextPath() + "/boardList");
            return;
        }
        
        int result = boardDAO.deleteBoard(postNo);
        
        if (result > 0) {
            request.getSession().setAttribute("success", "게시글이 삭제되었습니다.");
        } else {
            request.getSession().setAttribute("error", "게시글 삭제에 실패했습니다.");
        }
        
        response.sendRedirect(request.getContextPath() + "/boardList");
    }
}
