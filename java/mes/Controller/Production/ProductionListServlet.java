package mes.Controller.Production;

import mes.DAO.ProductionDAO;
import mes.DTO.ProductionDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/productionList")
public class ProductionListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        String q = req.getParameter("q");

        ProductionDAO dao = ProductionDAO.getInstance();
        List<ProductionDTO> list = (q != null && q.trim().length() >= 2)
                ? dao.searchByPrefix(q.trim())
                : dao.findAll();

        req.setAttribute("productionList", list);
        req.setAttribute("q", q == null ? "" : q.trim());

        // 목록 JSP 경로에 맞춰 수정
        req.getRequestDispatcher("/jsp/Product/production_list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}