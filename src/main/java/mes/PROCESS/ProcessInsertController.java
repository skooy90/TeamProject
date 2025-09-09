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
            String prDescription = request.getParameter("prDescription");
            String prType = request.getParameter("prType");
            String prOrderStr = request.getParameter("prOrder");
            String prImage = request.getParameter("prImage");
            
            // 유효성 검사
            if (standardCode == null || standardCode.trim().isEmpty() ||
                prDescription == null || prDescription.trim().isEmpty() ||
                prType == null || prType.trim().isEmpty() ||
                prOrderStr == null || prOrderStr.trim().isEmpty()) {
                
                response.sendRedirect(request.getContextPath() + "/process/form");
                return;
            }
            
            int prOrder;
            try {
                prOrder = Integer.parseInt(prOrderStr);
                if (prOrder < 1 || prOrder > 8) {
                    response.sendRedirect(request.getContextPath() + "/process/form");
                    return;
                }
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/process/form");
                return;
            }
            
            // ProcessDTO 생성
            ProcessDTO process = new ProcessDTO();
            process.setStandardCode(standardCode);
            process.setPrDescription(prDescription);
            process.setPrType(prType);
            process.setPrOrder(prOrder);
            process.setPrImage(prImage != null ? prImage : "default.png");
            
            // DB에 등록
            int result = processDAO.insertProcess(process);
            
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
