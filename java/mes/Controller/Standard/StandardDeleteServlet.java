// src/main/java/com/mes/Controller/StandardDeleteServlet.java
package mes.Controller.Standard;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import mes.DAO.StandardDAO;

import java.io.IOException;

//com.mes.Controller.Standard.StandardDeleteServlet
@WebServlet("/deleteStandard")
public class StandardDeleteServlet extends HttpServlet {
 @Override
 protected void doGet(HttpServletRequest req, HttpServletResponse resp)
         throws IOException {
     String code = req.getParameter("code");
     int affected = StandardDAO.getInstance().delete(code); // ← 여기!

     // 목록으로 리다이렉트
     resp.sendRedirect(req.getContextPath() + "/standardList");
 }
}
