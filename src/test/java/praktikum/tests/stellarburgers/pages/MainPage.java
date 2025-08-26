package praktikum.tests.stellarburgers.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


public class MainPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By loginButtonMain = By.xpath("//button[normalize-space(text())='Войти в аккаунт']");
    private final By profileLink     = By.xpath("//p[normalize-space(text())='Личный Кабинет']");

    // вкладки допускают разные контейнеры (button/a/div) с вложенным span с текстом
    private final By tabBunsBtn      = By.xpath("//*[self::button or self::a or self::div][.//span[normalize-space(text())='Булки']]");
    private final By tabSaucesBtn    = By.xpath("//*[self::button or self::a or self::div][.//span[normalize-space(text())='Соусы']]");
    private final By tabFillingsBtn  = By.xpath("//*[self::button or self::a or self::div][.//span[normalize-space(text())='Начинки']]");

    public MainPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15)); // было 10, увеличили
    }

    // заголовок секции — без привязки к тегу
    private By sectionHeader(String text) {
        return By.xpath("//*[self::h1 or self::h2 or self::h3 or self::div or self::span or self::p][normalize-space(text())='" + text + "']");
    }

    // активная вкладка: aria-selected/current на самом контейнере
    private By activeTabBy(String text) {
        return By.xpath(
                "//*[self::button or self::a or self::div][.//span[normalize-space(text())='" + text + "']]"
                        + "[@aria-selected='true' or contains(@class,'current')]"
        );
    }

    // ждём, что хотя бы одна секция появилась (контент подгрузился)
    private void ensureIngredientsLoaded() {
        wait.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(sectionHeader("Булки")),
                ExpectedConditions.presenceOfElementLocated(sectionHeader("Соусы")),
                ExpectedConditions.presenceOfElementLocated(sectionHeader("Начинки"))
        ));
    }

    private void jsScrollIntoView(WebElement el) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", el);
    }

    private void clickWithRetry(By by) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(by)).click();
        } catch (Exception e) {
            WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
            jsScrollIntoView(el);
            try { el.click(); } catch (Exception ex) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
            }
        }
    }

    // после клика доскроллимся именно к заголовку секции и ждём «успех»
    private void clickTabAndWait(By tabBtn, String text) {
        ensureIngredientsLoaded();

        // клик по вкладке
        clickWithRetry(tabBtn);

        // дождаться заголовок секции, доскроллить к нему (иногда активность меняется только после скролла)
        WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(sectionHeader(text)));
        jsScrollIntoView(header);

        // ждём одно из двух: вкладка активна ИЛИ заголовок в окне
        wait.until(d -> tabActive(text) || sectionInViewport(header));
    }

    @Step("Нажимаем 'Войти в аккаунт' на главной")
    public void clickLoginMain() {
        clickWithRetry(loginButtonMain);
    }

    @Step("Переходим в 'Личный кабинет'")
    public void clickProfile() {
        clickWithRetry(profileLink);
    }

    // --- вкладки
    @Step("Вкладка «Булки»")
    public void goBuns()     { clickTabAndWait(tabBunsBtn, "Булки"); }

    @Step("Вкладка «Соусы»")
    public void goSauces()   { clickTabAndWait(tabSaucesBtn, "Соусы"); }

    @Step("Вкладка «Начинки»")
    public void goFillings() { clickTabAndWait(tabFillingsBtn, "Начинки"); }

    public boolean tabActive(String text) {
        return !driver.findElements(activeTabBy(text)).isEmpty();
    }

    // перегрузка: при известном элементе заголовка
    private boolean sectionInViewport(WebElement h) {
        Number top = (Number)((JavascriptExecutor) driver)
                .executeScript("return Math.round(arguments[0].getBoundingClientRect().top);", h);
        Number innerH = (Number)((JavascriptExecutor) driver)
                .executeScript("return window.innerHeight;");
        long t = top.longValue();
        long hgt = innerH.longValue();
        return t >= 0 && t < hgt;
    }

    // универсальный: по тексту секции
    public boolean sectionInViewport(String text) {
        WebElement h = wait.until(ExpectedConditions.visibilityOfElementLocated(sectionHeader(text)));
        return sectionInViewport(h);
    }
}
