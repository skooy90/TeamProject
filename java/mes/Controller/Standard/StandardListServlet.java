package mes.Controller.Standard;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mes.DAO.StandardDAO;
import mes.DTO.StandardDTO;

// The URL pattern to access this servlet

@WebServlet("/standardList")
public class StandardListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String q = request.getParameter("q");
        int size = 10;                              // 페이지당 10건
        int page = 1;
        try { page = Integer.parseInt(request.getParameter("page")); } catch (Exception ignored) {}
        if (page < 1) page = 1;

        StandardDAO dao = StandardDAO.getInstance();
        int total = dao.count(q);
        int totalPages = (int)Math.ceil(total / (double)size);
        if (totalPages == 0) totalPages = 1;
        if (page > totalPages) page = totalPages;

        // 페이지 목록
        List<StandardDTO> list = dao.findPage(q, page, size);

        // 페이지 블록(1~5, 6~10 ...)
        int blockSize = 5;
        int startPage = ((page - 1) / blockSize) * blockSize + 1;
        int endPage = Math.min(startPage + blockSize - 1, totalPages);

        request.setAttribute("standardList", list);
        request.setAttribute("q", q == null ? "" : q.trim());
        request.setAttribute("page", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("total", total);
        request.setAttribute("startPage", startPage);
        request.setAttribute("endPage", endPage);
        request.setAttribute("size", size);

        request.getRequestDispatcher("/jsp/Standard/Standard_list.jsp").forward(request, response);
    }
}