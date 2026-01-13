package pageObject;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MakeMyTripFlightPage {
	
	    WebDriver driver;
	    WebDriverWait wait;

	    public MakeMyTripFlightPage(WebDriver driver) 
	    {
	        this.driver = driver;
	        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	        PageFactory.initElements(driver,this);
	    }

	    // Close popup
	    @FindBy(xpath = "//span[@class='commonModal__close']")
	    WebElement closePopup;

	    // Round Trip
	    @FindBy(xpath = "//li[@data-cy='roundTrip']")
	    WebElement roundTrip;

	    // From
	    @FindBy(id = "fromCity")
	    WebElement fromCity;

	    @FindBy(xpath = "//input[@placeholder='From']")
	    WebElement fromInput;

	    @FindBy(xpath = "//li[@role='option']//span[text()='Kolkata, India']")
	    WebElement kolkataOption;

	    // To
	    @FindBy(id = "toCity")
	    WebElement toCity;

	    @FindBy(xpath = "//input[@placeholder='To']")
	    WebElement toInput;

	    @FindBy(xpath = "//li[@role='option']//span[text()='New Delhi, India']")
	    WebElement delhiOption;

	    // Calendar
	    @FindBy(xpath = "//span[@aria-label='Next Month']")
	    WebElement nextMonth;

	    @FindBy(xpath = "//div[@class='DayPicker-Caption']")
	    WebElement calendarHeader;

	    @FindBy(xpath = "//div[contains(@class,'DayPicker-Day') and not(contains(@class,'disabled'))]")
	    List<WebElement> enabledDates;


	    // Search
	    @FindBy(xpath = "//a[text()='Search']")
	    WebElement searchBtn;

	    // ---------------- METHODS ----------------

	    public void closePopupIfPresent() {
	        try {
	            wait.until(ExpectedConditions.elementToBeClickable(closePopup)).click();
	        } catch (Exception e) {}
	    }

	    public void selectRoundTrip() {
	        wait.until(ExpectedConditions.elementToBeClickable(roundTrip)).click();
	    }

	    public void selectFromCityKolkata() {
	        fromCity.click();
	        wait.until(ExpectedConditions.visibilityOf(fromInput)).sendKeys("Kolk");
	        wait.until(ExpectedConditions.elementToBeClickable(kolkataOption));
	        new Actions(driver).moveToElement(kolkataOption).click().perform();
	    }

	    public void selectToCityDelhi() {
	        toCity.click();
	        wait.until(ExpectedConditions.visibilityOf(toInput)).sendKeys("Delhi");
	        wait.until(ExpectedConditions.elementToBeClickable(delhiOption));
	        new Actions(driver).moveToElement(delhiOption).click().perform();
	    }
	    public void clickAnywhereActions() {
	        Actions actions = new Actions(driver);
	        actions.moveByOffset(10, 10).click().perform();}
	    
	        public void selectDates2026LowestPricePlus3() {
	   // Navigate till 2026
        while (!calendarHeader.getText().contains("2026")) {
            wait.until(ExpectedConditions.elementToBeClickable(nextMonth)).click();
            wait.until(ExpectedConditions.visibilityOf(calendarHeader));
        }

        int lowestPrice = Integer.MAX_VALUE;
        WebElement lowestPriceDate = null;

        // Re-fetch visible enabled dates
        List<WebElement> dates = wait.until(
                ExpectedConditions.visibilityOfAllElements(enabledDates)
        );

        for (WebElement date : dates) {
            try {
                // Price is inside child <p> or <span>
                WebElement priceElement = date.findElement(
                        By.xpath(".//p[contains(@class,'price') or contains(@class,'todayPrice')]")
                );

                String priceText = priceElement.getText()
                        .replace("₹", "")
                        .replace(",", "")
                        .trim();

                if (!priceText.isEmpty()) {
                    int price = Integer.parseInt(priceText);

                    if (price < lowestPrice) {
                        lowestPrice = price;
                        lowestPriceDate = date;
                    }
                }

            } catch (NoSuchElementException e) {
                // Date without price – ignore
            }
        }

        if (lowestPriceDate == null) {
            throw new RuntimeException("No priced date found in calendar for 2026");
        }

        // Select departure date
        lowestPriceDate.click();

        // Get fresh dates after selecting departure
        List<WebElement> updatedDates = wait.until(
                ExpectedConditions.visibilityOfAllElements(enabledDates)
        );

        int departIndex = updatedDates.indexOf(lowestPriceDate);

        // Select return date +3 days safely
        if (departIndex + 3 < updatedDates.size()) {
            updatedDates.get(departIndex + 3).click();
        } else {
            throw new RuntimeException("Return date not available");
        }
    }

    

        public void clickSearch() {

            // Close calendar / price overlay
            clickAnywhereActions();

            // Wait for Search button to be clickable
            wait.until(ExpectedConditions.visibilityOf(searchBtn));
            wait.until(ExpectedConditions.elementToBeClickable(searchBtn));

            // JS click to avoid interception
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchBtn);
        }



}
