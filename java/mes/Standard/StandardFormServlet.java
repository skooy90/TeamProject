// mes.Controller.Standard.StandardFormServlet
package mes.Controller.Standard;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/standard/form")
public class StandardFormServlet extends HttpServlet {
  @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    // 등록/수정 공용 폼으로 포워드
    req.getRequestDispatcher("/jsp/Standard/Standard.jsp").forward(req, resp);
  }
}
