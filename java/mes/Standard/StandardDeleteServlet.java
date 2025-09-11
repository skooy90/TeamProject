// src/main/java/com/mes/Controller/StandardDeleteServlet.java
package mes.Controller.Standard;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import mes.DAO.StandardDAO;

import java.io.IOException;

//com.mes.Controller.Standard.StandardDeleteServlet
@WebServlet("/standard/delete")
public class StandardDeleteServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String code = req.getParameter("code");
    String ctx  = req.getContextPath();

    if (code == null || code.isBlank()) {
      resp.sendRedirect(ctx + "/standardList?msg=bad_param");
      return;
    }

    int affected;
    try {
      affected = StandardDAO.getInstance().delete(code);
    } catch (Exception e) {
      e.printStackTrace();
      resp.sendRedirect(ctx + "/standardList?msg=error");
      return;
    }

    resp.sendRedirect(ctx + "/standardList?msg=" + (affected > 0 ? "deleted" : "not_deleted"));
  }
}

