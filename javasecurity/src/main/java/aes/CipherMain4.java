package aes;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CipherMain4 {
   public static void main(String[] args) throws Exception {
   Class.forName("org.mariadb.jdbc.Driver");
   Connection conn = DriverManager.getConnection
            ("jdbc:mariadb://localhost:3306/classdb","scott","1234");
   PreparedStatement pstmt = conn.prepareStatement
            ("select userid,email from userbackup ");
   ResultSet rs = pstmt.executeQuery();
   while(rs.next()) {
	   String userid = rs.getString("userid");
	   String key = CipherUtil3.makehash(userid).substring(0,16);
       String email = rs.getString("email");
       String plainEmail = CipherUtil3.decrypt(email,key);
       System.out.println(userid + ": 이메일 =" + plainEmail);
   }
 }
}