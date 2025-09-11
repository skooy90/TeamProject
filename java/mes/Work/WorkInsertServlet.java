package mes.Controller.Work;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mes.DAO.WorkDAO;
import mes.DTO.WorkDTO;
@WebServlet("/work/insert")
public class WorkInsertServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        // 파라미터 이름은 JSP와 맞춰 주세요(아래는 흔한 예시 + 대체 키 지원)
        String workNo        = req.getParameter("workNo");             // (보통 시퀀스라면 null 허용)
        String productionNo  = req.getParameter("productionNo");
        String standardCode  = req.getParameter("standardCode");
        String employeeNo    = req.getParameter("employeeNo");
        String woScheduleStr = req.getParameter("woSchedule");         // <input type="date">
        String woQtyStr      = req.getParameter("woQuantity");
        String woStatus      = req.getParameter("woStatus");
        String woCompletedStr= req.getParameter("woCompleted");
        String woStartStr    = req.getParameter("woStart");            // <input type="datetime-local">
        String woEndStr      = req.getParameter("woEnd");              // <input type="datetime-local">

        // 혹시 다른 이름이라면 대체 키도 읽기(선택)
        if (woStartStr == null) woStartStr = req.getParameter("start"); 
        if (woEndStr   == null) woEndStr   = req.getParameter("end");

        mes.DTO.WorkDTO d = new mes.DTO.WorkDTO();
        d.setWorkNo(workNo);
        d.setProductionNo(productionNo);
        d.setStandardCode(standardCode);
        d.setEmployeeNo(employeeNo);

        // Date(yyyy-MM-dd)
        if (woScheduleStr != null && !woScheduleStr.isBlank()) {
            d.setWoSchedule(java.sql.Date.valueOf(woScheduleStr));
        }

        // 숫자
        d.setWoQuantity(woQtyStr == null || woQtyStr.isBlank() ? 0 : Integer.parseInt(woQtyStr));
        d.setWoStatus(woStatus);
        d.setWoCompleted(woCompletedStr == null || woCompletedStr.isBlank() ? 0 : Integer.parseInt(woCompletedStr));

        // Timestamp(yyyy-MM-ddTHH:mm → yyyy-MM-dd HH:mm:ss 로 보정)
        d.setWoStart(toTimestamp(woStartStr));
        d.setWoEnd(toTimestamp(woEndStr));

        // DAO 저장
        boolean ok = mes.DAO.WorkDAO.getInstance().insert(d);
        if (ok) {
            resp.sendRedirect(req.getContextPath() + "/workList");
        } else {
            req.setAttribute("error", "작업 등록 중 오류가 발생했습니다.");
            req.getRequestDispatcher("/jsp/Work/work_form.jsp").forward(req, resp);
        }
    }

    private java.sql.Timestamp toTimestamp(String v) {
        if (v == null) return null;
        v = v.trim();
        if (v.isEmpty()) return null;
        v = v.replace('T', ' ');
        if (v.length() == 16) v += ":00";
        int dot = v.indexOf('.');
        if (dot > 0) v = v.substring(0, dot);
        return java.sql.Timestamp.valueOf(v);
    }
}



