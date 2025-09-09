package com.mes.Controller.Standard;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mes.DAO.StandardDAO;
import com.mes.DTO.StandardDTO;

// The URL pattern to access this servlet
@WebServlet("/standardList")
public class StandardListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8"); // 한글 검색 대비

        String q = request.getParameter("q");
        List<StandardDTO> list;

        StandardDAO dao = StandardDAO.getInstance();
        if (q != null && q.trim().length() >= 2) {
            list = dao.searchByPrefix(q.trim());
        } else {
            list = dao.selectAllStandards(); // 2글자 미만이면 전체
        }

        request.setAttribute("standardList", list);
        request.setAttribute("q", q == null ? "" : q.trim());
        request.getRequestDispatcher("Standard_list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}