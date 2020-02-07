package aes;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CipherUtil2 {
	private static byte[] randomKey;
	private final static byte[] iv = new byte[] {
			(byte) 0x8E, 0x12, 0x39, (byte) 0x9C,
			       0x07, 0x72, 0x6F, (byte) 0x5A,
			(byte) 0x8E, 0x12, 0x39, (byte) 0x9C,
			       0x07, 0x72, 0x6F, (byte) 0x5A
	};
	static Cipher cipher;
	static {
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static byte[] getRandomKey(String algo) throws NoSuchAlgorithmException {
		//KeyGen : AES용 키를 생성할 수 있는 객체
	      KeyGenerator keyGen = KeyGenerator.getInstance(algo);
	      //AES 알고리즘 : 128bit ~ 192bit 키로 설정이 가능
	      keyGen.init(128); //128비트짜리 키로 생성
	      SecretKey key = keyGen.generateKey();
	      return key.getEncoded();
	   }
	public static String encrypt(String plain, String key) {
		byte[] cipherMsg = new byte[1024];
		try {
			//key : 암호화에 필요한 키 객체
			Key genkey = new SecretKeySpec(makeKey(key), "AES");
			//초기화 벡터 객체 생성
			AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
			//Cipher.ENCRYPT_MODE : 암호화
			cipher.init(Cipher.ENCRYPT_MODE, genkey, paramSpec);
			//암호화 실행
			cipherMsg = cipher.doFinal(plain.getBytes());
		} catch(Exception e) {
			e.printStackTrace();
		}
		return byteToHex(cipherMsg);
	}
	private static byte[] makeKey(String key) {
		int len = key.length();
		char ch = 'A';
		for(int i=len; i<16; i++) key += ch++;
		return key.substring(0,16).getBytes();
	}
	private static String byteToHex(byte[] cipherMsg) {
		if(cipherMsg == null)
			return null;
		String str = "";
		for(byte b : cipherMsg) {
			str += String.format("%02X", b);
		}
		return str;
	}
	public static String decrypt(String cipher1, String key) {
		byte[] plainMsg = new byte[1024];
		try {
			Key genkey = new SecretKeySpec(makeKey(key), "AES");
			AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
			//Cipher.DECRYPT_MODE : 복호화 모드
			cipher.init(Cipher.DECRYPT_MODE, genkey, paramSpec);
			plainMsg = cipher.doFinal(hexToByte(cipher1.trim()));
		} catch(Exception e) {
			e.printStackTrace();
		}
		return new String(plainMsg).trim();
	}
	private static byte[] hexToByte(String str) {
		if(str == null || str.length() <2) return null;
		int len = str.length() / 2;
		byte[] buf = new byte[len];
		for(int i=0; i<len; i++) {
			buf[i] = (byte)Integer.parseInt(str.substring(i*2,i*2+2),16);
		}
		return buf;
	}
}
