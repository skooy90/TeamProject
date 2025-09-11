// 등록
// src/main/java/com/mes/Controller/Quality/QualityInsertServlet.java
package mes.Controller.Quality;

import mes.DAO.QualityDAO;
import mes.DTO.QualityDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;

@WebServlet("/quality/insert")
public class QualityInsertServlet extends HttpServlet {

    /** datetime-local("yyyy-MM-ddTHH:mm") -> JDBC Timestamp("yyyy-MM-dd HH:mm:ss") */
    private Timestamp toTs(String v) {
        if (v == null || v.isBlank()) return null;
        v = v.trim().replace('T', ' ');  // 2025-01-17T16:30 -> 2025-01-17 16:30
        if (v.length() == 16) v += ":00"; // 초가 없으면 보강
        return Timestamp.valueOf(v);
    }

    /** 정수 안전 파싱 (빈값이면 0) */
    private int toInt(String v) {
        if (v == null || v.isBlank()) return 0;
        return Integer.parseInt(v);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        QualityDTO d = new QualityDTO();
        d.setWorkNo(req.getParameter("workNo"));
        d.setStandardCode(req.getParameter("standardCode"));
        d.setEmployeeNo(req.getParameter("employeeNo"));
        d.setQuResult(req.getParameter("quResult"));
        d.setQuQuantity(toInt(req.getParameter("quQuantity")));
        d.setDefectQuantity(toInt(req.getParameter("defectQuantity")));

        // date input 은 yyyy-MM-dd 형식
        String manu = req.getParameter("quManufactureDate");
        d.setQuManufactureDate((manu == null || manu.isBlank()) ? null : Date.valueOf(manu));

        // datetime-local -> Timestamp
        d.setInspectionDate(toTs(req.getParameter("inspectionDate")));

        int r = QualityDAO.getInstance().insert(d);
        resp.sendRedirect(req.getContextPath() + "/qualityList");
    }
}