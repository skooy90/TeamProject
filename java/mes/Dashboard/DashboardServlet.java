// src/main/java/mes/Controller/Dashboard/DashboardServlet.java
package mes.Controller.Dashboard;

import mes.DAO.BoardManagementDAO;
import mes.DAO.ProductionDAO;
import mes.DAO.WorkDAO;
import mes.DAO.QualityDAO;
import mes.DTO.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.*;
import java.io.IOException;
import java.util.*;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
  @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    // 1) 생산: 최근 8개 생산 Target vs Completed
    req.setAttribute("prodTvc", ProductionDAO.getInstance().findLastNForChart(8));
    // 2) 금일 생산량(완료수량 합계)
    req.setAttribute("todayProduction", ProductionDAO.getInstance().sumCompletedToday());
    // 3) 작업: 최근 8개 작업 Qty vs Completed
    req.setAttribute("workQvc", WorkDAO.getInstance().findLastNForChart(8));
    // 4) 품질: 제품별 불량 수량 Top5
    req.setAttribute("defectTop", QualityDAO.getInstance().sumDefectByProductTopN(5));
    // 5) 품질: 제품별 불량률 Top5 (불량 / (양품+불량))
    req.setAttribute("defectRateTop", QualityDAO.getInstance().defectRateByProductTopN(5));

    // 6) 공지
    req.setAttribute("notices", BoardManagementDAO.getInstance().selectLatestNotices(5));

    req.getRequestDispatcher("/jsp/Dashboard/dashboard.jsp").forward(req, resp);
  }
}
