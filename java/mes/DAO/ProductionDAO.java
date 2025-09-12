package mes.DAO;

import mes.DTO.ProductionDTO;
import mes.util.DBManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductionDAO {

	private ProductionDAO() {
	}

	private static final ProductionDAO instance = new ProductionDAO();

	public static ProductionDAO getInstance() {
		return instance;
	}

	// 목록
	public List<ProductionDTO> findAll() {
	    String sql =
	        "SELECT p.PRODUCTION_NO, p.STANDARD_CODE, p.PR_START, p.PR_END, " +
	        "       p.PR_TARGET, p.PR_COMPLETED, p.EMPLOYEE_NO, " +
	        "       p.CREATE_DATE, p.UPDATE_DATE, " +           // ← JSP에서 쓰면 선택
	        "       s.ST_NAME " +
	        "  FROM PRODUCTION p " +
	        "  JOIN STANDARD s ON s.STANDARD_CODE = p.STANDARD_CODE " +
	        " ORDER BY p.CREATE_DATE DESC";

	    List<ProductionDTO> list = new ArrayList<>();
	    try (Connection c = DBManager.getConnection();
	         PreparedStatement ps = c.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {
	        while (rs.next()) {
	            ProductionDTO d = new ProductionDTO();
	            d.setProductionNo(rs.getString("PRODUCTION_NO"));
	            d.setStandardCode(rs.getString("STANDARD_CODE"));
	            d.setPrStart(rs.getDate("PR_START"));
	            d.setPrEnd(rs.getDate("PR_END"));
	            d.setPrTarget(rs.getInt("PR_TARGET"));
	            d.setPrCompleted(rs.getInt("PR_COMPLETED"));
	            d.setEmployeeNo(rs.getString("EMPLOYEE_NO"));
	            d.setCreateDate(rs.getDate("CREATE_DATE"));     // ← 선택
	            d.setUpdateDate(rs.getDate("UPDATE_DATE"));     // ← 선택
	            d.setStName(rs.getString("ST_NAME"));           // ← 제품명
	            list.add(d);
	        }
	    } catch (Exception e) { e.printStackTrace(); }
	    return list;
	}
	/** 작업 등록 폼 드롭다운용: 생산번호 + 제품코드 + 제품명 */

	public List<Map<String, String>> findForWorkForm() {
	    String sql = "SELECT p.PRODUCTION_NO, p.STANDARD_CODE, s.ST_NAME, p.PR_TARGET " +
	                 "FROM PRODUCTION p " +
	                 "JOIN STANDARD s ON s.STANDARD_CODE = p.STANDARD_CODE " +
	                 "ORDER BY p.PRODUCTION_NO DESC";
	    List<Map<String, String>> list = new ArrayList<>();
	    try (Connection c = DBManager.getConnection();
	         PreparedStatement ps = c.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {
	        while (rs.next()) {
	            Map<String, String> row = new HashMap<>();
	            row.put("productionNo", rs.getString(1));
	            row.put("standardCode", rs.getString(2));
	            row.put("stName",       rs.getString(3));
	            row.put("target",       String.valueOf(rs.getInt(4))); // ★ PR_TARGET
	            list.add(row);
	        }
	    } catch (Exception e) { e.printStackTrace(); }
	    return list;
	}

	// 접두 검색(생산번호/제품코드 2글자 이상)
	public List<ProductionDTO> searchByPrefix(String q) {
		String sql = "SELECT production_no, standard_code, employee_no," + " pr_start, pr_end, pr_target, pr_completed,"
				+ " create_date, update_date" + " FROM production" + " WHERE UPPER(production_no) LIKE UPPER(?)"
				+ "    OR UPPER(standard_code) LIKE UPPER(?)" + " ORDER BY update_date DESC";
		List<ProductionDTO> list = new ArrayList<>();
		try (Connection c = DBManager.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			String like = q + "%";
			ps.setString(1, like);
			ps.setString(2, like);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next())
					list.add(map(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// 단건
	public ProductionDTO findByNo(String no) {
		String sql = "SELECT production_no, standard_code, employee_no," + " pr_start, pr_end, pr_target, pr_completed,"
				+ " create_date, update_date" + " FROM production WHERE production_no = ?";
		try (Connection c = DBManager.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, no);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next())
					return map(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

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
		try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
			rs.next();
			return rs.getString(1);
		}
	}

	public int insert(ProductionDTO d) throws SQLException {
		try (Connection conn = DBManager.getConnection()) {
			conn.setAutoCommit(false);
			try {
				String std = d.getStandardCode() == null ? null : d.getStandardCode().trim().toUpperCase();
				String emp = d.getEmployeeNo() == null ? null : d.getEmployeeNo().trim().toUpperCase();

				if (!existsStandard(conn, std)) {
					throw new SQLException("STANDARD_CODE_NOT_FOUND");
				}
				if (!existsEmployee(conn, emp)) {
					throw new SQLException("EMPLOYEE_NO_NOT_FOUND");
				}

				String proNo = nextProductionNo(conn);

				String sql = "INSERT INTO PRODUCTION("
						+ "PRODUCTION_NO, STANDARD_CODE, EMPLOYEE_NO, PR_START, PR_END, PR_TARGET, PR_COMPLETED, CREATE_DATE, UPDATE_DATE"
						+ ") VALUES (?, ?, ?, ?, ?, ?, ?, SYSDATE, SYSDATE)";
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
		String sql = "UPDATE production SET" + " standard_code=?, employee_no=?, pr_start=?, pr_end=?,"
				+ " pr_target=?, pr_completed=?, update_date=SYSDATE" + " WHERE production_no=?";
		try (Connection c = DBManager.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
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
		try (Connection c = DBManager.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, no);
			return ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	// 필요 시: 품질→작업→생산 순으로 삭제
	public int deleteCascade(String no) {
		String delQuality = "DELETE FROM QUALITY WHERE WORK_NO IN (SELECT WORK_NO FROM WORK WHERE PRODUCTION_NO=?)";
		String delWork = "DELETE FROM WORK WHERE PRODUCTION_NO=?";
		String delProduction = "DELETE FROM PRODUCTION WHERE PRODUCTION_NO=?";

		try (Connection c = DBManager.getConnection()) {
			c.setAutoCommit(false);
			try (PreparedStatement ps1 = c.prepareStatement(delQuality);
					PreparedStatement ps2 = c.prepareStatement(delWork);
					PreparedStatement ps3 = c.prepareStatement(delProduction)) {

				ps1.setString(1, no);
				ps1.executeUpdate();
				ps2.setString(1, no);
				ps2.executeUpdate();
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

	public String findStandardCodeByProductionNo(String productionNo) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<Map<String,Object>> findLastNForChart(int n){
		  String sql =
		    "SELECT production_no, pr_target, pr_completed " +
		    "FROM (SELECT * FROM production ORDER BY create_date DESC) WHERE ROWNUM<=?";
		  List<Map<String,Object>> out = new ArrayList<>();
		  try(var c=DBManager.getConnection();
		      var ps=c.prepareStatement(sql)){
		    ps.setInt(1,n);
		    try(var rs=ps.executeQuery()){
		      while(rs.next()){
		        Map<String,Object> m=new HashMap<>();
		        m.put("no", rs.getString(1));
		        m.put("t",  rs.getInt(2));
		        m.put("c",  rs.getInt(3));
		        out.add(m);
		      }
		    }
		  }catch(Exception e){e.printStackTrace();}
		  Collections.reverse(out); // 최근이 오른쪽에 보이도록
		  return out;
		}

		public int sumCompletedToday(){
		  String sql="SELECT NVL(SUM(pr_completed),0) FROM production " +
		             "WHERE TRUNC(update_date)=TRUNC(SYSDATE)";
		  try(var c=DBManager.getConnection();
		      var ps=c.prepareStatement(sql);
		      var rs=ps.executeQuery()){
		    if(rs.next()) return rs.getInt(1);
		  }catch(Exception e){e.printStackTrace();}
		  return 0;
		}
		 public Integer getTargetByNo(String productionNo) {
		        String sql = "SELECT PR_TARGET FROM PRODUCTION WHERE PRODUCTION_NO = ?";
		        try (Connection c = DBManager.getConnection();
		             PreparedStatement ps = c.prepareStatement(sql)) {
		            ps.setString(1, productionNo);
		            try (ResultSet rs = ps.executeQuery()) {
		                if (rs.next()) return rs.getInt(1);
		            }
		        } catch (Exception e) { e.printStackTrace(); }
		        return null;
		    }

		    public int updateCompleted(String productionNo, int completed) {
		        String sql = "UPDATE PRODUCTION SET PR_COMPLETED=?, UPDATE_DATE=SYSDATE WHERE PRODUCTION_NO=?";
		        try (Connection c = DBManager.getConnection();
		             PreparedStatement ps = c.prepareStatement(sql)) {
		            ps.setInt(1, completed);
		            ps.setString(2, productionNo);
		            return ps.executeUpdate();
		        } catch (Exception e) { e.printStackTrace(); }
		        return 0;
		    }

		    // (선택) 목표량 바뀔 때 연결된 작업량도 같이 맞추고 싶으면:
		    public int syncWorkQuantityToTarget(String productionNo) {
		        String sql = "UPDATE WORK SET WO_QUANTITY = " +
		                     "(SELECT PR_TARGET FROM PRODUCTION WHERE PRODUCTION_NO = ?) " +
		                     "WHERE PRODUCTION_NO = ?";
		        try (Connection c = DBManager.getConnection();
		             PreparedStatement ps = c.prepareStatement(sql)) {
		            ps.setString(1, productionNo);
		            ps.setString(2, productionNo);
		            return ps.executeUpdate();
		        } catch (Exception e) { e.printStackTrace(); }
		        return 0;
		    }

		    
		    public String getStandardCodeByNo(String productionNo) {
		        String sql = "SELECT STANDARD_CODE FROM PRODUCTION WHERE PRODUCTION_NO = ?";
		        try (Connection c = DBManager.getConnection();
		             PreparedStatement ps = c.prepareStatement(sql)) {
		            ps.setString(1, productionNo);
		            try (ResultSet rs = ps.executeQuery()) {
		                if (rs.next()) return rs.getString(1);
		            }
		        } catch (Exception e) { e.printStackTrace(); }
		        return null;
		    }
		    
			
			
}

