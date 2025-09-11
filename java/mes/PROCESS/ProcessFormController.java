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
import mes.DTO.StandardDTO;

@WebServlet("/process/form")
public class ProcessFormController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private ProcessDAO processDAO;
    
    public ProcessFormController() {
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
            String mode = "insert";
            ProcessDTO process = null;
            List<ProcessDTO> processSteps = null;
            
            // 수정 모드인 경우 (라우팅 수정)
            if (standardCode != null && !standardCode.trim().isEmpty()) {
                processSteps = processDAO.selectProcessStepsByRouting(standardCode);
                if (processSteps != null && !processSteps.isEmpty()) {
                    mode = "update";
                    process = processSteps.get(0); // 첫 번째 공정을 대표로 사용
                } else {
                    response.sendRedirect(request.getContextPath() + "/process");
                    return;
                }
            }
            
            // 제품 목록 조회 (드롭다운용) - SEMI, FINISH 타입만
            List<StandardDTO> standardList = processDAO.selectStandardsByType("SEMI,FINISH");
            
            request.setAttribute("mode", mode);
            request.setAttribute("process", process);
            request.setAttribute("processSteps", processSteps);
            request.setAttribute("standardList", standardList);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "공정 폼 로드 중 오류가 발생했습니다: " + e.getMessage());
        }
        
        // 페이지 이동
        RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/PROCESS/Prc_form.jsp");
        dispatcher.forward(request, response);
    }
}