package com.inhance.testFramework;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

//import com.google.common.base.Function;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.sikuli.script.FindFailed;
import org.sikuli.script.ImagePath;
import org.sikuli.script.Region;
import org.sikuli.script.Screen;
import org.testng.annotations.Test;

import com.google.common.base.Function;


public class SimpleFrameworkWithWaitAndImageComparer {
	protected WebDriver driver;
	protected String baseUrl;
	protected WebDriverWait wait;
	protected Screen s = new Screen();  
	public Function<WebElement,Boolean> testWebFixture;
	public Function<String, Boolean> testSikuliFixture;
	public List<Pages> pageList;
	public ArrayList<Pages> pageArrayList;
	public int diffCount;

//	@BeforeClass(alwaysRun = true)
//	public void setUp() throws Exception {	
//		initialize();
//	}
	
	public SimpleFrameworkWithWaitAndImageComparer() {
		initialize();
		
	}
	
	public void initialize() {
     
		ChromeOptions options = new ChromeOptions();
		options.setBinary("C:\\GoogleChromePortable\\GoogleChromePortable.exe");
		options.addArguments("disable-infobars");
		System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");              
		driver = new ChromeDriver(options);
      
	    baseUrl = "https://www.google.com";
//	    baseUrl = "https://www.google.com/imghp?hl=en&tab=wi";
//	    baseUrl = "https://app.mavenlink.com/login?from_redirect=true";
//	    baseUrl = "https://myapps.paychex.com/landing_remote/login.do?TYPE=33554433&REALMOID=06-fd3ba6b8-7a2f-1013-ba03-83af2ce30cb3&GUID=&SMAUTHREASON=0&METHOD=GET&SMAGENTNAME=09PZJoiHr8jiAF1z4DL6SopY5OyRzoKSeZ4yIhpJe7nkRdeIwtlMrg0rd7X3FRDM&TARGET=-SM-https%3a%2f%2fmyapps.paychex.com%2flanding_remote%2findex.do";
	    navigateByAddress(baseUrl);
	    driver.manage().window().maximize(); 
//	    driver.manage().window().setSize(new Dimension(800, 600));
	    wait = new WebDriverWait(driver, 20);
	    
	    //make sure to right click the resources\images folder, select Build Path -> Include
	    ImagePath.add(SimpleFrameworkWithWaitAndImageComparer.class.getCanonicalName()+"/images");
	        
	    pageArrayList = new ArrayList<Pages>();
	    pageArrayList.add(new Pages("/Baseline.xlsx"));
	    
	    diffCount = 0;
		
	}
	
//	@AfterClass(alwaysRun = true)
//	public void tearDown() throws Exception {
//		driver.navigate().to("about:config");
//		driver.navigate().to("about:blank");
//		driver.quit();
//	}
	
//	@Test
//	public void simpleTest() {
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
//	@Test
//	public void imageButtonTest() throws FindFailed {
//		clickElementBySikulixReferenceImageInLocalAreaPreparation("Capture.PNG", s);
//		clickElementBySikulixReferenceImageInLocalArea("Capture.PNG", s);
//	}
	
	@Test
	public void compareBaselineWithScreen() {
//		compareBaseLineWithImmediateScreenshot(takeScreenshot(-1,-1), pageArrayList.get(0).getBaselineImage(0));
		compareSetWithImmediateScreenshot();
//		Assert.fail();
//		takeScreenshot();
	}
	
	/***
	 * Summary: Given an arraylist (with structure arraylist[pageIndex][imageIndex]), go through each page and each image
	 * TODO: After Baseline screenshots have been taken, need to first navigate -> then take a screenshot -> then compare to baseline -> then save diffs
	 * TODO: Need to design and organize navigation input with the consideration of using this for Google Chrome Portable applications (not just website)
	 * Note: Using the baseline, maybe mark hotspots that would trigger the navigation
	 */
	public void compareSetWithImmediateScreenshot() {
		for(int i=0; i<pageArrayList.size(); i++) {
			for(int j=0;j<pageArrayList.get(i).getBaselineImageCount(); j++) {
				System.out.println("compareSetWithImmediateScreenshot(): [i][j]: [" + i + "][" + j + "]" + " [size]: [" + pageArrayList.get(i).getBaselineImageCount() + "]");
				compareBaseLineWithImmediateScreenshot(takeScreenshot(i, j), pageArrayList.get(i).getBaselineImage(j));	
			}
		}
	}
	
	public void navigateByAddress(String address) {
		System.out.println("navigateByAddress(String address): address = " + address); 
		driver.get(address);
	}
	
	
	
//	public void fluentWaitElementHasAppeared(Region r) {
//		Wait<Region> wait = new FluentWait<Region>(r)
//			    .withTimeout(6, TimeUnit.SECONDS)
//			    .pollingEvery(1, TimeUnit.SECONDS)
//			    .ignoring(NoSuchElementException.class);
//		wait.until(fixtureFluentWaitElementHasAppeared(r));
//	}
	
//	public boolean fixtureFluentWaitElementHasAppeared(Region r) {
//		if(false) {
//			return true;
//		}else {
//			return false;
//			
//		}
//	}
	
	public void clickElementByXpathPreparation(String xpath) {
		WebElement el = driver.findElement(By.xpath(xpath));
		Wait<WebElement> wait = new FluentWait<WebElement>(el)
			    .withTimeout(6, TimeUnit.SECONDS)
			    .pollingEvery(1, TimeUnit.SECONDS)
			    .ignoring(NoSuchElementException.class);

		initializeTestFixtureWeb(testWebFixture);
		wait.until(testWebFixture);//automatically feeds the parameter used to initialize wait into the testFixture	
	}
	
	public void clickElementBySikulixReferenceImageInLocalAreaPreparation(String imageFileLocation, Region r) throws FindFailed {
		Wait<String> wait = new FluentWait<String>(imageFileLocation)
			    .withTimeout(6, TimeUnit.SECONDS)
			    .pollingEvery(1, TimeUnit.SECONDS)
			    .ignoring(NoSuchElementException.class);
		
		initializeTestFixtureSikuli(testSikuliFixture, r);
		wait.until(testSikuliFixture);
	}
	
	public void initializeTestFixtureSikuli(Function<String,Boolean> testFixture, final Region r) throws FindFailed{
		testSikuliFixture = new Function<String,Boolean>() {
			public Boolean apply (String imageFileLocation){
				try {
					if(r.find(imageFileLocation)!=null) {
						return true;
					}else {
						return false;
					}
				} 
				catch (FindFailed e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					try {
						throw new FindFailed(imageFileLocation + " not found.");
					} catch (FindFailed e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						return false;
					}
				}				
			}	
		};	
	}
	
	public void initializeTestFixtureWeb(Function<WebElement,Boolean> testFixture) {	
		testFixture = new Function<WebElement,Boolean>(){
			public Boolean apply(WebElement el) {//shallow copy of address
				//wait until element is available					
				return el.isDisplayed();					
			}
		};			
	}
	
	public void clickElementByXpath(String xpath) {
		System.out.println("clickElementByXpath(String xpath): xpath = " + xpath);
		WebElement element = driver.findElement(By.xpath(xpath));
		element.click();
	}
	
	public void clickElementById(String id) {
		System.out.println("clickElementById(String id): id = " + id);
		WebElement element = driver.findElement(By.id(id));
		element.click();		
	}
	
	public void clickElementBySikulixReferenceImage(String imageFileLocation) {
		System.out.println("clickElementBySikulixReferenceImage(String imageFileLocation): imageFileLocation = " + imageFileLocation);
		try {
			s.click(imageFileLocation, 0);
		} catch (FindFailed e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void clickElementBySikulixReferenceImageInLocalArea(String imageFileLocation, Region region) {
		System.out.println("clickElementBySikulixReferenceImage(String imageFileLocation): \nimageFileLocation = " + imageFileLocation + "\nregion = " + region.toString());
		try {
			region.click(imageFileLocation, 0);
		} catch (FindFailed e) {
			// TODO Auto-generated catch block
			System.out.println("clickElementBySikulixReferenceImageInLocalArea(String imageFileLocation, Region region): " + imageFileLocation + " | " + region.toString());
			e.printStackTrace();
		}
	}
	
	public Region generateRegion(int leftMostXCoordinate, int topMostYCoordinate, int regionWidth, int regionHeight) {
		System.out.println("generateRegion(int leftMostXCoordinate, int topMostYCoordinate, int regionWidth, int regionHeight): "
				+ "\nleftMostXCoordinate: " + leftMostXCoordinate
				+ "\ntopMostYCoordinate: " + topMostYCoordinate
				+ "\nregionWidth: " + regionWidth
				+ "\nregionHeight: " + regionHeight);
		
		Region r = new Region(leftMostXCoordinate, topMostYCoordinate, regionWidth, regionHeight);
		return r;
	}	
	
	public void compareBaseLineWithImmediateScreenshot(BufferedImage baselineImage, BufferedImage newImage) {
		BufferedImage diff = getDifferenceImage(baselineImage, newImage);
        File f = new File("Diffs");
        if (!f.exists()) {
            f.mkdirs();
        }
		//The diffs need to be named differently, preferably with the name of the page and the actions, and maybe the element?
		File outputfile = new File("c:\\tmp\\diff"+diffCount+".png"); 
//        File outputfile = new File(f.getAbsolutePath()+"\\diff"+diffCount+".png");
		diffCount++;
		try {
			ImageIO.write(diff, "png", outputfile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected BufferedImage takeScreenshot(int i, int j) {
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		Long timestamp = (new Timestamp(System.currentTimeMillis())).getTime();
		System.out.println("Taking screenshot at: "+timestamp);
		
		BufferedImage in = null;
		try {
			//store screenshot as png
			FileUtils.copyFile(scrFile, new File("c:\\tmp\\screenshot_"+i+"_"+j+"_"+ timestamp +".png"));
			//convert screenshot into buffered image
			in = ImageIO.read(scrFile);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return in;
	}
	
	public static BufferedImage getDifferenceImage(BufferedImage img1, BufferedImage img2) {
	    int width1 = img1.getWidth(); // Change - getWidth() and getHeight() for BufferedImage
	    int width2 = img2.getWidth(); // take no arguments
	    int height1 = img1.getHeight();
	    int height2 = img2.getHeight();
	    if ((width1 != width2) || (height1 != height2)) {
	        System.err.println("Error: Images dimensions mismatch");
	        System.exit(1);
	    }

	    // NEW - Create output Buffered image of type RGB
	    BufferedImage outImg = new BufferedImage(width1, height1, BufferedImage.TYPE_INT_RGB);

	    // Modified - Changed to int as pixels are ints
	    int diff;
	    int result; // Stores output pixel
	    for (int i = 0; i < height1; i++) {
	        for (int j = 0; j < width1; j++) {
	            int rgb1 = img1.getRGB(j, i);
	            int rgb2 = img2.getRGB(j, i);
	            int r1 = (rgb1 >> 16) & 0xff;
	            int g1 = (rgb1 >> 8) & 0xff;
	            int b1 = (rgb1) & 0xff;
	            int r2 = (rgb2 >> 16) & 0xff;
	            int g2 = (rgb2 >> 8) & 0xff;
	            int b2 = (rgb2) & 0xff;
	            diff = Math.abs(r1 - r2); // Change
	            diff += Math.abs(g1 - g2);
	            diff += Math.abs(b1 - b2);
	            diff /= 3; // Change - Ensure result is between 0 - 255
	            // Make the difference image gray scale
	            // The RGB components are all the same
	            result = (diff << 16) | (diff << 8) | diff;
	            outImg.setRGB(j, i, result); // Set result
	        }
	    }

	    // Now return
	    return outImg;
	}
	
//	public static void main(String[] args) {
//		SimpleFrameworkWithWaitAndImageComparer temp = new SimpleFrameworkWithWaitAndImageComparer();
//		
//	}
	
}

