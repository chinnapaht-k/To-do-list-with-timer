import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Map;

public class loginFunction {
    WebDriver driver;
    @BeforeTest
    public void prepare() throws InterruptedException {
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption(
                "prefs",
                Map.of("profile.default_content_setting_values.notifications",1)
        );
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(options);
    }

    @AfterTest
    public void tearDown() {
        driver.quit();
    }

    @Test (priority = -2)
    public void openWebsite() throws InterruptedException {
        driver.navigate().to("https://chinnapaht.pythonanywhere.com/");
        Thread.sleep(2000);
    }

    @Test (priority = -1)
    public void openLoginPage() throws InterruptedException {
        driver.findElement(By.id("show-login")).click();
        Thread.sleep(2000);
    }


    @Test (priority = 0)
    public void invalidUsername(){
        driver.findElement(By.xpath("//input[@name='username']")).sendKeys("test007");
        driver.findElement(By.xpath("//input[@name='password']")).sendKeys("test!007");
        driver.findElement(By.tagName("button")).click();
        String error = driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[2]")).getText();
        Assert.assertEquals(error,"Your enter invalid user or password if you don't have account please register below");
    }

    @Test (priority = 0)
    public void invalidPassword(){
        driver.findElement(By.xpath("//input[@name='username']")).sendKeys("tester1");
        driver.findElement(By.xpath("//input[@name='password']")).sendKeys("a!123789");
        driver.findElement(By.tagName("button")).click();
        String error = driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[2]")).getText();
        Assert.assertEquals(error,"Your enter invalid user or password if you don't have account please register below");
    }


    @Test (priority = 3)
    public void validUsernamePassword(){
        driver.findElement(By.xpath("//input[@name='username']")).sendKeys("tester1");
        driver.findElement(By.xpath("//input[@name='password']")).sendKeys("a!123456");
        driver.findElement(By.tagName("button")).click();
        Assert.assertEquals(driver.getTitle() ,"25:00 - Time to work!");
    }


    @Test (priority = 2)
    public void sqlInjection() throws InterruptedException {
        driver.findElement(By.xpath("//input[@name='username']")).sendKeys("; DROP TABLE users; --");
        driver.findElement(By.xpath("//input[@name='password']")).sendKeys("pass!123");
        driver.findElement(By.tagName("button")).click();
        Thread.sleep(4000);
        String error = driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[2]")).getText();
        Assert.assertEquals(error ,"Your enter invalid user or password if you don't have account please register below");
    }


    @Test (priority =10)
    public void backToHomepage() throws InterruptedException {
        driver.findElement(By.xpath("//a[@href='/']")).click();
        Assert.assertEquals(driver.getTitle() ,"25:00 - Time to work!");
        Thread.sleep(2000);
    }

    @Test (priority =4)
    public void redirectSignup() throws InterruptedException {
        driver.findElement(By.id("show-login")).click();
        driver.findElement(By.xpath("//a[@href='/register']")).click();
        Assert.assertEquals(driver.getTitle() ,"Register");
        driver.findElement(By.xpath("//a[@href='/']")).click();
        driver.findElement(By.id("show-login")).click();
    }

}
