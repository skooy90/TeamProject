package mes.Controller.Work;

import mes.DAO.ProductionDAO;
import mes.DAO.WorkDAO;
import mes.DTO.WorkDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/work/insert")
public class WorkInsertServlet extends HttpServlet {

    private static java.sql.Timestamp toTimestamp(String v) {
        if (v == null) return null;
        v = v.trim();
        if (v.isEmpty()) return null;
        v = v.replace('T', ' ');
        if (v.length() == 16) v += ":00";         // yyyy-MM-dd HH:mm
        int dot = v.indexOf('.');
        if (dot > 0) v = v.substring(0, dot);
        return java.sql.Timestamp.valueOf(v);
    }

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        // 필수 파라미터
        String productionNo  = req.getParameter("productionNo");
        String standardCode  = req.getParameter("standardCode"); // (폼에서 안주면 아래에서 역조회)
        String employeeNo    = req.getParameter("employeeNo");

        String woScheduleStr = req.getParameter("woSchedule");
        String woStatus      = req.getParameter("woStatus");
        String woCompletedStr= req.getParameter("woCompleted");
        String woStartStr    = req.getParameter("woStart");
        String woEndStr      = req.getParameter("woEnd");

        // 1) 작업량 = 생산목표량
        Integer target = ProductionDAO.getInstance().getTargetByNo(productionNo);
        int woQuantity = (target != null ? target : 0);

        // 2) standardCode 비었으면 생산번호로 역조회(선택)
        if (standardCode == null || standardCode.isBlank()) {
            try {
                standardCode = ProductionDAO.getInstance()
                        .getStandardCodeByNo(productionNo); // 아래 3) 참고
            } catch (Exception ignore) {}
        }

        // 3) DTO 채우기
        WorkDTO d = new WorkDTO();
        d.setProductionNo(productionNo);
        d.setStandardCode(standardCode);
        d.setEmployeeNo(employeeNo);

        if (woScheduleStr != null && !woScheduleStr.isBlank()) {
            d.setWoSchedule(java.sql.Date.valueOf(woScheduleStr)); // yyyy-MM-dd
        }
        d.setWoQuantity(woQuantity);                                // ★ 생산목표량으로 고정
        d.setWoStatus(woStatus);
        d.setWoCompleted((woCompletedStr == null || woCompletedStr.isBlank())
                ? 0 : Integer.parseInt(woCompletedStr));           // 사용자가 입력

        d.setWoStart(toTimestamp(woStartStr));                      // yyyy-MM-ddTHH:mm
        d.setWoEnd(toTimestamp(woEndStr));

        // 4) 저장 + 생산완료량 동기화
        int r = WorkDAO.getInstance().insert(d);                    // insert → int 반환
        if (r > 0) {
            // 작업 저장 성공 → 생산완료량 = 작업완료량
            ProductionDAO.getInstance().updateCompleted(
                    productionNo, d.getWoCompleted());

            resp.sendRedirect(req.getContextPath() + "/workList");
            return;
        }

        // 실패
        req.setAttribute("error", "작업 등록 중 오류가 발생했습니다.");
        req.getRequestDispatcher("/jsp/Work/work_form.jsp").forward(req, resp);
    }
}


