package mes.DAO;

import mes.DTO.WorkDTO;
import mes.util.DBManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkDAO {
	private WorkDAO() {
	}

	private static WorkDAO instance = new WorkDAO();

	public static WorkDAO getInstance() {
		return instance;
	}

	
	private static final String WORK_SELECT =
		    "SELECT DISTINCT " +
		    "  w.WORK_NO, w.PRODUCTION_NO, w.STANDARD_CODE, s.ST_NAME, " +
		    "  w.EMPLOYEE_NO, w.WO_SCHEDULE, w.WO_QUANTITY, w.WO_STATUS, " +
		    "  w.WO_COMPLETED, w.WO_START, w.WO_END, w.CREATE_DATE, w.UPDATE_DATE " +
		    "FROM WORK w " +
		    "JOIN STANDARD s ON s.STANDARD_CODE = w.STANDARD_CODE ";
	
	// 전체 조회
	public List<WorkDTO> findAll() {
	    String sql = WORK_SELECT + " ORDER BY w.CREATE_DATE DESC";
	    List<WorkDTO> list = new ArrayList<>();
	    try (Connection c = DBManager.getConnection();
	         PreparedStatement ps = c.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {
	        while (rs.next()) list.add(mapResultSet(rs));
	    } catch (Exception e) { e.printStackTrace(); }
	    return list;
	}

	// 단건 조회
	public WorkDTO findByNo(String workNo) {
	    String sql = WORK_SELECT + " WHERE w.WORK_NO = ?";
	    try (Connection c = DBManager.getConnection();
	         PreparedStatement ps = c.prepareStatement(sql)) {
	        ps.setString(1, workNo);
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) return mapResultSet(rs);
	        }
	    } catch (Exception e) { e.printStackTrace(); }
	    return null;
	}

	//	isert 
	// WorkDAO.java
	public int insert(WorkDTO d) {
	    String sql =
	        "INSERT INTO WORK (WORK_NO, PRODUCTION_NO, STANDARD_CODE, EMPLOYEE_NO, " +
	        " WO_SCHEDULE, WO_QUANTITY, WO_STATUS, WO_COMPLETED, " +
	        " WO_START, WO_END, CREATE_DATE, UPDATE_DATE) " +
	        "VALUES ('W' || LPAD(SEQ_WORK_NO.NEXTVAL, 4, '0'), ?, ?, ?, " +
	        "        ?, ?, ?, ?, ?, ?, SYSDATE, SYSDATE)";

	    try (Connection c = DBManager.getConnection();
	         PreparedStatement ps = c.prepareStatement(sql)) {

	        int i = 1;
	        ps.setString(i++, d.getProductionNo());
	        ps.setString(i++, d.getStandardCode());
	        ps.setString(i++, d.getEmployeeNo());
	        ps.setDate(i++, d.getWoSchedule());
	        ps.setInt(i++, d.getWoQuantity());     // ★ 생산목표량으로 고정된 값
	        ps.setString(i++, d.getWoStatus());
	        ps.setInt(i++, d.getWoCompleted());
	        ps.setTimestamp(i++, d.getWoStart());
	        ps.setTimestamp(i++, d.getWoEnd());

	        return ps.executeUpdate();             // ← int 반환
	    } catch (Exception e) {
	        e.printStackTrace();
	        return 0;
	    }
	}

    /* ------- 이하 보조 메서드들: NULL 안전 바인딩 ------- */
    private void setNullableString(PreparedStatement ps, int idx, String v) throws SQLException {
        if (v == null || v.isBlank()) ps.setNull(idx, Types.VARCHAR);
        else ps.setString(idx, v);
    }
    private void setNullableInt(PreparedStatement ps, int idx, Integer v) throws SQLException {
        if (v == null) ps.setNull(idx, Types.INTEGER);
        else ps.setInt(idx, v);
    }
    private void setNullableDate(PreparedStatement ps, int idx, Date v) throws SQLException {
        if (v == null) ps.setNull(idx, Types.DATE);
        else ps.setDate(idx, v);
    }
    private void setNullableTs(PreparedStatement ps, int idx, Timestamp v) throws SQLException {
        if (v == null) ps.setNull(idx, Types.TIMESTAMP);
        else ps.setTimestamp(idx, v);
    }

    /** (선택) 과거 boolean 반환을 쓰던 코드 호환용 */
    @Deprecated
    public boolean insertAsBoolean(WorkDTO d) {
        return insert(d) > 0;
    }

	// 수정
	public int update(WorkDTO w) {
		String sql = "UPDATE WORK SET " + "PRODUCTION_NO=?, STANDARD_CODE=?, EMPLOYEE_NO=?, "
				+ "WO_SCHEDULE=?, WO_QUANTITY=?, WO_STATUS=?, WO_COMPLETED=?, WO_START=?, WO_END=?, UPDATE_DATE=SYSDATE "
				+ "WHERE WORK_NO=?";
		try (Connection conn = DBManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, w.getProductionNo());
			ps.setString(2, w.getStandardCode());
			ps.setString(3, w.getEmployeeNo());
			ps.setDate(4, w.getWoSchedule());
			ps.setInt(5, w.getWoQuantity());
			ps.setString(6, w.getWoStatus());
			ps.setInt(7, w.getWoCompleted());
			ps.setTimestamp(8, w.getWoStart());
			ps.setTimestamp(9, w.getWoEnd());
			ps.setString(10, w.getWorkNo());

			return ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	// 삭제
	public int delete(String workNo) {
		String sql = "DELETE FROM WORK WHERE WORK_NO=?";
		try (Connection conn = DBManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, workNo);
			return ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	// 검색 (작업번호 or 생산번호 접두검색)
	public List<WorkDTO> searchByPrefix(String q) {
		String sql = "SELECT * FROM WORK WHERE WORK_NO LIKE ? OR PRODUCTION_NO LIKE ? ORDER BY UPDATE_DATE DESC";
		List<WorkDTO> list = new ArrayList<>();
		try (Connection conn = DBManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			String like = q + "%";
			ps.setString(1, like);
			ps.setString(2, like);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					list.add(mapResultSet(rs));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// ResultSet → DTO 매핑
	private WorkDTO mapResultSet(ResultSet rs) throws SQLException {
	    WorkDTO w = new WorkDTO();
	    w.setWorkNo(rs.getString("WORK_NO"));
	    w.setProductionNo(rs.getString("PRODUCTION_NO"));
	    w.setStandardCode(rs.getString("STANDARD_CODE"));
	    w.setStName(rs.getString("ST_NAME"));          // ✅ 제품명 매핑
	    w.setEmployeeNo(rs.getString("EMPLOYEE_NO"));
	    w.setWoSchedule(rs.getDate("WO_SCHEDULE"));
	    w.setWoQuantity(rs.getInt("WO_QUANTITY"));
	    w.setWoStatus(rs.getString("WO_STATUS"));
	    w.setWoCompleted(rs.getInt("WO_COMPLETED"));
	    w.setWoStart(rs.getTimestamp("WO_START"));
	    w.setWoEnd(rs.getTimestamp("WO_END"));
	    w.setCreateDate(rs.getDate("CREATE_DATE"));
	    w.setUpdateDate(rs.getDate("UPDATE_DATE"));
	    return w;
	}

	public String peekNextNo() {
		String sql = "SELECT 'W' || LPAD(SEQ_WORK_NO.NEXTVAL, 4, '0') AS NO FROM DUAL";
		try (Connection c = DBManager.getConnection();
				PreparedStatement ps = c.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			if (rs.next())
				return rs.getString(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<WorkDTO> findForQuality() {
	    String sql =
	        "SELECT w.WORK_NO, w.STANDARD_CODE, s.ST_NAME, " +
	        "       NVL(w.WO_COMPLETED,0) AS WO_COMPLETED " +
	        "  FROM WORK w " +
	        "  JOIN STANDARD s ON s.STANDARD_CODE = w.STANDARD_CODE " +
	        " ORDER BY w.UPDATE_DATE DESC";
	    List<WorkDTO> list = new ArrayList<>();
	    try (Connection c = DBManager.getConnection();
	         PreparedStatement ps = c.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {
	        while (rs.next()) {
	            WorkDTO d = new WorkDTO();
	            d.setWorkNo(rs.getString("WORK_NO"));
	            d.setStandardCode(rs.getString("STANDARD_CODE"));
	            d.setWoCompleted(rs.getInt("WO_COMPLETED"));
	            d.setStName(rs.getString("ST_NAME")); // DTO에 stName 필드(제품명) 하나 추가하세요
	            list.add(d);
	        }
	    } catch (Exception e) { e.printStackTrace(); }
	    return list;
	}
	
	public String findStandardCodeByWorkNo(String workNo) {
	    String sql = "SELECT STANDARD_CODE FROM WORK WHERE WORK_NO = ?";
	    try (Connection c = DBManager.getConnection();
	         PreparedStatement ps = c.prepareStatement(sql)) {
	        ps.setString(1, workNo);
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) return rs.getString(1);
	        }
	    } catch (Exception e) { e.printStackTrace(); }
	    return null;
	}
	
	public List<Map<String,Object>> findLastNForChart(int n){
		  String sql =
		    "SELECT work_no, wo_quantity, wo_completed " +
		    "FROM (SELECT * FROM work ORDER BY create_date DESC) WHERE ROWNUM<=?";
		  List<Map<String,Object>> out=new ArrayList<>();
		  try(var c=DBManager.getConnection();
		      var ps=c.prepareStatement(sql)){
		    ps.setInt(1,n);
		    try(var rs=ps.executeQuery()){
		      while(rs.next()){
		        Map<String,Object> m=new HashMap<>();
		        m.put("no", rs.getString(1));
		        m.put("q",  rs.getInt(2));
		        m.put("c",  rs.getInt(3));
		        out.add(m);
		      }
		    }
		  }catch(Exception e){e.printStackTrace();}
		  Collections.reverse(out);
		  return out;
		}
	
}
