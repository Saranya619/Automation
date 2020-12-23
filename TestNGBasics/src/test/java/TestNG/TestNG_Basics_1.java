package TestNG;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class TestNG_Basics_1 {
	public WebDriver driver;

	public void testIntialize(String browser) throws Exception {

		if (browser.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver", "E:\\Obsqura\\Selenium_Files\\Drivers\\chromedriver.exe");
			driver = new ChromeDriver();

		} else if (browser.equalsIgnoreCase("firefox")) {
			System.setProperty("webdriver.gecko.driver", "E:\\Obsqura\\Selenium_Files\\Drivers\\geckodriver.exe");
			driver = new FirefoxDriver();

		} else if (browser.equalsIgnoreCase("edge")) {
			System.setProperty("webdriver.edge.driver", "E:\\Obsqura\\Selenium_Files\\Drivers\\msedgedriver.exe");
			driver = new EdgeDriver();

		} else {
			throw new Exception("Invalid Browser");
		}
	}
	@BeforeSuite
	public void environmentVerify() {
		System.out.println("Environment setup done");
	}
	@AfterSuite
	public void environmentClose() {
		System.out.println("Environment close");
	}
	@BeforeTest
	public void dbConnection() {
		System.out.println("Database Connected successfully!");
	}

	@AfterTest
	public void dbterminate() {
		System.out.println("Database disconnected!");
	}

	@BeforeMethod
	@Parameters({"browser","url"})
	public void browserLaunch(String browserName,String URL) throws Exception {
		System.out.println("Launching browser...");
		testIntialize(browserName);
		driver.get(URL);
	}

	@AfterMethod
	public void browserClose(ITestResult result) throws IOException {
		if (ITestResult.FAILURE == result.getStatus()) {
			TakesScreenshot takeScreenshot = (TakesScreenshot) driver;
			File Screenshot = takeScreenshot.getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(Screenshot, new File("./Screenshots/" + result.getName() + ".png"));
		}
		System.out.println("Closing browser.....");
		driver.close();
		System.out.println("Test Completed");
	}

	@Test(priority=0,enabled=true)
	public void verifyTitle() {
		String actualTitle = driver.getTitle();
		System.out.println(actualTitle);
		String expTitle = "Welcome: Mercury Tours:";
		//Assert.assertEquals(actualTitle, expTitle, "Title mismatched");
		int a=10;int b=5;
		Assert.assertFalse(10>5,"false");
		/*SoftAssert asserts = new SoftAssert();
		asserts.assertEquals("hello", "hai","invalid");
		System.out.println("I am here........");
		asserts.assertAll();*/
	}

	@Test(priority=1,enabled = false,dataProvider="userCredentials")
	public void verifyLogin(String userName,String password) {
		WebElement username = driver.findElement(By.name("userName"));
		username.sendKeys(userName);
		WebElement pass = driver.findElement(By.name("password"));
		pass.sendKeys(password);
		WebElement submit = driver.findElement(By.name("submit"));
		submit.click();
	}
	@DataProvider(name = "userCredentials")
	public Object[][] dataForVerifyLogin(){
		Object data[][]=new Object[2][2];
		data[0][0]="admin";
		data[0][1]="admin";
		data[1][0]="saranya";
		data[1][1]="saranya";
		return data;
	}
	@Test(priority=2, enabled=false)
	public void verifyRegisterLink() {
		WebElement register = driver.findElement(By.xpath("//a[text()='REGISTER']"));
		register.click();
	}

	@Test(priority=3,enabled=false)
	public void verifyContactLink() {
		WebElement contact = driver.findElement(By.xpath("//a[text()='CONTACT']"));
		contact.click();
	}
}
