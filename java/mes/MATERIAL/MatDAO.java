package mes.MATERIAL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import mes.DTO.MaterialDTO;
import mes.DTO.StandardDTO;
import mes.DTO.UsersDTO;
import mes.util.DBManager;

public class MatDAO {
    
    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;
    
    // DB 연결 메서드
    private Connection getConnection() {
        DBManager dbManager = new DBManager();
        return dbManager.getConnection();
    }
    
    // 자원 해제 메서드
    private void closeResources(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // C - 재고 등록
    public int insertMaterial(MaterialDTO material) {
        // 제품 유형에 따른 접두사 직접 지정
        String prefix = "MA"; // 기본값
        
        // STANDARD 테이블에서 제품 유형 조회
        String typeSql = "SELECT ST_TYPE FROM STANDARD WHERE STANDARD_CODE = ?";
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(typeSql);
            pstmt.setString(1, material.getStandardCode());
            rs = pstmt.executeQuery();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        // INSERT 문 실행 (시퀀스 직접 사용)
        String sql = "INSERT INTO MATERIAL (MATERIAL_CODE, STANDARD_CODE, EMPLOYEE_NO, MA_QUANTITY, MA_CREATION_DATE, MA_UPDATE_DATE) " +
                    "VALUES ('" + prefix + "' || LPAD(SEQ_MATERIAL_CODE.NEXTVAL, 4, '0'), ?, ?, ?, SYSDATE, SYSDATE)";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, material.getStandardCode());
            pstmt.setString(2, material.getEmployeeNo());
            pstmt.setInt(3, material.getMaQuantity());
            
            int result = pstmt.executeUpdate();
            return result;
            
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }
    
    // R - 재고코드로 특정 재고 조회
    public MaterialDTO selectMaterialByCode(String materialCode) {
        MaterialDTO material = null;
        String sql = "SELECT m.MATERIAL_CODE, m.STANDARD_CODE, s.ST_NAME, s.ST_TYPE, s.ST_UNIT, " +
                    "m.MA_QUANTITY, m.EMPLOYEE_NO, u.US_NAME, u.US_POSITION, " +
                    "m.MA_CREATION_DATE, m.MA_UPDATE_DATE " +
                    "FROM MATERIAL m " +
                    "JOIN STANDARD s ON m.STANDARD_CODE = s.STANDARD_CODE " +
                    "JOIN USERS u ON m.EMPLOYEE_NO = u.EMPLOYEE_NO " +
                    "WHERE m.MATERIAL_CODE = ?";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, materialCode);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                material = new MaterialDTO();
                material.setMaterialCode(rs.getString("MATERIAL_CODE"));
                material.setStandardCode(rs.getString("STANDARD_CODE"));
                material.setEmployeeNo(rs.getString("EMPLOYEE_NO"));
                material.setMaQuantity(rs.getInt("MA_QUANTITY"));
                material.setMaCreationDate(rs.getDate("MA_CREATION_DATE"));
                material.setMaUpdateDate(rs.getDate("MA_UPDATE_DATE"));
                
                // JOIN된 데이터 저장 (DB 테이블과 일치)
                material.setStName(rs.getString("ST_NAME"));
                material.setStType(rs.getString("ST_TYPE"));
                material.setStUnit(rs.getString("ST_UNIT"));
                material.setUsName(rs.getString("US_NAME"));
                material.setUsPosition(rs.getString("US_POSITION"));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return material;
    }
    
    
    
    // U - 재고 정보 수정
    public int updateMaterial(MaterialDTO material) {
        String sql = "UPDATE MATERIAL SET STANDARD_CODE = ?, EMPLOYEE_NO = ?, MA_QUANTITY = ?, MA_UPDATE_DATE = SYSDATE " +
                    "WHERE MATERIAL_CODE = ?";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, material.getStandardCode());
            pstmt.setString(2, material.getEmployeeNo());
            pstmt.setInt(3, material.getMaQuantity());
            pstmt.setString(4, material.getMaterialCode());
            
            int result = pstmt.executeUpdate();
            return result;
            
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }
    
    // U - 재고량만 수정
    public int updateMaterialQuantity(String materialCode, int quantity) {
        String sql = "UPDATE MATERIAL SET MA_QUANTITY = ?, MA_UPDATE_DATE = SYSDATE WHERE MATERIAL_CODE = ?";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, quantity);
            pstmt.setString(2, materialCode);
            
            int result = pstmt.executeUpdate();
            return result;
            
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }
    
    // D - 재고 삭제
    public int deleteMaterial(String materialCode) {
        String sql = "DELETE FROM MATERIAL WHERE MATERIAL_CODE = ?";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, materialCode);
            
            int result = pstmt.executeUpdate();
            return result;
            
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }
    
    
    
    // FK 데이터 조회 - 제품 목록 (드롭다운용)
    public List<StandardDTO> selectAllStandards() {
        List<StandardDTO> standardList = new ArrayList<>();
        String sql = "SELECT STANDARD_CODE, ST_TYPE, ST_NAME, ST_UNIT FROM STANDARD ORDER BY ST_TYPE, ST_NAME";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                StandardDTO standard = new StandardDTO();
                standard.setStandardCode(rs.getString("STANDARD_CODE"));
                standard.setStType(rs.getString("ST_TYPE"));
                standard.setStName(rs.getString("ST_NAME"));
                standard.setStUnit(rs.getString("ST_UNIT"));
                
                standardList.add(standard);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return standardList;
    }
    
    // FK 데이터 조회 - 사용자 목록 (드롭다운용)
    public List<UsersDTO> selectAllUsers() {
        List<UsersDTO> userList = new ArrayList<>();
        String sql = "SELECT EMPLOYEE_NO, US_NAME, US_POSITION FROM USERS ORDER BY US_NAME";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                UsersDTO user = new UsersDTO();
                user.setEmployeeNo(rs.getString("EMPLOYEE_NO"));
                user.setUsName(rs.getString("US_NAME"));
                user.setUsPosition(rs.getString("US_POSITION"));
                
                userList.add(user);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return userList;
    }
    

    // LOT별 재고 조회 (담당자 포함)
    public List<MaterialDTO> selectMaterialsByLot() {
        List<MaterialDTO> materialList = new ArrayList<>();
        String sql = "SELECT m.MATERIAL_CODE, m.STANDARD_CODE, s.ST_NAME, s.ST_TYPE, s.ST_UNIT, " +
                    "m.MA_QUANTITY, m.EMPLOYEE_NO, u.US_NAME, u.US_POSITION, " +
                    "m.MA_CREATION_DATE, m.MA_UPDATE_DATE " +
                    "FROM MATERIAL m " +
                    "JOIN STANDARD s ON m.STANDARD_CODE = s.STANDARD_CODE " +
                    "JOIN USERS u ON m.EMPLOYEE_NO = u.EMPLOYEE_NO " +
                    "ORDER BY m.MA_CREATION_DATE DESC, m.MATERIAL_CODE DESC";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                MaterialDTO material = new MaterialDTO();
                material.setMaterialCode(rs.getString("MATERIAL_CODE"));
                material.setStandardCode(rs.getString("STANDARD_CODE"));
                material.setStName(rs.getString("ST_NAME"));
                material.setStType(rs.getString("ST_TYPE"));
                material.setStUnit(rs.getString("ST_UNIT"));
                material.setMaQuantity(rs.getInt("MA_QUANTITY"));
                material.setEmployeeNo(rs.getString("EMPLOYEE_NO"));
                material.setUsName(rs.getString("US_NAME"));
                material.setUsPosition(rs.getString("US_POSITION"));
                material.setMaCreationDate(rs.getDate("MA_CREATION_DATE"));
                material.setMaUpdateDate(rs.getDate("MA_UPDATE_DATE"));
                
                materialList.add(material);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return materialList;
    }
    
    // 제품별 총수량 조회
    public List<MaterialDTO> selectMaterialsTotalByProduct() {
        List<MaterialDTO> totalList = new ArrayList<>();
        String sql = "SELECT s.STANDARD_CODE, s.ST_NAME, s.ST_TYPE, s.ST_UNIT, " +
                    "SUM(m.MA_QUANTITY) AS TOTAL_QUANTITY " +
                    "FROM MATERIAL m " +
                    "JOIN STANDARD s ON m.STANDARD_CODE = s.STANDARD_CODE " +
                    "GROUP BY s.STANDARD_CODE, s.ST_NAME, s.ST_TYPE, s.ST_UNIT " +
                    "ORDER BY s.ST_NAME";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                MaterialDTO material = new MaterialDTO();
                material.setStandardCode(rs.getString("STANDARD_CODE"));
                material.setStName(rs.getString("ST_NAME"));
                material.setStType(rs.getString("ST_TYPE"));
                material.setStUnit(rs.getString("ST_UNIT"));
                material.setMaQuantity(rs.getInt("TOTAL_QUANTITY")); // 총수량을 maQuantity에 저장
                
                totalList.add(material);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return totalList;
    }
    
    // 한국어 → 영어 매핑
    private String getEnglishType(String koreanType) {
        switch (koreanType) {
            case "원자재": return "RAW";
            case "반제품": return "SEMI";
            case "완제품": return "FINISH";
            default: return "OTHER";
        }
    }

    // 영어 → 한국어 매핑
    private String getKoreanType(String englishType) {
        switch (englishType) {
            case "RAW": return "원자재";
            case "SEMI": return "반제품";
            case "FINISH": return "완제품";
            default: return "기타";
        }
    }
    
    // 새로운 구조에 맞는 통계 메서드들
    
    // 전체 LOT 개수
    public int getTotalLotCount() {
        String sql = "SELECT COUNT(*) FROM MATERIAL";
        int count = 0;
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                count = rs.getInt(1);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return count;
    }
    
    // 재고 부족 LOT 개수 (임계값 이하)
    public int getLowStockLotCount(int threshold) {
        String sql = "SELECT COUNT(*) FROM MATERIAL WHERE MA_QUANTITY <= ?";
        int count = 0;
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, threshold);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                count = rs.getInt(1);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return count;
    }
    
    // 제품 유형별 LOT 개수
    public int getLotCountByType(String type) {
        String sql = "SELECT COUNT(*) FROM MATERIAL m JOIN STANDARD s ON m.STANDARD_CODE = s.STANDARD_CODE WHERE s.ST_TYPE = ?";
        int count = 0;
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, type);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                count = rs.getInt(1);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return count;
    }
    
    // 새로운 구조에 맞는 검색 메서드들
    
    // 전체 필드 검색 (LOT별 재고)
    public List<MaterialDTO> searchMaterialsByLot(String keyword) {
        List<MaterialDTO> materialList = new ArrayList<>();
        String sql = "SELECT m.MATERIAL_CODE, m.STANDARD_CODE, s.ST_NAME, s.ST_TYPE, s.ST_UNIT, " +
                    "m.MA_QUANTITY, m.EMPLOYEE_NO, u.US_NAME, u.US_POSITION, " +
                    "m.MA_CREATION_DATE, m.MA_UPDATE_DATE " +
                    "FROM MATERIAL m " +
                    "JOIN STANDARD s ON m.STANDARD_CODE = s.STANDARD_CODE " +
                    "JOIN USERS u ON m.EMPLOYEE_NO = u.EMPLOYEE_NO " +
                    "WHERE (UPPER(m.MATERIAL_CODE) LIKE UPPER(?) OR " +
                    "      UPPER(s.ST_NAME) LIKE UPPER(?) OR " +
                    "      UPPER(s.ST_TYPE) LIKE UPPER(?) OR " +
                    "      UPPER(u.US_NAME) LIKE UPPER(?)) " +
                    "ORDER BY m.MA_CREATION_DATE DESC, m.MATERIAL_CODE DESC";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt.setString(4, searchPattern);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                MaterialDTO material = new MaterialDTO();
                material.setMaterialCode(rs.getString("MATERIAL_CODE"));
                material.setStandardCode(rs.getString("STANDARD_CODE"));
                material.setStName(rs.getString("ST_NAME"));
                material.setStType(rs.getString("ST_TYPE"));
                material.setStUnit(rs.getString("ST_UNIT"));
                material.setMaQuantity(rs.getInt("MA_QUANTITY"));
                material.setEmployeeNo(rs.getString("EMPLOYEE_NO"));
                material.setUsName(rs.getString("US_NAME"));
                material.setUsPosition(rs.getString("US_POSITION"));
                material.setMaCreationDate(rs.getDate("MA_CREATION_DATE"));
                material.setMaUpdateDate(rs.getDate("MA_UPDATE_DATE"));
                
                materialList.add(material);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return materialList;
    }
    
    // 전체 필드 검색 (총수량)
    public List<MaterialDTO> searchMaterialsTotalByProduct(String keyword) {
        List<MaterialDTO> totalList = new ArrayList<>();
        String sql = "SELECT s.STANDARD_CODE, s.ST_NAME, s.ST_TYPE, s.ST_UNIT, " +
                    "SUM(m.MA_QUANTITY) AS TOTAL_QUANTITY " +
                    "FROM MATERIAL m " +
                    "JOIN STANDARD s ON m.STANDARD_CODE = s.STANDARD_CODE " +
                    "WHERE (UPPER(s.STANDARD_CODE) LIKE UPPER(?) OR " +
                    "      UPPER(s.ST_NAME) LIKE UPPER(?) OR " +
                    "      UPPER(s.ST_TYPE) LIKE UPPER(?)) " +
                    "GROUP BY s.STANDARD_CODE, s.ST_NAME, s.ST_TYPE, s.ST_UNIT " +
                    "ORDER BY s.ST_NAME";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                MaterialDTO material = new MaterialDTO();
                material.setStandardCode(rs.getString("STANDARD_CODE"));
                material.setStName(rs.getString("ST_NAME"));
                material.setStType(rs.getString("ST_TYPE"));
                material.setStUnit(rs.getString("ST_UNIT"));
                material.setMaQuantity(rs.getInt("TOTAL_QUANTITY"));
                
                totalList.add(material);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return totalList;
    }
    
    // 특정 타입별 검색 (LOT별 재고)
    public List<MaterialDTO> searchMaterialsByLotByType(String searchType, String keyword) {
        List<MaterialDTO> materialList = new ArrayList<>();
        String sql;
        String searchPattern = "%" + keyword + "%";
        
        if ("code".equals(searchType)) {
            // 재고코드로 검색
            sql = "SELECT m.MATERIAL_CODE, m.STANDARD_CODE, s.ST_NAME, s.ST_TYPE, s.ST_UNIT, " +
                  "m.MA_QUANTITY, m.EMPLOYEE_NO, u.US_NAME, u.US_POSITION, " +
                  "m.MA_CREATION_DATE, m.MA_UPDATE_DATE " +
                  "FROM MATERIAL m " +
                  "JOIN STANDARD s ON m.STANDARD_CODE = s.STANDARD_CODE " +
                  "JOIN USERS u ON m.EMPLOYEE_NO = u.EMPLOYEE_NO " +
                  "WHERE UPPER(m.MATERIAL_CODE) LIKE UPPER(?) " +
                  "ORDER BY m.MA_CREATION_DATE DESC, m.MATERIAL_CODE DESC";
        } else if ("name".equals(searchType)) {
            // 제품명으로 검색
            sql = "SELECT m.MATERIAL_CODE, m.STANDARD_CODE, s.ST_NAME, s.ST_TYPE, s.ST_UNIT, " +
                  "m.MA_QUANTITY, m.EMPLOYEE_NO, u.US_NAME, u.US_POSITION, " +
                  "m.MA_CREATION_DATE, m.MA_UPDATE_DATE " +
                  "FROM MATERIAL m " +
                  "JOIN STANDARD s ON m.STANDARD_CODE = s.STANDARD_CODE " +
                  "JOIN USERS u ON m.EMPLOYEE_NO = u.EMPLOYEE_NO " +
                  "WHERE UPPER(s.ST_NAME) LIKE UPPER(?) " +
                  "ORDER BY m.MA_CREATION_DATE DESC, m.MATERIAL_CODE DESC";
        } else {
            // 기본값: 전체 검색
            return searchMaterialsByLot(keyword);
        }
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, searchPattern);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                MaterialDTO material = new MaterialDTO();
                material.setMaterialCode(rs.getString("MATERIAL_CODE"));
                material.setStandardCode(rs.getString("STANDARD_CODE"));
                material.setStName(rs.getString("ST_NAME"));
                material.setStType(rs.getString("ST_TYPE"));
                material.setStUnit(rs.getString("ST_UNIT"));
                material.setMaQuantity(rs.getInt("MA_QUANTITY"));
                material.setEmployeeNo(rs.getString("EMPLOYEE_NO"));
                material.setUsName(rs.getString("US_NAME"));
                material.setUsPosition(rs.getString("US_POSITION"));
                material.setMaCreationDate(rs.getDate("MA_CREATION_DATE"));
                material.setMaUpdateDate(rs.getDate("MA_UPDATE_DATE"));
                
                materialList.add(material);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return materialList;
    }
    
    // 특정 타입별 검색 (총수량)
    public List<MaterialDTO> searchMaterialsTotalByProductByType(String searchType, String keyword) {
        List<MaterialDTO> totalList = new ArrayList<>();
        String sql;
        String searchPattern = "%" + keyword + "%";
        
        if ("code".equals(searchType)) {
            // 제품코드로 검색
            sql = "SELECT s.STANDARD_CODE, s.ST_NAME, s.ST_TYPE, s.ST_UNIT, " +
                  "SUM(m.MA_QUANTITY) AS TOTAL_QUANTITY " +
                  "FROM MATERIAL m " +
                  "JOIN STANDARD s ON m.STANDARD_CODE = s.STANDARD_CODE " +
                  "WHERE UPPER(s.STANDARD_CODE) LIKE UPPER(?) " +
                  "GROUP BY s.STANDARD_CODE, s.ST_NAME, s.ST_TYPE, s.ST_UNIT " +
                  "ORDER BY s.ST_NAME";
        } else if ("name".equals(searchType)) {
            // 제품명으로 검색
            sql = "SELECT s.STANDARD_CODE, s.ST_NAME, s.ST_TYPE, s.ST_UNIT, " +
                  "SUM(m.MA_QUANTITY) AS TOTAL_QUANTITY " +
                  "FROM MATERIAL m " +
                  "JOIN STANDARD s ON m.STANDARD_CODE = s.STANDARD_CODE " +
                  "WHERE UPPER(s.ST_NAME) LIKE UPPER(?) " +
                  "GROUP BY s.STANDARD_CODE, s.ST_NAME, s.ST_TYPE, s.ST_UNIT " +
                  "ORDER BY s.ST_NAME";
        } else {
            // 기본값: 전체 검색
            return searchMaterialsTotalByProduct(keyword);
        }
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, searchPattern);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                MaterialDTO material = new MaterialDTO();
                material.setStandardCode(rs.getString("STANDARD_CODE"));
                material.setStName(rs.getString("ST_NAME"));
                material.setStType(rs.getString("ST_TYPE"));
                material.setStUnit(rs.getString("ST_UNIT"));
                material.setMaQuantity(rs.getInt("TOTAL_QUANTITY"));
                
                totalList.add(material);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return totalList;
    }
}