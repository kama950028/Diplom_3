package praktikum.tests.stellarburgers.infra;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Browsers {
    String[] value();
}

