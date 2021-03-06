package DemoScenario;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.mirketa.factory.BrowserFactory;
import com.mirketa.factory.DataProviderFactory;
import com.mirketa.pages.EditTeamPage;
import com.mirketa.pages.HomePage;
import com.mirketa.pages.RRDSearchpage;
import com.mirketa.pages.RRD_user_adding;
import com.mirketa.pages.SalesForceLoginPage;
import com.mirketa.pages.Step1ConfigurationPage;
import com.mirketa.pages.TeamPage;
import com.mirketa.utility.Helper;
import com.mirketa.utility.Xls_Reader;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class EditTeamSetupCase {
	
	WebDriver driver;
	ExtentReports report;
	ExtentTest logger;
	Properties pro;
	public String Testdata_sheet_path = System.getProperty("user.dir") + File.separator + "AppData" + File.separator
			+ "TeamData.xlsx";

	@BeforeMethod
	public void setUp() throws FileNotFoundException, InterruptedException {
		report = new ExtentReports("./Report/DefineTeamSetup.html");
		logger = report.startTest("Verify add  Define Team Setup ");
		logger.log(LogStatus.INFO, "Open Browser and type url in address bar");
		driver = BrowserFactory.getBrowser("chrome");
		logger.log(LogStatus.INFO, "Salesforce login page is loading.");
		driver.get(DataProviderFactory.getConfig().getApplicationUrl());
		logger.log(LogStatus.INFO, "Application is up and running");
		SalesForceLoginPage salesForceLoginPage = PageFactory.initElements(driver, SalesForceLoginPage.class);
		salesForceLoginPage.loginApplicaiton(DataProviderFactory.getExcel().getData(0, 1, 0),
				DataProviderFactory.getExcel().getData(0, 1, 1));
		logger.log(LogStatus.INFO, "Login into application");
         Thread.sleep(5000);
		RRDSearchpage rrdsearch = PageFactory.initElements(driver, RRDSearchpage.class);

		rrdsearch.appclick();
		logger.log(LogStatus.INFO, "Succesfully Clicked on Applauncher");

		rrdsearch.RRdsearch();
		logger.log(LogStatus.INFO, "Succesfully search the RRD");

		rrdsearch.RRDdrpdwnselection();
		logger.log(LogStatus.INFO,
				"Succesfully  RRD selection from Dropdown and Directed to RRDHome page successfully");

	}
	
	@Test
	public void EditTeamsetup() throws InterruptedException{

		Xls_Reader reader = new Xls_Reader(Testdata_sheet_path);
		
		int RRDTeamrowCount = reader.getRowCount("RRDTeam");
		int RRDusercount = reader.getRowCount("RRDUser");
		
		String SteamName = reader.getCellData("RRDTeam", "TeamName", RRDTeamrowCount);
		
		String EditMaxinlimit=reader.getCellData("RRDTeam", "EditMaxIntakeLimit" , RRDTeamrowCount);
		String teamopencaset=reader.getCellData("RRDTeam", "EditTeamopencase", RRDTeamrowCount);
		String EditRecordlimit=reader.getCellData("RRDUser", "EditRecordAssignlimit", RRDusercount);
		String Edituseropencases=reader.getCellData("RRDUser", "EditAllowOpencases", RRDusercount);
		
		
	
		
		
		TeamPage teamobj = PageFactory.initElements(driver, TeamPage.class);

		teamobj.clickonDefineTeamtab();
		logger.log(LogStatus.INFO, "Successfully clicked on Define Team tab");
		Thread.sleep(2000);

		teamobj.ProcessonSearchTeam(SteamName);
		logger.log(LogStatus.INFO, "Successfully searched the  Team name and get the value");
		teamobj.clickonTeamname();
		logger.log(LogStatus.INFO, "Successfully clicked on TeamId and Directed to Team details page");
		
		EditTeamPage editteamobj=PageFactory.initElements(driver,EditTeamPage.class);
		editteamobj.ClickonEditTeam();
		logger.log(LogStatus.INFO, "Successfully clicked on Edit Team btn");
		editteamobj. scrolldowntoreadonlydata();
		logger.log(LogStatus.INFO, "Successfully scroll down to Max intake limit");
		
		editteamobj.EditMaxIntakeLimit(EditMaxinlimit);
		logger.log(LogStatus.INFO, "Clear the value and enter the  Max intake limit");
		//editteamobj.scrolldowntoopencase();
		editteamobj.EditTeamopencase(teamopencaset);
		editteamobj.clickonEditteamsave(); 
		logger.log(LogStatus.INFO, "Save the Edited value successfully");
		driver.navigate().refresh();
		RRD_user_adding userobj= PageFactory.initElements(driver,RRD_user_adding.class);
		
		editteamobj.clickonRelatedtab();
		editteamobj.clickonviewall();
		editteamobj.ProcessEditUsers(EditRecordlimit,Edituseropencases);
		logger.log(LogStatus.INFO, "Save the Edited value successfully");
		
		}
		
		

	@AfterMethod
	public void teardown(ITestResult result) {
		if (result.getStatus() == ITestResult.FAILURE) {
			String path = Helper.captureScreenshot(driver, result.getName());
			logger.log(LogStatus.FAIL, logger.addScreenCapture(path));
		}
		BrowserFactory.closeBrowser(driver);
		report.endTest(logger);
		report.flush();
	}


	

}

	
	


