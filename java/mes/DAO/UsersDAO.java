// src/main/java/com/mes/DAO/UsersDAO.java
package mes.DAO;

import mes.DTO.UsersDTO;
import mes.util.DBManager;

import java.sql.*;

public class UsersDAO {
    private static final UsersDAO instance = new UsersDAO();
    private UsersDAO() {}
    public static UsersDAO getInstance() { return instance; }

    /** 아이디(사번) + 비밀번호로 사용자 조회 (일치하면 UsersDTO 리턴, 아니면 null) */
    public UsersDTO login(String employeeNo, String plainPw) {
        String sql =
            "SELECT EMPLOYEE_NO, US_NAME, US_POSITION, US_PASSWORD, " +
            "       US_AUTHORITY, US_PS_UP_STATUS, CREATE_DATE, UPDATE_DATE " +
            "  FROM USERS " +
            " WHERE EMPLOYEE_NO = ? AND US_PASSWORD = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, employeeNo);
            ps.setString(2, plainPw);               // ★ 현재는 평문 PW (향후 해시 권장)

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    UsersDTO u = new UsersDTO();
                    u.setEmployeeNo(rs.getString("EMPLOYEE_NO"));
                    u.setUsName(rs.getString("US_NAME"));
                    u.setUsPosition(rs.getString("US_POSITION"));
                    u.setUsPassword(rs.getString("US_PASSWORD"));
                    u.setUsAuthority(rs.getString("US_AUTHORITY"));
                    u.setUsPsUpStatus(rs.getInt("US_PS_UP_STATUS"));
                    u.setCreateDate(rs.getDate("CREATE_DATE"));
                    u.setUpdateDate(rs.getDate("UPDATE_DATE"));
                    return u;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
