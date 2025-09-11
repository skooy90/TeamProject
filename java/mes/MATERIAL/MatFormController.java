package mes.MATERIAL;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mes.DTO.MaterialDTO;
import mes.DTO.StandardDTO;
import mes.DTO.UsersDTO;

@WebServlet("/material/form")
public class MatFormController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private MatDAO matDAO;
    
    public MatFormController() {
        super();
        matDAO = new MatDAO();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 한글깨짐방지
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8;");
        
        try {
            String materialCode = request.getParameter("code");
            
            // 제품 목록과 사용자 목록 조회 (드롭다운용)
            List<StandardDTO> standardList = matDAO.selectAllStandards();
            List<UsersDTO> userList = matDAO.selectAllUsers();
            
            request.setAttribute("standardList", standardList);
            request.setAttribute("userList", userList);
            
            if (materialCode != null && !materialCode.trim().isEmpty()) {
                // 수정 모드
                MaterialDTO material = matDAO.selectMaterialByCode(materialCode);
                if (material != null) {
                    request.setAttribute("material", material);
                    request.setAttribute("mode", "update");
                } else {
                    request.setAttribute("error", "해당 재고를 찾을 수 없습니다.");
                    response.sendRedirect("/TeamProject/material");
                    return;
                }
            } else {
                // 등록 모드
                request.setAttribute("mode", "insert");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "폼 로드 중 오류가 발생했습니다: " + e.getMessage());
        }
        
        // 페이지 이동
        RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/MATERIAL/Mat_form.jsp");
        dispatcher.forward(request, response);
    }
}
