package mes.Controller.Work;

import mes.DAO.ProductionDAO;
import mes.DAO.WorkDAO;
import mes.DTO.WorkDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/work/update")
public class WorkUpdateServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String workNo = req.getParameter("workNo");
        if (workNo == null || workNo.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/workList?err=bad_param");
            return;
        }

        // 1) 수정 전 행을 조회 (생산번호 등 고정값을 안전하게 가져오기 위함)
        WorkDTO before = WorkDAO.getInstance().findByNo(workNo);
        if (before == null) {
            resp.sendRedirect(req.getContextPath() + "/workList?err=not_found");
            return;
        }

        // 2) 업데이트할 DTO 구성
        WorkDTO d = new WorkDTO();
        d.setWorkNo(workNo);

        // 생산번호/표준코드는 보통 수정 불가이므로 'before'에서 유지 (보안/무결성)
        d.setProductionNo(before.getProductionNo());
        d.setStandardCode(before.getStandardCode());

        // 담당자 사번도 수정 불가라면 유지 (필요하면 주석 해제)
        // d.setEmployeeNo(before.getEmployeeNo());

        // 일정
        String sch = req.getParameter("woSchedule");  // yyyy-MM-dd
        if (sch != null && !sch.isBlank()) {
            d.setWoSchedule(java.sql.Date.valueOf(sch));
        } else {
            d.setWoSchedule(before.getWoSchedule());
        }

        // 작업량(보통 생산목표량과 동기화된 읽기전용) – 수정 불가라면 before 유지
        String qty = req.getParameter("woQuantity");
        if (qty == null || qty.isBlank()) {
            d.setWoQuantity(before.getWoQuantity());
        } else {
            d.setWoQuantity(Integer.parseInt(qty));
        }

        d.setWoStatus(req.getParameter("woStatus"));

        // 완료량(사용자가 수정)
        String comp = req.getParameter("woCompleted");
        d.setWoCompleted((comp == null || comp.isBlank()) ? 0 : Integer.parseInt(comp));

        // 시작/종료 (datetime-local)
        d.setWoStart(parseTs(req.getParameter("woStart")));
        d.setWoEnd(parseTs(req.getParameter("woEnd")));

        // 3) 업데이트 실행
        int ok = WorkDAO.getInstance().update(d);

        // 4) 성공 시: 해당 생산건의 완료량을 "합계로 재설정"
        if (ok == 1) {
            String productionNo = before.getProductionNo(); // 안전하게 before에서
            int sum = WorkDAO.getInstance().sumCompletedByProduction(productionNo);
            ProductionDAO.getInstance().setCompleted(productionNo, sum);
            resp.sendRedirect(req.getContextPath() + "/workList");
        } else {
            resp.sendRedirect(req.getContextPath() + "/workList?err=update");
        }
    }

    /** "yyyy-MM-dd'T'HH:mm" 또는 "yyyy-MM-dd'T'HH:mm:ss" 를 Timestamp로 */
    private java.sql.Timestamp parseTs(String v) {
        if (v == null || v.isBlank()) return null;
        v = v.replace('T', ' ');
        if (v.length() == 16) v += ":00";          // 초가 없으면 보정
        int dot = v.indexOf('.');
        if (dot > 0) v = v.substring(0, dot);      // 소수점 이하 제거
        return java.sql.Timestamp.valueOf(v);
    }
}