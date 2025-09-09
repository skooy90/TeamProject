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

@WebServlet("/material/detail")
public class MatDetailController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private MatDAO matDAO;
    
    public MatDetailController() {
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
            
            if (materialCode == null || materialCode.trim().isEmpty()) {
                request.setAttribute("error", "재고코드가 필요합니다.");
                response.sendRedirect("/TeamProject/material");
                return;
            }
            
            MaterialDTO material = matDAO.selectMaterialByCode(materialCode);
            
            if (material == null) {
                request.setAttribute("error", "해당 재고를 찾을 수 없습니다.");
                response.sendRedirect("/TeamProject/material");
                return;
            }
            
            // 같은 제품의 다른 재고들 (비교용)
            List<MaterialDTO> relatedMaterials = matDAO.selectMaterialsByType(
                getMaterialTypeByCode(material.getStandardCode())
            );
            
            request.setAttribute("material", material);
            request.setAttribute("relatedMaterials", relatedMaterials);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "재고 상세 조회 중 오류가 발생했습니다: " + e.getMessage());
        }
        
        // 페이지 이동
        RequestDispatcher dispatcher = request.getRequestDispatcher("/src/jsp/MATERIAL/Mat_detail.jsp");
        dispatcher.forward(request, response);
    }
    
    // 재고코드로 제품 유형 조회 (헬퍼 메서드)
    private String getMaterialTypeByCode(String standardCode) {
        if (standardCode.startsWith("RA")) return "원자제";
        if (standardCode.startsWith("SE")) return "반제품";
        if (standardCode.startsWith("FI")) return "완제품";
        return "기타";
    }
}
