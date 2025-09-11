package mes.PROCESS;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mes.DTO.ProcessDTO;

@WebServlet("/process")
public class ProcessListController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private ProcessDAO processDAO;
    
    public ProcessListController() {
        super();
        processDAO = new ProcessDAO();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 한글깨짐방지
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8;");
        
        try {
            // 라우팅 목록 조회
            List<ProcessDTO> processList = processDAO.selectRoutingProcesses();
            
            // 통계 정보
            int totalCount = processDAO.getRoutingCount();
            int semiProcessCount = processDAO.getRoutingCountByType("SEMI");
            int finishProcessCount = processDAO.getRoutingCountByType("FINISH");
            
            request.setAttribute("processList", processList);
            request.setAttribute("totalCount", totalCount);
            request.setAttribute("semiProcessCount", semiProcessCount);
            request.setAttribute("finishProcessCount", finishProcessCount);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "공정 목록 조회 중 오류가 발생했습니다: " + e.getMessage());
        }
        
        // 페이지 이동
        RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/PROCESS/Prc_list.jsp");
        dispatcher.forward(request, response);
    }
}