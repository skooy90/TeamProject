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

@WebServlet("/material/filter")
public class MatFilterController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private MatDAO matDAO;
    
    public MatFilterController() {
        super();
        matDAO = new MatDAO();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 한글깨짐방지
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8;");
        
        try {
            String type = request.getParameter("type");
            
            // 한글 파라미터를 DB 값으로 변환
            if ("원자재".equals(type)) {
                type = "RAW";
            } else if ("반제품".equals(type)) {
                type = "SEMI";
            } else if ("완제품".equals(type)) {
                type = "FINISH";
            }
            
            List<MaterialDTO> materialList;
            
            if (type == null || type.trim().isEmpty() || type.equals("all")) {
                materialList = matDAO.selectAllMaterials();
            } else {
                materialList = matDAO.selectMaterialsByType(type);
            }
            
            // 통계 정보
            int totalCount = matDAO.getTotalMaterialCount();
            int lowStockCount = matDAO.getLowStockCount(50);
            int rawMaterialCount = matDAO.getMaterialCountByType("RAW");
            int semiProductCount = matDAO.getMaterialCountByType("SEMI");
            int finishedProductCount = matDAO.getMaterialCountByType("FINISH");
            
            request.setAttribute("materialList", materialList);
            request.setAttribute("totalCount", totalCount);
            request.setAttribute("lowStockCount", lowStockCount);
            request.setAttribute("rawMaterialCount", rawMaterialCount);
            request.setAttribute("semiProductCount", semiProductCount);
            request.setAttribute("finishedProductCount", finishedProductCount);
            request.setAttribute("selectedType", type);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "필터링 중 오류가 발생했습니다: " + e.getMessage());
        }
        
        // 페이지 이동
        RequestDispatcher dispatcher = request.getRequestDispatcher("/src/jsp/MATERIAL/Mat_list.jsp");
        dispatcher.forward(request, response);
    }
}
