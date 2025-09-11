package mes.PROCESS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import mes.DTO.ProcessDTO;
import mes.DTO.StandardDTO;
import mes.util.DBManager;

public class ProcessDAO {
    
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
    
    // 라우팅별 공정 목록 조회 (4개 라우팅만)
    public List<ProcessDTO> selectRoutingProcesses() {
        List<ProcessDTO> processList = new ArrayList<>();
        String sql = "SELECT s.STANDARD_CODE, s.ST_NAME, s.ST_TYPE, s.ST_UNIT, " +
                    "CASE " +
                    "WHEN s.ST_TYPE = 'SEMI' THEN '반제품' " +
                    "WHEN s.ST_TYPE = 'FINISH' THEN s.ST_NAME " +
                    "END as PR_DESCRIPTION, " +
                    "CASE " +
                    "WHEN s.ST_TYPE = 'SEMI' THEN 4 " +
                    "WHEN s.ST_TYPE = 'FINISH' THEN 8 " +
                    "END as PR_ORDER, " +
                    "'PC' || LPAD(ROWNUM, 4, '0') as PROCESS_NO " +
                    "FROM STANDARD s " +
                    "WHERE s.ST_TYPE IN ('SEMI', 'FINISH') " +
                    "ORDER BY s.ST_TYPE, s.ST_NAME";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            System.out.println("SQL 쿼리 실행: " + sql);
            
            // ResultSet이 비어있는지 확인
            if (rs != null) {
                int count = 0;
                while (rs.next()) {
                    count++;
                    System.out.println("데이터 행 " + count + " 처리 중...");
                    
                    ProcessDTO process = new ProcessDTO();
                    process.setProcessNo(rs.getString("PROCESS_NO"));
                    process.setStandardCode(rs.getString("STANDARD_CODE"));
                    process.setPrDescription(rs.getString("PR_DESCRIPTION"));
                    process.setPrType("라우팅"); // 공정 유형을 라우팅으로 통일
                    process.setPrOrder(rs.getInt("PR_ORDER"));
                    process.setPrImage("routing.png");
                    
                    // JOIN된 데이터 저장
                    process.setStName(rs.getString("ST_NAME"));
                    process.setStType(rs.getString("ST_TYPE"));
                    process.setStUnit(rs.getString("ST_UNIT"));
                    
                    processList.add(process);
                }
                System.out.println("총 " + count + "개의 라우팅 데이터를 조회했습니다.");
            } else {
                System.out.println("ResultSet이 null입니다.");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("SQL 쿼리 실행 중 오류: " + sql);
            System.out.println("오류 메시지: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return processList;
    }
    
    // 특정 라우팅의 상세 공정 단계 조회
    public List<ProcessDTO> selectProcessStepsByRouting(String standardCode) {
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
    
    // 라우팅별 통계 조회
    public int getRoutingCount() {
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
    
    // 라우팅 공정 생성 (여러 단계를 한 번에 생성)
    public int insertRoutingProcesses(String standardCode, String routingType, int stepCount) {
        String[] stepDescriptions;
        String[] stepTypes;
        
        if ("SEMI".equals(routingType)) {
            // 반제품 라우팅: 4단계
            stepDescriptions = new String[]{"원료 혼합", "성분 배합", "건조", "베이스 포장"};
            stepTypes = new String[]{"혼합", "성형", "건조", "포장"};
        } else {
            // 완제품 라우팅: 8단계 (반제품 4단계 + 완제품 4단계)
            stepDescriptions = new String[]{"원료 혼합", "성분 배합", "건조", "베이스 포장", 
                                          "성분 추가", "최종 혼합", "완제품 성형", "완제품 포장"};
            stepTypes = new String[]{"혼합", "성형", "건조", "포장", 
                                   "혼합", "혼합", "성형", "포장"};
        }
        
        String sql = "INSERT INTO PROCESS (PROCESS_NO, STANDARD_CODE, PR_DESCRIPTION, PR_TYPE, PR_ORDER, PR_IMAGE, CREATE_DATE, UPDATE_DATE) " +
                    "VALUES ('PC' || LPAD(SEQ_PROCESS_NO.NEXTVAL, 4, '0'), ?, ?, ?, ?, ?, SYSDATE, SYSDATE)";
        
        try {
            conn = getConnection();
            conn.setAutoCommit(false); // 트랜잭션 시작
            
            pstmt = conn.prepareStatement(sql);
            
            for (int i = 0; i < stepCount; i++) {
                pstmt.setString(1, standardCode);
                pstmt.setString(2, stepDescriptions[i]);
                pstmt.setString(3, stepTypes[i]);
                pstmt.setInt(4, i + 1);
                pstmt.setString(5, stepTypes[i].toLowerCase() + ".png");
                
                pstmt.addBatch();
            }
            
            int[] results = pstmt.executeBatch();
            conn.commit(); // 트랜잭션 커밋
            
            return results.length;
            
        } catch (Exception e) {
            try {
                if (conn != null) {
                    conn.rollback(); // 오류 시 롤백
                }
            } catch (Exception rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
            return 0;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            closeResources(conn, pstmt, null);
        }
    }
    
    // 특정 STANDARD_CODE의 모든 공정 삭제
    public int deleteProcessesByStandardCode(String standardCode) {
        String sql = "DELETE FROM PROCESS WHERE STANDARD_CODE = ?";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, standardCode);
            
            return pstmt.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }
    
    // 라우팅 검색
    public List<ProcessDTO> searchRoutingProcesses(String searchKeyword) {
        List<ProcessDTO> processList = new ArrayList<>();
        String sql = "SELECT s.STANDARD_CODE, s.ST_NAME, s.ST_TYPE, s.ST_UNIT, " +
                    "CASE " +
                    "WHEN s.ST_TYPE = 'SEMI' THEN '반제품' " +
                    "WHEN s.ST_TYPE = 'FINISH' THEN s.ST_NAME " +
                    "END as PR_DESCRIPTION, " +
                    "CASE " +
                    "WHEN s.ST_TYPE = 'SEMI' THEN 4 " +
                    "WHEN s.ST_TYPE = 'FINISH' THEN 8 " +
                    "END as PR_ORDER, " +
                    "'PC' || LPAD(ROWNUM, 4, '0') as PROCESS_NO " +
                    "FROM STANDARD s " +
                    "WHERE s.ST_TYPE IN ('SEMI', 'FINISH') " +
                    "AND (s.ST_NAME LIKE ? OR s.STANDARD_CODE LIKE ?) " +
                    "ORDER BY s.ST_TYPE, s.ST_NAME";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            String searchPattern = "%" + searchKeyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                ProcessDTO process = new ProcessDTO();
                process.setProcessNo(rs.getString("PROCESS_NO"));
                process.setStandardCode(rs.getString("STANDARD_CODE"));
                process.setPrDescription(rs.getString("PR_DESCRIPTION"));
                process.setPrType("라우팅");
                process.setPrOrder(rs.getInt("PR_ORDER"));
                process.setPrImage("routing.png");
                
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
    
    // 라우팅 타입별 조회
    public List<ProcessDTO> selectRoutingProcessesByType(String type) {
        List<ProcessDTO> processList = new ArrayList<>();
        String sql = "SELECT s.STANDARD_CODE, s.ST_NAME, s.ST_TYPE, s.ST_UNIT, " +
                    "CASE " +
                    "WHEN s.ST_TYPE = 'SEMI' THEN '반제품' " +
                    "WHEN s.ST_TYPE = 'FINISH' THEN s.ST_NAME " +
                    "END as PR_DESCRIPTION, " +
                    "CASE " +
                    "WHEN s.ST_TYPE = 'SEMI' THEN 4 " +
                    "WHEN s.ST_TYPE = 'FINISH' THEN 8 " +
                    "END as PR_ORDER, " +
                    "'PC' || LPAD(ROWNUM, 4, '0') as PROCESS_NO " +
                    "FROM STANDARD s " +
                    "WHERE s.ST_TYPE = ? " +
                    "ORDER BY s.ST_NAME";
        
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
                process.setPrType("라우팅");
                process.setPrOrder(rs.getInt("PR_ORDER"));
                process.setPrImage("routing.png");
                
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
    
    // 라우팅 타입별 개수 조회
    public int getRoutingCountByType(String type) {
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
}
