import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;


public class vgbagg {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.print(bz("https://tapi.m.zing.vn/api/contact/searchProfile?api_key=0be3747aacc670a51a83230bcfe80173&session_key=x4xC.100640129.a1.cg3YONWf9MFvZDfcU2abOtWf9MDuAILZUTD7dYvM9MC"));
	}
	public static String bz(String str) {
        byte[] bytes;
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            try {
                bytes = str.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                bytes = str.getBytes();
            }
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : instance.digest(bytes)) {
                stringBuilder.append(Integer.toHexString((b & 240) >>> 4));
                stringBuilder.append(Integer.toHexString(b & 15));
            }
            return stringBuilder.toString();
        } catch (Throwable e2) {
            e2.printStackTrace();
            throw new RuntimeException(e2);
        }
    }
}