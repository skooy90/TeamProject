// src/main/java/com/mes/Controller/StandardUpdateServlet.java
package com.mes.Controller.Standard;

import com.mes.DAO.StandardDAO;
import com.mes.DTO.StandardDTO;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/standard/update")
public class StandardUpdateServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String code = req.getParameter("productCode");
        String name = req.getParameter("productName");
        String type = req.getParameter("productType");
        String unit = req.getParameter("unit");
        String qtyStr = req.getParameter("quantity");

        int qty = 0;
        try { qty = Integer.parseInt(qtyStr); } catch (Exception ignore) {}

        StandardDTO d = new StandardDTO();
        d.setStandardCode(code);
        d.setStName(name);
        d.setStType(type);
        d.setStUnit(unit);
        d.setStQuantity(qty);

        StandardDAO.getInstance().update(d);

        resp.sendRedirect(req.getContextPath() + "/standardList");
    }
}
