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
            // 공정 목록 조회
            List<ProcessDTO> processList = processDAO.selectAllProcesses();
            
            // 통계 정보
            int totalCount = processDAO.getTotalProcessCount();
            int rawProcessCount = processDAO.getProcessCountByType("RAW");
            int semiProcessCount = processDAO.getProcessCountByType("SEMI");
            int finishProcessCount = processDAO.getProcessCountByType("FINISH");
            int mixProcessCount = processDAO.getProcessCountByProcessType("혼합");
            int moldProcessCount = processDAO.getProcessCountByProcessType("성형");
            int dryProcessCount = processDAO.getProcessCountByProcessType("건조");
            int packageProcessCount = processDAO.getProcessCountByProcessType("포장");
            
            request.setAttribute("processList", processList);
            request.setAttribute("totalCount", totalCount);
            request.setAttribute("rawProcessCount", rawProcessCount);
            request.setAttribute("semiProcessCount", semiProcessCount);
            request.setAttribute("finishProcessCount", finishProcessCount);
            request.setAttribute("mixProcessCount", mixProcessCount);
            request.setAttribute("moldProcessCount", moldProcessCount);
            request.setAttribute("dryProcessCount", dryProcessCount);
            request.setAttribute("packageProcessCount", packageProcessCount);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "공정 목록 조회 중 오류가 발생했습니다: " + e.getMessage());
        }
        
        // 페이지 이동
        RequestDispatcher dispatcher = request.getRequestDispatcher("/src/jsp/PROCESS/Prc_list.jsp");
        dispatcher.forward(request, response);
    }
}
