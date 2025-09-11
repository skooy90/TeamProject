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

    private Timestamp ts(String v) {
        // null/공백이면 null
        if (v == null || v.isBlank()) return null;
        v = v.trim().replace('T', ' '); // "2025-01-17T16:30" -> "2025-01-17 16:30"
        if (v.length() == 16) v += ":00"; // 초가 없으면 보강 -> "2025-01-17 16:30:00"
        return Timestamp.valueOf(v);      // 이제 JDBC 규격과 일치
    }

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        WorkDTO d = new WorkDTO();
        d.setWorkNo(req.getParameter("workNo"));
        d.setProductionNo(req.getParameter("productionNo"));
        d.setStandardCode(req.getParameter("standardCode"));
        d.setEmployeeNo(req.getParameter("employeeNo"));

        // date input 은 yyyy-MM-dd
        String sch = req.getParameter("woSchedule");
        d.setWoSchedule((sch == null || sch.isBlank()) ? null : Date.valueOf(sch));

        // 숫자 파라미터 기본값 처리
        String qty = req.getParameter("woQuantity");
        d.setWoQuantity((qty == null || qty.isBlank()) ? 0 : Integer.parseInt(qty));

        d.setWoStatus(req.getParameter("woStatus"));

        String comp = req.getParameter("woCompleted");
        d.setWoCompleted((comp == null || comp.isBlank()) ? 0 : Integer.parseInt(comp));

        // datetime-local -> Timestamp (null 허용)
        d.setWoStart(ts(req.getParameter("woStart")));
        d.setWoEnd(ts(req.getParameter("woEnd")));

        int updated = WorkDAO.getInstance().update(d);
        resp.sendRedirect(req.getContextPath() + "/workList"); // 목록 URL에 맞게
    }
}