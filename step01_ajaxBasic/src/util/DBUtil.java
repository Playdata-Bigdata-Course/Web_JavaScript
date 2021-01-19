/* 1. 모든 DB 연동 로직에서 공통적으로 사용되는 코드들로만 구성되 재사용성 끝장판 클래스
 * 2. 주의사항
 *	- enterprise 관점
 *	- 구현하는 로직들은 24시간, 365일 웹서버 통해서 서비스 되는 로직으로 간주
 *	- 간결성(유지보수, 수정 용이), 가독성이 좋고 확장이 용이한 코드로만 개발 및 관리
 * 	- 재사용성 필수적인 고려
 * 	- 자원 절약(서버와 디비 시스템등)
 * 
 * 모든 DAO가 공유하게 되는 자원 제공 및 자원 반환 처리
 * sql의 내용을 load하고 있는 Properties 객체도 단일 객체로 생성해서 모든 DAO가 공유
 */

package util;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBUtil {
	private static Properties dbInfo = new Properties(); //DBUtil에서만 사용
	public static Properties sqlAll = new Properties();
	//byte code가 로딩시 단 한번 실행 보장, 이름도 없어서 호출 불가
	//모든 웹상의 user들이 공유하는 자원, 단 한번만 서버 실행시에 실행되면 됨
	static {
		try {
			dbInfo.load(new FileInputStream("dbinfo.properties"));
			sqlAll.load(new FileInputStream("allSql.properties"));
			
			
			Class.forName(dbInfo.getProperty("driver"));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("해당 driver가 존재하지 않습니다.");
		}
	}
	public static Connection getConnection() throws SQLException{
		return DriverManager.getConnection(dbInfo.getProperty("url"), dbInfo.getProperty("id"), dbInfo.getProperty("pw"));
	}
	public static Properties getSqlAll() {
		return sqlAll;
	}
	
	public static void close(Connection con, Statement stmt, ResultSet rset) {
		try {
			if(rset != null) {
				rset.close();
				rset = null;
			}
			if(stmt != null) {
				stmt.close();
				stmt = null;
			}
			if(con != null) { 						
				con.close();
				con = null;			
			}
		}catch(SQLException e) {
				e.printStackTrace();
		}
	}
	//insert/update/delete = DML 자원반환용
	public static void close(Connection con, Statement stmt) {
		try {
			if(stmt != null) {
				stmt.close();
				stmt = null;
			}
			if(con != null) { 						
				con.close();
				con = null;
			}			
				}catch(SQLException e) {
					e.printStackTrace();
				}
		}
	
}
