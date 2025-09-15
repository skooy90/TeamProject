package mes.Controller.Production;

import mes.DAO.ProductionDAO;
import mes.DTO.ProductionDTO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.*;
import java.io.IOException;
import java.sql.Date;

@WebServlet("/production/insert")
public class ProductionInsertServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        ProductionDTO d = new ProductionDTO();
        d.setStandardCode(req.getParameter("standardCode"));
        d.setEmployeeNo(req.getParameter("employeeNo"));

        // 날짜는 required라면 그대로, 선택 사항이면 isBlank 체크 후 null 허용
        d.setPrStart(java.sql.Date.valueOf(req.getParameter("prStart")));
        d.setPrEnd(java.sql.Date.valueOf(req.getParameter("prEnd")));

        // ✔ 안전 파싱
        d.setPrTarget(getInt(req, "prTarget", 0));
        d.setPrCompleted(getInt(req, "prCompleted", 0));

        try {
            int ok = ProductionDAO.getInstance().insert(d);
            resp.sendRedirect(req.getContextPath() + "/productionList" + (ok == 1 ? "" : "?err=insert"));
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/productionList?err=insert_exc");
        }
    }

    private int getInt(HttpServletRequest req, String name, int def) {
        String v = req.getParameter(name);
        if (v == null || v.isBlank()) return def;
        try {
            return Integer.parseInt(v.trim());
        } catch (NumberFormatException e) {
            return def;
        }
    }
}


