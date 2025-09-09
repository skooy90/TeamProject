// com.mes.Controller.Standard.StandardInsertServlet
package com.mes.Controller.Standard;

import com.mes.DAO.StandardDAO;
import com.mes.DTO.StandardDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/standard/insert")
public class StandardInsertServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        // 폼에서 넘어온 값
        String stType   = req.getParameter("productType");   // RAW/SEMI/FINISH
        String stName   = req.getParameter("productName");
        String quantity = req.getParameter("quantity");
        String unit     = req.getParameter("unit");           // L/kg/개

        StandardDTO d = new StandardDTO();
        d.setStType(stType);
        d.setStName(stName);
        d.setStQuantity(quantity == null || quantity.isEmpty() ? 0 : Integer.parseInt(quantity));
        d.setStUnit(unit);

        // 코드 자동 생성하여 INSERT
        String newCode = StandardDAO.getInstance().insertAuto(d);

        // 결과 처리
        if (newCode != null) {
            // 성공 → 목록으로
            resp.sendRedirect(req.getContextPath() + "/standardList");
        } else {
            // 실패 → 폼으로 안내(간단 처리)
            req.setAttribute("error", "등록에 실패했습니다.");
            req.getRequestDispatcher("/Standard.jsp").forward(req, resp);
        }
    }
}
