package testCases;
import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.AccountRegistrationPage;
import pageObjects.HomePage;
import testBase.BaseClass;

public class TC_001_AccountRegistrationTest extends BaseClass{
	//public WebDriver driver;
	
	@Test(groups= {"regression", "master"})
	public void verify_account_registration()
	{
		logger.info("**** Starting TC_001_AccountRegistrationTest ****");
		try {
			HomePage hp = new HomePage(driver);
			hp.clickMyAccount();
			logger.info("Clicked on MyAccount link");
			hp.clickRegister();
			logger.info("Clicked on Registration link");
			AccountRegistrationPage regpage = new AccountRegistrationPage(driver);
			
			Thread.sleep(5000);
			
			logger.info("Entering customer details...");
			regpage.setFirstName(randomString().toUpperCase());
			regpage.setLastName(randomString().toUpperCase());
			regpage.setEmail(randomString()+"@gmail.com");//randomly generated the email.
			
			String password = randomAlphaNumeric();
			regpage.setTelephone("9894146365");
			regpage.setPassword(password);
			regpage.setConfirmPassword(password);
			regpage.setPrivacyPolicy();
			regpage.clickContinue();
			logger.info("Clicked on continue..");
			
			Thread.sleep(5000);
			
			String confmsg = regpage.getConfirmationMsg();
			logger.info("Validating expected message..");
			Assert.assertEquals(confmsg, "Your Account Has Been Created!");
		}
		catch(Exception e) {
			logger.error("test failed..");
			Assert.fail();
		}
		logger.info("**** finished TC_001_AccountRegistrationTest ****");
	}
}
