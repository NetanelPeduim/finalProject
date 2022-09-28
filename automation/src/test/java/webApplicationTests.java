import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class webApplicationTests {
    @Test
    public void testSeleniumWebApplication() {
        System.setProperty("webdriver.chrome.driver","chromedriver.exe");
        ChromeDriver driver;
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);
        String url = "https://www.selenium.dev/selenium/web/web-form.html";
        driver.get(url);
        WebElement textBox = driver.findElement(By.name("my-text"));
        WebElement submitButton = driver.findElement(By.cssSelector("button"));
        textBox.sendKeys("Selenium");
        submitButton.click();
        WebElement msg = driver.findElement(By.id("message"));
        String value = msg.getText();
        assertEquals(value,"Received!");
        driver.quit();
    }
    @Test
    public void testWebpageName(){
        System.setProperty("webdriver.chrome.driver","chromedriver.exe");
        ChromeDriver driver;
        ChromeOptions options = new ChromeOptions();
        options.setPageLoadTimeout(Duration.ofMinutes(1));
        driver = new ChromeDriver(options);
        String url = "http://localhost" + ":" + 90;
        String actualTitle;
        driver.get(url);
        actualTitle = driver.getTitle();
        assertEquals("HIT - Devops course",actualTitle);
        driver.quit();
    }
}
