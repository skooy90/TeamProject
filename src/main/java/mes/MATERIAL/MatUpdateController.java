package mes.MATERIAL;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mes.DTO.MaterialDTO;

@WebServlet("/material/update")
public class MatUpdateController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private MatDAO matDAO;
    
    public MatUpdateController() {
        super();
        matDAO = new MatDAO();
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 한글깨짐방지
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8;");
        
        try {
            // 파라미터 받기
            String materialCode = request.getParameter("materialCode");
            String standardCode = request.getParameter("standardCode");
            String employeeNo = request.getParameter("employeeNo");
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            
            // 유효성 검사
            if (materialCode == null || materialCode.trim().isEmpty()) {
                request.getSession().setAttribute("error", "재고코드가 필요합니다.");
                response.sendRedirect("/TeamProject/material");
                return;
            }
            
            if (standardCode == null || standardCode.trim().isEmpty()) {
                request.getSession().setAttribute("error", "제품코드를 선택해주세요.");
                response.sendRedirect("/TeamProject/material/form?code=" + materialCode);
                return;
            }
            
            if (employeeNo == null || employeeNo.trim().isEmpty()) {
                request.getSession().setAttribute("error", "담당자를 선택해주세요.");
                response.sendRedirect("/TeamProject/material/form?code=" + materialCode);
                return;
            }
            
            if (quantity < 0) {
                request.getSession().setAttribute("error", "재고량은 0 이상이어야 합니다.");
                response.sendRedirect("/TeamProject/material/form?code=" + materialCode);
                return;
            }
            
            // MaterialDTO 생성
            MaterialDTO material = new MaterialDTO();
            material.setMaterialCode(materialCode);
            material.setStandardCode(standardCode);
            material.setEmployeeNo(employeeNo);
            material.setMaQuantity(quantity);
            
            // DB 수정
            int result = matDAO.updateMaterial(material);
            
            if (result > 0) {
                request.getSession().setAttribute("success", "재고가 성공적으로 수정되었습니다.");
            } else {
                request.getSession().setAttribute("error", "재고 수정에 실패했습니다.");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "재고 수정 중 오류가 발생했습니다: " + e.getMessage());
        }
        
        response.sendRedirect("/TeamProject/material");
    }
}
