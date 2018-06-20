package com.inhance.testFramework;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import javax.swing.text.html.HTMLDocument.Iterator;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;

/***
 * Use WebDriver to pull all elements from the page
 * Tag each element with a custom attribute (qadate) and give it a timestamp for its value
 * Pull all attributes and event handlers of the web element
 * @author gaguilar
 *
 */
public class WebAppEventListener {
	WebDriver driver;
	JavascriptExecutor jse;
	Calendar calendar;	
	SimpleDateFormat sdf;
	String attributeName;
	HashMap<String, String> elementAttributesAndEventHandlers;
	
	String javascriptString;
	
	Timestamp timestamp;
	String value;
	long startProcess;
	long endProcess;
	
	String loc;
	PrintWriter out;
	
	String startDataStr;

	public WebAppEventListener() {
		try {
//			initialize();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//for external use
	public WebAppEventListener(WebDriver driverParam) {
		try {
			initialize(driverParam);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	public void initialize() throws Exception {
//		startTimer("initialize");
//		System.out.println("initialize");
//		//chromeDriverLocation == "the driver"
//		//chromeBinaryLocation == "the car"
//		/***
//		 * From our resources folder, copy chromedriver.exe into a Driver folder
//		 * Modify that chrome driver to attach to the chrome binary as designated in the Fitnesse table
//		 */
//		ClassLoader classLoader = getClass().getClassLoader();
//        URL resource = classLoader.getResource("chromedriver.exe");
//		File chromedriver = new File("Driver"+"\\chromedriver.exe");
//        if (!chromedriver.exists()) {
//        	chromedriver.createNewFile();
//            FileUtils.copyURLToFile(resource, chromedriver);
//        }
//		String chromeDriverLocation = chromedriver.getAbsolutePath();
//        
//		ChromeOptions options = new ChromeOptions();
//		options.setBinary("C:\\GoogleChromePortable\\GoogleChromePortable.exe");
//		options.addArguments("disable-infobars");
//		options.addArguments("--allow-file-access-from-files");
//		
//		System.setProperty("webdriver.chrome.driver", chromeDriverLocation);              
//		driver = new ChromeDriver(options);
//		
//		calendar = Calendar.getInstance();		
//		sdf = new SimpleDateFormat("yyyyMMMdd");
//		attributeName = "qaId"+sdf.format(calendar.getTime());
//		startDataStr = "qaEventHandlers"+sdf.format(calendar.getTime());
//		javascriptString= 
//				"var type = document.createAttribute(arguments[1]);" + 
//				"type.nodeValue = arguments[2];" + 
//				"arguments[0].setAttributeNode(type);";
//		
////		String baseUrl = "http://localhost:8024/index.html?pet=Dog";
//		String baseUrl = "http://www.inhance.com";
////		String baseUrl = "http://store.demoqa.com/";
//	    driver.get(baseUrl);
//	    endTimer("initialize");
//	    getDuration("initialize");
//	    elementAttributesAndEventHandlers = new HashMap();
//	    createLogFile();
//	    
//	    waitForLoadToFinish();
//	    isjQueryLoaded(driver);
//	    
////	    String xpathString = "//li[@id='menu-item-33']//a[@href='http://store.demoqa.com/products-page/product-category/']";
////	    WebElement el = driver.findElement(By.xpath(xpathString));
//	    
//	    //driver.findElements would have to be recreated to store xpaths of each element instead, why not use xslt?
//	    List<WebElement> elList = driver.findElements(By.xpath("//*"));
//	    generateCustomIdForWebElementList(elList);
//	    //Tree or dictionary?
//	    //A Tree would be faster, but a dictionary would be easier to create
////	    Dictionary<WebElement, String> elDictionary = new Dictionary<WebElement, String>();//dictionary is deprecated in Java
////	    HashMap<String, WebElement> elMap = new HashMap<String, WebElement>();
////	    String xpathKey;
//	    
//	    
////	    for(int i=0; i<elList.size(); i++) {
////	    	xpathKey = getXpathOfWebElement(elList, i);
////	    	elMap.put(xpathKey, elList.get(i));
////	    }
//	    
////	    for(int i=0; i<elList.size(); i++) {
//////	    	System.out.print(i+": " + elList.get(i).getTagName()+ "\t");
////	    	System.out.print(i+": " + elList.get(i).getTagName()+ "\n");
////	    	lookForEventListeners(elList.get(i));	    	
////	    }
//
//	    
//
//	}
	public void initialize(WebDriver driverParam) throws Exception {
		startTimer("initialize");
		this.driver = driverParam;
		calendar = Calendar.getInstance();		
		sdf = new SimpleDateFormat("yyyyMMMdd");
		attributeName = "qaId"+sdf.format(calendar.getTime());
		startDataStr = "qaEventHandlers"+sdf.format(calendar.getTime());
		javascriptString= 
				"var type = document.createAttribute(arguments[1]);" + 
				"type.nodeValue = arguments[2];" + 
				"arguments[0].setAttributeNode(type);";

	    endTimer("initialize");
	    getDuration("initialize");
	    elementAttributesAndEventHandlers = new HashMap();
	    createLogFile();
	    
	    waitForLoadToFinish();
	    try {
	    	isjQueryLoaded(driver);
	    }catch(TimeoutException e) {
	    	System.out.println("Jquery is not loaded, injecting Jquery....");
	    }finally {
	    	injectJquery();
	    }
	    
	    List<WebElement> elList = driver.findElements(By.xpath("//*"));
	    generateCustomIdForWebElementList(elList);

	}
	
//	//from: https://stackoverflow.com/questions/36930362/is-there-a-way-in-selenium-through-which-we-can-verify-that-the-image-displayed
//	protected String generateElementHash(WebElement el) {
//		final String JS_GET_IMAGE_HASH =
//				  "var hash = 0, ele = arguments[0], xhr = new XMLHttpRequest();      " +
//				  "var src = ele.src || window.getComputedStyle(ele).backgroundImage; " +
//				  "xhr.open('GET', src.match(/https?:[^\"')]+/)[0], false);           " +
//				  "xhr.send();                                                        " +
//				  "for (var i = 0, buffer = xhr.response; i < buffer.length; i++)     " +
//				  "  hash = (((hash << 5) - hash) + buffer.charCodeAt(i)) | 0;        " +
//				  "return hash.toString(16).toUpperCase();                            ";
//
//				JavascriptExecutor js = (JavascriptExecutor)driver;
//		
//				// compute the hash of the logo
//				String element_hash = (String)js.executeScript(JS_GET_IMAGE_HASH, el);
//
//				// print the hash code
//				System.out.println(element_hash);
//				
//				return element_hash;
//	}
	
	protected void generateCustomIdForWebElementList(List<WebElement> elementList) {	
		//using xmlreader would give us our reader.Read()
		//can we just increment elementList as we go through the Doc?
		//but now, we have a possible issue with the doc and the elementlist DESYNC due to dynamic elements
		//Imagine a div being deleted, and another div is appended at the bottom
		//I really want to append a custom attribute to each element, but imagine appending an element with its own xpath, but then something previous to it changes
		//what we can do instead is create that custom attribute, and then do a WebDriver.findElement(By.cssSelector("[attributeName='qa06082018']");
		//the point for the qa with date attributename is that we want a name that the devs wouldn't normally use, but still have the attributeName be predictable.
		//For dynamic elements that come out, we'll have to capture them and tag them as we trigger them with clicks and maybe timing
		//If we use timestamps as the value of the attribute, and store those timestamps elsewhere, it may work
		//But then, what if the element with the timestamp disappears and we're trying to click it?
		//This issue can be approached by making each trigger independent of each other (or maybe through scanning for new elements?) 
		//The former would really slow us down, the latter may work with WebDriver.FindElements(), then find the ones without a timestamp?
		
		/* Plan:
		 * Using WebDriver.FindElements(), tag each element with a new timestamp, and add to a HashMap
		 * To find the clickable elements, iterate through the HashMap, and call lookForEventListeners on each
		 * */
		
//		Timestamp timestamp;
//		String attributeName;
//		String attributeValue;
		
//		URL url = new URL(baseUrl);
//		URLConnection connection = url.openConnection();//wouldn't this interfere with our driver?
//		connection.setDoOutput(true);

		WebElement el;

//		startTimer("tagging "+elementList.size()+" elements");
//		for(int i=0; i<elementList.size(); i++) {
//			//ugh, I just realized, how do I write to the current open html?
//			//We can try writing to a URLConnection, but that would interfere with our WebDriver
//			//Solution: Use javascriptexecutor to call container.appendChild
//			el = elementList.get(i);
//			tagElement(el);
////			lookForEventListeners(el);
//		}
//		endTimer("taggingElements");
//		getDuration("taggingElements");
//		
//		startTimer("lookingForAllEventListeners in "+elementList.size()+" elements");
//		for(int i=0; i<elementList.size(); i++) {
//			//ugh, I just realized, how do I write to the current open html?
//			//We can try writing to a URLConnection, but that would interfere with our WebDriver
//			//Solution: Use javascriptexecutor to call container.appendChild
//			el = elementList.get(i);
////			tagElement(el);
//			lookForEventListeners(el);
//		}
//		endTimer("lookingForAllEventListeners");
//		getDuration("lookingForAllEventListeners");
		
		String id;
		
		startTimer("tag element and look for event handlers in "+elementList.size()+" elements");
		for(int i=0; i<elementList.size(); i++) {
			el = elementList.get(i);
			id = String.format("%04d", i);
			populateLogFile(id + ": " + tagElementAlt(el));
		}
		endTimer("tag element and look for event handlers in "+elementList.size()+" elements");
		getDuration("tag element and look for event handlers in "+elementList.size()+" elements");

		interactWithMap(elementAttributesAndEventHandlers);
		
//		return xpathOfWebElement;
	}
	
	public void waitForIdenticalPageSources() {
		
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
			    .withTimeout(6, TimeUnit.SECONDS)
			    .pollingEvery(1, TimeUnit.SECONDS)
			    .ignoring(NoSuchElementException.class);

		wait.until(new Function<WebDriver, Boolean>() 
		{
			public Boolean apply(WebDriver driverCopy) {
				boolean sameSource = false;
				try {
					String pageSourceFirst = driverCopy.getPageSource();
					Thread.sleep(50);
					String pageSourceSecond = driverCopy.getPageSource();
					if(pageSourceSecond.equals(pageSourceFirst)) {
						sameSource=true;
					}			
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				return sameSource;
			}
		});	
	}
	
	public void interactWithMap(Map mp) {
		java.util.Iterator<Map.Entry> it = mp.entrySet().iterator();
		String pairValue;
		int mapCount = 0;
		String mapCountStr;
		WebElement element;
		while(it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			pairValue = pair.getValue().toString();
			mapCountStr = String.format("%04d", mapCount);
			if(pairValue.contains("click")) {
//				System.out.println(mapCountStr + ": Click-able: " + pair.getKey() + ": " + pairValue);
				element = driver.findElement(By.cssSelector("["+attributeName+"='"+pair.getKey()+"']"));
//				element.click();
//				try {
//					Thread.sleep(2000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
			}
			
			if(pairValue.contains("mouseover")|pairValue.contains("mouseout")) {
//				System.out.println(mapCountStr + ": Mouse-able: " + pair.getKey() + ": " + pairValue);
			}
			
			if(pairValue.contains("href")) {
//				System.out.println(mapCountStr + ": Has-href: " + pair.getKey() + ": " + pairValue);
			}
			mapCount++;
		}
	}
	
	public void createLogFile() {
        try {
        	loc ="C:\\WebAppEventListenerLog.txt";
        	out = new PrintWriter(loc);
	        out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void populateLogFile(String str) {
		//open out for appending
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(loc, true)));
//	    	out.println(appendedIndex + ": " + navigationPathAlternate.get(navArray.size()-1));
	    	out.append("\r\n\r\n" + str);
	    	out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}    	
	}
	
	public void startTimer(String methodName) {
		startProcess = new Timestamp(System.currentTimeMillis()).getTime();
		System.out.println("start "+methodName+ ": " + startProcess);
	}
	
	public void endTimer(String methodName) {
		endProcess = new Timestamp(System.currentTimeMillis()).getTime();
		System.out.println("done " +methodName+ ": " + endProcess);
	}
	
	public void getDuration(String methodName) {
		System.out.println("duration of "+ methodName +": " + (endProcess - startProcess) + " msec");
	}
	
	/***
	 * Write a new attribute into our web element and keep a hashmap of tags
	 * qaDate = timestamp
	 * Below code has been moved to globals
	 * @param el
	 */
//	Calendar calendar = Calendar.getInstance();		
//	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMMdd");
//	String id = "qa_"+sdf.format(calendar.getTime());
//	
//	JavascriptExecutor jseCopy = (JavascriptExecutor)driver;
//	String javascriptString= 
//			"var type = document.createAttribute(arguments[1]);" + 
//			"type.nodeValue = arguments[2];" + 
//			"arguments[0].setAttributeNode(type);";
//	
//	Timestamp timestamp;
//	String value;
	
	public void tagElement(WebElement el) {		
		timestamp = new Timestamp(System.currentTimeMillis());
		value = timestamp.getTime()+"";
		JavascriptExecutor jseCopy = (JavascriptExecutor)driver;
		jseCopy.executeScript(javascriptString,  el, attributeName, value);
	}
	
	//creating attribute with arguments[4] ends up becoming undefined for some reason, but the event handlers do get logged

	public String tagElementAlt(WebElement el) {
		
		timestamp = new Timestamp(System.currentTimeMillis());
		value = timestamp.getTime()+"";
		JavascriptExecutor jseCopy = (JavascriptExecutor)driver;
		javascriptString= 
				"var type = document.createAttribute(arguments[1]);" + 
				"type.nodeValue = arguments[2];" + 
				"arguments[0].setAttributeNode(type);" +
				"var items = {};"+
				"for (index = 0; index <arguments[0].attributes.length; ++index){"+
				"	items[arguments[0].attributes[index].name] = arguments[0].attributes[index].value"+
				"};"+
//				"var startData = arguments[3];"+
				"	items[arguments[3]] = jQuery._data(arguments[0], \"events\");"+
//				"var events = {};"+
//				"events = jQuery._data(arguments[0], \"events\");" +
//				"var temp = {};"+
//				"for (index = 0; index <events.size(); ++index){"+
//				"temp = events[index].split(\"=\");"+
//				"items[temp[0]] = temp[1];"+
//				"};"+
				"return items;";

		String attributesAndEvents = jseCopy.executeScript(javascriptString,  el, attributeName, value, startDataStr)+"";
		if(attributesAndEvents.contains("qaEventHandlers2018Jun13={")) {
			System.out.println("tagElementAlt: " + attributesAndEvents);			
		}
		elementAttributesAndEventHandlers.put(value, attributesAndEvents);
		return attributesAndEvents;
	}
	
	public static List<String> generateEventList(String str){
		List<String> ls = new ArrayList<String>();
		CharStack stack = new CharStack();
		String temp = "";
		for(int i=0; i<str.length(); i++) {
			if(str.charAt(i)=='}') {
				//pop chars until {
				while(stack.size()!=0 && stack!=null) {
					//should only occur once during the while loop
					if(stack.peek()=='{') {
//						temp = stack.pop() + temp;	
						stack.pop();
						//append list
						ls.add(temp);
						temp = "";
						break;
					}else {
						temp = stack.pop() + temp;
					}					
				}
			}else if(str.charAt(i) == ']') {
				//pop chars until [				
				while(stack.size()!=0 && stack!=null) {
					//should only occur once during the while loop
					if(stack.peek()=='[') {
//						temp = stack.pop() + temp;
						stack.pop();
						//append list
						ls.add(temp);
						temp = "";
						break;
					}else {
						temp = stack.pop() + temp;
					}		
				}
			}else {
				//fill up our stack
				stack.push(str.charAt(i));
			}
		}	
		return ls;
	}

	//This needs to be more responsive
	public void waitForLoadToFinish() {
		//wait for invisibility of a certain //div[@class='se-pre-con']
		//For now, sleep is fine
	    startTimer("waitForLoadToFinish");
		System.out.println("waitForLoadToFinish()");
		try {
			Thread.sleep(7000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    endTimer("waitForLoadToFinish");
	    getDuration("waitForLoadToFinish");
	}
	
	/***
	 * We need a watch function to compare the current set of tagged elements with any new ones that result from interaction with the website
	 */
	
//	/***
//	 * Takes RAW Html and uses Jsoup to turn it into a document (meant for cleaning)
//	 */
//	public Document convertRawHtmlToDocumentUsingJsoup(String rawHtml) {
//		Document document = Jsoup.parse(rawHtml);
//		return document;
//	}
	
	//don't need to generate a copy of the html file
	//but we do need to create a parallel that we refer to in order to grab the xpath for reference
	//again, the point is to access everything by xpath AUTOMATICALLY
	//unless we use a crawler and generate the xpath when we encounter a new element in realtime instead of the pre-processing
	//which seems to be the better option since we need to consistently refresh the driver page anyway?
	public void convertJsoupDocumentToXml() {
		
	}

	
	
	public void lookForEventListeners(WebElement el) {
//		System.out.println("addEventListenerByJse()");
//		String javascriptCommand = "document.getElement(By.xpath(\"//div[@class='button bioVisualizer']\")).click()";
//		String javascriptCommand = 
//				"var getEventListeners = function(elem, type) {" + 
//				"  return getFromGlobalCache(elem, type);" + 
//				"};"+
//				"var list = getEventListeners(document.getElementById(\"navigation\")); console.log(list[\"focus\"][0][\"listener\"].toString());";
//		WebElement biovisualizer = driver.findElement(By.xpath("//div[@class=\"petView\"]"));
//		String xpathString = "//a[@href='http://demoqa.com/registration/']";
		
		
		JavascriptExecutor jseCopy = (JavascriptExecutor)driver;
//		jseCopy.executeScript("arguments[0].click();", biovisualizer);
//		jseCopy.executeScript(javascriptCommand);
//		jseCopy.executeScript("arguments[0].click();", biovisualizer);
//		jseCopy.executeScript("alert('Welcome To SoftwareTestingMaterial');");
//		String sText =  jseCopy.executeScript("return document").toString();
//		System.out.println(sText);
		
		
//		System.out.println(jseCopy.executeScript("return $(\".copyright\").text();"));
//		System.out.println(jseCopy.executeScript("return $(\"#petRoto\").text();"));
//		isjQueryLoaded(driver);
		
//		System.out.println("-");
		//TODO: This line is what gives us all the registered events of an element 
		//jseCopy.executeScript("return jQuery._data(arguments[0], \"events\");", el)
		String temp = jseCopy.executeScript("return jQuery._data(arguments[0], \"events\");", el)+"";
//		String temp2 = el.getAttribute("innerHTML");
//		String temp3 = el.getText();
//		String temp4 = el.toString();
//		String temp5 = el.getAttribute("id");
//		String temp6 = el.getAttribute("class");
		String temp7 = getAllAttributesOfElement(el);
//		if(!temp.equals("null")) {
//			System.out.println(el.toString());
//			System.out.println("innerHTML: "+temp2);
//			System.out.println("getText(): "+temp3);
//			System.out.println("toString(): "+temp4);
			
//			System.out.println("events: "+temp);
//			System.out.println("attributes: "+temp7);
//			System.out.print("\n");
//		}
//		System.out.println("Done");
//		el.click();
//		driver.close();
//		biovisualizer.click();
	}
	
	//modified from https://stackoverflow.com/questions/36416796/selenium-webdriver-get-all-the-data-attributes-of-an-element
	protected String getAllAttributesOfElement(WebElement element) {
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		Object aa=executor.executeScript("var items = {}; for (index = 0; index < arguments[0].attributes.length; ++index) { items[arguments[0].attributes[index].name] = arguments[0].attributes[index].value }; return items;", element);
		return aa.toString();
	}
	
	//Throws an unignored Timeout exception
	//need to do either promises or try/catch/finally block
	public boolean isjQueryLoaded(WebDriver driver) {
	    System.out.println("Waiting for jquery ready state complete");
		//return typeof jQuery != 'undefined'
//		startTimer("isjQueryLoaded");
	    boolean test = new WebDriverWait(driver, 30).until(new ExpectedCondition<Boolean>() {
	            public Boolean apply(WebDriver d) {
	                JavascriptExecutor js = (JavascriptExecutor) d;
	                String readyState = js.executeScript("return document.readyState").toString();
//	                System.out.println("Ready State: " + readyState);
	                return (Boolean) js.executeScript("return !!window.jQuery && window.jQuery.active == 0");
	            }
	        });
	    return test;
//		endTimer("isjQueryLoaded");
//		getDuration("isjQueryLoaded");
	}
	
	public String[] returnFirstKeyAndValue() {
		String[] strArray = new String[2];
		Map mp = (Map)elementAttributesAndEventHandlers;
		java.util.Iterator<Map.Entry> it = mp.entrySet().iterator();
		if(it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			strArray[0] = pair.getKey().toString();
			strArray[1] = pair.getValue().toString();//this is our collection of event handlers		
			return strArray;			
		}
		else {
			return null;
		}
	}
	
	public void removeFirstKeyAndValueGivenKey(String key) {
		elementAttributesAndEventHandlers.remove(key);
	}
	
	public String getAttributeName() {
		return attributeName;
	}
	
	public void injectJquery() {
		System.out.println("injectJquery start");
		String injectJqueryCommand =
				"    function l(u, i) {\r\n" + 
				"        var d = document;\r\n" + 
				"        if (!d.getElementById(i)) {\r\n" + 
				"            var s = d.createElement('script');\r\n" + 
				"            s.src = u;\r\n" + 
				"            s.id = i;\r\n" + 
				"            d.body.appendChild(s);\r\n" + 
				"        }\r\n" + 
				"    }\r\n" + 
				"    l('//code.jquery.com/jquery-3.2.1.min.js', 'jquery');";
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript(injectJqueryCommand);
		System.out.println("injectJquery end");
	}
	
	public static void main(String[] args) {
		WebAppEventListener temp = new WebAppEventListener();
	}
}

//partially from https://stackoverflow.com/questions/8746428/how-to-define-a-char-stack?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
class CharStack {
    final StringBuilder sb = new StringBuilder();

    public void push(char ch) {
        sb.append(ch);
    }

    public char pop() {
        int last = sb.length() -1;
        char ch= sb.charAt(last);
        sb.setLength(last);
        return ch;
    }
    
    public char peek() {
    	int last = sb.length() -1;
    	char ch= sb.charAt(last);
    	return ch;
    }

    public int size() {
        return sb.length();
    }
}
