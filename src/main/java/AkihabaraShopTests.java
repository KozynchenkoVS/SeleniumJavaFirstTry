import net.bytebuddy.implementation.auxiliary.MethodCallProxy;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AkihabaraShopTests {

    WebDriver driver;

    @Before
    public void setUp () {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.setBinary("C:\\Program Files\\Google\\Chrome Beta\\Application\\chrome.exe");
        System.setProperty("webdriver.chrome.driver", "chromedriver_win32/chromedriver.exe");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        driver.get("https://akihabarashop.ru/");
    }

    @Test
    public void testFilter() {
        driver.findElement((By.xpath("//*[@id='menu']//a[.='Каталог товаров']"))).click();
        driver.findElement(By.xpath("//*[@title='Перейти к категории «Аниме фигурки»']")).click();
        driver.findElement(By.xpath("//span[contains(text(), 'Цена')]")).click();
        WebElement min = driver.findElement(By.xpath("//*[@id=\"goods-filter-min-price\"]"));
        min.clear();
        min.sendKeys("2000");
        WebElement max = driver.findElement(By.xpath("//*[@id=\"goods-filter-max-price\"]"));
        max.clear();
        max.sendKeys("2500");
        driver.findElement(By.cssSelector("div.goodsFilterPriceSubmit > button")).click();
        List<WebElement> animePrices = driver.findElements(By.xpath("//span[@class = 'num']"));
        ArrayList<Integer> animePricesInt = new ArrayList<>();
        for (WebElement price : animePrices) {
            animePricesInt.add(Integer.parseInt(price.getText().replaceAll("\\s", "")));
        }
        for (int price : animePricesInt) {
            Assert.assertFalse("Price is out of range", price < 2000 || price > 2500);
        }
    }


    @Test
    public void testTooltip() {
        String predicted = "Полный список товаров на сайте";
        String Tooltip = driver.findElement(By.xpath("//*[@id='footer']//a[.='Каталог товаров']"))
                .getAttribute("title");
        Assert.assertEquals("Tooltip's text is not equal to predicted",predicted, Tooltip);
    }


    @Test
    public void testRegistrationWithoutReqField() {
        Actions builder = new Actions(driver);
        builder.moveToElement( driver.findElement(By.xpath("//*[@class='user-menu']//*[@class='material-icons']")))
                .build().perform();
        WebElement element = driver.findElement(By.xpath("//*[@class='dropdown']//*[@title='Регистрация']"));
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
        driver.findElement(By.xpath("//*[@id=\"reg_name\"]")).sendKeys("Idon'tKnow");
        driver.findElement(By.xpath("//*[@id=\"sites_client_mail\"]")).sendKeys("lalka1999q@gmail.com");
        driver.findElement(By.xpath("//*[@id=\"sites_client_pass\"]")).sendKeys("123456As");
        driver.findElement(By.xpath("//button[@title='Зарегистрироваться']")).click();
        String expectedURL = "https://akihabarashop.ru/user/register";
        String actualURL = driver.getCurrentUrl();
        Assert.assertEquals("Expected URL is not equal to actual",expectedURL, actualURL);
    }

    @After
    public void quit() {
        driver.quit();
    }
}


