import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class webApplicationTests {
    private static final String EXPECTED_TITLE = "localhost";
    private static final String EXPECTED_VALUE = "Received!";
    private static final int QA_PORT = 90;
    @Test
    public void testWebApplication() {
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
        assertEquals(value,EXPECTED_VALUE);
        driver.quit();
    }


    @Test
    public void testUI(){
        System.setProperty("webdriver.chrome.driver","chromedriver.exe");
        ChromeDriver driver;
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);
        String url = "http://localhost" + ":" + QA_PORT;
        String actualTitle = "";
        try {
            driver.get(url);
            actualTitle = driver.getTitle();
        }
        catch(Exception e) {
            System.out.println("TEST FAILED");
            driver.quit();
        }
        finally {
            assertEquals(EXPECTED_TITLE,actualTitle);
            driver.quit();
        }
    }
}
