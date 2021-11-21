import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class AutomationProject1 {
    public static void main(String[] args) throws InterruptedException {

        System.setProperty("webdriver.chrome.driver", "C:\\Users\\parvi\\Desktop\\Duotech\\Selenium\\Drivers\\chromedriver.exe");
        WebDriver wd = new ChromeDriver();

//        wd.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

//      1. Navigate to http://duotifyapp.us-east-2.elasticbeanstalk.com/register.php
        wd.navigate().to("http://duotifyapp.us-east-2.elasticbeanstalk.com/register.php");

//      2. Verify the the title is "Welcome to Duotify!"
        try {
            assertTrue(wd.getTitle().contains(("Welcome to Duotify!")));
        }catch (AssertionError ae){
            ae.printStackTrace();
        }

//      3. Click on Signup here
        wd.findElement(By.id("hideLogin")).click();
//      4. Fill out the form with the required info username
//      5. Click on Sign up
        String username = "phatamov";
        String firstName = "Parviz";
        String lastName = "Hatamov";
        String email = "parviz.hatamov@gmail.com";
        String password = "Pass1234";
        wd.findElement(By.id("username")).sendKeys(username, Keys.TAB, firstName, Keys.TAB, lastName, Keys.TAB,
        email, Keys.TAB, email, Keys.TAB, password, Keys.TAB, password + Keys.ENTER);
        Thread.sleep(2000);

//      6. Once logged in to the application, verify that the URL is: http://duotifyapp.us-east-2.elasticbeanstalk.com/browse.php?
        if(!wd.getCurrentUrl().equals("http://duotifyapp.us-east-2.elasticbeanstalk.com/browse.php?")){
            wd.findElement(By.id("hideRegister")).click();
            Thread.sleep(2000);
            wd.findElement(By.id("loginUsername")).sendKeys(username, Keys.TAB, password + Keys.ENTER);
            Thread.sleep(2000);
        }

//      7. In the left navigation bar, verify that your username (first+lastname)
//      is the combination of the same first and last name that you used when signing up.
        try {
            assertEquals(wd.findElement(By.id("nameFirstAndLast")).getText(), firstName +" "+ lastName);
        }catch (AssertionError ae){
            ae.printStackTrace();
        }

//      8. Click on the username on the left navigation bar and verify the username on the main window is correct and then click logout.
        wd.findElement(By.id("nameFirstAndLast")).click();
        Thread.sleep(2000);
        try {
            assertTrue(wd.findElement(By.tagName("h1")).getText().contains("Parviz Hatamov"));
        }catch (AssertionError ae){
            ae.printStackTrace();
        }
        wd.findElement(By.id("rafael")).click();
        Thread.sleep(2000);

//      9. Verify that you are logged out by verifying the URL is: http://duotifyapp.us-east-2.elasticbeanstalk.com/register.php
        try {
            assertEquals(wd.getCurrentUrl(), "http://duotifyapp.us-east-2.elasticbeanstalk.com/register.php");
        }catch (AssertionError ae){
            ae.printStackTrace();
        }

//      10. Login using the same username and password when you signed up.
        wd.findElement(By.id("loginUsername")).sendKeys(username, Keys.TAB, password + Keys.ENTER);
        Thread.sleep(2000);

//      11. Verify successful login by verifying that the home page contains the text "You Might Also Like".
        try {
            assertTrue(wd.getPageSource().contains("You Might Also Like"));
        }catch (AssertionError ae){
            ae.printStackTrace();
        }

//      12. Log out once again and verify that you are logged out.
        wd.findElement(By.id("nameFirstAndLast")).click();
        Thread.sleep(2000);
        wd.findElement(By.id("rafael")).click();
        Thread.sleep(2000);
        try {
            assertEquals(wd.getCurrentUrl(), "http://duotifyapp.us-east-2.elasticbeanstalk.com/register.php");
        }catch (AssertionError ae){
            ae.printStackTrace();
        }

        wd.quit();

    }
}