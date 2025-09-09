package com.mes.Controller.Work;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mes.DAO.WorkDAO;
import com.mes.DTO.WorkDTO;

@WebServlet("/work/update")
public class WorkUpdateServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        WorkDTO w = new WorkDTO();

        w.setWorkNo(req.getParameter("workNo"));
        w.setProductionNo(req.getParameter("productionNo"));
        w.setStandardCode(req.getParameter("standardCode"));
        w.setEmployeeNo(req.getParameter("employeeNo"));
        w.setWoSchedule(Date.valueOf(req.getParameter("woSchedule")));
        w.setWoQuantity(Integer.parseInt(req.getParameter("woQuantity")));
        w.setWoStatus(req.getParameter("woStatus"));
        w.setWoCompleted(Integer.parseInt(req.getParameter("woCompleted")));
        w.setWoStart(Timestamp.valueOf(req.getParameter("woStart") + " 00:00:00"));
        w.setWoEnd(Timestamp.valueOf(req.getParameter("woEnd") + " 00:00:00"));

        WorkDAO.getInstance().update(w);
        resp.sendRedirect(req.getContextPath() + "/workList");
    }
}

