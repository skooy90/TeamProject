package mes.Controller.Work;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mes.DAO.WorkDAO;



@WebServlet("/work/delete")
public class WorkDeleteServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String no = req.getParameter("no");
        WorkDAO.getInstance().delete(no);
        resp.sendRedirect(req.getContextPath() + "/workList");
    }
}
