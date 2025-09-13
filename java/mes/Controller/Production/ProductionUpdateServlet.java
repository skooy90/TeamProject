package mes.Controller.Production;

import mes.DAO.ProductionDAO;
import mes.DTO.ProductionDTO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.*;
import java.io.IOException;
import java.sql.Date;

@WebServlet("/production/update")
public class ProductionUpdateServlet extends HttpServlet {

    // 1) 수정 폼 표시 (GET)
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String no = req.getParameter("no");
        if (no == null || no.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/productionList?err=bad_param");
            return;
        }

        var prod = ProductionDAO.getInstance().findByNo(no);
        if (prod == null) {
            resp.sendRedirect(req.getContextPath() + "/productionList?err=not_found");
            return;
        }

        req.setAttribute("prod", prod);
        // 수정 폼 JSP로 forward (아래 폼에서 action="/production/update" method="post")
        req.getRequestDispatcher("/jsp/Product/production_form.jsp").forward(req, resp);
    }

    // 2) 저장 처리 (POST)
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        ProductionDTO d = new ProductionDTO();
        d.setProductionNo(req.getParameter("productionNo"));  // hidden
        d.setStandardCode(req.getParameter("standardCode"));
        d.setEmployeeNo(req.getParameter("employeeNo"));
        d.setPrStart(java.sql.Date.valueOf(req.getParameter("prStart")));
        d.setPrEnd(java.sql.Date.valueOf(req.getParameter("prEnd")));
        d.setPrTarget(Integer.parseInt(req.getParameter("prTarget")));
        d.setPrCompleted(Integer.parseInt(req.getParameter("prCompleted")));

        int ok = ProductionDAO.getInstance().update(d);
        resp.sendRedirect(req.getContextPath() + "/productionList" + (ok == 1 ? "" : "?err=update"));
        
    }
}