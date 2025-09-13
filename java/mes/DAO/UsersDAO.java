// src/main/java/com/mes/DAO/UsersDAO.java
package mes.DAO;

import mes.DTO.UsersDTO;
import mes.security.PasswordUtil;
import mes.util.DBManager;

import java.sql.*;

public class UsersDAO {
    private static final UsersDAO instance = new UsersDAO();
    private UsersDAO() {}
    public static UsersDAO getInstance() { return instance; }

    /** 사번으로만 조회하여 저장된 PW(해시/평문 포함)를 가져온다 */
    public UsersDTO findByEmployeeNo(String employeeNo) {
        String sql =
            "SELECT EMPLOYEE_NO, US_NAME, US_POSITION, US_PASSWORD, " +
            "       US_AUTHORITY, US_PS_UP_STATUS, CREATE_DATE, UPDATE_DATE " +
            "  FROM USERS WHERE EMPLOYEE_NO = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, employeeNo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    UsersDTO u = new UsersDTO();
                    u.setEmployeeNo(rs.getString("EMPLOYEE_NO"));
                    u.setUsName(rs.getString("US_NAME"));
                    u.setUsPosition(rs.getString("US_POSITION"));
                    u.setUsPassword(rs.getString("US_PASSWORD")); // ← 해시/평문 그대로
                    u.setUsAuthority(rs.getString("US_AUTHORITY"));
                    u.setUsPsUpStatus(rs.getInt("US_PS_UP_STATUS"));
                    u.setCreateDate(rs.getDate("CREATE_DATE"));
                    u.setUpdateDate(rs.getDate("UPDATE_DATE"));
                    return u;
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    /** 비밀번호 변경은 항상 해시 저장 */
    public int updatePassword(String employeeNo, String plainPw) {
        String hashed = PasswordUtil.hash(plainPw);
        String sql = "UPDATE USERS SET US_PASSWORD=?, US_PS_UP_STATUS=1, UPDATE_DATE=SYSDATE WHERE EMPLOYEE_NO=?";
        try (Connection c = DBManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, hashed);
            ps.setString(2, employeeNo);
            return ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); return 0; }
    }
}
