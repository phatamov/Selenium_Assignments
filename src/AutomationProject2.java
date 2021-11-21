import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import static org.testng.Assert.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class AutomationProject2 {

    public static void main(String[] args) throws IOException {

//      1. Launch Chrome browser.
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\parvi\\Desktop\\Duotech\\Selenium\\Drivers\\chromedriver.exe");
        WebDriver wd = new ChromeDriver();
        wd.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

//      2. Navigate to http://secure.smartbearsoftware.com/samples/TestComplete12/WebOrders/Login.aspx
        wd.navigate().to("http://secure.smartbearsoftware.com/samples/TestComplete12/WebOrders/Login.aspx");

//      3. Login using username Tester and password test
        wd.findElement(By.id("ctl00_MainContent_username")).sendKeys("Tester", Keys.TAB, "test", Keys.ENTER);

//      4. Click on Order link
        wd.findElement(By.linkText("Order")).click();

//      5. Enter a random product quantity between 1 and 100
        String product = wd.findElement(By.name("ctl00$MainContent$fmwOrder$ddlProduct")).getAttribute("value");
        int randomQuantity = (int) (Math.random() * 99) + 1;
        wd.findElement(By.id("ctl00_MainContent_fmwOrder_txtQuantity"))
        .sendKeys(Keys.BACK_SPACE, String.valueOf(randomQuantity), Keys.ENTER);

/*      6. Click on Calculate and verify that the Total value is correct.
        Price per unit is 100.  The discount of 8 % is applied to quantities of 10+.
        So for example, if the quantity is 8, the Total should be 800.
        If the quantity is 20, the Total should be 1840.
        If the quantity is 77, the Total should be 7084. And so on.
 */     String value = wd.findElement(By.id("ctl00_MainContent_fmwOrder_txtTotal")).getAttribute("value");
        double actualTotalValue = Double.parseDouble(value);
        double expectedTotalValue = 0;
        if (randomQuantity > 10) {
            expectedTotalValue = randomQuantity * 100 - (randomQuantity * 100 * 0.08);
        } else if (randomQuantity < 10 && randomQuantity > 0) {
            expectedTotalValue = randomQuantity * 100;
        } else {
            expectedTotalValue = 0;
        }
        assertTrue(actualTotalValue == expectedTotalValue);

//      6.1 Generate and enter random first name and last name.
//      7. Generate and Enter random street address
//      8. Generate and Enter random city
//      9. Generate and Enter random state
//      10. Generate and Enter a random 5 digit zip code
//      EXTRA: As an extra challenge, for steps 6-10 download 1000 row of corresponding realistic data from mockaroo.com
//      in a csv format and load it to your program and use the random set of data from there each time.
        List<String[]> list = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader("MOCK_DATA.csv"));
        String line;
        while((line = bufferedReader.readLine()) != null ){
            String[] split = line.split(",");
            list.add(split);
        }
        String[] randomMockData = null;
        for (int i = 1; i < list.size(); i++){
            int randomIndex = (int)(Math.random()* list.size()-1)+1;
            randomMockData = list.get(randomIndex);
        }
        wd.findElement(By.id("ctl00_MainContent_fmwOrder_txtName")).sendKeys(randomMockData[0]+" "+randomMockData[1],
        Keys.TAB, randomMockData[2], Keys.TAB, randomMockData[3], Keys.TAB, randomMockData[4], Keys.TAB, randomMockData[5]);

//      11. Select the card type randomly. On each run your script should select a random type.
        String[] cardTypeId = {"ctl00_MainContent_fmwOrder_cardList_0",
                               "ctl00_MainContent_fmwOrder_cardList_1",
                               "ctl00_MainContent_fmwOrder_cardList_2"};
        String randomCardTypeId = null;
        for (int i = 0; i < cardTypeId.length; i++){
            int randomIndex = (int)(Math.random()* cardTypeId.length);
            randomCardTypeId = cardTypeId[randomIndex];
        }
        wd.findElement(By.id(randomCardTypeId)).click();

//      12. Generate and enter the random card number:
//        If Visa is selected, the card number should start with 4.
//        If MasterCard is selected, card number should start with 5.
//        If American Express is selected, card number should start with 3.
//        Card numbers should be 16 digits for Visa and MasterCard, 15 for American Express.
        String randomCardNo = "";
        Random randomNum = new Random();
        for (int i = 0; i < 15; i++) {
            randomCardNo += randomNum.nextInt(10);
        }
        String inputtedCardNumber;
        if (randomCardTypeId.equals("ctl00_MainContent_fmwOrder_cardList_0")){
            inputtedCardNumber = "4" + randomCardNo;
            wd.findElement(By.id("ctl00_MainContent_fmwOrder_TextBox6")).sendKeys(inputtedCardNumber);
        }else if(randomCardTypeId.equals("ctl00_MainContent_fmwOrder_cardList_1")) {
            inputtedCardNumber = "5" + randomCardNo;
            wd.findElement(By.id("ctl00_MainContent_fmwOrder_TextBox6")).sendKeys(inputtedCardNumber);
        }else {
            inputtedCardNumber = "3" + randomCardNo.substring(1);
            wd.findElement(By.id("ctl00_MainContent_fmwOrder_TextBox6")).sendKeys(inputtedCardNumber);
        }
        String cardType = wd.findElement(By.id(randomCardTypeId)).getAttribute("value");

//      13. Enter a valid expiration date (newer than the current date)
        String expireDate = "12/22";
        wd.findElement(By.id("ctl00_MainContent_fmwOrder_TextBox1")).sendKeys(expireDate);

//      14. Click on Process
        wd.findElement(By.id("ctl00_MainContent_fmwOrder_InsertButton")).click();

//      15. Verify that “New order has been successfully added” message appeared on the page.
        assertTrue(wd.getPageSource().contains("New order has been successfully added."));

//      16. Click on View All Orders link.
        wd.findElement(By.linkText("View all orders")).click();

//      17. The placed order details appears on the first row of the orders table. Verify that the entire information
//      contained on the row (Name, Product, Quantity, etc) matches the previously entered information in previous steps.
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        String todaysDate= formatter.format(date);

        String actualFirstRowOfTr = wd.findElements(By.tagName("tr")).get(2).getText();
        String expectedFirstRowOfTr = randomMockData[0]+" "+randomMockData[1]+" "+product+" "+randomQuantity+" "
                                      +todaysDate+" "+randomMockData[2]+" "+randomMockData[3]+" "+randomMockData[4]+" "
                                      +randomMockData[5]+" "+cardType+" "+inputtedCardNumber+" "+expireDate;
        assertEquals(actualFirstRowOfTr, expectedFirstRowOfTr);

//      18. Log out of the application.
        wd.findElement(By.id("ctl00_logout")).click();
        wd.quit();

    }
}
