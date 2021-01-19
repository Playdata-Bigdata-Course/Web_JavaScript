package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import model.domain.DeptDTO;
import util.DBUtil;

public class DeptDAO {
	
	private static Properties sqlAll = new Properties();
	
	/*this - 객체 참조 변수
	 * 		this 사용 시점은 객체가 존재 한 직후에 실제 실행
	 * static은 객체 생성 없이도 사용 가능한 키워드라 두개의 키워드는 상극
	 * static {} 즉 객체 생성 전에도 실행되는 이 블록 내에서는 this 키워드 사용 불가
	 */
	
	public DeptDAO() {
		try {
			//Class.getResourceAsStream()
//			sqlAll.load(new FileInputStream("allSql.properties"));
			
			//web 기반의 properties 사용을 위해서는 getResourceAaStream() 메소드 활용
			//web project의 src 하위에 저장
			sqlAll.load(this.getClass().getClassLoader().getResourceAsStream("allSql.properties"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static ArrayList<DeptDTO> deptAll() throws SQLException{  
		
		Connection con = null;
		Statement stmt = null;
		ResultSet rset = null;
		
		ArrayList<DeptDTO> datas = null;
		
		try {
			con = DBUtil.getConnection();
			stmt = con.createStatement();

			rset = stmt.executeQuery(sqlAll.getProperty("deptAll"));

			datas = new ArrayList<>();
			while(rset.next()) {
				datas.add( new DeptDTO(rset.getInt("deptno"), rset.getString("dname"), rset.getString("loc")) );
			}
		} finally {
			DBUtil.close(con, stmt, rset);
		}
		
		return datas;
	}
	
	public static DeptDTO getDept(int deptno) throws SQLException{
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sqlAll.getProperty("dept.getDept"));
			
			pstmt.setInt(1, deptno);
			
			rset = pstmt.executeQuery();

			if(rset.next()) {
				return new DeptDTO(rset.getInt("deptno"), rset.getString("dname"), rset.getString("loc"));
			}
		} finally {
			DBUtil.close(con, pstmt, rset);
		}
		return null;
	}
	
	public static boolean insert(DeptDTO newDept) throws SQLException {
		Connection con = null;
		PreparedStatement pstmt = null;		
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sqlAll.getProperty("dept.insert"));

			pstmt.setInt(1, newDept.getDeptno());
			pstmt.setString(2, newDept.getDname());
			pstmt.setString(3, newDept.getLoc());
			
			if(pstmt.executeUpdate() != 0) {
				return true;
			}
		} finally {
			DBUtil.close(con, pstmt);
		}
		return false;
	}
	
	public static boolean update(int deptno, String newLoc) throws SQLException{
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sqlAll.getProperty("dept.update"));
			
			pstmt.setString(1, newLoc);
			pstmt.setInt(2, deptno);
			
			int result = pstmt.executeUpdate();
			if(result != 0) {
				return true;
			}
		} finally {
			DBUtil.close(con, pstmt);
		}
		return false;
	}
	
	static boolean delete(int deptno) throws SQLException{
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sqlAll.getProperty("dept.delete"));

			pstmt.setInt(1, deptno);
			
			int result = pstmt.executeUpdate();
			if(result != 0) {
				return true;
			}
		} finally {
			DBUtil.close(con, pstmt);
		}
		return false;
	}
}
