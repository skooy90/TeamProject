package mes.Controller.Work;

import mes.DAO.WorkDAO;
import mes.DTO.WorkDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;

@WebServlet("/work/update")
public class WorkUpdateServlet extends HttpServlet {

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        WorkDTO d = new WorkDTO();
        d.setWorkNo(req.getParameter("workNo"));
        d.setProductionNo(req.getParameter("productionNo"));
        d.setStandardCode(req.getParameter("standardCode"));
//        d.setEmployeeNo(req.getParameter("employeeNo"));

        // date input 은 yyyy-MM-dd
        String sch = req.getParameter("woSchedule");
        if (sch != null && !sch.isBlank()) {
            d.setWoSchedule(java.sql.Date.valueOf(sch));
        }
        String qty = req.getParameter("woQuantity");
        d.setWoQuantity((qty == null || qty.isBlank()) ? 0 : Integer.parseInt(qty));
        d.setWoStatus(req.getParameter("woStatus"));

        String comp = req.getParameter("woCompleted");
        d.setWoCompleted((comp == null || comp.isBlank()) ? 0 : Integer.parseInt(comp));

        d.setWoStart(ts(req.getParameter("woStart")));
        d.setWoEnd(ts(req.getParameter("woEnd")));

        int updated = WorkDAO.getInstance().update(d);
        resp.sendRedirect(req.getContextPath() + "/workList");
    }

    private java.sql.Timestamp ts(String v){
        if (v == null || v.isBlank()) return null;
        v = v.replace('T', ' ');
        if (v.length() == 16) v += ":00";
        int dot = v.indexOf('.');
        if (dot > 0) v = v.substring(0, dot);
        return java.sql.Timestamp.valueOf(v);
    }
}