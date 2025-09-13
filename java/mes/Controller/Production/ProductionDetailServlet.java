package mes.Controller.Production;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mes.DAO.ProductionDAO;
import mes.DTO.ProductionDTO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.text.SimpleDateFormat;

@WebServlet("/production/detail")
public class ProductionDetailServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String no = req.getParameter("no");
        ProductionDTO prod = ProductionDAO.getInstance().findByNo(no);
        req.setAttribute("prod", prod);
        req.getRequestDispatcher("/jsp/Product/production_detail.jsp").forward(req, resp);
    }
}

