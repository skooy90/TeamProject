package mes.Controller.Work;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mes.DAO.ProductionDAO;
import mes.DAO.WorkDAO;
import mes.DTO.WorkDTO;

//WorkFormServlet.java  (신규/수정 공용 폼 진입)
@WebServlet("/work/form")
public class WorkFormServlet extends HttpServlet {
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setAttribute("productions", ProductionDAO.getInstance().findForWorkForm());
        // 신규 작업번호 미리보기(선택): req.setAttribute("nextWorkNo", WorkDAO.getInstance().peekNextNo());

        req.getRequestDispatcher("/jsp/WORK/work_form.jsp").forward(req, resp);
    }
}
