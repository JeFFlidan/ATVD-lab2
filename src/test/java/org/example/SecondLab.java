package org.example;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.*;
import java.time.Duration;

public class SecondLab {
    private WebDriver chromeDriver;
    private static final String baseUrl = "https://www.nmu.org.ua/ua/";

    @BeforeClass(alwaysRun = true)
    public void setup() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--start-fullscreen");

        this.chromeDriver = new ChromeDriver(chromeOptions);
        this.chromeDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
    }

    @BeforeMethod
    public void preconditions() {
        chromeDriver.get(baseUrl);
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (chromeDriver != null) {
            chromeDriver.quit();
        }
    }

    @Test
    public void testHeaderExists() {
        WebElement header = chromeDriver.findElement(By.id("masthead"));
        Assert.assertNotNull(header);
    }

    @Test
    public void testClickOnLibrary() {
        WebElement libraryButton = chromeDriver.findElement(By.xpath("/html/body/div[1]/header/div[1]/div/div/div/div/div/div/div[2]/div/div/div/nav/div/ul/li[4]"));
        libraryButton.click();
        Assert.assertNotEquals(chromeDriver.getCurrentUrl(), baseUrl);
    }

    @Test
    public void testSearchFieldOnForStudentPage() {
        chromeDriver.get(baseUrl + "content/student_life/students/");

        WebElement searchField = chromeDriver.findElement(By.tagName("input"));
        Assert.assertNotNull(searchField);

        System.out.println(String.format("Name attribute: %s", searchField.getAttribute("name")));
        System.out.println(String.format("Type attribute: %s", searchField.getAttribute("type")));
        System.out.println(String.format("Position: %s", searchField.getLocation().toString()));
        System.out.println(String.format("Size: %s", searchField.getSize().toString()));

        String inputValue = "I need info";
        searchField.sendKeys(inputValue);
        searchField.sendKeys(Keys.ENTER);

        Assert.assertNotEquals(chromeDriver.getCurrentUrl(), baseUrl + "content/student_life/students/");
    }

    @Test
    public void testSlider() {
        WebElement nextButton = chromeDriver.findElement(By.className("swiper-button-next"));

        WebElement nextButtonByCss = chromeDriver.findElement(By.cssSelector("div.swiper-button-next"));

        Assert.assertEquals(nextButton, nextButtonByCss);

        WebElement previousButton = chromeDriver.findElement(By.className("swiper-button-prev"));

        for(int i = 0; i < 3; i++) {
            if(nextButton.getAttribute("class").contains("disabled")) {
                previousButton.click();
            } else {
                nextButton.click();
            }
        }
    }

    @Test
    public void testIndependentScenario() {
        chromeDriver.get("https://rozetka.com.ua/ua/");

        WebElement searchInput = chromeDriver.findElement(By.xpath("//input[contains(@placeholder, 'Я шукаю')]"));

        String query = "Ноутбук";
        searchInput.sendKeys(query);

        Assert.assertEquals(searchInput.getAttribute("value"), query, "Текст у полі не відповідає введеному!");

        searchInput.sendKeys(Keys.ENTER);

        org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(chromeDriver, Duration.ofSeconds(10));

        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.not(org.openqa.selenium.support.ui.ExpectedConditions.titleIs("Інтернет-магазин ROZETKA™: офіційний сайт")));

        boolean isSearchSuccessful = chromeDriver.getTitle().toLowerCase().contains(query.toLowerCase())
                || chromeDriver.getCurrentUrl().contains("search");

        Assert.assertTrue(isSearchSuccessful, "Сценарій пошуку не вдався або сторінка не завантажилась!");
    }
}
