package mes.DAO;

import mes.DTO.BoardDTO;
import mes.util.DBManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BoardManagementDAO {
    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;

    // DB 연결
    private Connection getConnection() throws Exception {
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

    // 전체 게시글 조회
    public List<BoardDTO> selectAllBoards() {
        String sql = "SELECT * FROM BOARD ORDER BY BO_CREATION_DATE DESC";
        List<BoardDTO> boardList = new ArrayList<>();
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                BoardDTO board = new BoardDTO();
                board.setPostNo(rs.getString("POST_NO"));
                board.setEmployeeNo(rs.getString("EMPLOYEE_NO"));
                board.setBoCategory(rs.getString("BO_CATEGORY"));
                board.setBoTitle(rs.getString("BO_TITLE"));
                board.setBoContent(rs.getString("BO_CONTENT"));
                board.setBoWriter(rs.getString("BO_WRITER"));
                board.setBoCreationDate(rs.getDate("BO_CREATION_DATE"));
                board.setCreateDate(rs.getDate("CREATE_DATE"));
                board.setUpdateDate(rs.getDate("UPDATE_DATE"));
                boardList.add(board);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return boardList;
    }

    // 게시글 검색
    public List<BoardDTO> searchBoards(String searchType, String keyword) {
        String sql = "";
        List<BoardDTO> boardList = new ArrayList<>();
        
        try {
            conn = getConnection();
            
            if ("title".equals(searchType)) {
                sql = "SELECT * FROM BOARD WHERE BO_TITLE LIKE ? ORDER BY BO_CREATION_DATE DESC";
            } else if ("content".equals(searchType)) {
                sql = "SELECT * FROM BOARD WHERE BO_CONTENT LIKE ? ORDER BY BO_CREATION_DATE DESC";
            } else if ("writer".equals(searchType)) {
                sql = "SELECT * FROM BOARD WHERE BO_WRITER LIKE ? ORDER BY BO_CREATION_DATE DESC";
            } else {
                sql = "SELECT * FROM BOARD WHERE (BO_TITLE LIKE ? OR BO_CONTENT LIKE ? OR BO_WRITER LIKE ?) ORDER BY BO_CREATION_DATE DESC";
            }
            
            pstmt = conn.prepareStatement(sql);
            
            if ("all".equals(searchType)) {
                String searchKeyword = "%" + keyword + "%";
                pstmt.setString(1, searchKeyword);
                pstmt.setString(2, searchKeyword);
                pstmt.setString(3, searchKeyword);
            } else {
                pstmt.setString(1, "%" + keyword + "%");
            }
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                BoardDTO board = new BoardDTO();
                board.setPostNo(rs.getString("POST_NO"));
                board.setEmployeeNo(rs.getString("EMPLOYEE_NO"));
                board.setBoCategory(rs.getString("BO_CATEGORY"));
                board.setBoTitle(rs.getString("BO_TITLE"));
                board.setBoContent(rs.getString("BO_CONTENT"));
                board.setBoWriter(rs.getString("BO_WRITER"));
                board.setBoCreationDate(rs.getDate("BO_CREATION_DATE"));
                board.setCreateDate(rs.getDate("CREATE_DATE"));
                board.setUpdateDate(rs.getDate("UPDATE_DATE"));
                boardList.add(board);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return boardList;
    }

    // 카테고리별 게시글 조회
    public List<BoardDTO> selectBoardsByCategory(String category) {
        String sql = "SELECT * FROM BOARD WHERE BO_CATEGORY = ? ORDER BY BO_CREATION_DATE DESC";
        List<BoardDTO> boardList = new ArrayList<>();
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, category);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                BoardDTO board = new BoardDTO();
                board.setPostNo(rs.getString("POST_NO"));
                board.setEmployeeNo(rs.getString("EMPLOYEE_NO"));
                board.setBoCategory(rs.getString("BO_CATEGORY"));
                board.setBoTitle(rs.getString("BO_TITLE"));
                board.setBoContent(rs.getString("BO_CONTENT"));
                board.setBoWriter(rs.getString("BO_WRITER"));
                board.setBoCreationDate(rs.getDate("BO_CREATION_DATE"));
                board.setCreateDate(rs.getDate("CREATE_DATE"));
                board.setUpdateDate(rs.getDate("UPDATE_DATE"));
                boardList.add(board);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return boardList;
    }

    // 작성자별 게시글 조회
    public List<BoardDTO> selectBoardsByWriter(String writer) {
        String sql = "SELECT * FROM BOARD WHERE BO_WRITER = ? ORDER BY BO_CREATION_DATE DESC";
        List<BoardDTO> boardList = new ArrayList<>();
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, writer);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                BoardDTO board = new BoardDTO();
                board.setPostNo(rs.getString("POST_NO"));
                board.setEmployeeNo(rs.getString("EMPLOYEE_NO"));
                board.setBoCategory(rs.getString("BO_CATEGORY"));
                board.setBoTitle(rs.getString("BO_TITLE"));
                board.setBoContent(rs.getString("BO_CONTENT"));
                board.setBoWriter(rs.getString("BO_WRITER"));
                board.setBoCreationDate(rs.getDate("BO_CREATION_DATE"));
                board.setCreateDate(rs.getDate("CREATE_DATE"));
                board.setUpdateDate(rs.getDate("UPDATE_DATE"));
                boardList.add(board);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return boardList;
    }

    // 게시글 등록
    public int insertBoard(BoardDTO board) {
        String sql = "INSERT INTO BOARD (POST_NO, EMPLOYEE_NO, BO_CATEGORY, BO_TITLE, BO_CONTENT, BO_WRITER, BO_CREATION_DATE, CREATE_DATE) VALUES (?, ?, ?, ?, ?, ?, SYSDATE, SYSDATE)";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, board.getPostNo());
            pstmt.setString(2, board.getEmployeeNo());
            pstmt.setString(3, board.getBoCategory());
            pstmt.setString(4, board.getBoTitle());
            pstmt.setString(5, board.getBoContent());
            pstmt.setString(6, board.getBoWriter());
            
            int result = pstmt.executeUpdate();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }

    // 게시글 수정
    public int updateBoard(BoardDTO board) {
        String sql = "UPDATE BOARD SET BO_CATEGORY = ?, BO_TITLE = ?, BO_CONTENT = ?, BO_WRITER = ?, UPDATE_DATE = SYSDATE WHERE POST_NO = ?";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, board.getBoCategory());
            pstmt.setString(2, board.getBoTitle());
            pstmt.setString(3, board.getBoContent());
            pstmt.setString(4, board.getBoWriter());
            pstmt.setString(5, board.getPostNo());
            
            int result = pstmt.executeUpdate();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }

    // 게시글 삭제
    public int deleteBoard(String postNo) {
        String sql = "DELETE FROM BOARD WHERE POST_NO = ?";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, postNo);
            
            int result = pstmt.executeUpdate();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }

    // 게시글 상세 조회
    public BoardDTO selectBoardByPostNo(String postNo) {
        String sql = "SELECT * FROM BOARD WHERE POST_NO = ?";
        BoardDTO board = null;
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, postNo);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                board = new BoardDTO();
                board.setPostNo(rs.getString("POST_NO"));
                board.setEmployeeNo(rs.getString("EMPLOYEE_NO"));
                board.setBoCategory(rs.getString("BO_CATEGORY"));
                board.setBoTitle(rs.getString("BO_TITLE"));
                board.setBoContent(rs.getString("BO_CONTENT"));
                board.setBoWriter(rs.getString("BO_WRITER"));
                board.setBoCreationDate(rs.getDate("BO_CREATION_DATE"));
                board.setCreateDate(rs.getDate("CREATE_DATE"));
                board.setUpdateDate(rs.getDate("UPDATE_DATE"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return board;
    }

    // 다음 게시글 번호 생성
    public String getNextPostNo() {
        String sql = "SELECT NVL(MAX(TO_NUMBER(SUBSTR(POST_NO, 2))), 0) + 1 AS NEXT_NO FROM BOARD";
        String nextNo = "";
        
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int nextNumber = rs.getInt("NEXT_NO");
                nextNo = "P" + String.format("%04d", nextNumber);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return nextNo;
    }

    // 게시판 통계 조회
    public BoardStatistics getBoardStatistics() {
        BoardStatistics stats = new BoardStatistics();
        
        try {
            conn = getConnection();
            
            // 전체 게시글 수
            String totalSql = "SELECT COUNT(*) AS TOTAL FROM BOARD";
            pstmt = conn.prepareStatement(totalSql);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                stats.setTotalBoards(rs.getInt("TOTAL"));
            }
            
            // 카테고리별 게시글 수
            String categorySql = "SELECT BO_CATEGORY, COUNT(*) AS COUNT FROM BOARD GROUP BY BO_CATEGORY";
            pstmt = conn.prepareStatement(categorySql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String category = rs.getString("BO_CATEGORY");
                int count = rs.getInt("COUNT");
                stats.getCategoryCounts().put(category, count);
            }
            
            // 오늘 작성된 게시글 수
            String todaySql = "SELECT COUNT(*) AS TODAY FROM BOARD WHERE TO_CHAR(BO_CREATION_DATE, 'YYYY-MM-DD') = TO_CHAR(SYSDATE, 'YYYY-MM-DD')";
            pstmt = conn.prepareStatement(todaySql);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                stats.setTodayBoards(rs.getInt("TODAY"));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return stats;
    }

    // 게시판 통계 클래스
    public static class BoardStatistics {
        private int totalBoards = 0;
        private int todayBoards = 0;
        private java.util.Map<String, Integer> categoryCounts = new java.util.HashMap<>();
        
        public int getTotalBoards() { return totalBoards; }
        public void setTotalBoards(int totalBoards) { this.totalBoards = totalBoards; }
        
        public int getTodayBoards() { return todayBoards; }
        public void setTodayBoards(int todayBoards) { this.todayBoards = todayBoards; }
        
        public java.util.Map<String, Integer> getCategoryCounts() { return categoryCounts; }
        public void setCategoryCounts(java.util.Map<String, Integer> categoryCounts) { this.categoryCounts = categoryCounts; }
    }
}

