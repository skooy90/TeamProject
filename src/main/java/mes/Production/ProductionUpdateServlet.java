package com.mes.Controller.Production;

import com.mes.DAO.ProductionDAO;
import com.mes.DTO.ProductionDTO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.*;
import java.io.IOException;
import java.sql.Date;

@WebServlet("/production/update")
public class ProductionUpdateServlet extends HttpServlet {
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        ProductionDTO d = new ProductionDTO();
        d.setProductionNo(req.getParameter("productionNo")); // hidden
        d.setStandardCode(req.getParameter("standardCode"));
        d.setEmployeeNo(req.getParameter("employeeNo"));
        d.setPrStart(Date.valueOf(req.getParameter("prStart")));
        d.setPrEnd(Date.valueOf(req.getParameter("prEnd")));
        d.setPrTarget(Integer.parseInt(req.getParameter("prTarget")));
        d.setPrCompleted(Integer.parseInt(req.getParameter("prCompleted")));

        int ok = ProductionDAO.getInstance().update(d);
        resp.sendRedirect(req.getContextPath() + "/productionList" + (ok==1 ? "" : "?err=update"));
    }
}
