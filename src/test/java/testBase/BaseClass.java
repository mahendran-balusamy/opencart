package testBase;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

public class BaseClass {
	static public WebDriver driver;
	public Logger logger;//org.apache.logging.log4j.Logger
	public Properties p;//java.util package
	
	@BeforeClass(groups= {"sanity", "regression", "master"})
	@Parameters({"os", "browser"})
	public void setup(String os, String br) throws IOException, InterruptedException {
		FileInputStream file = new FileInputStream(".//src/test/resources/config.properties");
		p = new Properties();
		p.load(file);
		
		//Loading log4j2 file
		logger = LogManager.getLogger(this.getClass());
		System.out.println("Value of execution_env in config properties is: " + p.getProperty("execution_env"));
		if(p.getProperty("execution_env").equalsIgnoreCase("remote")) {
			System.out.println("Inside the remote machine");
			DesiredCapabilities capabilities = new DesiredCapabilities();
			
			// os
			if(os.equalsIgnoreCase("windows")) {
				capabilities.setPlatform(Platform.WIN11);
			}
			else if(os.equalsIgnoreCase("mac")) {
				capabilities.setPlatform(Platform.MAC);
			}
			else {
				System.out.println("No matching os..");
				return;
			}
			//Launching browser based on condition
			switch(br.toLowerCase()) {
			case "chrome":
				capabilities.setBrowserName("chrome");
				break;
			case "edge":
				capabilities.setBrowserName("MicrosoftEdge");
				break;
			default:
				System.out.println("No matching browser..");
				return;//exit from the entire script
			}
			
			driver = new RemoteWebDriver(new URL("http://192.168.1.107:4444/wd/hub"), capabilities);
		}
		else if(p.getProperty("execution_env").equalsIgnoreCase("local")) {
			// Launching browser on condition - locally
			switch(br.toLowerCase()) {
			case "chrome":
				driver = new ChromeDriver();
				break;
			case "edge":
				driver = new EdgeDriver();
				break;
			default:
				System.out.println("No matching browser..");
				return;// exit from the entire script
			}
			
			driver.get(p.getProperty("appURL"));
		}
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
		
		driver.manage().window().maximize();
		
		Thread.sleep(5000);
	}
	
	@AfterClass(groups= {"sanity", "regression", "master"})
	public void tearDown() {
		driver.close();
	}
	
	public String randomString() {
		String generatedString = RandomStringUtils.randomAlphabetic(5);
		return generatedString;
	}
	
	public String randomNumber() {
		String generatedString = RandomStringUtils.randomNumeric(10);
		return generatedString;
	}
	
	public String randomAlphaNumeric() {
		String str = RandomStringUtils.randomAlphabetic(3);
		String num = RandomStringUtils.randomNumeric(3);
		return (str + "@" + num);
	}
	
	public String captureScreen(String tname) {
		String timeStamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		TakesScreenshot takeScreenshot = (TakesScreenshot) driver;
		File sourceFile = takeScreenshot.getScreenshotAs(OutputType.FILE);
		
		String targetFilePath = System.getProperty("user.dir") + "\\screenshots\\" + tname + "_" + timeStamp + ".png";
		File targetFile = new File(targetFilePath);
		sourceFile.renameTo(targetFile);
		return targetFilePath;
	}
}
