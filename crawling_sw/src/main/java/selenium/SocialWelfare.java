package selenium;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

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
		int total = 37475;
		int cnt = 1;
		for(int i = 1 ; i < total ; i++) {
			List<WebElement> gets = driver.findElements(By.cssSelector(".bbs_tit a"));
			
			for(int j = 0 ; j < gets.size() ; j++) {
					gets.get(j).click();
					List<WebElement> tables = driver.findElements(By.cssSelector(".tbl_st"));
					List<WebElement> table1 = tables.get(0).findElements(By.cssSelector("table tbody tr"));
					List<WebElement> tr = tables.get(0).findElements(By.cssSelector("table tbody tr"));
					List<WebElement> td = tr.get(0).findElements(By.cssSelector("td"));
					System.out.println(cnt++);
					System.out.println(td.get(0).getText());
//					System.out.println(tr.get(0).getText());
					driver.navigate().back(); // 뒤로가기
					gets = driver.findElements(By.cssSelector(".bbs_tit a")); // List 초기화
			}
			
			// 페이지 이동
			if(i % 10 == 0) {
				driver.findElement(By.cssSelector(".pgeR1")).click();
			} else {
				driver.findElement(By.linkText(i+1 + "")).click();
			}
		}
		System.out.println(driver.findElements(By.cssSelector(".bbs_tit a")).size());
//		driver.findElement(By.name("address_input")).click();
//		new Actions(driver).pause(1000);
//		Thread.sleep(1000);
//		
//		driver.findElement(By.linkText("현재 위치로 설정합니다.")).click();
//		Thread.sleep(2000);
////		System.out.println(driver.getPageSource());
//		WebElement clsContent = driver.findElement(By.id("content")).findElements(By.cssSelector(".content > div")).get(3);
//		List<WebElement> infos = clsContent.findElements(By.cssSelector(".restaurant-list > div table .restaurants-info"));
//		for(WebElement el : infos) {
//			System.out.println(el.getText());
//		}
//		System.out.println(infos.size());
	}
	
	public static void main(String[] args) throws Exception {
		new SocialWelfare().crawl();
	}
}
