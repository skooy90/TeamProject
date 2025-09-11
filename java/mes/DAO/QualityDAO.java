// src/main/java/com/mes/DAO/QualityDAO.java
package mes.DAO;

import mes.DTO.QualityDTO;
import mes.util.DBManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String sql =
            "SELECT q.QUALITY_NO, q.WORK_NO, q.STANDARD_CODE, q.EMPLOYEE_NO, " +
            "       q.QU_RESULT, q.QU_QUANTITY, q.DEFECT_QUANTITY, " +
            "       q.INSPECTION_DATE, q.CREATE_DATE, q.UPDATE_DATE, " +
            "       s.ST_NAME " +                    // ✅ 제품명
            "  FROM QUALITY q " +
            "  JOIN STANDARD s ON s.STANDARD_CODE = q.STANDARD_CODE " + // 또는 WORK를 경유해도 됨
            " ORDER BY q.CREATE_DATE DESC";
        List<QualityDTO> list = new ArrayList<>();
        try (Connection c = DBManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                QualityDTO d = new QualityDTO();
                d.setQualityNo(rs.getString("QUALITY_NO"));
                d.setWorkNo(rs.getString("WORK_NO"));
                d.setStandardCode(rs.getString("STANDARD_CODE"));
                d.setEmployeeNo(rs.getString("EMPLOYEE_NO"));
                d.setQuResult(rs.getString("QU_RESULT"));
                d.setQuQuantity(rs.getInt("QU_QUANTITY"));
                d.setDefectQuantity(rs.getInt("DEFECT_QUANTITY"));
                d.setInspectionDate(rs.getTimestamp("INSPECTION_DATE"));
                d.setCreateDate(rs.getDate("CREATE_DATE"));
                d.setUpdateDate(rs.getDate("UPDATE_DATE"));
                d.setStName(rs.getString("ST_NAME"));           // ✅ 제품명 세팅
                list.add(d);
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
        String sql =
            "INSERT INTO QUALITY (" +
            "  QUALITY_NO, WORK_NO, STANDARD_CODE, EMPLOYEE_NO," +
            "  QU_RESULT, QU_QUANTITY, DEFECT_QUANTITY," +
            "  QU_MANUFACTURE_DATE, INSPECTION_DATE, CREATE_DATE, UPDATE_DATE" +
            ") VALUES (" +
            "  'QU' || LPAD(SEQ_QUALITY_NO.NEXTVAL, 4, '0'), ?, ?, ?," +
            "  ?, ?, ?," +
            "  SYSDATE, ?, SYSDATE, SYSDATE)";
        try (Connection c = DBManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            int i = 1;
            ps.setString(i++, d.getWorkNo());
            ps.setString(i++, d.getStandardCode()); // JS에서 숨은 필드로 내려줍니다
            ps.setString(i++, d.getEmployeeNo());
            ps.setString(i++, d.getQuResult());
            ps.setInt(   i++, d.getQuQuantity());
            ps.setInt(   i++, d.getDefectQuantity());
            ps.setTimestamp(i++, d.getInspectionDate());
            return ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
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

	public List<QualityDTO> searchByPrefix(String trim) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<Map<String,Object>> defectRateByProductTopN(int n){
		  String sql =
		    "SELECT name, rate FROM ( " +
		    "  SELECT s.st_name AS name, " +
		    "         ROUND( SUM(q.defect_quantity) / " +
		    "                NULLIF(SUM(q.qu_quantity + q.defect_quantity), 0) * 100, 1) AS rate " +
		    "    FROM quality q " +
		    "    JOIN standard s ON s.standard_code = q.standard_code " +
		    "   GROUP BY s.st_name " +
		    "   ORDER BY rate DESC " +
		    ") WHERE ROWNUM <= ?";

		  List<Map<String,Object>> out = new ArrayList<>();
		  try (var c = DBManager.getConnection();
		       var ps = c.prepareStatement(sql)) {
		    ps.setInt(1, n);
		    try (var rs = ps.executeQuery()) {
		      while (rs.next()) {
		        Map<String,Object> m = new HashMap<>();
		        m.put("name", rs.getString("name"));
		        m.put("rate", rs.getDouble("rate"));
		        out.add(m);
		      }
		    }
		  } catch (Exception e) { e.printStackTrace(); }
		  return out;
		}

	public List<Map<String,Object>> sumDefectByProductTopN(int n){
		  String sql =
		    "SELECT name, defect_sum FROM ( " +
		    "  SELECT s.st_name AS name, SUM(q.defect_quantity) AS defect_sum " +
		    "    FROM quality q " +
		    "    JOIN standard s ON s.standard_code = q.standard_code " +
		    "   GROUP BY s.st_name " +
		    "   ORDER BY defect_sum DESC " +
		    ") WHERE ROWNUM <= ?";

		  List<Map<String,Object>> out = new ArrayList<>();
		  try (var c = DBManager.getConnection();
		       var ps = c.prepareStatement(sql)) {
		    ps.setInt(1, n);
		    try (var rs = ps.executeQuery()) {
		      while (rs.next()) {
		        Map<String,Object> m = new HashMap<>();
		        m.put("name", rs.getString("name"));
		        m.put("cnt",  rs.getInt("defect_sum"));
		        out.add(m);
		      }
		    }
		  } catch (Exception e) { e.printStackTrace(); }
		  return out;
		}
}
