package com.mes.Controller.Production;

import com.mes.DAO.ProductionDAO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.*;
import java.io.IOException;

@WebServlet("/production/delete")
public class ProductionDeleteServlet extends HttpServlet {
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String no = req.getParameter("no");
        // FK가 ON DELETE CASCADE면 delete()로 충분
        int ok = ProductionDAO.getInstance().delete(no);
        // 필요 시: int ok = ProductionDAO.getInstance().deleteCascade(no);

        resp.sendRedirect(req.getContextPath() + "/productionList" + (ok==1 ? "" : "?err=delete"));
    }
}
