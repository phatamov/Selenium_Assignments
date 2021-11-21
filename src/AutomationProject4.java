import com.github.javafaker.Faker;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class AutomationProject4 {
    public static void main(String[] args) {

        System.setProperty("webdriver.chrome.driver", "C:\\Users\\parvi\\Desktop\\Duotech\\Selenium\\Drivers\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();

//      System.setProperty("webdriver.gecko.driver", System.getProperty("os.name").contains("Windows") ? "drivers/geckodriver.exe" : "drivers/geckodriver");
//      WebDriver driver = new FirefoxDriver();

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();

//      1. Navigate to carmax.com:
        driver.get("https://www.carmax.com/");
        driver.manage().deleteAllCookies();

//      2. On the bottom of the page in the appraisal form, choose VIN and fill out the form with
//      the below info and click get started:
//      VIN: 4T1BE46K67U162207
//      Zipcode:22182
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,1500)");
        driver.findElement(By.id("button-VIN")).click();
        driver.findElement(By.id("ico-form-vin")).sendKeys("4T1BE46K67U162207");
        driver.findElement(By.id("ico-form-zipcode")).sendKeys("22182");
        driver.findElement(By.xpath("//button[@class='submit-button kmx-button--primary kmx-button']")).click();

//      3. On the next page, choose the following info:
        new WebDriverWait(driver, 20).until(ExpectedConditions.presenceOfElementLocated
                (By.xpath("//input[@class='mdc-radio__native-control'][@value='mnHlNWA']"))).click();
        new WebDriverWait(driver, 20).until(ExpectedConditions.elementToBeClickable(By.id("select-ico-features-drive")));
        new Select(driver.findElement(By.id("select-ico-features-drive"))).selectByValue("4WD/AWD");

//      4. For features, check all options:
        List<WebElement> checkboxes = driver.findElements(By.xpath("//label[starts-with(@for, 'checkbox-ico-cb')]"));
        for (WebElement checkbox : checkboxes) {
            if (!checkbox.isSelected()) {
                js.executeScript("arguments[0].click();", checkbox);
            }
        }

//      5. Enter the following mileage and the choose the following options:
        js.executeScript("arguments[0].click();", driver.findElement(By.id("ico-step-Mileage_and_Condition-btn")));
        driver.findElement(By.xpath("//input[@name='currentMileage']")).sendKeys("60000");
        js.executeScript("arguments[0].click();", driver.findElement(By.id("radio-ico-r-100-1")));
        js.executeScript("arguments[0].click();", driver.findElement(By.id("radio-ico-r-910-1")));
        js.executeScript("arguments[0].click();", driver.findElement(By.id("radio-ico-r-920-1")));
        js.executeScript("arguments[0].click();", driver.findElement(By.id("radio-ico-r-200-1")));
        js.executeScript("arguments[0].click();", driver.findElement(By.id("radio-ico-r-1000-1")));
        js.executeScript("arguments[0].click();", driver.findElement(By.id("radio-ico-r-300-1")));
        js.executeScript("arguments[0].click();", driver.findElement(By.id("checkbox-ico-yn-310-1")));
        js.executeScript("arguments[0].click();", driver.findElement(By.id("radio-ico-r-410-1")));
        js.executeScript("arguments[0].click();", driver.findElement(By.id("radio-ico-r-420-1")));
        js.executeScript("arguments[0].click();", driver.findElement(By.id("radio-ico-r-500-1")));
        js.executeScript("arguments[0].click();", driver.findElement(By.id("radio-ico-r-600-1")));
        js.executeScript("arguments[0].click();", driver.findElement(By.id("radio-ico-r-700-1")));
        js.executeScript("arguments[0].click();", driver.findElement(By.id("radio-ico-r-800-1")));

//      6. Verify that Vehicle Information table contains the following expected data for the below 2 columns:
        List<String> expectedColumn1 = Arrays.asList("Year, Make, and Model", "Drive", "Transmission", "VIN", "Mileage");
        List<String> expectedColumn2 = Arrays.asList("2007 Toyota Camry", "4WD/AWD", "Automatic", "4T1BE46K67U162207", "60,000");
        List<String> actual1 = Utilities.getTableText(driver.findElements(By.xpath("//div[@id='icoVehicleBody']//table[@class='kmx-table']//tr/td[1]//p")));
        List<String> actual2 = Utilities.getTableText(driver.findElements(By.xpath("//div[@id='icoVehicleBody']//table[@class='kmx-table']//tr/td[2]//p")));
        Assert.assertEquals(actual1, expectedColumn1);
        Assert.assertEquals(actual2, expectedColumn2);

//      7.Click continue
        js.executeScript("arguments[0].click();", driver.findElement(By.id("ico-continue-button")));

//      8. On the next page, verify that the appraisal amount is 6600.
        String actualOffer = driver.findElement(By.xpath("//div[@class='kmx-ico-offer-offerinfo Offer-module__offerInfo--26dFt']")).getText();
        Assert.assertTrue(actualOffer.contains("7,000"));

//      9. Click continue
        //Thread.sleep(5000);
        driver.findElement(By.xpath("//button[.='Continue']")).click();

//      10. On the next page which opens in new window, write a code that chooses one of the locations randomly:
        String winHandleBefore = driver.getWindowHandle();
        Set<String> windowHandles = driver.getWindowHandles();
        for (String windowHandle : windowHandles) {
            driver.switchTo().window(windowHandle);
            if(driver.getTitle().equals("Appraisal Appointment | CarMax")){
                break;
            }
        }
        List<WebElement> locations = driver.findElements(By.xpath("//div[@class='mdc-select kmx-select']//option"));
        WebElement sortClick = driver.findElement(By.xpath("//select[@class='mdc-select__native-control']"));
        Select selectOption = new Select(sortClick);
        selectOption.selectByIndex((int) (Math.random() * locations.size()));

//      11. Choose the first available date:
        driver.findElement(By.xpath("//input[@id='react-datepicker']")).click();
        WebElement firstAvailableDate = driver.findElement(By.xpath("//div[starts-with(@aria-label, 'Choose')]"));
        firstAvailableDate.click();

//      12. Choose the first available time:
        driver.findElement(By.xpath("//input[@id='react-timepicker']")).click();
        WebElement firstAvailableTime = driver.findElement(By.xpath("//li[@class='react-datepicker__time-list-item ']"));
        firstAvailableTime.click();

//      13. Click next
        driver.findElement(By.xpath("//button[.='next']")).click();

//      14. On the next page, fill out the form with random info. You can use Faker library
        // or external data file from Mockaroo. DO NOT click on next afterwards since clicking
        // it will create an actual appraisal appointment and will occupy the actual time slot.
        Faker fakeData = new Faker();
        driver.findElement(By.xpath("//input[@id='fname']")).sendKeys(fakeData.name().firstName());
        driver.findElement(By.xpath("//input[@id='lname']")).sendKeys(fakeData.name().lastName());
        driver.findElement(By.xpath("//input[@id='email']")).sendKeys(fakeData.internet().emailAddress());
        driver.findElement(By.xpath("//input[@id='phone']")).sendKeys("2361231234");

//      15. Click on Privacy policy link which opens the new tab and verify that the title is "Privacy Policy | CarMax"
        driver.findElement(By.xpath("//a[.='Privacy Policy']")).click();
        for (String windowHandle : windowHandles) {
            driver.switchTo().window(windowHandle);
            if (driver.getTitle().equals("Privacy Policy | CarMax")) {
                break;
            }
        }

//      16. Go back to previous window with the offer amount and click on Save this offer
        driver.close();
        driver.switchTo().window(winHandleBefore);
        driver.findElement(By.xpath("//button[.='Save this offer']")).click();

//      17. On the pop-up window add random email and click send my offer
        driver.findElement(By.xpath("//label[.='Preferred email']")).sendKeys(fakeData.internet().emailAddress());
        driver.findElement(By.xpath("//button[@id='ico-send-offer-email']")).click();

//      18. End the session by closing down all the windows
        driver.quit();
    }
}