package com.mes.Controller.Work;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mes.DAO.WorkDAO;
import com.mes.DTO.WorkDTO;


//WorkFormServlet.java  (신규/수정 공용 폼 진입)
@WebServlet("/work/form")
public class WorkFormServlet extends HttpServlet {
@Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp)
   throws ServletException, IOException {

 String no = req.getParameter("no");   // 있으면 수정, 없으면 신규
 WorkDAO dao = WorkDAO.getInstance();

 if (no != null && !no.isEmpty()) {
   WorkDTO w = dao.findByNo(no);
   req.setAttribute("work", w);
 } else {
   // 신규: 화면에 보여줄 미리보기 번호 (호출 시 시퀀스가 소비됩니다)
   String preview = dao.peekNextNo();
   req.setAttribute("nextNo", preview);
 }
 req.getRequestDispatcher("/work_form.jsp").forward(req, resp);
}
}
