package aes;

/*
 * AES 암호화
 */
public class CipherMain1 {
	public static void main(String[] args) {
		String plain1 = "안녕하세요. 홍길동 입니다.";
		String cipher1 = CipherUtil.encrypt(plain1);
		System.out.println("암호문:" + cipher1);
		String plain2 = CipherUtil.decrypt(cipher1);
		System.out.println("복호문 :" + plain2);
	}
}
