package com.webinar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 * 
 * @author webinar
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class WebinarApplication
{
    public static void main(String[] args)
    {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(WebinarApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  Webinar-Boot启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                " __      __      ___.   .__                             __________               __   \n" +
                "/  \\    /  \\ ____\\_ |__ |__| ____ _____ _______         \\______   \\ ____   _____/  |_ \n" +
                "\\   \\/\\/   // __ \\| __ \\|  |/    \\\\__  \\\\_  __ \\  ______ |    |  _//  _ \\ /  _ \\   __\\\n" +
                " \\        /\\  ___/| \\_\\ \\  |   |  \\/ __ \\|  | \\/ /_____/ |    |   (  <_> |  <_> )  |  \n" +
                "  \\__/\\  /  \\___  >___  /__|___|  (____  /__|            |______  /\\____/ \\____/|__|  \n" +
                "       \\/       \\/    \\/        \\/     \\/                       \\/                    \n" );
    }
}
