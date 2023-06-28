import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.zaproxy.clientapi.core.ClientApiException;

import java.io.IOException;
import java.time.Duration;

public class SeleniumZap {

    static final String proxy_address_zap = "localhost";
    static final int proxy_port_zap = 8080;
    WebDriver driver;
    static String appUrl = "your_app_url";

    @BeforeMethod
    public void startUp(){
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--ignore-certificate-errors");
        String proxyUrl = proxy_address_zap + ":" + proxy_port_zap;
        Proxy proxyServer = new Proxy();
        proxyServer.setHttpProxy(proxyUrl)
                .setSslProxy(proxyUrl);
        options.setCapability(CapabilityType.PROXY, proxyServer);
        System.setProperty("webdriver.chrome.driver","your_driver_path");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    public void loginToWebApp() {
        driver.get(appUrl);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        driver.findElement(By.id("username")).sendKeys("");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
        driver.findElement(By.id("password")).sendKeys("");
        driver.findElement(By.xpath("//input[@value='Sign In']")).click();
    }

    @Test
    public void testLoginSpiderSecurity() throws ClientApiException {
        loginToWebApp();
        Utility.spiderScan(appUrl, proxy_address_zap, proxy_port_zap);
    }

    @AfterMethod
    public void tearDown() throws ClientApiException, IOException {
        driver.close();
        Utility.getReports(appUrl);
    }
}
