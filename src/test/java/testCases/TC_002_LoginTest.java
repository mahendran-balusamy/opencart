package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.HomePage;
import pageObjects.LoginPage;
import pageObjects.MyAccountPage;
import testBase.BaseClass;

public class TC_002_LoginTest extends BaseClass{
	@Test(groups= {"sanity", "master"})
	public void verify_login()
	{
		logger.info("**** starting TC_002_LoginTest ****");
		logger.debug("capturing application debug logs....");
		
		try {
			HomePage hp = new HomePage(driver);
			hp.clickMyAccount();
			Thread.sleep(5000);
			logger.info("clicked myaccount link on the home page..");
			hp.clickLogin();//Login link under MyAccount
			Thread.sleep(5000);
			logger.info("clicked on login link under myaccount..");
			
			//Login page
			LoginPage lp = new LoginPage(driver);
			logger.info("Entering valid email and password..");
			lp.setEmail(p.getProperty("email"));
			Thread.sleep(5000);
			lp.setPassword(p.getProperty("password"));
			logger.info("Enterd the password");
			Thread.sleep(5000);
			lp.clickLogin();//Login button
			logger.info("clicked on login button");
			
			//My Account Page
			MyAccountPage macc = new MyAccountPage(driver);
			boolean targetPage = macc.isMyAccountPageExists();
			
			if (targetPage == true)
			{
				logger.info("Login test passed...");
				Assert.assertTrue(true);
			}
			else
			{
				logger.error("Login test failed");
				Assert.fail();
			}
		}
		catch(Exception e)
		{
			Assert.fail();
		}
		logger.info("**** Finished TC_002_LoginTest ****");
	}
}
