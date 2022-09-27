import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static io.restassured.RestAssured.when;

public class webApplicationTests {
    @Test
    public void testWebApplication() {
        //Your test
    }


    @Test
    public void testUI(){
        new ChromeDriver();
        ChromeDriver driver;
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);


        //Your test



        driver.quit();
    }
}
