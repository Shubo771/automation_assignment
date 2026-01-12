package testCase;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import pageObject.MakeMyTripFlightPage;

public class MakeMyTripHomeTest1 {
	
	protected WebDriver driver;

    @BeforeClass
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://www.makemytrip.com/");
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
    @Test
    public void completeRoundTripSearch() throws InterruptedException {
        MakeMyTripFlightPage flight = new MakeMyTripFlightPage(driver);
        Thread.sleep(3000);
        //flight.closePopupIfPresent();
        flight.clickAnywhereActions();
        Thread.sleep(3000);
        flight.clickAnywhereActions();
        Thread.sleep(3000);
        flight.selectRoundTrip();
        Thread.sleep(3000);
        flight.selectFromCityKolkata();
        Thread.sleep(3000);
        flight.selectToCityDelhi();
        Thread.sleep(3000);
        flight.selectDates2026LowestPricePlus3();
        Thread.sleep(3000);
        flight.clickSearch();
        Thread.sleep(6000);
    }

}
