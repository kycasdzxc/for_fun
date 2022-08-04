package selenium;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class SocialWelfare {
	private WebDriver driver;
	
	public static final String WEB_DRIVER_ID = "webdriver.chrome.driver";
	public static final String WEB_DRIVER_PATH = "D:/devtools/chromedriver_win32/chromedriver.exe";
	
	private String base_url;
	
	public SocialWelfare() {
		System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
		
		driver = (WebDriver) new ChromeDriver();
		base_url = "https://www.welfare.net/welfare/jo/joboffer/selectJobofferList.do";
	}

	public void crawl() throws Exception {
		driver.get(base_url);
		int total = 37488; // 전체 건수
		int cnt = 1;
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		
		// 최초 페이지 이동
		for(int i = 0 ; i < 10 ; i++) {
			driver.findElement(By.cssSelector(".pgeR1")).click();
		}		
		
		// 크롤링 시작
		for(int i = 101 ; i < total ; i++) {
			List<WebElement> gets = driver.findElements(By.cssSelector(".bbs_tit a"));
			
			for(int j = 0 ; j < gets.size() ; j++) {
					gets.get(j).click();
					Map<String, String> map = new HashMap<>();
					
					try {
						WebElement company = driver.findElement(By.xpath("//*[@id='cntntsView']/div[1]/table/tbody/tr[1]/td[1]"));
						map.put("company", company.getText());
						System.out.println("회사명 : " + company.getText());
					} catch (Exception e) {
						map.put("company", "");
						System.out.println("회사명 : 없음");
					}
					
					try {
						WebElement boss = driver.findElement(By.xpath("//*[@id='cntntsView']/div[1]/table/tbody/tr[1]/td[2]"));
						map.put("boss", boss.getText());
						System.out.println("대표자 : " + boss.getText());
					} catch (Exception e) {
						map.put("boss", "");
						System.out.println("대표자 : 없음");
					}
					
					try {
						WebElement addr = driver.findElement(By.xpath("//*[@id='cntntsView']/div[1]/table/tbody/tr[2]/td"));
						map.put("addr", addr.getText());
						System.out.println("주소 : " + addr.getText());
					} catch (Exception e) {
						map.put("addr", "");
						System.out.println("주소 : 없음");
					}
					
					try {
						WebElement phone = driver.findElement(By.xpath("//*[@id='cntntsView']/div[1]/table/tbody/tr[3]/td[1]"));
						map.put("phone", phone.getText());
						System.out.println("전화번호 : " + phone.getText());
					} catch (Exception e) {
						map.put("phone", "");
						System.out.println("전화번호 : 없음");
					}
					
					try {
						WebElement fax = driver.findElement(By.xpath("//*[@id='cntntsView']/div[1]/table/tbody/tr[3]/td[2]"));
						map.put("fax", fax.getText());
						System.out.println("팩스 : " + fax.getText());
					} catch (Exception e) {
						map.put("fax", "");
						System.out.println("팩스 : 없음");
					}
					
					try {
						WebElement homepage = driver.findElement(By.xpath("//*[@id='cntntsView']/div[1]/table/tbody/tr[4]/td/a"));
						map.put("homepage", homepage.getText());
						System.out.println("홈페이지 : " + homepage.getText());
					} catch (Exception e) {
						map.put("homepage", "");						
						System.out.println("홈페이지 : 없음");
					}
					
					try {
						WebElement name = driver.findElement(By.xpath("//*[@id='cntntsView']/div[2]/table/tbody/tr[1]/td"));
						map.put("name", name.getText());
						System.out.println("담당자 : " + name.getText());
					} catch (Exception e) {
						map.put("name", "");
						System.out.println("담당자 : 없음");
					}
					
					try {
						WebElement email = driver.findElement(By.xpath("//*[@id='cntntsView']/div[2]/table/tbody/tr[2]/td"));
						map.put("email", email.getText());
						System.out.println("이메일 : " + email.getText());
					} catch (Exception e) {
						map.put("email", "");
						System.out.println("이메일 : 없음");
					}
					list.add(map);
					
					System.out.println("============================================================================");
					System.out.println(cnt++ + "번째 완료");
					System.out.println("============================================================================");
					driver.navigate().back(); // 뒤로가기
					gets = driver.findElements(By.cssSelector(".bbs_tit a")); // List 초기화
			}
			
			// 페이지 이동
			if(i % 10 == 0) {
				for(Map<String,String> map : list) { saveDB(map); } // 100건마다 DB에 저장
				list = new ArrayList<>();
				driver.findElement(By.cssSelector(".pgeR1")).click();
			} else {
				driver.findElement(By.linkText(i+1 + "")).click();
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		new SocialWelfare().crawl();
	}
	
	static void saveDB(Map<String, String> map) throws Exception {
		PreparedStatement pstmt = getConnection().prepareStatement(
				"INSERT INTO TBL_SW_CRAWLING VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
		int idx = 1;
		pstmt.setString(idx++, map.get("company"));
		pstmt.setString(idx++, map.get("boss"));
		pstmt.setString(idx++, map.get("addr"));
		pstmt.setString(idx++, map.get("phone"));
		pstmt.setString(idx++, map.get("fax"));
		pstmt.setString(idx++, map.get("homepage"));
		pstmt.setString(idx++, map.get("name"));
		pstmt.setString(idx++, map.get("email"));
		pstmt.executeUpdate();
		pstmt.close();
	}
	
	public static Connection getConnection() throws SQLException, ClassNotFoundException {
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/xe", "SW_CRAWLING", "1234");
		return conn;
	}
}
