// src/main/java/com/mes/DAO/QualityDAO.java
package com.mes.DAO;

import com.mes.DTO.QualityDTO;
import com.mes.util.DBManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QualityDAO {

    private QualityDAO() {}
    private static final QualityDAO instance = new QualityDAO();
    public static QualityDAO getInstance() { return instance; }

    private QualityDTO map(ResultSet rs) throws Exception {
        QualityDTO d = new QualityDTO();
        d.setQualityNo(rs.getString("QUALITY_NO"));
        d.setWorkNo(rs.getString("WORK_NO"));
        d.setStandardCode(rs.getString("STANDARD_CODE"));
        d.setEmployeeNo(rs.getString("EMPLOYEE_NO"));
        d.setQuResult(rs.getString("QU_RESULT"));
        d.setQuQuantity(rs.getInt("QU_QUANTITY"));
        d.setQuManufactureDate(rs.getDate("QU_MANUFACTURE_DATE"));
        d.setDefectQuantity(rs.getInt("DEFECT_QUANTITY"));
        d.setInspectionDate(rs.getTimestamp("INSPECTION_DATE"));
        d.setCreateDate(rs.getDate("CREATE_DATE"));
        d.setUpdateDate(rs.getDate("UPDATE_DATE"));
        return d;
    }

    public List<QualityDTO> findAll() {
        String sql = "SELECT * FROM QUALITY ORDER BY CREATE_DATE DESC";
        List<QualityDTO> list = new ArrayList<>();
        try (Connection c = DBManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public List<QualityDTO> searchByPrefix(String q) {
        String like = q + "%";
        String sql = "SELECT * FROM QUALITY " +
                     "WHERE UPPER(QUALITY_NO) LIKE UPPER(?) " +
                     "   OR UPPER(WORK_NO)    LIKE UPPER(?) " +
                     "ORDER BY UPDATE_DATE DESC";
        List<QualityDTO> list = new ArrayList<>();
        try (Connection c = DBManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, like);
            ps.setString(2, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public QualityDTO findByNo(String no) {
        String sql = "SELECT * FROM QUALITY WHERE QUALITY_NO = ?";
        try (Connection c = DBManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, no);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    // INSERT: 키는 시퀀스로 생성
    public int insert(QualityDTO d) {
        String sql = "INSERT INTO QUALITY (" +
                "QUALITY_NO, WORK_NO, STANDARD_CODE, EMPLOYEE_NO, " +
                "QU_RESULT, QU_QUANTITY, QU_MANUFACTURE_DATE, DEFECT_QUANTITY, " +
                "INSPECTION_DATE, CREATE_DATE, UPDATE_DATE" +
                ") VALUES (" +
                "'I' || LPAD(SEQ_QUALITY_NO.NEXTVAL, 4, '0'), ?, ?, ?, " +
                "?, ?, ?, ?, ?, SYSDATE, SYSDATE)";
        try (Connection c = DBManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            int i=1;
            ps.setString(i++, d.getWorkNo());
            ps.setString(i++, d.getStandardCode());
            ps.setString(i++, d.getEmployeeNo());
            ps.setString(i++, d.getQuResult());
            ps.setInt(i++, d.getQuQuantity());
            ps.setDate(i++, d.getQuManufactureDate());
            ps.setInt(i++, d.getDefectQuantity());
            ps.setTimestamp(i++, d.getInspectionDate());
            return ps.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException fk) {
            // FK 불일치 시 원인 파악에 도움되는 메시지
            System.err.println("[QUALITY INSERT FK ERROR] " + fk.getMessage());
            fk.printStackTrace();
            return 0;
        } catch (Exception e) { e.printStackTrace(); return 0; }
    }

    public int update(QualityDTO d) {
        String sql = "UPDATE QUALITY SET " +
                "WORK_NO=?, STANDARD_CODE=?, EMPLOYEE_NO=?, " +
                "QU_RESULT=?, QU_QUANTITY=?, QU_MANUFACTURE_DATE=?, DEFECT_QUANTITY=?, " +
                "INSPECTION_DATE=?, UPDATE_DATE=SYSDATE " +
                "WHERE QUALITY_NO=?";
        try (Connection c = DBManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            int i=1;
            ps.setString(i++, d.getWorkNo());
            ps.setString(i++, d.getStandardCode());
            ps.setString(i++, d.getEmployeeNo());
            ps.setString(i++, d.getQuResult());
            ps.setInt(i++, d.getQuQuantity());
            ps.setDate(i++, d.getQuManufactureDate());
            ps.setInt(i++, d.getDefectQuantity());
            ps.setTimestamp(i++, d.getInspectionDate());
            ps.setString(i++, d.getQualityNo());
            return ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); return 0; }
    }

    public int delete(String no) {
        String sql = "DELETE FROM QUALITY WHERE QUALITY_NO = ?";
        try (Connection c = DBManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, no);
            return ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); return 0; }
    }
}
