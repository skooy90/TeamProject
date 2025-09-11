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

@WebServlet("/process/detail")
	
public class ProcessDetailController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    private ProcessDAO processDAO;
    
    public ProcessDetailController() {
        super();
        processDAO = new ProcessDAO();
    }
	
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 한글깨짐방지
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8;");
        
        try {
            String standardCode = request.getParameter("code");
            
            if (standardCode == null || standardCode.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/process");
                return;
            }
            
            // 라우팅의 모든 공정 단계 조회
            System.out.println("=== ProcessDetailController 디버깅 ===");
            System.out.println("요청된 standardCode: " + standardCode);
            List<ProcessDTO> processSteps = processDAO.selectProcessStepsByRouting(standardCode);
            System.out.println("조회된 공정 단계 수: " + (processSteps != null ? processSteps.size() : 0));
            
            if (processSteps == null || processSteps.isEmpty()) {
                System.out.println("공정 단계가 없어서 목록으로 리다이렉트");
                response.sendRedirect(request.getContextPath() + "/process");
                return;
            }
            
            // 첫 번째 공정을 대표로 사용 (라우팅 정보)
            ProcessDTO process = processSteps.get(0);
            
            // 통계 정보
            int totalCount = processDAO.getRoutingCount();
            int semiProcessCount = processDAO.getRoutingCountByType("SEMI");
            int finishProcessCount = processDAO.getRoutingCountByType("FINISH");
            
            request.setAttribute("process", process);
            request.setAttribute("processSteps", processSteps);
            request.setAttribute("totalCount", totalCount);
            request.setAttribute("semiProcessCount", semiProcessCount);
            request.setAttribute("finishProcessCount", finishProcessCount);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "공정 상세 조회 중 오류가 발생했습니다: " + e.getMessage());
        }
        
        // 페이지 이동
        RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/PROCESS/Prc_detail.jsp");
        dispatcher.forward(request, response);
    }
}
