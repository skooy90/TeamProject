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
    @Override // ProductionInsertServlet.java (핵심만)
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductionDTO dto = new ProductionDTO();
        dto.setStandardCode(req.getParameter("standardCode"));
        dto.setEmployeeNo(req.getParameter("employeeNo"));
        dto.setPrStart(java.sql.Date.valueOf(req.getParameter("prStart")));
        dto.setPrEnd(java.sql.Date.valueOf(req.getParameter("prEnd")));
        dto.setPrTarget(Integer.parseInt(req.getParameter("prTarget")));
        dto.setPrCompleted(Integer.parseInt(req.getParameter("prCompleted")));

        try {
            int r = ProductionDAO.getInstance().insert(dto);
            resp.sendRedirect(req.getContextPath() + "/productionList");
        } catch (Exception e) {
            String msg;
            String code = e.getMessage();
            if ("STANDARD_CODE_NOT_FOUND".equals(code)) {
                msg = "존재하지 않는 제품코드입니다. 기준관리에서 등록된 제품코드를 선택해 주세요.";
            } else if ("EMPLOYEE_NO_NOT_FOUND".equals(code)) {
                msg = "존재하지 않는 담당자 사번입니다.";
            } else {
                msg = "등록 중 오류가 발생했습니다: " + e.getMessage();
            }
            req.setAttribute("error", msg);
            req.setAttribute("prod", dto); // 사용자가 입력한 값 유지
            req.getRequestDispatcher("/jsp/Product//production_form.jsp").forward(req, resp);
        }
    }
}


