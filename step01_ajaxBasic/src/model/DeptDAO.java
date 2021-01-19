package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import model.domain.DeptDTO;
import util.DBUtil;

public class DeptDAO {

	// 모든 메소드가 allSQL.properties 파일의 내용을 load하고 있는 객체를 사용
	static Properties sqlAll = DBUtil.getSqlAll();
	// ? allSQL.properties 파일의 내용을 read해서 각각의 메소드의 sql문장 반영하기
	// ? " 다수의 DAO가 공유해서 사용하는 자원 파일 - 최적의 구조로 설계해서 개발 하고자 한다면?

	// dept가 존재하는 수많큼 DeptDTO 생성해서 ArrayList에 저장 후 반환
	public static ArrayList<DeptDTO> deptAll() throws SQLException {
		Connection con = null;
		Statement stmt = null;
		ResultSet rset = null;

		ArrayList<DeptDTO> datas = null;

		try {
			con = DBUtil.getConnection();
			stmt = con.createStatement();

//			rset = stmt.executeQuery(DBUtil.getSqlAll().getProperty("deptAll"));
			rset = stmt.executeQuery(sqlAll.getProperty("deptAll")); // 멤버 변수로 선언 후 멤버변수 활용
			// 정상적으로

			datas = new ArrayList<>();
			while (rset.next()) {
				datas.add(new DeptDTO(rset.getInt("deptno"), rset.getString("dname"), rset.getString("loc")));
			}
		} finally {
			DBUtil.close(con, stmt, rset);
		}

		return datas;
	}

	public static DeptDTO getDept(int deptno) throws SQLException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sqlAll.getProperty("dept.getDept"));

			pstmt.setInt(1, deptno);

			rset = pstmt.executeQuery();

			if (rset.next()) {
				return new DeptDTO(rset.getInt("deptno"), rset.getString("dname"), rset.getString("loc"));
			}
		} finally {
			DBUtil.close(con, pstmt, rset);
		}
		return null;
	}

	// Controller에서 view 화면 통해서 입력된 데이터를 가령 포멧 검증 후 문제 없을 경우 DeptDTO 객체 생성해서 저장 요청
	public static boolean insert(DeptDTO newDept) throws SQLException {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sqlAll.getProperty("dept.insert"));

			pstmt.setInt(1, newDept.getDeptno());
			pstmt.setString(2, newDept.getDname());
			pstmt.setString(3, newDept.getLoc());

			int result = pstmt.executeUpdate(); // db에 실제 insert하는 로직

			if (pstmt.executeUpdate() != 0) {
				return true;
			}
		} finally {
			DBUtil.close(con, pstmt);
		}
		return false;
	}

	public static boolean update(int deptno, String newLoc) throws SQLException {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sqlAll.getProperty("dept.update"));
			pstmt.setString(1, newLoc);
			pstmt.setInt(2, deptno);

			int result = pstmt.executeUpdate();
			if (result != 0) {
				return true;
			}
		} finally {
			DBUtil.close(con, pstmt);
		}
		return false;
	}

	public static boolean delete(int deptno) throws SQLException {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sqlAll.getProperty("dept.delete"));
			pstmt.setInt(1, deptno);

			int result = pstmt.executeUpdate();
			if (result != 0) {
				return true;
			}
		} finally {
			DBUtil.close(con, pstmt);
		}
		return false;
	}

	public static ArrayList<String> deptcol(String s) throws SQLException {

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		ResultSetMetaData rsmd;
		ArrayList<String> datas = null;

		try {
			con = DBUtil.getConnection();

			pstmt = con.prepareStatement(sqlAll.getProperty("dept.column"));

			pstmt.setString(1, s);

			rset = pstmt.executeQuery();

			rsmd = rset.getMetaData();
			int cols = rsmd.getColumnCount();
						System.out.println();
			while (rset.next()) {
				for (int i = 1; i <= cols; i++)
					System.out.println(rset.getObject(i) + "\t\t");
			}
		} finally {
			DBUtil.close(con, pstmt, rset);
		}
		return datas;
	}

}
