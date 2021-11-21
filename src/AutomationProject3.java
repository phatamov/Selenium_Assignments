import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class AutomationProject3 extends Utilities {
    public static void main(String[] args) throws InterruptedException {

        System.setProperty("webdriver.chrome.driver", "C:\\Users\\parvi\\Desktop\\Duotech\\Selenium\\Drivers\\chromedriver.exe");
        WebDriver wd = new ChromeDriver();
        wd.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        wd.manage().window().maximize();
        wd.manage().deleteAllCookies();

//      1.Navigate  to carfax.com
        wd.get("https://www.carfax.com/");

//      2.Click on Find a Used Car
        wd.findElement(By.xpath("//div[@id='hero-buttons-container-default']//a[@href='/cars-for-sale'] ")).click();

//      3.Verify the page title contains the word “Used Cars”
        wd.getTitle().contains("Used Cars");

//      4.Choose “Tesla” for  Make.
        new Select(wd.findElement(By.xpath("//select[@name='make']"))).selectByIndex(31);

//      5.Verify that the Select Model dropdown box contains 4 current Tesla models (Model 3, Model S, Model X, Model Y).
        List<String> actualModels = new ArrayList<>();
        List<String> expectedModels = Arrays.asList("Model 3", "Model S", "Model X", "Model Y");
        List<WebElement> elements = new Select(wd.findElement(By.xpath("//select[@name='model']"))).getOptions();
        for (int i = 1; i < elements.size() - 1; i++) {
            actualModels.add(elements.get(i).getText().trim());
        }
        Assert.assertEquals(actualModels, expectedModels);

//      6.Choose “Model S” for Model.
        new Select(wd.findElement(By.xpath("//select[@name='model']"))).selectByValue("Model S");

//      7.Enter the zip code 22182 and click Next
        wd.findElement(By.xpath("//input[@placeholder='Zip Code']")).sendKeys("22182");
        wd.findElement(By.id("make-model-form-submit")).submit();

//      8.Verify that the page contains the text “Step 2 - Show me cars with”
        Assert.assertTrue(wd.getPageSource().contains("Step 2 - Show me cars with"));

//      9.Check all 4 checkboxes.
        List<WebElement> checkboxes = wd.findElements(By.xpath("//span[@class='checkbox-list-item--fancyCbx']"));
        for (WebElement checkbox : checkboxes) {
            if (!checkbox.isSelected()) {
                checkbox.click();
            }
        }

//      10.Save the count of results from “Show me X Results” button. In this case it is 10.
        int countOfResults = Integer.parseInt(wd.findElement(By.cssSelector("span[class='totalRecordsText']")).getText());

//      11.Click on “Show me x Results” button.
        wd.findElement(By.xpath("//button[@class='button large primary-green show-me-search-submit']")).click();

//      12.Verify the results count by getting the actual number of results displayed in the page
//      by getting the count of WebElements that represent each result
        String results = wd.findElement(By.id("totalResultCount")).getText();
        int actualCountOfResults = Integer.parseInt(results.replaceAll("[^0-9]", ""));
        int expectedCountOfResults = wd.findElements(By.xpath("//article[@class='srp-list-item']")).size();
        Assert.assertEquals(actualCountOfResults, expectedCountOfResults);

//      13.Verify that each result header contains “Tesla Model S”.
        List<WebElement> nameOfElements = wd.findElements(By.xpath("//h4[@class='srp-list-item-basic-info-model']"));
        for (WebElement element : nameOfElements) {
            Assert.assertTrue(element.getText().contains("Tesla Model S"));
        }

//      14.Get the price of each result and save them into a List in the order of their appearance.
//      (You can exclude “Call for price” options)
        List<String> actualPrices = getElementsText(wd.findElements(By.xpath("//div//span[@class='srp-list-item-price']")));

//      15.Choose “Price - High to Low” option from the Sort By menu
        new Select(wd.findElement(By.xpath("//select[@class='srp-header-sort-select srp-header-sort-select-desktop--srp']"))).selectByValue("PRICE_DESC");
//        Thread.sleep(2000);
        wd.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);

//      16.Verify that the results are displayed from high to low price.
        List<String> pricesHighToLow = getElementsText(wd.findElements(By.xpath("//div//span[@class='srp-list-item-price']")));
        actualPrices = sortData(actualPrices);
        Collections.sort(actualPrices, Collections.reverseOrder());
        Assert.assertEquals(pricesHighToLow, actualPrices);

//      17.Choose “Mileage - Low to High” option from Sort By menu
        List<String> actualMileages = getElementsText(wd.findElements(By.xpath("//span[@class='srp-list-item-basic-info-value']")));
        actualMileages = sortData(actualMileages);
        new Select(wd.findElement(By.xpath("//select[@class='srp-header-sort-select srp-header-sort-select-desktop--srp']"))).selectByValue("MILEAGE_ASC");
        Thread.sleep(2000);

//      18.Verify that the results are displayed from low to high mileage.
        List<String> mileagesLowToHigh = getElementsText(wd.findElements(By.xpath("//span[@class='srp-list-item-basic-info-value']")));
        Assert.assertEquals(mileagesLowToHigh, actualMileages);

//      19.Choose “Year - New to Old” option from Sort By menu
        List<WebElement> titles = wd.findElements(By.xpath("//h4[@class='srp-list-item-basic-info-model']"));
        List<String> actualYears = new LinkedList<>();
        for (WebElement element : titles) {
            actualYears.add(element.getText().substring(0, 4));
        }
        actualYears = sortData(actualYears);
        Collections.sort(actualYears, Collections.reverseOrder());
        new Select(wd.findElement(By.xpath("//select[@class='srp-header-sort-select srp-header-sort-select-desktop--srp']"))).selectByValue("YEAR_DESC");
        Thread.sleep(2000);

//      20.Verify that the results are displayed from new to old year.
        titles = wd.findElements(By.xpath("//h4[@class='srp-list-item-basic-info-model']"));
        List<String> sortedYears = new LinkedList<>();
        for (WebElement element : titles) {
            sortedYears.add(element.getText().substring(0, 4));
        }
        Assert.assertEquals(sortedYears, actualYears);

        wd.close();

    }
}
