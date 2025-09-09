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

@WebServlet("/material")
public class MatListController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private MatDAO matDAO;
    
    public MatListController() {
        super();
        matDAO = new MatDAO();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 한글깨짐방지
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8;");
        
        try {
            List<MaterialDTO> materialList = matDAO.selectAllMaterials();
            
            // 통계 정보
            int totalCount = matDAO.getTotalMaterialCount();
            int lowStockCount = matDAO.getLowStockCount(50); // 임계값 50
            int rawMaterialCount = matDAO.getMaterialCountByType("RAW");
            int semiProductCount = matDAO.getMaterialCountByType("SEMI");
            int finishedProductCount = matDAO.getMaterialCountByType("FINISH");
            
            request.setAttribute("materialList", materialList);
            request.setAttribute("totalCount", totalCount);
            request.setAttribute("lowStockCount", lowStockCount);
            request.setAttribute("rawMaterialCount", rawMaterialCount);
            request.setAttribute("semiProductCount", semiProductCount);
            request.setAttribute("finishedProductCount", finishedProductCount);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "재고 목록 조회 중 오류가 발생했습니다: " + e.getMessage());
        }
        
        // 페이지 이동
        RequestDispatcher dispatcher = request.getRequestDispatcher("/src/jsp/MATERIAL/Mat_list.jsp");
        dispatcher.forward(request, response);
    }
}
