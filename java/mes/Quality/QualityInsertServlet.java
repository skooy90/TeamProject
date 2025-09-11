// 등록
// src/main/java/com/mes/Controller/Quality/QualityInsertServlet.java
package mes.Controller.Quality;

import mes.DAO.QualityDAO;
import mes.DAO.WorkDAO;
import mes.DTO.QualityDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;

@WebServlet("/quality/insert")
public class QualityInsertServlet extends HttpServlet {
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        QualityDTO d = new QualityDTO();
        d.setWorkNo(req.getParameter("workNo"));
        String stCode = req.getParameter("standardCode"); // hidden
        if (stCode == null || stCode.isBlank()) {
            // ✅ hidden이 비어오면 workNo로 보강
            stCode = WorkDAO.getInstance().findStandardCodeByWorkNo(d.getWorkNo());
        }
        if (stCode == null || stCode.isBlank()) {
            // 그래도 없으면 폼으로 돌려보내며 안내
            req.setAttribute("error", "STANDARD_CODE가 비었습니다. 작업번호를 다시 선택해 주세요.");
            req.getRequestDispatcher("/quality/form").forward(req, resp);
            return;
        }
        d.setStandardCode(stCode);

        d.setEmployeeNo(req.getParameter("employeeNo"));
        d.setQuResult(req.getParameter("quResult"));
        d.setQuQuantity(Integer.parseInt(req.getParameter("quQuantity")));
        d.setDefectQuantity(Integer.parseInt(req.getParameter("defectQuantity")));

        String ins = req.getParameter("inspectionDate");
        if (ins != null && !ins.isBlank()) {
            ins = ins.trim().replace('T',' ');
            if (ins.length() == 16) ins += ":00";
            d.setInspectionDate(java.sql.Timestamp.valueOf(ins));
        }

        int r = QualityDAO.getInstance().insert(d);
        resp.sendRedirect(req.getContextPath() + "/qualityList");
    }
}