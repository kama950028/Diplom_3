package praktikum.tests.stellarburgers.infra;

import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.FrameworkMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Кастомный Suite, который запускает класс тестов последовательно
 * для каждого браузера из @Browsers({"chrome","yandex"}).
 *
 * Браузер прокидывается в тесты через System.setProperty("browser", <value>),
 * что читает твой BaseUiTest.setUp().
 *
 * В отчёте имена тестов будут с суффиксом: testName[chrome], testName[yandex].
 */
public class BrowserSuite extends Suite {

    public BrowserSuite(Class<?> klass) throws InitializationError {
        super(klass, buildRunners(klass));
    }

    private static List<Runner> buildRunners(Class<?> klass) throws InitializationError {
        Browsers browsers = klass.getAnnotation(Browsers.class);
        if (browsers == null || browsers.value().length == 0) {
            throw new InitializationError("Класс " + klass.getName() + " должен быть аннотирован @Browsers");
        }

        List<Runner> runners = new ArrayList<>();
        for (String browser : browsers.value()) {
            runners.add(new ParamRunner(klass, browser));
        }
        return runners;
    }

    /**
     * Индивидуальный раннер, который:
     * - перед запуском класса выставляет System.setProperty("browser", ...)
     * - добавляет суффикс [browser] к имени класса/теста
     */
    private static class ParamRunner extends BlockJUnit4ClassRunner {
        private final String browser;

        ParamRunner(Class<?> klass, String browser) throws InitializationError {
            super(klass);
            this.browser = browser;
        }

        @Override
        protected String getName() {
            return super.getName() + " [" + browser + "]";
        }

        @Override
        protected String testName(FrameworkMethod method) {
            return method.getName() + " [" + browser + "]";
        }

        @Override
        public void run(RunNotifier notifier) {
            // пробрасываем браузер в System properties для BaseUiTest.setUp()
            System.setProperty("browser", browser);
            // необязательно, но полезно для Allure Environment (если ты пишешь env в target/allure-results)
            System.setProperty("allure.browser", browser);
            super.run(notifier);
        }
    }
}
