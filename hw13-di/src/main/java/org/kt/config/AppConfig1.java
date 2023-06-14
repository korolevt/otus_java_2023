package org.kt.config;

import org.kt.appcontainer.api.AppComponent;
import org.kt.appcontainer.api.AppComponentsContainerConfig;
import org.kt.services.*;

// Для проверки опционального варианта
//@AppComponentsContainerConfig(order = 0)
public class AppConfig1 {

    @AppComponent(order = 0, name = "equationPreparer")
    public EquationPreparer equationPreparer(){
        return new EquationPreparerImpl();
    }

    @AppComponent(order = 0, name = "ioService")
    public IOService ioService() {
        return new IOServiceStreams(System.out, System.in);
    }
}
