package com.mes.Controller.Production;

//package com.mes.Controller.Production;

import com.mes.DAO.ProductionDAO;
import com.mes.DTO.ProductionDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/production/form")
public class ProductionFormServlet extends HttpServlet {
 @Override
 protected void doGet(HttpServletRequest req, HttpServletResponse resp)
         throws ServletException, IOException {

     String no = req.getParameter("no");
     if (no != null && !no.isBlank()) {
         ProductionDTO prod = ProductionDAO.getInstance().findByNo(no);
         req.setAttribute("prod", prod);  // 수정 폼에 바인딩
     }
     req.getRequestDispatcher("/production_form.jsp").forward(req, resp);
 }
}
