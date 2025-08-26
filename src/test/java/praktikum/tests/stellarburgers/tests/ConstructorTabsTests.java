package praktikum.tests.stellarburgers.tests;

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
public class ConstructorTabsTests extends BaseUiTest {

    @Test
    @Story("Конструктор: вкладки «Булки», «Соусы», «Начинки» переключаются корректно")
    public void tabsSwitchingTest() {
        openMain();
        MainPage main = new MainPage(driver);

        main.goSauces();
        assertTrue("После клика по «Соусы» вкладка не активна и секция не видна",
                main.tabActive("Соусы") || main.sectionInViewport("Соусы"));

        main.goFillings();
        assertTrue("После клика по «Начинки» вкладка не активна и секция не видна",
                main.tabActive("Начинки") || main.sectionInViewport("Начинки"));

        main.goBuns();
        assertTrue("После клика по «Булки» вкладка не активна и секция не видна",
                main.tabActive("Булки") || main.sectionInViewport("Булки"));
    }
}
