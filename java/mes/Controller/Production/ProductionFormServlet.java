package mes.Controller.Production;

import mes.DAO.ProductionDAO;
import mes.DAO.StandardDAO;
import mes.DTO.ProductionDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

//mes.Controller.Production.ProductionFormServlet
@WebServlet("/production/form")
public class ProductionFormServlet extends HttpServlet {
 @Override
 protected void doGet(HttpServletRequest req, HttpServletResponse resp)
         throws ServletException, IOException {

     req.setCharacterEncoding("UTF-8");

     // 제품코드 드롭다운용
     req.setAttribute("standards",
             StandardDAO.getInstance().selectAllStandards());

     // (필요하면 담당자 드롭다운도 같이)
     // req.setAttribute("users", UsersDAO.getInstance().selectAllSimple());

     req.getRequestDispatcher("/jsp/Product/production_form.jsp").forward(req, resp);
 }
}

