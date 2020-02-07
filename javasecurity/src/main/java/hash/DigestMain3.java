package hash;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/*
 * 화면에서 아이디와 비밀번호를 입력받아서
 * 해당 아이디가 userbackup 테이블에 없으면 "아이디 확인" 출력
 * 해당 어아디의 비밀번호를 비교해서 맞으면 "반갑습니다. 아이디님" 출력
 * 해당 아이디의 비밀번호를 비교해서 틀리면 "비밀번호 확인" 출력
 */
public class DigestMain3 {
	public static void main(String[] args) throws Exception {
		Class.forName("org.mariadb.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/classdb","scott","1234");
		PreparedStatement pstmt = conn.prepareStatement("select userid, password from userbackup where userid=?");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("아이디를 입력하세요");
		String inId = br.readLine();
		System.out.println("비밀번호를 입력해주세요.");
		String inPass = br.readLine();
		pstmt.setString(1, inId);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()) { //아이디 존재
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			String hashpass="";
			byte[] plain = inPass.getBytes();
			byte[] hash = md.digest(plain);
			for(byte b : hash) {
				hashpass += String.format("%02X", b);
			}
			if(rs.getString(1).equals(hashpass)) {
				System.out.println(inId + "님 로그인되었습니다.");
			} else {
				System.out.println("비밀번호가 틀렸습니다. 다시 입력해주세요.");
			}
		} else { //아이디없음
			System.out.println("아이디 없음.");
		}
	}
}
