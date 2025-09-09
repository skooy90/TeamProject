package mes.BOM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mes.DTO.BOMDTO;
import mes.DTO.StandardDTO;
import util.DBManager;

public class BOMDAO {
    
    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;
    
    // DB 연결 메서드
    private Connection getConnection() {
        DBManager dbManager = new DBManager();
        return dbManager.getConn();
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
    
    // C - BOM 등록
    public int insertBOM(BOMDTO bom) {
        String sql = "INSERT INTO BOM (BOM_NO, STANDARD_CODE, BOM_DESCRIPTION, BOM_TYPE, BOM_ORDER, BOM_IMAGE, CREATE_DATE, UPDATE_DATE) " +
                    "VALUES ('B' || LPAD(SEQ_BOM_NO.NEXTVAL, 4, '0'), ?, ?, ?, ?, ?, SYSDATE, SYSDATE)";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, bom.getStandardCode());
            pstmt.setString(2, bom.getBomDescription());
            pstmt.setString(3, bom.getBomType());
            pstmt.setInt(4, bom.getBomOrder());
            pstmt.setString(5, bom.getBomImage());
            
            int result = pstmt.executeUpdate();
            return result;
            
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }
    
    // R - 전체 BOM 목록 조회 (JOIN)
    public List<BOMDTO> selectAllBOMs() {
        List<BOMDTO> bomList = new ArrayList<>();
        String sql = "SELECT b.BOM_NO, b.STANDARD_CODE, s.ST_NAME, s.ST_TYPE, s.ST_UNIT, " +
                    "b.BOM_DESCRIPTION, b.BOM_TYPE, b.BOM_ORDER, b.BOM_IMAGE, " +
                    "b.CREATE_DATE, b.UPDATE_DATE " +
                    "FROM BOM b " +
                    "JOIN STANDARD s ON b.STANDARD_CODE = s.STANDARD_CODE " +
                    "ORDER BY b.BOM_ORDER ASC";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                BOMDTO bom = new BOMDTO();
                bom.setBomNo(rs.getString("BOM_NO"));
                bom.setStandardCode(rs.getString("STANDARD_CODE"));
                bom.setBomDescription(rs.getString("BOM_DESCRIPTION"));
                bom.setBomType(rs.getString("BOM_TYPE"));
                bom.setBomOrder(rs.getInt("BOM_ORDER"));
                bom.setBomImage(rs.getString("BOM_IMAGE"));
                bom.setCreateDate(rs.getDate("CREATE_DATE"));
                bom.setUpdateDate(rs.getDate("UPDATE_DATE"));
                
                // JOIN된 데이터 저장
                bom.setStName(rs.getString("ST_NAME"));
                bom.setStType(rs.getString("ST_TYPE"));
                bom.setStUnit(rs.getString("ST_UNIT"));
                
                bomList.add(bom);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return bomList;
    }
    
    // R - BOM번호로 특정 BOM 조회
    public BOMDTO selectBOMByNo(String bomNo) {
        BOMDTO bom = null;
        String sql = "SELECT b.BOM_NO, b.STANDARD_CODE, s.ST_NAME, s.ST_TYPE, s.ST_UNIT, " +
                    "b.BOM_DESCRIPTION, b.BOM_TYPE, b.BOM_ORDER, b.BOM_IMAGE, " +
                    "b.CREATE_DATE, b.UPDATE_DATE " +
                    "FROM BOM b " +
                    "JOIN STANDARD s ON b.STANDARD_CODE = s.STANDARD_CODE " +
                    "WHERE b.BOM_NO = ?";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, bomNo);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                bom = new BOMDTO();
                bom.setBomNo(rs.getString("BOM_NO"));
                bom.setStandardCode(rs.getString("STANDARD_CODE"));
                bom.setBomDescription(rs.getString("BOM_DESCRIPTION"));
                bom.setBomType(rs.getString("BOM_TYPE"));
                bom.setBomOrder(rs.getInt("BOM_ORDER"));
                bom.setBomImage(rs.getString("BOM_IMAGE"));
                bom.setCreateDate(rs.getDate("CREATE_DATE"));
                bom.setUpdateDate(rs.getDate("UPDATE_DATE"));
                
                // JOIN된 데이터 저장
                bom.setStName(rs.getString("ST_NAME"));
                bom.setStType(rs.getString("ST_TYPE"));
                bom.setStUnit(rs.getString("ST_UNIT"));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return bom;
    }
    
    // R - 제품코드별 BOM 조회
    public BOMDTO selectBOMByStandardCode(String standardCode) {
        BOMDTO bom = null;
        String sql = "SELECT b.BOM_NO, b.STANDARD_CODE, s.ST_NAME, s.ST_TYPE, s.ST_UNIT, " +
                    "b.BOM_DESCRIPTION, b.BOM_TYPE, b.BOM_ORDER, b.BOM_IMAGE, " +
                    "b.CREATE_DATE, b.UPDATE_DATE " +
                    "FROM BOM b " +
                    "JOIN STANDARD s ON b.STANDARD_CODE = s.STANDARD_CODE " +
                    "WHERE b.STANDARD_CODE = ?";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, standardCode);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                bom = new BOMDTO();
                bom.setBomNo(rs.getString("BOM_NO"));
                bom.setStandardCode(rs.getString("STANDARD_CODE"));
                bom.setBomDescription(rs.getString("BOM_DESCRIPTION"));
                bom.setBomType(rs.getString("BOM_TYPE"));
                bom.setBomOrder(rs.getInt("BOM_ORDER"));
                bom.setBomImage(rs.getString("BOM_IMAGE"));
                bom.setCreateDate(rs.getDate("CREATE_DATE"));
                bom.setUpdateDate(rs.getDate("UPDATE_DATE"));
                
                // JOIN된 데이터 저장
                bom.setStName(rs.getString("ST_NAME"));
                bom.setStType(rs.getString("ST_TYPE"));
                bom.setStUnit(rs.getString("ST_UNIT"));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return bom;
    }
    
    // R - 제품 유형별 BOM 조회
    public List<BOMDTO> selectBOMsByType(String type) {
        List<BOMDTO> bomList = new ArrayList<>();
        String sql = "SELECT b.BOM_NO, b.STANDARD_CODE, s.ST_NAME, s.ST_TYPE, s.ST_UNIT, " +
                    "b.BOM_DESCRIPTION, b.BOM_TYPE, b.BOM_ORDER, b.BOM_IMAGE, " +
                    "b.CREATE_DATE, b.UPDATE_DATE " +
                    "FROM BOM b " +
                    "JOIN STANDARD s ON b.STANDARD_CODE = s.STANDARD_CODE " +
                    "WHERE s.ST_TYPE = ? " +
                    "ORDER BY b.BOM_ORDER ASC";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, type);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                BOMDTO bom = new BOMDTO();
                bom.setBomNo(rs.getString("BOM_NO"));
                bom.setStandardCode(rs.getString("STANDARD_CODE"));
                bom.setBomDescription(rs.getString("BOM_DESCRIPTION"));
                bom.setBomType(rs.getString("BOM_TYPE"));
                bom.setBomOrder(rs.getInt("BOM_ORDER"));
                bom.setBomImage(rs.getString("BOM_IMAGE"));
                bom.setCreateDate(rs.getDate("CREATE_DATE"));
                bom.setUpdateDate(rs.getDate("UPDATE_DATE"));
                
                // JOIN된 데이터 저장
                bom.setStName(rs.getString("ST_NAME"));
                bom.setStType(rs.getString("ST_TYPE"));
                bom.setStUnit(rs.getString("ST_UNIT"));
                
                bomList.add(bom);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return bomList;
    }
    
    // U - BOM 정보 수정
    public int updateBOM(BOMDTO bom) {
        String sql = "UPDATE BOM SET STANDARD_CODE = ?, BOM_DESCRIPTION = ?, BOM_TYPE = ?, BOM_ORDER = ?, BOM_IMAGE = ?, UPDATE_DATE = SYSDATE " +
                    "WHERE BOM_NO = ?";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, bom.getStandardCode());
            pstmt.setString(2, bom.getBomDescription());
            pstmt.setString(3, bom.getBomType());
            pstmt.setInt(4, bom.getBomOrder());
            pstmt.setString(5, bom.getBomImage());
            pstmt.setString(6, bom.getBomNo());
            
            int result = pstmt.executeUpdate();
            return result;
            
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }
    
    // D - BOM 삭제
    public int deleteBOM(String bomNo) {
        String sql = "DELETE FROM BOM WHERE BOM_NO = ?";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, bomNo);
            
            int result = pstmt.executeUpdate();
            return result;
            
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }
    
    // 통계 조회 - 전체 BOM 개수
    public int getTotalBOMCount() {
        String sql = "SELECT COUNT(*) FROM BOM";
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
    
    // 통계 조회 - 제품 유형별 BOM 개수
    public int getBOMCountByType(String type) {
        String sql = "SELECT COUNT(*) FROM BOM b JOIN STANDARD s ON b.STANDARD_CODE = s.STANDARD_CODE WHERE s.ST_TYPE = ?";
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
    
    // 통계 조회 - 총 자재 수 (BOM별 자재 개수 합계)
    public int getTotalMaterialCount() {
        // BOM_MATERIAL 테이블이 없으므로 BOM 테이블의 개수로 대체
        String sql = "SELECT COUNT(*) FROM BOM";
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
    
    // BOM별 관련 자재 목록 조회 (JOIN 사용)
    public List<Map<String, Object>> getBOMMaterials(String bomNo) {
        List<Map<String, Object>> materialList = new ArrayList<>();
        String sql = "SELECT m.MATERIAL_CODE, s.ST_NAME as MATERIAL_NAME, s.ST_TYPE as MATERIAL_TYPE, " +
                    "s.ST_UNIT, m.MA_QUANTITY as CURRENT_STOCK, " +
                    "1.0 as BOM_QUANTITY, 'BOM 자재' as REMARK " +
                    "FROM MATERIAL m " +
                    "JOIN STANDARD s ON m.STANDARD_CODE = s.STANDARD_CODE " +
                    "WHERE s.ST_TYPE IN ('RAW', 'SEMI') " +  // 원자재, 반제품만
                    "ORDER BY s.ST_TYPE, s.ST_NAME";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> material = new HashMap<>();
                material.put("materialCode", rs.getString("MATERIAL_CODE"));
                material.put("materialName", rs.getString("MATERIAL_NAME"));
                material.put("materialType", rs.getString("MATERIAL_TYPE"));
                material.put("unit", rs.getString("ST_UNIT"));
                material.put("currentStock", rs.getDouble("CURRENT_STOCK"));
                material.put("bomQuantity", rs.getDouble("BOM_QUANTITY"));
                material.put("remark", rs.getString("REMARK"));
                
                materialList.add(material);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return materialList;
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
    
    // 검색 메서드
    public List<BOMDTO> searchBOMs(String searchType, String searchKeyword) {
        List<BOMDTO> bomList = new ArrayList<>();
        String sql;
        
        if ("code".equals(searchType)) {
            // BOM번호로 검색
            sql = "SELECT b.BOM_NO, b.STANDARD_CODE, s.ST_NAME, s.ST_TYPE, s.ST_UNIT, " +
                  "b.BOM_DESCRIPTION, b.BOM_TYPE, b.BOM_ORDER, b.BOM_IMAGE, " +
                  "b.CREATE_DATE, b.UPDATE_DATE " +
                  "FROM BOM b " +
                  "JOIN STANDARD s ON b.STANDARD_CODE = s.STANDARD_CODE " +
                  "WHERE b.BOM_NO LIKE ? " +
                  "ORDER BY b.BOM_ORDER ASC";
        } else if ("name".equals(searchType)) {
            // 제품명으로 검색
            sql = "SELECT b.BOM_NO, b.STANDARD_CODE, s.ST_NAME, s.ST_TYPE, s.ST_UNIT, " +
                  "b.BOM_DESCRIPTION, b.BOM_TYPE, b.BOM_ORDER, b.BOM_IMAGE, " +
                  "b.CREATE_DATE, b.UPDATE_DATE " +
                  "FROM BOM b " +
                  "JOIN STANDARD s ON b.STANDARD_CODE = s.STANDARD_CODE " +
                  "WHERE s.ST_NAME LIKE ? " +
                  "ORDER BY b.BOM_ORDER ASC";
        } else {
            // 기본값: 제품명으로 검색
            sql = "SELECT b.BOM_NO, b.STANDARD_CODE, s.ST_NAME, s.ST_TYPE, s.ST_UNIT, " +
                  "b.BOM_DESCRIPTION, b.BOM_TYPE, b.BOM_ORDER, b.BOM_IMAGE, " +
                  "b.CREATE_DATE, b.UPDATE_DATE " +
                  "FROM BOM b " +
                  "JOIN STANDARD s ON b.STANDARD_CODE = s.STANDARD_CODE " +
                  "WHERE s.ST_NAME LIKE ? " +
                  "ORDER BY b.BOM_ORDER ASC";
        }
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + searchKeyword + "%");
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                BOMDTO bom = new BOMDTO();
                bom.setBomNo(rs.getString("BOM_NO"));
                bom.setStandardCode(rs.getString("STANDARD_CODE"));
                bom.setBomDescription(rs.getString("BOM_DESCRIPTION"));
                bom.setBomType(rs.getString("BOM_TYPE"));
                bom.setBomOrder(rs.getInt("BOM_ORDER"));
                bom.setBomImage(rs.getString("BOM_IMAGE"));
                bom.setCreateDate(rs.getDate("CREATE_DATE"));
                bom.setUpdateDate(rs.getDate("UPDATE_DATE"));
                
                // JOIN된 데이터 저장
                bom.setStName(rs.getString("ST_NAME"));
                bom.setStType(rs.getString("ST_TYPE"));
                bom.setStUnit(rs.getString("ST_UNIT"));
                
                bomList.add(bom);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return bomList;
    }
    
    // 전체 검색 메서드 (BOM번호, 제품코드, 제품명 모두 검색)
    public List<BOMDTO> searchAllFields(String searchKeyword) {
        List<BOMDTO> bomList = new ArrayList<>();
        String sql = "SELECT b.BOM_NO, b.STANDARD_CODE, s.ST_NAME, s.ST_TYPE, s.ST_UNIT, " +
                    "b.BOM_DESCRIPTION, b.BOM_TYPE, b.BOM_ORDER, b.BOM_IMAGE, " +
                    "b.CREATE_DATE, b.UPDATE_DATE " +
                    "FROM BOM b " +
                    "JOIN STANDARD s ON b.STANDARD_CODE = s.STANDARD_CODE " +
                    "WHERE b.BOM_NO LIKE ? " +
                    "   OR b.STANDARD_CODE LIKE ? " +
                    "   OR s.ST_NAME LIKE ? " +
                    "ORDER BY b.BOM_ORDER ASC";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            String keyword = "%" + searchKeyword + "%";
            pstmt.setString(1, keyword);  // BOM번호
            pstmt.setString(2, keyword);  // 제품코드
            pstmt.setString(3, keyword);  // 제품명
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                BOMDTO bom = new BOMDTO();
                bom.setBomNo(rs.getString("BOM_NO"));
                bom.setStandardCode(rs.getString("STANDARD_CODE"));
                bom.setBomDescription(rs.getString("BOM_DESCRIPTION"));
                bom.setBomType(rs.getString("BOM_TYPE"));
                bom.setBomOrder(rs.getInt("BOM_ORDER"));
                bom.setBomImage(rs.getString("BOM_IMAGE"));
                bom.setCreateDate(rs.getDate("CREATE_DATE"));
                bom.setUpdateDate(rs.getDate("UPDATE_DATE"));
                
                // JOIN된 데이터 저장
                bom.setStName(rs.getString("ST_NAME"));
                bom.setStType(rs.getString("ST_TYPE"));
                bom.setStUnit(rs.getString("ST_UNIT"));
                
                bomList.add(bom);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return bomList;
    }
}
