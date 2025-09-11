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

@WebServlet("/process/search")
public class ProcessSearchController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private ProcessDAO processDAO;
    
    public ProcessSearchController() {
        super();
        processDAO = new ProcessDAO();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 한글깨짐방지
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8;");
        
        try {
            String searchType = request.getParameter("searchType");
            String searchKeyword = request.getParameter("searchKeyword");
            
            List<ProcessDTO> processList;
            
            if (searchKeyword == null || searchKeyword.trim().isEmpty()) {
                // 검색어가 없으면 전체 라우팅 목록
                processList = processDAO.selectRoutingProcesses();
            } else if ("all".equals(searchType)) {
                // "all" 선택 + 검색어 있으면 라우팅에서 검색
                processList = processDAO.searchRoutingProcesses(searchKeyword);
            } else {
                // "code" 또는 "name" 선택 + 검색어 있으면 해당 타입으로 검색
                processList = processDAO.searchRoutingProcesses(searchKeyword);
            }
            
            // 통계 정보
            int totalCount = processDAO.getRoutingCount();
            int semiProcessCount = processDAO.getRoutingCountByType("SEMI");
            int finishProcessCount = processDAO.getRoutingCountByType("FINISH");
            
            request.setAttribute("processList", processList);
            request.setAttribute("totalCount", totalCount);
            request.setAttribute("semiProcessCount", semiProcessCount);
            request.setAttribute("finishProcessCount", finishProcessCount);
            request.setAttribute("searchType", searchType);
            request.setAttribute("searchKeyword", searchKeyword);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "공정 검색 중 오류가 발생했습니다: " + e.getMessage());
        }
        
        
        // 페이지 이동
        RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/PROCESS/Prc_list.jsp");
        dispatcher.forward(request, response);
    }
}