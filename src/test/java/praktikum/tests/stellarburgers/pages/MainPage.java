package praktikum.tests.stellarburgers.pages;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.ByteArrayInputStream;
import java.time.Duration;

public class MainPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public MainPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(8));
    }

    private final By loginMainBtn = By.xpath("//button[normalize-space(text())='Войти в аккаунт']");
    private final By profileBtn = By.xpath("//p[normalize-space(text())='Личный Кабинет']");

    private By tabDiv(String name) {
        return By.xpath("//div[contains(@class,'tab_tab')][.//span[normalize-space(text())='" + name + "']]");
    }

    private By sectionHeader(String name) {
        return By.xpath("//h2[normalize-space(text())='" + name + "']");
    }

    @Step("Клик по кнопке «Войти в аккаунт» на главной")
    public void clickLoginMain() {
        driver.findElement(loginMainBtn).click();
    }

    @Step("Клик по кнопке «Личный кабинет»")
    public void clickProfile() {
        driver.findElement(profileBtn).click();
    }

    @Step("Клик по вкладке «{name}»")
    public void clickTab(String name) {
        WebElement tab = driver.findElement(tabDiv(name));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center', inline:'nearest'});", tab);
        sleep(120);
        try {
            tab.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", tab);
        } catch (WebDriverException e) {
            try {
                new Actions(driver).moveToElement(tab).pause(Duration.ofMillis(80)).click().perform();
            } catch (Exception ignore) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", tab);
            }
        }
    }

    private void bringSectionToTop(String name) {
        WebElement h = driver.findElement(sectionHeader(name));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'start', inline:'nearest'});", h);
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -100);");
        sleep(120);
    }

    private ExpectedCondition<Boolean> tabHasCurrent(String name) {
        return drv -> {
            WebElement el = drv.findElement(tabDiv(name));
            String cls = el.getAttribute("class");
            return cls != null && cls.contains("tab_tab_type_current");
        };
    }

    public void waitTabIsCurrent(String name) {
        wait.until(tabHasCurrent(name));
    }

    public boolean isTabCurrent(String name) {
        String cls = driver.findElement(tabDiv(name)).getAttribute("class");
        return cls != null && cls.contains("tab_tab_type_current");
    }

    private void snap(String name) {
        byte[] png = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        Allure.addAttachment(name, "image/png", new ByteArrayInputStream(png), ".png");
    }

    private void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }

    @Step("Переходим во вкладку «Соусы»")
    public void goSauces() {
        clickTab("Соусы");
        bringSectionToTop("Соусы");
        waitTabIsCurrent("Соусы");
        snap("Tab active: Соусы");
    }

    @Step("Переходим во вкладку «Начинки»")
    public void goFillings() {
        clickTab("Начинки");
        bringSectionToTop("Начинки");
        waitTabIsCurrent("Начинки");
        snap("Tab active: Начинки");
    }

    @Step("Переходим во вкладку «Булки»")
    public void goBuns() {
        clickTab("Булки");
        bringSectionToTop("Булки");
        waitTabIsCurrent("Булки");
        snap("Tab active: Булки");
    }
}
