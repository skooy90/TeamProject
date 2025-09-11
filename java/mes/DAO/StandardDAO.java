package mes.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import mes.DTO.StandardDTO;
import mes.util.DBManager;

public class StandardDAO {

	private StandardDAO() {
	}

	private static StandardDAO instance = new StandardDAO();

	public static StandardDAO getInstance() {
		return instance;
	}

	public List<StandardDTO> selectAllStandards() {
		List<StandardDTO> list = new ArrayList<>();
		String sql = "SELECT * FROM standard ORDER BY create_date DESC";

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				StandardDTO standard = new StandardDTO();
				standard.setStandardCode(rs.getString("standard_code"));
				standard.setStType(rs.getString("st_type"));
				standard.setStName(rs.getString("st_name"));
				standard.setStQuantity(rs.getInt("st_quantity"));
				standard.setStUnit(rs.getString("st_unit"));
				standard.setCreateDate(rs.getDate("create_date"));
				standard.setUpdateDate(rs.getDate("update_date"));
				list.add(standard);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(conn, pstmt, rs);
		}
		return list;
	}

//  단건 조회 (수정 폼 출력용)
	public StandardDTO findByCode(String code) {
		String sql = "SELECT standard_code, st_type, st_name, st_quantity, st_unit, create_date, update_date "
				+ "FROM standard WHERE standard_code = ?";
		try (Connection conn = DBManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, code);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					StandardDTO d = new StandardDTO();
					d.setStandardCode(rs.getString("standard_code"));
					d.setStType(rs.getString("st_type"));
					d.setStName(rs.getString("st_name"));
					d.setStQuantity(rs.getInt("st_quantity"));
					d.setStUnit(rs.getString("st_unit"));
					d.setCreateDate(rs.getDate("create_date"));
					d.setUpdateDate(rs.getDate("update_date"));
					return d;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

// 수정
	public int update(StandardDTO d) {
		String sql = "UPDATE standard SET st_type=?, st_name=?, st_quantity=?, st_unit=?, update_date=SYSDATE "
				+ "WHERE standard_code=?";
		try (Connection conn = DBManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, d.getStType());
			ps.setString(2, d.getStName());
			ps.setInt(3, d.getStQuantity());
			ps.setString(4, d.getStUnit());
			ps.setString(5, d.getStandardCode());
			return ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	// 삭제 
	public int delete(String code) {
	    String sql = "DELETE FROM STANDARD WHERE STANDARD_CODE = ?"; // 이 한 줄이면 충분!
	    try (Connection conn = DBManager.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {
	        ps.setString(1, code);
	        return ps.executeUpdate(); // 트랜잭션도 필요 없어짐
	    } catch (Exception e) {
	        e.printStackTrace();
	        return 0;
	    }
	}
	// isnert
	private String prefixOf(String type) {
		if (type == null)
			return "RA";
		switch (type.toUpperCase()) {
		case "RAW":
			return "RA";
		case "SEMI":
			return "SE";
		case "FINISH":
			return "FI";
		default:
			return "RA";
		}
	}

	/** 시퀀스에서 다음 번호를 읽어 접두어와 합쳐 코드 생성 */
	public String nextStandardCode(String type) {
		String sql = "SELECT LPAD(SEQ_STANDARD_CODE.NEXTVAL, 4, '0') AS N FROM dual";
		try (Connection c = DBManager.getConnection();
				PreparedStatement ps = c.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			if (rs.next()) {
				return prefixOf(type) + rs.getString("N");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null; // 실패 시
	}

	/** INSERT: 제품코드가 비어 있으면 자동 생성해서 저장, 생성된 코드를 반환 */
	public String insertAuto(StandardDTO d) {
		// 1) 코드가 없으면 시퀀스로 생성
		String code = d.getStandardCode();
		if (code == null || code.trim().isEmpty()) {
			code = nextStandardCode(d.getStType());
			if (code == null)
				return null;
		}

		// 2) INSERT
		String sql = "INSERT INTO standard "
				+ "(standard_code, st_type, st_name, st_quantity, st_unit, create_date, update_date) "
				+ "VALUES (?, ?, ?, ?, ?, SYSDATE, SYSDATE)";
		try (Connection c = DBManager.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, code);
			ps.setString(2, d.getStType());
			ps.setString(3, d.getStName());
			ps.setInt(4, d.getStQuantity());
			ps.setString(5, d.getStUnit());
			int n = ps.executeUpdate();
			return (n == 1) ? code : null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 검색 기능 like.
	public List<StandardDTO> searchByPrefix(String q) {
		List<StandardDTO> list = new ArrayList<>();
		String sql = "SELECT standard_code, st_type, st_name, st_quantity, st_unit, create_date, update_date "
				+ "FROM standard " + "WHERE UPPER(standard_code) LIKE UPPER(?) "
				+ "   OR UPPER(st_name)        LIKE UPPER(?) " + "ORDER BY update_date DESC";

		try (Connection conn = DBManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			String like = q + "%"; // 앞에서부터 매칭(접두 검색)
			ps.setString(1, like);
			ps.setString(2, like);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					StandardDTO d = new StandardDTO();
					d.setStandardCode(rs.getString("standard_code"));
					d.setStType(rs.getString("st_type"));
					d.setStName(rs.getString("st_name"));
					d.setStQuantity(rs.getInt("st_quantity"));
					d.setStUnit(rs.getString("st_unit"));
					d.setCreateDate(rs.getDate("create_date"));
					d.setUpdateDate(rs.getDate("update_date"));
					list.add(d);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
