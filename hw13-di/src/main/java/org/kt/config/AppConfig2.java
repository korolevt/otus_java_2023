package org.kt.config;

import org.kt.appcontainer.api.AppComponent;
import org.kt.appcontainer.api.AppComponentsContainerConfig;
import org.kt.services.*;

// Для проверки опционального варианта
//@AppComponentsContainerConfig(order = 1)
public class AppConfig2 {

    @AppComponent(order = 2, name = "gameProcessor")
    public GameProcessor gameProcessor(IOService ioService,
                                       PlayerService playerService,
                                       EquationPreparer equationPreparer) {
        return new GameProcessorImpl(ioService, equationPreparer, playerService);
    }


    @AppComponent(order = 1, name = "playerService")
    public PlayerService playerService(IOService ioService) {
        return new PlayerServiceImpl(ioService);
    }
}
