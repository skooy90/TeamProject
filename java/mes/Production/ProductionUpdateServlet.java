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

  // 수정 폼 표시 (GET)
  @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    req.setCharacterEncoding("UTF-8");
    String no = req.getParameter("no");
    if (no == null || no.isBlank()) {
      resp.sendRedirect(req.getContextPath()+"/productionList?err=bad_param");
      return;
    }

    var prod = ProductionDAO.getInstance().findByNo(no);
    if (prod == null) {
      resp.sendRedirect(req.getContextPath()+"/productionList?err=not_found");
      return;
    }

    req.setAttribute("prod", prod);
    // 폼에서 제품코드 드롭다운을 쓰면 필요
    // req.setAttribute("standards", StandardDAO.getInstance().findAll());

    req.getRequestDispatcher("/jsp/Product/production_form.jsp").forward(req, resp);
  }

  // 저장 처리 (POST)
  @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    req.setCharacterEncoding("UTF-8");

    ProductionDTO d = new ProductionDTO();
    d.setProductionNo(req.getParameter("productionNo"));
    d.setStandardCode(req.getParameter("standardCode"));
    d.setEmployeeNo(req.getParameter("employeeNo"));
    d.setPrStart(parseDate(req.getParameter("prStart")));
    d.setPrEnd(parseDate(req.getParameter("prEnd")));
    d.setPrTarget(parseIntSafe(req.getParameter("prTarget")));
    d.setPrCompleted(parseIntSafe(req.getParameter("prCompleted")));

    int ok = ProductionDAO.getInstance().update(d);
    resp.sendRedirect(req.getContextPath()+"/productionList"+(ok==1 ? "" : "?err=update"));
  }

  private java.sql.Date parseDate(String v){
    try { return (v==null||v.isBlank())?null:java.sql.Date.valueOf(v); }
    catch(Exception e){ return null; }
  }
  private int parseIntSafe(String v){
    try { return (v==null||v.isBlank())?0:Integer.parseInt(v); }
    catch(Exception e){ return 0; }
  }
}