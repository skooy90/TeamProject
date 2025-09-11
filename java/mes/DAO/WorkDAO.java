package mes.DAO;

import mes.DTO.WorkDTO;
import mes.util.DBManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WorkDAO {
	private WorkDAO() {
	}

	private static WorkDAO instance = new WorkDAO();

	public static WorkDAO getInstance() {
		return instance;
	}

	// 전체 조회
	public List<WorkDTO> findAll() {
		String sql = "SELECT * FROM WORK ORDER BY CREATE_DATE DESC";
		List<WorkDTO> list = new ArrayList<>();

		try (Connection conn = DBManager.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				WorkDTO w = mapResultSet(rs);
				list.add(w);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// 단건 조회
	public WorkDTO findByNo(String workNo) {
		String sql = "SELECT * FROM WORK WHERE WORK_NO = ?";
		try (Connection conn = DBManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, workNo);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return mapResultSet(rs);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 등록
	// WorkDAO.java
	public boolean insert(WorkDTO d) {
		String sql = "INSERT INTO WORK (" + "  WORK_NO, PRODUCTION_NO, STANDARD_CODE, EMPLOYEE_NO,"
				+ "  WO_SCHEDULE, WO_QUANTITY, WO_STATUS, WO_COMPLETED, WO_START, WO_END,"
				+ "  CREATE_DATE, UPDATE_DATE" + ") VALUES (" + "  'W' || LPAD(SEQ_WORK_NO.NEXTVAL, 4, '0'), ?, ?, ?,"
				+ "  ?, ?, ?, ?, ?, ?," + "  SYSDATE, SYSDATE)";
		try (Connection c = DBManager.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			int i = 1;
			ps.setString(i++, d.getProductionNo());
			ps.setString(i++, d.getStandardCode());
			ps.setString(i++, d.getEmployeeNo());
			ps.setDate(i++, d.getWoSchedule());
			ps.setInt(i++, d.getWoQuantity());
			ps.setString(i++, d.getWoStatus());
			ps.setInt(i++, d.getWoCompleted());
			ps.setTimestamp(i++, d.getWoStart());
			ps.setTimestamp(i++, d.getWoEnd());
			return ps.executeUpdate() > 0; // ← boolean
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
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
}
