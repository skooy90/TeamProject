package mes.Controller.Quality;

import mes.DAO.QualityDAO;
import mes.DTO.QualityDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;

@WebServlet("/quality/update")
public class QualityUpdateServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        QualityDTO d = new QualityDTO();
        d.setQualityNo(req.getParameter("qualityNo"));
        d.setWorkNo(req.getParameter("workNo"));
        d.setStandardCode(req.getParameter("standardCode"));
        d.setEmployeeNo(req.getParameter("employeeNo"));
        d.setQuResult(req.getParameter("quResult"));

        // 숫자 파싱(빈값/잘못된 값은 0)
        d.setQuQuantity(parseIntSafe(req.getParameter("quQuantity")));
        d.setDefectQuantity(parseIntSafe(req.getParameter("defectQuantity")));

        // 제조일: input type="date" -> yyyy-MM-dd (빈값이면 null)
        d.setQuManufactureDate(parseSqlDateSafe(req.getParameter("quManufactureDate")));

        // 검사일시: input type="datetime-local" -> yyyy-MM-ddTHH:mm (빈값이면 null)
        d.setInspectionDate(parseTimestampSafe(req.getParameter("inspectionDate")));

        int ok = QualityDAO.getInstance().update(d);

        resp.sendRedirect(req.getContextPath() + "/qualityList" + (ok == 1 ? "" : "?err=update"));
    }

    private int parseIntSafe(String s) {
        try {
            if (s == null || s.isBlank()) return 0;
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    private Date parseSqlDateSafe(String s) {
        try {
            if (s == null || s.isBlank()) return null; // 빈값이면 null
            return Date.valueOf(s.trim());             // yyyy-MM-dd만 허용
        } catch (Exception e) {
            return null;
        }
    }

    private Timestamp parseTimestampSafe(String s) {
        try {
            if (s == null || s.isBlank()) return null; // 빈값이면 null
            s = s.trim().replace('T', ' ');            // yyyy-MM-dd HH:mm
            if (s.length() == 16) s += ":00";          // 초가 없으면 보정
            int dot = s.indexOf('.');
            if (dot > 0) s = s.substring(0, dot);      // 소수점(밀리초) 제거
            return Timestamp.valueOf(s);               // yyyy-MM-dd HH:mm[:ss]
        } catch (Exception e) {
            return null;
        }
    }
}