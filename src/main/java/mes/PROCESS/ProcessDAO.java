package mes.PROCESS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import mes.DTO.ProcessDTO;
import mes.DTO.StandardDTO;
import util.DBManager;

public class ProcessDAO {
    
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
    
    // C - 공정 등록
    public int insertProcess(ProcessDTO process) {
        String sql = "INSERT INTO PROCESS (PROCESS_NO, STANDARD_CODE, PR_DESCRIPTION, PR_TYPE, PR_ORDER, PR_IMAGE, CREATE_DATE, UPDATE_DATE) " +
                    "VALUES ('PC' || LPAD(SEQ_PROCESS_NO.NEXTVAL, 4, '0'), ?, ?, ?, ?, ?, SYSDATE, SYSDATE)";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, process.getStandardCode());
            pstmt.setString(2, process.getPrDescription());
            pstmt.setString(3, process.getPrType());
            pstmt.setInt(4, process.getPrOrder());
            pstmt.setString(5, process.getPrImage());
            
            int result = pstmt.executeUpdate();
            return result;
            
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }
    
    // R - 전체 공정 목록 조회 (JOIN)
    public List<ProcessDTO> selectAllProcesses() {
        List<ProcessDTO> processList = new ArrayList<>();
        String sql = "SELECT p.PROCESS_NO, p.STANDARD_CODE, s.ST_NAME, s.ST_TYPE, s.ST_UNIT, " +
                    "p.PR_DESCRIPTION, p.PR_TYPE, p.PR_ORDER, p.PR_IMAGE, " +
                    "p.CREATE_DATE, p.UPDATE_DATE " +
                    "FROM PROCESS p " +
                    "JOIN STANDARD s ON p.STANDARD_CODE = s.STANDARD_CODE " +
                    "ORDER BY p.PR_ORDER ASC";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                ProcessDTO process = new ProcessDTO();
                process.setProcessNo(rs.getString("PROCESS_NO"));
                process.setStandardCode(rs.getString("STANDARD_CODE"));
                process.setPrDescription(rs.getString("PR_DESCRIPTION"));
                process.setPrType(rs.getString("PR_TYPE"));
                process.setPrOrder(rs.getInt("PR_ORDER"));
                process.setPrImage(rs.getString("PR_IMAGE"));
                process.setCreateDate(rs.getDate("CREATE_DATE"));
                process.setUpdateDate(rs.getDate("UPDATE_DATE"));
                
                // JOIN된 데이터 저장
                process.setStName(rs.getString("ST_NAME"));
                process.setStType(rs.getString("ST_TYPE"));
                process.setStUnit(rs.getString("ST_UNIT"));
                
                processList.add(process);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return processList;
    }
    
    // R - 공정번호로 특정 공정 조회
    public ProcessDTO selectProcessByNo(String processNo) {
        ProcessDTO process = null;
        String sql = "SELECT p.PROCESS_NO, p.STANDARD_CODE, s.ST_NAME, s.ST_TYPE, s.ST_UNIT, " +
                    "p.PR_DESCRIPTION, p.PR_TYPE, p.PR_ORDER, p.PR_IMAGE, " +
                    "p.CREATE_DATE, p.UPDATE_DATE " +
                    "FROM PROCESS p " +
                    "JOIN STANDARD s ON p.STANDARD_CODE = s.STANDARD_CODE " +
                    "WHERE p.PROCESS_NO = ?";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, processNo);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                process = new ProcessDTO();
                process.setProcessNo(rs.getString("PROCESS_NO"));
                process.setStandardCode(rs.getString("STANDARD_CODE"));
                process.setPrDescription(rs.getString("PR_DESCRIPTION"));
                process.setPrType(rs.getString("PR_TYPE"));
                process.setPrOrder(rs.getInt("PR_ORDER"));
                process.setPrImage(rs.getString("PR_IMAGE"));
                process.setCreateDate(rs.getDate("CREATE_DATE"));
                process.setUpdateDate(rs.getDate("UPDATE_DATE"));
                
                // JOIN된 데이터 저장
                process.setStName(rs.getString("ST_NAME"));
                process.setStType(rs.getString("ST_TYPE"));
                process.setStUnit(rs.getString("ST_UNIT"));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return process;
    }
    
    // R - 제품코드별 공정 조회
    public List<ProcessDTO> selectProcessesByStandardCode(String standardCode) {
        List<ProcessDTO> processList = new ArrayList<>();
        String sql = "SELECT p.PROCESS_NO, p.STANDARD_CODE, s.ST_NAME, s.ST_TYPE, s.ST_UNIT, " +
                    "p.PR_DESCRIPTION, p.PR_TYPE, p.PR_ORDER, p.PR_IMAGE, " +
                    "p.CREATE_DATE, p.UPDATE_DATE " +
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
                ProcessDTO process = new ProcessDTO();
                process.setProcessNo(rs.getString("PROCESS_NO"));
                process.setStandardCode(rs.getString("STANDARD_CODE"));
                process.setPrDescription(rs.getString("PR_DESCRIPTION"));
                process.setPrType(rs.getString("PR_TYPE"));
                process.setPrOrder(rs.getInt("PR_ORDER"));
                process.setPrImage(rs.getString("PR_IMAGE"));
                process.setCreateDate(rs.getDate("CREATE_DATE"));
                process.setUpdateDate(rs.getDate("UPDATE_DATE"));
                
                // JOIN된 데이터 저장
                process.setStName(rs.getString("ST_NAME"));
                process.setStType(rs.getString("ST_TYPE"));
                process.setStUnit(rs.getString("ST_UNIT"));
                
                processList.add(process);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return processList;
    }
    
    // R - 제품 유형별 공정 조회
    public List<ProcessDTO> selectProcessesByType(String type) {
        List<ProcessDTO> processList = new ArrayList<>();
        String sql = "SELECT p.PROCESS_NO, p.STANDARD_CODE, s.ST_NAME, s.ST_TYPE, s.ST_UNIT, " +
                    "p.PR_DESCRIPTION, p.PR_TYPE, p.PR_ORDER, p.PR_IMAGE, " +
                    "p.CREATE_DATE, p.UPDATE_DATE " +
                    "FROM PROCESS p " +
                    "JOIN STANDARD s ON p.STANDARD_CODE = s.STANDARD_CODE " +
                    "WHERE s.ST_TYPE = ? " +
                    "ORDER BY p.PR_ORDER ASC";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, type);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                ProcessDTO process = new ProcessDTO();
                process.setProcessNo(rs.getString("PROCESS_NO"));
                process.setStandardCode(rs.getString("STANDARD_CODE"));
                process.setPrDescription(rs.getString("PR_DESCRIPTION"));
                process.setPrType(rs.getString("PR_TYPE"));
                process.setPrOrder(rs.getInt("PR_ORDER"));
                process.setPrImage(rs.getString("PR_IMAGE"));
                process.setCreateDate(rs.getDate("CREATE_DATE"));
                process.setUpdateDate(rs.getDate("UPDATE_DATE"));
                
                // JOIN된 데이터 저장
                process.setStName(rs.getString("ST_NAME"));
                process.setStType(rs.getString("ST_TYPE"));
                process.setStUnit(rs.getString("ST_UNIT"));
                
                processList.add(process);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return processList;
    }
    
    // U - 공정 정보 수정
    public int updateProcess(ProcessDTO process) {
        String sql = "UPDATE PROCESS SET STANDARD_CODE = ?, PR_DESCRIPTION = ?, PR_TYPE = ?, PR_ORDER = ?, PR_IMAGE = ?, UPDATE_DATE = SYSDATE " +
                    "WHERE PROCESS_NO = ?";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, process.getStandardCode());
            pstmt.setString(2, process.getPrDescription());
            pstmt.setString(3, process.getPrType());
            pstmt.setInt(4, process.getPrOrder());
            pstmt.setString(5, process.getPrImage());
            pstmt.setString(6, process.getProcessNo());
            
            int result = pstmt.executeUpdate();
            return result;
            
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }
    
    // D - 공정 삭제
    public int deleteProcess(String processNo) {
        String sql = "DELETE FROM PROCESS WHERE PROCESS_NO = ?";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, processNo);
            
            int result = pstmt.executeUpdate();
            return result;
            
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }
    
    // 통계 조회 - 전체 공정 개수
    public int getTotalProcessCount() {
        String sql = "SELECT COUNT(*) FROM PROCESS";
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
    
    // 통계 조회 - 제품 유형별 공정 개수
    public int getProcessCountByType(String type) {
        String sql = "SELECT COUNT(*) FROM PROCESS p JOIN STANDARD s ON p.STANDARD_CODE = s.STANDARD_CODE WHERE s.ST_TYPE = ?";
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
    
    // 통계 조회 - 공정 유형별 개수
    public int getProcessCountByProcessType(String processType) {
        String sql = "SELECT COUNT(*) FROM PROCESS WHERE PR_TYPE = ?";
        int count = 0;
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, processType);
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
    public List<ProcessDTO> searchProcesses(String searchType, String searchKeyword) {
        List<ProcessDTO> processList = new ArrayList<>();
        String sql;
        
        if ("code".equals(searchType)) {
            // 공정번호로 검색
            sql = "SELECT p.PROCESS_NO, p.STANDARD_CODE, s.ST_NAME, s.ST_TYPE, s.ST_UNIT, " +
                  "p.PR_DESCRIPTION, p.PR_TYPE, p.PR_ORDER, p.PR_IMAGE, " +
                  "p.CREATE_DATE, p.UPDATE_DATE " +
                  "FROM PROCESS p " +
                  "JOIN STANDARD s ON p.STANDARD_CODE = s.STANDARD_CODE " +
                  "WHERE p.PROCESS_NO LIKE ? " +
                  "ORDER BY p.PR_ORDER ASC";
        } else if ("name".equals(searchType)) {
            // 제품명으로 검색
            sql = "SELECT p.PROCESS_NO, p.STANDARD_CODE, s.ST_NAME, s.ST_TYPE, s.ST_UNIT, " +
                  "p.PR_DESCRIPTION, p.PR_TYPE, p.PR_ORDER, p.PR_IMAGE, " +
                  "p.CREATE_DATE, p.UPDATE_DATE " +
                  "FROM PROCESS p " +
                  "JOIN STANDARD s ON p.STANDARD_CODE = s.STANDARD_CODE " +
                  "WHERE s.ST_NAME LIKE ? " +
                  "ORDER BY p.PR_ORDER ASC";
        } else {
            // 기본값: 제품명으로 검색
            sql = "SELECT p.PROCESS_NO, p.STANDARD_CODE, s.ST_NAME, s.ST_TYPE, s.ST_UNIT, " +
                  "p.PR_DESCRIPTION, p.PR_TYPE, p.PR_ORDER, p.PR_IMAGE, " +
                  "p.CREATE_DATE, p.UPDATE_DATE " +
                  "FROM PROCESS p " +
                  "JOIN STANDARD s ON p.STANDARD_CODE = s.STANDARD_CODE " +
                  "WHERE s.ST_NAME LIKE ? " +
                  "ORDER BY p.PR_ORDER ASC";
        }
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + searchKeyword + "%");
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                ProcessDTO process = new ProcessDTO();
                process.setProcessNo(rs.getString("PROCESS_NO"));
                process.setStandardCode(rs.getString("STANDARD_CODE"));
                process.setPrDescription(rs.getString("PR_DESCRIPTION"));
                process.setPrType(rs.getString("PR_TYPE"));
                process.setPrOrder(rs.getInt("PR_ORDER"));
                process.setPrImage(rs.getString("PR_IMAGE"));
                process.setCreateDate(rs.getDate("CREATE_DATE"));
                process.setUpdateDate(rs.getDate("UPDATE_DATE"));
                
                // JOIN된 데이터 저장
                process.setStName(rs.getString("ST_NAME"));
                process.setStType(rs.getString("ST_TYPE"));
                process.setStUnit(rs.getString("ST_UNIT"));
                
                processList.add(process);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return processList;
    }
    
    // 전체 검색 메서드 (공정번호, 제품코드, 제품명 모두 검색)
    public List<ProcessDTO> searchAllFields(String searchKeyword) {
        List<ProcessDTO> processList = new ArrayList<>();
        String sql = "SELECT p.PROCESS_NO, p.STANDARD_CODE, s.ST_NAME, s.ST_TYPE, s.ST_UNIT, " +
                    "p.PR_DESCRIPTION, p.PR_TYPE, p.PR_ORDER, p.PR_IMAGE, " +
                    "p.CREATE_DATE, p.UPDATE_DATE " +
                    "FROM PROCESS p " +
                    "JOIN STANDARD s ON p.STANDARD_CODE = s.STANDARD_CODE " +
                    "WHERE p.PROCESS_NO LIKE ? " +
                    "   OR p.STANDARD_CODE LIKE ? " +
                    "   OR s.ST_NAME LIKE ? " +
                    "ORDER BY p.PR_ORDER ASC";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            String keyword = "%" + searchKeyword + "%";
            pstmt.setString(1, keyword);  // 공정번호
            pstmt.setString(2, keyword);  // 제품코드
            pstmt.setString(3, keyword);  // 제품명
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                ProcessDTO process = new ProcessDTO();
                process.setProcessNo(rs.getString("PROCESS_NO"));
                process.setStandardCode(rs.getString("STANDARD_CODE"));
                process.setPrDescription(rs.getString("PR_DESCRIPTION"));
                process.setPrType(rs.getString("PR_TYPE"));
                process.setPrOrder(rs.getInt("PR_ORDER"));
                process.setPrImage(rs.getString("PR_IMAGE"));
                process.setCreateDate(rs.getDate("CREATE_DATE"));
                process.setUpdateDate(rs.getDate("UPDATE_DATE"));
                
                // JOIN된 데이터 저장
                process.setStName(rs.getString("ST_NAME"));
                process.setStType(rs.getString("ST_TYPE"));
                process.setStUnit(rs.getString("ST_UNIT"));
                
                processList.add(process);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return processList;
    }
}
