package hash;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/*
 * SHA256 알고리즘을 이용하여 userbackup 테이블의 비밀번호를 해쉬값으로 변경하기
 */
public class DigestMain2 {
	public static void main(String[] args) throws Exception {
		Class.forName("org.mariadb.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/classdb","scott","1234");
		PreparedStatement pstmt = conn.prepareStatement("select userid, password from userbackup");
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			String id = rs.getString(1);
			String pass = rs.getString(2);
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			String hashpass = "";
			byte[] plain = pass.getBytes();
			byte[] hash = md.digest(plain);
			for(byte b : hash) {
				hashpass += String.format("%02X", b);
			}
			pstmt.close();
			pstmt = conn.prepareStatement("update userbackup set password=? where userid=?");
			pstmt.setString(1, hashpass);
			pstmt.setString(2, id);
			pstmt.execute();
		}
	}
}
