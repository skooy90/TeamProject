package mes.PROCESS;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mes.DTO.ProcessDTO;

@WebServlet("/process/insert")
public class ProcessInsertController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private ProcessDAO processDAO;
    
    public ProcessInsertController() {
        super();
        processDAO = new ProcessDAO();
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 한글깨짐방지
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8;");
        
        try {
            // 폼 데이터 받기
            String standardCode = request.getParameter("standardCode");
            String routingType = request.getParameter("routingType"); // SEMI 또는 FINISH
            
            // 유효성 검사
            if (standardCode == null || standardCode.trim().isEmpty() ||
                routingType == null || routingType.trim().isEmpty()) {
                
                response.sendRedirect(request.getContextPath() + "/process/form");
                return;
            }
            
            // 라우팅 타입에 따른 공정 단계 수 결정
            int stepCount = "SEMI".equals(routingType) ? 4 : 8;
            
            // 라우팅 공정 생성
            int result = processDAO.insertRoutingProcesses(standardCode, routingType, stepCount);
            
            if (result > 0) {
                response.sendRedirect(request.getContextPath() + "/process");
            } else {
                response.sendRedirect(request.getContextPath() + "/process/form");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/process/form");
        }
    }
}
