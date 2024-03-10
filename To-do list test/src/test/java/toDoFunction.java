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

public class toDoFunction{

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

    @Test (priority = 0)
    public void addValidTask() throws InterruptedException {
        String testText = "Task1";
        driver.findElement(By.xpath("//input[@id='input-box']")).sendKeys(testText);
        Thread.sleep(2000);
        driver.findElement(By.xpath("//button[contains(text(),'Add')]")).click();
        Thread.sleep(2000);
        String[] findTexts = driver.findElement(By.tagName("li")).getText().split("\n") ; // Split the string based on the newline character
        String findText = findTexts[0];   // Retrieve the first part
        Thread.sleep(2000);
        //System.out.println(testText);
        //System.out.println(findText);
        Assert.assertEquals(findText,testText);
    }

    @Test (priority = 1)
    public void markTask() throws InterruptedException {
        driver.findElement(By.tagName("li")).click();
        Thread.sleep(2000);
        String classValue = driver.findElement(By.tagName("li")).getAttribute("class");
        Assert.assertEquals(classValue,"checked");
    }

    @Test (priority = 2)
    public void unMarkTask() throws InterruptedException {
        driver.findElement(By.tagName("li")).click();
        Thread.sleep(2000);
        String classValue = driver.findElement(By.tagName("li")).getAttribute("class");
        //System.out.println(classValue);
        Assert.assertNotEquals(classValue,"checked");
    }

    @Test (priority = 3)
    public void deleteTask() throws InterruptedException {
        driver.findElement(By.tagName("span")).click();
        Thread.sleep(2000);
        Assert.assertEquals(0, driver.findElements(By.tagName("li")).size());
    }

    @Test (priority = 4)
    public void addEmptyTask() throws InterruptedException {
        driver.findElement(By.xpath("//button[contains(text(),'Add')]")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        System.out.println(driver.switchTo().alert().getText());
        if(wait.until(ExpectedConditions.alertIsPresent())==null) {
            Assert.fail();
        }
        Thread.sleep(2000);
        driver.switchTo().alert().accept(); //accept the alert
        Thread.sleep(2000);
    }

    @Test (priority = 5)
    public void addMaxCharacter() throws InterruptedException {
        //System.out.println("In addmaxtask");
        String testText = "";
        for (int i = 0; i<= 500; i++) {
            testText += "a";
        }
        driver.findElement(By.xpath("//input[@id='input-box']")).sendKeys(testText);
        driver.findElement(By.xpath("//button[contains(text(),'Add')]")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        if(wait.until(ExpectedConditions.alertIsPresent())==null) {
            Assert.fail();
        }
        Thread.sleep(2000);
        driver.switchTo().alert().accept();
        driver.findElement(By.tagName("span")).click();
    }


    @Test (priority = 6)
    public void addMaxtask() throws InterruptedException {
        //System.out.println("In addmaxtask");
        for (int i = 0; i<= 50; i++) {
            //System.out.println("In loop");
            driver.findElement(By.xpath("//input[@id='input-box']")).sendKeys("Task" + i);
            //System.out.println("pass");
            driver.findElement(By.xpath("//button[contains(text(),'Add')]")).click();
        }
       WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        if(wait.until(ExpectedConditions.alertIsPresent())==null) {
            Assert.fail();
        }
        Thread.sleep(2000);
        driver.switchTo().alert().accept();
    }
}
