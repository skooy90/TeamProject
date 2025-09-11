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
                response.sendRedirect(request.getContextPath() + "/material");
                return;
            }
            
            MaterialDTO material = matDAO.selectMaterialByCode(materialCode);
            
            if (material == null) {
                response.sendRedirect(request.getContextPath() + "/material");
                return;
            }
            
            // 같은 제품의 다른 LOT들 (비교용)
            List<MaterialDTO> relatedMaterials = matDAO.selectMaterialsByLot();
            // 현재 재고와 같은 제품의 LOT들만 필터링
            relatedMaterials.removeIf(m -> !m.getStandardCode().equals(material.getStandardCode()));
            
            // 통계 정보
            int totalCount = matDAO.getTotalLotCount();
            int lowStockCount = matDAO.getLowStockLotCount(50);
            int rawMaterialCount = matDAO.getLotCountByType("RAW");
            int semiProductCount = matDAO.getLotCountByType("SEMI");
            int finishedProductCount = matDAO.getLotCountByType("FINISH");
            
            request.setAttribute("material", material);
            request.setAttribute("relatedMaterials", relatedMaterials);
            request.setAttribute("totalCount", totalCount);
            request.setAttribute("lowStockCount", lowStockCount);
            request.setAttribute("rawMaterialCount", rawMaterialCount);
            request.setAttribute("semiProductCount", semiProductCount);
            request.setAttribute("finishedProductCount", finishedProductCount);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "재고 상세 조회 중 오류가 발생했습니다: " + e.getMessage());
        }
        
        // 페이지 이동
        RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/MATERIAL/Mat_detail.jsp");
        dispatcher.forward(request, response);
    }
}
