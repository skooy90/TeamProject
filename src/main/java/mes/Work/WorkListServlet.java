package com.mes.Controller.Work;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mes.DAO.WorkDAO;
import com.mes.DTO.WorkDTO;

@WebServlet("/workList")

public class WorkListServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       
    	String q = req.getParameter("q");
        WorkDAO dao = WorkDAO.getInstance();

        List<WorkDTO> list = (q != null && q.trim().length() >= 2)
                ? dao.searchByPrefix(q.trim())
                : dao.findAll();

        req.setAttribute("workList", list);
        req.setAttribute("q", q == null ? "" : q.trim());
        req.getRequestDispatcher("/work_list.jsp").forward(req, resp);
    }
}
