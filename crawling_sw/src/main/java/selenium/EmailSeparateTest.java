package selenium;

public class EmailSeparateTest {
	public static void main(String[] args) {
		String str = "sewoom21848700@hanmail.net@sewoom21848700@hanmail.net";
		int x = str.indexOf("@", str.indexOf("@") + 1);
		System.out.println(str.substring(0, x) + ", " + str.substring(x + 1));
	}
}
