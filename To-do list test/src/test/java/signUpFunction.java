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



public class signUpFunction {
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
    public void openSignUpPage() throws InterruptedException {
        driver.findElement(By.id("show-login")).click();
        driver.findElement(By.xpath("//a[@href='/register']")).click();
        Thread.sleep(2000);
    }

    @Test (priority =2)
    public void backToHomepage() throws InterruptedException {
        driver.findElement(By.xpath("//a[@href='/']")).click();
        Assert.assertEquals(driver.getTitle() ,"25:00 - Time to work!");
        Thread.sleep(2000);
        openSignUpPage();
        }

    @Test (priority = 0)
    public void validSignup_registeredUsername() throws InterruptedException {
        driver.findElement(By.xpath("//input[@name='username']")).sendKeys("Tester10");
        driver.findElement(By.xpath("//input[@name='password']")).sendKeys("a!123456");
        driver.findElement(By.xpath("//input[@name='re-password']")).sendKeys("a!123456");
        driver.findElement(By.tagName("button")).click();
        driver.findElement(By.id("show-login")).click();
        Thread.sleep(3000);
        driver.findElement(By.xpath("//a[@href='/register']")).click();
        Thread.sleep(3000);
        driver.findElement(By.xpath("//input[@name='username']")).sendKeys("Tester10");
        Thread.sleep(3000);
        driver.findElement(By.xpath("//input[@name='password']")).sendKeys("a!123456");
        driver.findElement(By.xpath("//input[@name='re-password']")).sendKeys("a!123456");
        driver.findElement(By.tagName("button")).click();
        String error = driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[2]")).getText().trim();
        Assert.assertEquals(error,"Your username is alerady regiestered, Please select enter other username");
        Thread.sleep(2000);
    }


    @Test (priority = 1)
        public void longPassword() throws InterruptedException {
        driver.findElement(By.xpath("//input[@name='username']")).sendKeys("Test1");
        driver.findElement(By.xpath("//input[@name='password']")).sendKeys("a!12345678910000");
        driver.findElement(By.xpath("//input[@name='re-password']")).sendKeys("a!12345678910000");
        driver.findElement(By.tagName("button")).click();
        String error = driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[2]")).getText();
        Thread.sleep(2000);
        Assert.assertEquals(error,"You should enter password less than 13 digits");
        Thread.sleep(2000);
        }


    @Test (priority = 1)
    public void longUsername() throws InterruptedException {
        driver.findElement(By.xpath("//input[@name='username']")).sendKeys("thistesterissohandsome");
        driver.findElement(By.xpath("//input[@name='password']")).sendKeys("a!123456");
        driver.findElement(By.xpath("//input[@name='re-password']")).sendKeys("a!123456");
        driver.findElement(By.tagName("button")).click();
        String error = driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[2]")).getText().trim();
        Assert.assertEquals(error,"You should enter username less than 10 character");
        Thread.sleep(2000);
    }


    @Test (priority = 1)
    public void shortPassword() throws InterruptedException {
        driver.findElement(By.xpath("//input[@name='username']")).sendKeys("Tester1");
        driver.findElement(By.xpath("//input[@name='password']")).sendKeys("a!123");
        driver.findElement(By.xpath("//input[@name='re-password']")).sendKeys("a!123");
        driver.findElement(By.tagName("button")).click();
        String error = driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[2]")).getText().trim();
        Assert.assertEquals(error,"You should enter password more than or equal to 6 digits");
        Thread.sleep(2000);
    }

    @Test (priority = 1)
    public void shortUsername() throws InterruptedException {
        driver.findElement(By.xpath("//input[@name='username']")).sendKeys("Tes");
        driver.findElement(By.xpath("//input[@name='password']")).sendKeys("a!123456");
        driver.findElement(By.xpath("//input[@name='re-password']")).sendKeys("a!123456");
        driver.findElement(By.tagName("button")).click();
        String error = driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[2]")).getText().trim();
        Assert.assertEquals(error,"You should enter username more than or equal to 3 character");
        Thread.sleep(2000);
    }

    @Test (priority = 1)
    public void misMatchPassword() throws InterruptedException {
        driver.findElement(By.xpath("//input[@name='username']")).sendKeys("Tester1");
        driver.findElement(By.xpath("//input[@name='password']")).sendKeys("a!123456");
        driver.findElement(By.xpath("//input[@name='re-password']")).sendKeys("b!123456");
        driver.findElement(By.tagName("button")).click();
        String error = driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[2]")).getText().trim();
        Assert.assertEquals(error,"Your password don't match");
        Thread.sleep(2000);
    }

    @Test (priority = 3)
    public void trimWhiteSpace() throws InterruptedException {

        driver.findElement(By.xpath("//input[@name='username']")).sendKeys(" trim9 ");
        driver.findElement(By.xpath("//input[@name='password']")).sendKeys(" trim!123 ");
        driver.findElement(By.xpath("//input[@name='re-password']")).sendKeys(" trim!123 ");
        driver.findElement(By.tagName("button")).click();
        Thread.sleep(3000);
        driver.findElement(By.id("show-login")).click();
        driver.findElement(By.xpath("//input[@name='username']")).sendKeys("trim9");
        driver.findElement(By.xpath("//input[@name='password']")).sendKeys("trim!123");
        driver.findElement(By.tagName("button")).click();
        Thread.sleep(3000);
        Assert.assertEquals(driver.getTitle() ,"25:00 - Time to work!");
    }

    @Test (priority = 1)
    public void withoutSpecial() throws InterruptedException {
        driver.findElement(By.xpath("//input[@name='username']")).sendKeys("Tester1");
        driver.findElement(By.xpath("//input[@name='password']")).sendKeys("a1234567");
        driver.findElement(By.xpath("//input[@name='re-password']")).sendKeys("a1234567");
        driver.findElement(By.tagName("button")).click();
        String error = driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[2]")).getText().trim();
        Assert.assertEquals(error,"Your password should contain at least one character, number and special characters");
        Thread.sleep(2000);
    }

    @Test (priority = 1)
    public void withoutCharacter() throws InterruptedException {
        driver.findElement(By.xpath("//input[@name='username']")).sendKeys("Tester1");
        driver.findElement(By.xpath("//input[@name='password']")).sendKeys("!123456");
        driver.findElement(By.xpath("//input[@name='re-password']")).sendKeys("!123456");
        driver.findElement(By.tagName("button")).click();
        String error = driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[2]")).getText().trim();
        Assert.assertEquals(error,"Your password should contain at least one character, number and special characters");
        Thread.sleep(2000);
    }

    @Test (priority = 1)
    public void withoutNumber() throws InterruptedException {
        driver.findElement(By.xpath("//input[@name='username']")).sendKeys("Tester1");
        driver.findElement(By.xpath("//input[@name='password']")).sendKeys("password!");
        driver.findElement(By.xpath("//input[@name='re-password']")).sendKeys("password!");
        driver.findElement(By.tagName("button")).click();
        String error = driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[2]")).getText().trim();
        Assert.assertEquals(error,"Your password should contain at least one character, number and special characters");
        Thread.sleep(2000);
    }

    }


