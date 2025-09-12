package mes.DAO;

import mes.DTO.UsersDTO;
import mes.util.DBManager;
import java.sql.*;
import java.util.*;

public class UserManagementDAO {
    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;
    
    // DB 연결
    private Connection getConnection() {
        return DBManager.getConnection();
    }
    
    // 리소스 해제
    private void closeResources(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // 전체 사용자 목록 조회
    public List<UsersDTO> selectAllUsers() {
        List<UsersDTO> userList = new ArrayList<>();
        String sql = "SELECT * FROM USERS ORDER BY CREATE_DATE DESC";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                UsersDTO user = new UsersDTO();
                user.setEmployeeNo(rs.getString("EMPLOYEE_NO"));
                user.setUsName(rs.getString("US_NAME"));
                user.setUsPosition(rs.getString("US_POSITION"));
                user.setUsPassword(rs.getString("US_PASSWORD"));
                user.setUsAuthority(rs.getString("US_AUTHORITY"));
                user.setUsPsUpStatus(rs.getInt("US_PS_UP_STATUS"));
                user.setCreateDate(rs.getDate("CREATE_DATE"));
                user.setUpdateDate(rs.getDate("UPDATE_DATE"));
                userList.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return userList;
    }
    
    // 사용자 검색 (이름 또는 사원번호)
    public List<UsersDTO> searchUsers(String searchType, String keyword) {
        List<UsersDTO> userList = new ArrayList<>();
        String sql = "";
        
        if ("name".equals(searchType)) {
            sql = "SELECT * FROM USERS WHERE US_NAME LIKE ? ORDER BY CREATE_DATE DESC";
        } else if ("employeeNo".equals(searchType)) {
            sql = "SELECT * FROM USERS WHERE EMPLOYEE_NO LIKE ? ORDER BY CREATE_DATE DESC";
        } else {
            sql = "SELECT * FROM USERS WHERE (US_NAME LIKE ? OR EMPLOYEE_NO LIKE ?) ORDER BY CREATE_DATE DESC";
        }
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            
            if ("all".equals(searchType)) {
                pstmt.setString(1, "%" + keyword + "%");
                pstmt.setString(2, "%" + keyword + "%");
            } else {
                pstmt.setString(1, "%" + keyword + "%");
            }
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                UsersDTO user = new UsersDTO();
                user.setEmployeeNo(rs.getString("EMPLOYEE_NO"));
                user.setUsName(rs.getString("US_NAME"));
                user.setUsPosition(rs.getString("US_POSITION"));
                user.setUsPassword(rs.getString("US_PASSWORD"));
                user.setUsAuthority(rs.getString("US_AUTHORITY"));
                user.setUsPsUpStatus(rs.getInt("US_PS_UP_STATUS"));
                user.setCreateDate(rs.getDate("CREATE_DATE"));
                user.setUpdateDate(rs.getDate("UPDATE_DATE"));
                userList.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return userList;
    }
    
    // 권한별 사용자 조회
    public List<UsersDTO> selectUsersByAuthority(String authority) {
        List<UsersDTO> userList = new ArrayList<>();
        String sql = "SELECT * FROM USERS WHERE US_AUTHORITY = ? ORDER BY CREATE_DATE DESC";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, authority);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                UsersDTO user = new UsersDTO();
                user.setEmployeeNo(rs.getString("EMPLOYEE_NO"));
                user.setUsName(rs.getString("US_NAME"));
                user.setUsPosition(rs.getString("US_POSITION"));
                user.setUsPassword(rs.getString("US_PASSWORD"));
                user.setUsAuthority(rs.getString("US_AUTHORITY"));
                user.setUsPsUpStatus(rs.getInt("US_PS_UP_STATUS"));
                user.setCreateDate(rs.getDate("CREATE_DATE"));
                user.setUpdateDate(rs.getDate("UPDATE_DATE"));
                userList.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return userList;
    }
    
    // 직급별 사용자 조회
    public List<UsersDTO> selectUsersByPosition(String position) {
        List<UsersDTO> userList = new ArrayList<>();
        String sql = "SELECT * FROM USERS WHERE US_POSITION = ? ORDER BY CREATE_DATE DESC";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, position);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                UsersDTO user = new UsersDTO();
                user.setEmployeeNo(rs.getString("EMPLOYEE_NO"));
                user.setUsName(rs.getString("US_NAME"));
                user.setUsPosition(rs.getString("US_POSITION"));
                user.setUsPassword(rs.getString("US_PASSWORD"));
                user.setUsAuthority(rs.getString("US_AUTHORITY"));
                user.setUsPsUpStatus(rs.getInt("US_PS_UP_STATUS"));
                user.setCreateDate(rs.getDate("CREATE_DATE"));
                user.setUpdateDate(rs.getDate("UPDATE_DATE"));
                userList.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return userList;
    }
    
    // 사용자 등록
    public int insertUser(UsersDTO user) {
        String sql = "INSERT INTO USERS (EMPLOYEE_NO, US_NAME, US_POSITION, US_PASSWORD, US_AUTHORITY, US_PS_UP_STATUS, CREATE_DATE) VALUES (?, ?, ?, ?, ?, ?, SYSDATE)";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getEmployeeNo());
            pstmt.setString(2, user.getUsName());
            pstmt.setString(3, user.getUsPosition());
            pstmt.setString(4, user.getUsPassword());
            pstmt.setString(5, user.getUsAuthority());
            pstmt.setInt(6, user.getUsPsUpStatus());
            
            int result = pstmt.executeUpdate();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }
    
    // 사용자 수정
    public int updateUser(UsersDTO user) {
        String sql = "UPDATE USERS SET US_NAME = ?, US_POSITION = ?, US_AUTHORITY = ?, UPDATE_DATE = SYSDATE WHERE EMPLOYEE_NO = ?";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getUsName());
            pstmt.setString(2, user.getUsPosition());
            pstmt.setString(3, user.getUsAuthority());
            pstmt.setString(4, user.getEmployeeNo());
            
            int result = pstmt.executeUpdate();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }
    
    // 비밀번호 초기화 (사원번호로 초기화)
    public int resetPassword(String employeeNo) {
        String sql = "UPDATE USERS SET US_PASSWORD = ?, US_PS_UP_STATUS = 0, UPDATE_DATE = SYSDATE WHERE EMPLOYEE_NO = ?";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, employeeNo);  // 비밀번호를 사원번호로 설정
            pstmt.setString(2, employeeNo);  // WHERE 조건의 사원번호
            
            int result = pstmt.executeUpdate();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }
    
    // 최대 사원번호 조회
    public String getMaxEmployeeNo() {
        String sql = "SELECT MAX(EMPLOYEE_NO) FROM USERS WHERE EMPLOYEE_NO LIKE 'K%'";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return null;
    }
    
    // 사용자 통계
    public Map<String, Integer> getUserStatistics() {
        Map<String, Integer> stats = new HashMap<>();
        
        try {
            conn = getConnection();
            
            // 전체 사용자 수
            pstmt = conn.prepareStatement("SELECT COUNT(*) FROM USERS");
            rs = pstmt.executeQuery();
            if (rs.next()) stats.put("totalUsers", rs.getInt(1));
            
            // 관리자 수
            pstmt = conn.prepareStatement("SELECT COUNT(*) FROM USERS WHERE US_AUTHORITY = 'ADMIN'");
            rs = pstmt.executeQuery();
            if (rs.next()) stats.put("adminUsers", rs.getInt(1));
            
            // 일반사원 수
            pstmt = conn.prepareStatement("SELECT COUNT(*) FROM USERS WHERE US_AUTHORITY = 'EMPLOYEE'");
            rs = pstmt.executeQuery();
            if (rs.next()) stats.put("employeeUsers", rs.getInt(1));
            
            // 비밀번호 변경 완료 수
            pstmt = conn.prepareStatement("SELECT COUNT(*) FROM USERS WHERE US_PS_UP_STATUS = 1");
            rs = pstmt.executeQuery();
            if (rs.next()) stats.put("passwordChanged", rs.getInt(1));
            
            // 비밀번호 변경 필요 수
            pstmt = conn.prepareStatement("SELECT COUNT(*) FROM USERS WHERE US_PS_UP_STATUS = 0");
            rs = pstmt.executeQuery();
            if (rs.next()) stats.put("passwordPending", rs.getInt(1));
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return stats;
    }
}
