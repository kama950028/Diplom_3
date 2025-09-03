package praktikum.tests.stellarburgers.tests;

import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Story;
import org.junit.Test;
import org.junit.runner.RunWith;
import praktikum.tests.stellarburgers.BaseUiTest;
import praktikum.tests.stellarburgers.infra.BrowserSuite;
import praktikum.tests.stellarburgers.infra.Browsers;
import praktikum.tests.stellarburgers.pages.MainPage;

import static org.junit.Assert.assertTrue;

@RunWith(BrowserSuite.class)
@Browsers({"chrome", "yandex"})
@DisplayName("UI-тесты вкладок конструктора бургера")
public class ConstructorTabsTests extends BaseUiTest {

    @Test
    @DisplayName("Переключение вкладок: Соусы")
    @Story("Конструктор: вкладка «Соусы» становится активной")
    public void saucesTabBecomesActiveTest() {
        openMain();
        MainPage main = new MainPage(driver);
        main.goSauces();
        assertTrue("Вкладка «Соусы» не активна", main.isTabCurrent("Соусы"));
    }

    @Test
    @DisplayName("Переключение вкладок: Начинки")
    @Story("Конструктор: вкладка «Начинки» становится активной")
    public void fillingsTabBecomesActiveTest() {
        openMain();
        MainPage main = new MainPage(driver);
        main.goFillings();
        assertTrue("Вкладка «Начинки» не активна", main.isTabCurrent("Начинки"));
    }

    @Test
    @DisplayName("Переключение вкладок: Булки")
    @Story("Конструктор: вкладка «Булки» становится активной")
    public void bunsTabBecomesActiveTest() {
        openMain();
        MainPage main = new MainPage(driver);
        main.goBuns();
        assertTrue("Вкладка «Булки» не активна", main.isTabCurrent("Булки"));
    }
}
