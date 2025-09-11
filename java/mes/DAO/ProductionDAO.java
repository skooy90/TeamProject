package mes.DAO;

import mes.DTO.ProductionDTO;
import mes.util.DBManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductionDAO {

    private ProductionDAO(){}
    private static final ProductionDAO instance = new ProductionDAO();
    public static ProductionDAO getInstance(){ return instance; }

    // 목록
    public List<ProductionDTO> findAll() {
        String sql = "SELECT production_no, standard_code, employee_no," +
                     " pr_start, pr_end, pr_target, pr_completed," +
                     " create_date, update_date" +
                     " FROM production ORDER BY create_date DESC";
        List<ProductionDTO> list = new ArrayList<>();
        try (Connection c = DBManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 접두 검색(생산번호/제품코드 2글자 이상)
    public List<ProductionDTO> searchByPrefix(String q) {
        String sql = "SELECT production_no, standard_code, employee_no," +
                     " pr_start, pr_end, pr_target, pr_completed," +
                     " create_date, update_date" +
                     " FROM production" +
                     " WHERE UPPER(production_no) LIKE UPPER(?)" +
                     "    OR UPPER(standard_code) LIKE UPPER(?)" +
                     " ORDER BY update_date DESC";
        List<ProductionDTO> list = new ArrayList<>();
        try (Connection c = DBManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            String like = q + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 단건
    public ProductionDTO findByNo(String no) {
        String sql = "SELECT production_no, standard_code, employee_no," +
                     " pr_start, pr_end, pr_target, pr_completed," +
                     " create_date, update_date" +
                     " FROM production WHERE production_no = ?";
        try (Connection c = DBManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, no);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

//    // 신규 번호(예: PRO00011)
//    public String nextProductionNo(Connection c) throws SQLException {
//        String sql = "SELECT 'PRO' || LPAD(SEQ_PRODUCTION_NO.NEXTVAL, 5, '0') AS NO FROM dual";
//        try (PreparedStatement ps = c.prepareStatement(sql);
//             ResultSet rs = ps.executeQuery()) {
//            rs.next();
//            return rs.getString(1);
//        }
//    }
//
//    // 등록
//    public int insert(ProductionDTO d) {
//        String sql = "INSERT INTO production(" +
//                     " production_no, standard_code, employee_no," +
//                     " pr_start, pr_end, pr_target, pr_completed," +
//                     " create_date, update_date)" +
//                     " VALUES(?, ?, ?, ?, ?, ?, ?, SYSDATE, SYSDATE)";
//        try (Connection c = DBManager.getConnection();
//             PreparedStatement ps = c.prepareStatement(sql)) {
//
//            String newNo = nextProductionNo(c);
//            ps.setString(1, newNo);
//            ps.setString(2, d.getStandardCode());
//            ps.setString(3, d.getEmployeeNo());
//            ps.setDate(4, d.getPrStart());
//            ps.setDate(5, d.getPrEnd());
//            ps.setInt(6, d.getPrTarget());
//            ps.setInt(7, d.getPrCompleted());
//
//            int affected = ps.executeUpdate();
//            d.setProductionNo(newNo); // 폼 리다이렉트 등에 쓰고 싶으면 여기에 저장
//            return affected;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return 0;
//        }
//    }

 // ProductionDAO.java (발췌)
    public boolean existsStandard(Connection conn, String code) throws SQLException {
        String sql = "SELECT 1 FROM STANDARD WHERE STANDARD_CODE = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean existsEmployee(Connection conn, String empNo) throws SQLException {
        String sql = "SELECT 1 FROM USERS WHERE EMPLOYEE_NO = ?"; // 사용자 테이블명/PK에 맞게
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, empNo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // 생산번호 생성 (시퀀스 사용)
    private String nextProductionNo(Connection conn) throws SQLException {
        String sql = "SELECT 'PRO' || LPAD(SEQ_PRODUCTION_NO.NEXTVAL, 5, '0') FROM DUAL";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getString(1);
        }
    }

    public int insert(ProductionDTO d) throws SQLException {
        try (Connection conn = DBManager.getConnection()) {
            conn.setAutoCommit(false);
            try {
                String std = d.getStandardCode() == null ? null : d.getStandardCode().trim().toUpperCase();
                String emp = d.getEmployeeNo()   == null ? null : d.getEmployeeNo().trim().toUpperCase();

                if (!existsStandard(conn, std)) {
                    throw new SQLException("STANDARD_CODE_NOT_FOUND");
                }
                if (!existsEmployee(conn, emp)) {
                    throw new SQLException("EMPLOYEE_NO_NOT_FOUND");
                }

                String proNo = nextProductionNo(conn);

                String sql = "INSERT INTO PRODUCTION(" +
                        "PRODUCTION_NO, STANDARD_CODE, EMPLOYEE_NO, PR_START, PR_END, PR_TARGET, PR_COMPLETED, CREATE_DATE, UPDATE_DATE" +
                        ") VALUES (?, ?, ?, ?, ?, ?, ?, SYSDATE, SYSDATE)";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, proNo);
                    ps.setString(2, std);
                    ps.setString(3, emp);
                    ps.setDate(4, d.getPrStart());
                    ps.setDate(5, d.getPrEnd());
                    ps.setInt(6, d.getPrTarget());
                    ps.setInt(7, d.getPrCompleted());
                    int r = ps.executeUpdate();
                    conn.commit();
                    return r;
                }
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    // 수정
    public int update(ProductionDTO d) {
        String sql = "UPDATE production SET" +
                     " standard_code=?, employee_no=?, pr_start=?, pr_end=?," +
                     " pr_target=?, pr_completed=?, update_date=SYSDATE" +
                     " WHERE production_no=?";
        try (Connection c = DBManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, d.getStandardCode());
            ps.setString(2, d.getEmployeeNo());
            ps.setDate(3, d.getPrStart());
            ps.setDate(4, d.getPrEnd());
            ps.setInt(5, d.getPrTarget());
            ps.setInt(6, d.getPrCompleted());
            ps.setString(7, d.getProductionNo());
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 삭제 (FK가 ON DELETE CASCADE가 아니면 아래 deleteCascade 사용)
    public int delete(String no) {
        String sql = "DELETE FROM production WHERE production_no = ?";
        try (Connection c = DBManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, no);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 필요 시: 품질→작업→생산 순으로 삭제
    public int deleteCascade(String no) {
        String delQuality =
            "DELETE FROM QUALITY WHERE WORK_NO IN (SELECT WORK_NO FROM WORK WHERE PRODUCTION_NO=?)";
        String delWork = "DELETE FROM WORK WHERE PRODUCTION_NO=?";
        String delProduction = "DELETE FROM PRODUCTION WHERE PRODUCTION_NO=?";

        try (Connection c = DBManager.getConnection()) {
            c.setAutoCommit(false);
            try (PreparedStatement ps1 = c.prepareStatement(delQuality);
                 PreparedStatement ps2 = c.prepareStatement(delWork);
                 PreparedStatement ps3 = c.prepareStatement(delProduction)) {

                ps1.setString(1, no); ps1.executeUpdate();
                ps2.setString(1, no); ps2.executeUpdate();
                ps3.setString(1, no);
                int affected = ps3.executeUpdate();

                c.commit();
                return affected;
            } catch (Exception e) {
                c.rollback();
                throw e;
            } finally {
                c.setAutoCommit(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private ProductionDTO map(ResultSet rs) throws SQLException {
        ProductionDTO d = new ProductionDTO();
        d.setProductionNo(rs.getString("production_no"));
        d.setStandardCode(rs.getString("standard_code"));
        d.setEmployeeNo(rs.getString("employee_no"));
        d.setPrStart(rs.getDate("pr_start"));
        d.setPrEnd(rs.getDate("pr_end"));
        d.setPrTarget(rs.getInt("pr_target"));
        d.setPrCompleted(rs.getInt("pr_completed"));
        d.setCreateDate(rs.getDate("create_date"));
        d.setUpdateDate(rs.getDate("update_date"));
        return d;
    }
}


