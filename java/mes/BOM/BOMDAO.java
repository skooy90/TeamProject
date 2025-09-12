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
import mes.util.DBManager;

public class BOMDAO {
    
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
    
    // C - BOM 등록
    public int insertBOM(BOMDTO bom) {
        int result = 0;
        String sql = "INSERT INTO BOM (BOM_NO, STANDARD_CODE, BOM_DESCRIPTION, BOM_TYPE, BOM_ORDER, BOM_IMAGE, CREATE_DATE, UPDATE_DATE) " +
                    "VALUES (SEQ_BOM_NO.NEXTVAL, ?, ?, ?, ?, ?, SYSDATE, SYSDATE)";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, bom.getStandardCode());
            pstmt.setString(2, bom.getBomDescription());
            pstmt.setString(3, bom.getBomType());
            pstmt.setInt(4, bom.getBomOrder());
            pstmt.setString(5, bom.getBomImage());
            
            result = pstmt.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, null);
        }
        
        return result;
    }
    
    // R - 전체 BOM 목록 조회 (JOIN)
    public List<BOMDTO> selectAllBOMs() {
        List<BOMDTO> bomList = new ArrayList<>();
        String sql = "SELECT b.BOM_NO, b.STANDARD_CODE, s.ST_NAME, s.ST_TYPE, s.ST_UNIT, " +
                    "b.BOM_DESCRIPTION, b.BOM_TYPE, b.BOM_ORDER, b.BOM_IMAGE, " +
                    "b.CREATE_DATE, b.UPDATE_DATE " +
                    "FROM BOM b " +
                    "JOIN STANDARD s ON b.STANDARD_CODE = s.STANDARD_CODE " +
                    "ORDER BY b.BOM_ORDER, b.CREATE_DATE DESC";
        
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
    
    // R - 제품코드로 BOM 조회
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
    
    // U - BOM 수정
    public int updateBOM(BOMDTO bom) {
        int result = 0;
        String sql = "UPDATE BOM SET STANDARD_CODE = ?, BOM_DESCRIPTION = ?, BOM_TYPE = ?, " +
                    "BOM_ORDER = ?, BOM_IMAGE = ?, UPDATE_DATE = SYSDATE " +
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
            
            result = pstmt.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, null);
        }
        
        return result;
    }
    
    // D - BOM 삭제
    public int deleteBOM(String bomNo) {
        int result = 0;
        String sql = "DELETE FROM BOM WHERE BOM_NO = ?";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, bomNo);
            
            result = pstmt.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, null);
        }
        
        return result;
    }
    
    // 통계 - 전체 BOM 개수
    public int getTotalBOMCount() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM BOM";
        
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
    
    // 통계 - BOM 유형별 개수
    public int getBOMCountByType(String bomType) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM BOM WHERE BOM_TYPE = ?";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, bomType);
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
    
    // 통계 - 전체 자재 개수
    public int getTotalMaterialCount() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM BOM";
        
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
    
    // 검색 - BOM번호로 검색
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
                  "ORDER BY b.BOM_ORDER, b.CREATE_DATE DESC";
        } else if ("name".equals(searchType)) {
            // 제품명으로 검색
            sql = "SELECT b.BOM_NO, b.STANDARD_CODE, s.ST_NAME, s.ST_TYPE, s.ST_UNIT, " +
                  "b.BOM_DESCRIPTION, b.BOM_TYPE, b.BOM_ORDER, b.BOM_IMAGE, " +
                  "b.CREATE_DATE, b.UPDATE_DATE " +
                  "FROM BOM b " +
                  "JOIN STANDARD s ON b.STANDARD_CODE = s.STANDARD_CODE " +
                  "WHERE s.ST_NAME LIKE ? " +
                  "ORDER BY b.BOM_ORDER, b.CREATE_DATE DESC";
        } else {
            // 기본값: 제품명으로 검색
            sql = "SELECT b.BOM_NO, b.STANDARD_CODE, s.ST_NAME, s.ST_TYPE, s.ST_UNIT, " +
                  "b.BOM_DESCRIPTION, b.BOM_TYPE, b.BOM_ORDER, b.BOM_IMAGE, " +
                  "b.CREATE_DATE, b.UPDATE_DATE " +
                  "FROM BOM b " +
                  "JOIN STANDARD s ON b.STANDARD_CODE = s.STANDARD_CODE " +
                  "WHERE s.ST_NAME LIKE ? " +
                  "ORDER BY b.BOM_ORDER, b.CREATE_DATE DESC";
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
    
    // 전체 필드 검색
    public List<BOMDTO> searchAllFields(String searchKeyword) {
        List<BOMDTO> bomList = new ArrayList<>();
        String sql = "SELECT b.BOM_NO, b.STANDARD_CODE, s.ST_NAME, s.ST_TYPE, s.ST_UNIT, " +
                    "b.BOM_DESCRIPTION, b.BOM_TYPE, b.BOM_ORDER, b.BOM_IMAGE, " +
                    "b.CREATE_DATE, b.UPDATE_DATE " +
                    "FROM BOM b " +
                    "JOIN STANDARD s ON b.STANDARD_CODE = s.STANDARD_CODE " +
                    "WHERE b.BOM_NO LIKE ? OR b.STANDARD_CODE LIKE ? OR s.ST_NAME LIKE ? " +
                    "ORDER BY b.BOM_ORDER, b.CREATE_DATE DESC";
        
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
    
    // BOM별 관련 자재 목록 조회 (JOIN 사용)
    public List<Map<String, Object>> getBOMMaterials(String bomNo) {
        List<Map<String, Object>> materialList = new ArrayList<>();
        String sql;
        
        // 현재 테이블 구조상 BOM과 MATERIAL을 직접 연결할 수 없으므로
        // 모든 원자재와 반제품 재고를 BOM 자재로 표시
        sql = "SELECT m.MATERIAL_CODE, s.ST_NAME as MATERIAL_NAME, s.ST_TYPE as MATERIAL_TYPE, " +
              "s.ST_UNIT, m.MA_QUANTITY as CURRENT_STOCK, " +
              "1.0 as BOM_QUANTITY, 'BOM 자재' as REMARK " +
              "FROM MATERIAL m " +
              "JOIN STANDARD s ON m.STANDARD_CODE = s.STANDARD_CODE " +
              "WHERE s.ST_TYPE IN ('RAW', 'SEMI') " +
              "ORDER BY s.ST_TYPE, s.ST_NAME";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> material = new HashMap<>();
                material.put("MATERIAL_CODE", rs.getString("MATERIAL_CODE"));
                material.put("MATERIAL_NAME", rs.getString("MATERIAL_NAME"));
                material.put("MATERIAL_TYPE", rs.getString("MATERIAL_TYPE"));
                material.put("ST_UNIT", rs.getString("ST_UNIT"));
                material.put("CURRENT_STOCK", rs.getDouble("CURRENT_STOCK"));
                material.put("BOM_QUANTITY", rs.getDouble("BOM_QUANTITY"));
                material.put("REMARK", rs.getString("REMARK"));
                
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
        String sql = "SELECT STANDARD_CODE, ST_TYPE, ST_NAME, ST_QUANTITY, ST_UNIT, CREATE_DATE, UPDATE_DATE " +
                    "FROM STANDARD " +
                    "ORDER BY ST_TYPE, ST_NAME";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                StandardDTO standard = new StandardDTO();
                standard.setStandardCode(rs.getString("STANDARD_CODE"));
                standard.setStType(rs.getString("ST_TYPE"));
                standard.setStName(rs.getString("ST_NAME"));
                standard.setStQuantity(rs.getInt("ST_QUANTITY"));
                standard.setStUnit(rs.getString("ST_UNIT"));
                standard.setCreateDate(rs.getDate("CREATE_DATE"));
                standard.setUpdateDate(rs.getDate("UPDATE_DATE"));
                
                standardList.add(standard);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return standardList;
    }
    
    
    // 라우팅별 BOM 목록 조회 (공정 관리와 연동)
    public List<BOMDTO> selectRoutingBOMs() {
        List<BOMDTO> bomList = new ArrayList<>();
        String sql = "SELECT s.STANDARD_CODE, s.ST_NAME, s.ST_TYPE, s.ST_UNIT, " +
                    "CASE " +
                    "WHEN s.ST_TYPE = 'SEMI' THEN '반제품' " +
                    "WHEN s.ST_TYPE = 'FINISH' THEN s.ST_NAME " +
                    "END as BOM_DESCRIPTION, " +
                    "CASE " +
                    "WHEN s.ST_TYPE = 'SEMI' THEN 4 " +
                    "WHEN s.ST_TYPE = 'FINISH' THEN 8 " +
                    "END as BOM_ORDER, " +
                    "'B' || LPAD(ROWNUM, 4, '0') as BOM_NO " +
                    "FROM STANDARD s " +
                    "WHERE s.ST_TYPE IN ('SEMI', 'FINISH') " +
                    "ORDER BY s.ST_TYPE, s.ST_NAME";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                BOMDTO bom = new BOMDTO();
                bom.setBomNo(rs.getString("BOM_NO"));
                bom.setStandardCode(rs.getString("STANDARD_CODE"));
                bom.setBomDescription(rs.getString("BOM_DESCRIPTION"));
                bom.setBomType("라우팅");
                bom.setBomOrder(rs.getInt("BOM_ORDER"));
                bom.setBomImage("routing.png");
                
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
    
    // 특정 라우팅의 공정별 자재 조회
    public List<BOMDTO> selectBOMStepsByRouting(String standardCode) {
        List<BOMDTO> bomList = new ArrayList<>();
        String sql = "SELECT p.PROCESS_NO as BOM_NO, p.STANDARD_CODE, s.ST_NAME, s.ST_TYPE, s.ST_UNIT, " +
                    "p.PR_DESCRIPTION as BOM_DESCRIPTION, p.PR_TYPE as BOM_TYPE, p.PR_ORDER as BOM_ORDER, " +
                    "p.PR_IMAGE as BOM_IMAGE, p.CREATE_DATE, p.UPDATE_DATE " +
                    "FROM PROCESS p " +
                    "JOIN STANDARD s ON p.STANDARD_CODE = s.STANDARD_CODE " +
                    "WHERE p.STANDARD_CODE = ? " +
                    "ORDER BY p.PR_ORDER ASC";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, standardCode);
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
    
    // 라우팅 개수 조회
    public int getBOMRoutingCount() {
        String sql = "SELECT COUNT(DISTINCT s.STANDARD_CODE) " +
                    "FROM STANDARD s " +
                    "WHERE s.ST_TYPE IN ('SEMI', 'FINISH')";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return 0;
    }
    
    // 라우팅 타입별 개수 조회
    public int getBOMRoutingCountByType(String type) {
        String sql = "SELECT COUNT(*) FROM STANDARD WHERE ST_TYPE = ?";
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
    
    
    // 특정 STANDARD_CODE의 모든 공정 삭제
    public int deleteProcessesByStandardCode(String standardCode) {
        String sql = "DELETE FROM PROCESS WHERE STANDARD_CODE = ?";
        int result = 0;
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, standardCode);
            result = pstmt.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, null);
        }
        
        return result;
    }
    
    // 특정 타입의 STANDARD 조회 (드롭다운용)
    public List<StandardDTO> selectStandardsByType(String types) {
        List<StandardDTO> standardList = new ArrayList<>();
        String sql = "SELECT STANDARD_CODE, ST_NAME, ST_TYPE, ST_UNIT, CREATE_DATE, UPDATE_DATE " +
                    "FROM STANDARD " +
                    "WHERE ST_TYPE IN ('SEMI', 'FINISH') " +
                    "ORDER BY ST_TYPE, ST_NAME";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                StandardDTO standard = new StandardDTO();
                standard.setStandardCode(rs.getString("STANDARD_CODE"));
                standard.setStName(rs.getString("ST_NAME"));
                standard.setStType(rs.getString("ST_TYPE"));
                standard.setStUnit(rs.getString("ST_UNIT"));
                standard.setCreateDate(rs.getDate("CREATE_DATE"));
                standard.setUpdateDate(rs.getDate("UPDATE_DATE"));
                
                standardList.add(standard);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return standardList;
    }
    
    // 라우팅 BOM 검색 - 전체 필드
    public List<BOMDTO> searchRoutingBOMs(String keyword) {
        List<BOMDTO> bomList = new ArrayList<>();
        String sql = "SELECT DISTINCT b.STANDARD_CODE, s.ST_NAME, s.ST_TYPE, " +
                    "b.BOM_DESCRIPTION, b.BOM_ORDER, b.BOM_NO " +
                    "FROM BOM b " +
                    "JOIN STANDARD s ON b.STANDARD_CODE = s.STANDARD_CODE " +
                    "WHERE (UPPER(b.BOM_NO) LIKE UPPER(?) OR " +
                    "      UPPER(b.STANDARD_CODE) LIKE UPPER(?) OR " +
                    "      UPPER(b.BOM_DESCRIPTION) LIKE UPPER(?) OR " +
                    "      UPPER(s.ST_NAME) LIKE UPPER(?)) " +
                    "ORDER BY s.ST_TYPE, b.BOM_ORDER";
        
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
                BOMDTO bom = new BOMDTO();
                bom.setStandardCode(rs.getString("STANDARD_CODE"));
                bom.setStName(rs.getString("ST_NAME"));
                bom.setStType(rs.getString("ST_TYPE"));
                bom.setBomDescription(rs.getString("BOM_DESCRIPTION"));
                bom.setBomOrder(rs.getInt("BOM_ORDER"));
                bom.setBomNo(rs.getString("BOM_NO"));
                
                bomList.add(bom);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return bomList;
    }
    
    // 라우팅 BOM 검색 - 특정 타입별
    public List<BOMDTO> searchRoutingBOMsByType(String searchType, String keyword) {
        List<BOMDTO> bomList = new ArrayList<>();
        String sql;
        String searchPattern = "%" + keyword + "%";
        
        if ("code".equals(searchType)) {
            // 라우팅번호로 검색
            sql = "SELECT DISTINCT b.STANDARD_CODE, s.ST_NAME, s.ST_TYPE, " +
                  "b.BOM_DESCRIPTION, b.BOM_ORDER, b.BOM_NO " +
                  "FROM BOM b " +
                  "JOIN STANDARD s ON b.STANDARD_CODE = s.STANDARD_CODE " +
                  "WHERE UPPER(b.BOM_NO) LIKE UPPER(?) " +
                  "ORDER BY s.ST_TYPE, b.BOM_ORDER";
        } else if ("name".equals(searchType)) {
            // 라우팅명으로 검색
            sql = "SELECT DISTINCT b.STANDARD_CODE, s.ST_NAME, s.ST_TYPE, " +
                  "b.BOM_DESCRIPTION, b.BOM_ORDER, b.BOM_NO " +
                  "FROM BOM b " +
                  "JOIN STANDARD s ON b.STANDARD_CODE = s.STANDARD_CODE " +
                  "WHERE UPPER(b.BOM_DESCRIPTION) LIKE UPPER(?) " +
                  "ORDER BY s.ST_TYPE, b.BOM_ORDER";
        } else {
            // 기본값: 전체 검색
            return searchRoutingBOMs(keyword);
        }
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, searchPattern);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                BOMDTO bom = new BOMDTO();
                bom.setStandardCode(rs.getString("STANDARD_CODE"));
                bom.setStName(rs.getString("ST_NAME"));
                bom.setStType(rs.getString("ST_TYPE"));
                bom.setBomDescription(rs.getString("BOM_DESCRIPTION"));
                bom.setBomOrder(rs.getInt("BOM_ORDER"));
                bom.setBomNo(rs.getString("BOM_NO"));
                
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