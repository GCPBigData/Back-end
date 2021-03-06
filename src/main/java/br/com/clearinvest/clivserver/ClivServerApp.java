package br.com.clearinvest.clivserver;

import br.com.clearinvest.clivserver.config.ApplicationProperties;
import br.com.clearinvest.clivserver.config.DefaultProfileUtil;
import br.com.clearinvest.clivserver.config.FileStorageProperties;
import io.allune.quickfixj.spring.boot.starter.EnableQuickFixJClient;
import io.github.jhipster.config.JHipsterConstants;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collection;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;

@SpringBootApplication
@EnableQuickFixJClient
@EnableConfigurationProperties({LiquibaseProperties.class, ApplicationProperties.class, FileStorageProperties.class})
public class ClivServerApp {

    private static final Logger log = LoggerFactory.getLogger(ClivServerApp.class);

    public static final String TIMEZONE_DEFAULT = "GMT-3:00";

    private final Environment env;

    public ClivServerApp(Environment env) {
        this.env = env;
    }

    /**
     * Initializes cliv_server.
     * <p>
     * Spring profiles can be configured with a program argument --spring.profiles.active=your-active-profile
     * <p>
     * You can find more information on how profiles work with JHipster on <a href="https://www.jhipster.tech/profiles/">https://www.jhipster.tech/profiles/</a>.
     */
    @PostConstruct
    public void initApplication() {
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_PRODUCTION)) {
            log.error("You have misconfigured your application! It should not run " +
                    "with both the 'dev' and 'prod' profiles at the same time.");
        }
        if (activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_CLOUD)) {
            log.error("You have misconfigured your application! It should not " +
                    "run with both the 'dev' and 'cloud' profiles at the same time.");
        }
    }

    @PostConstruct
    void started() {
        TimeZone.setDefault(getTimeZoneDefault());
    }

    public static TimeZone getTimeZoneDefault() {
        return TimeZone.getTimeZone(TIMEZONE_DEFAULT);
    }

    public static ZoneId getZoneIdDefault() {
        return TimeZone.getTimeZone(TIMEZONE_DEFAULT).toZoneId();
    }

    /**
     * Main method, used to run the application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ClivServerApp.class);
        DefaultProfileUtil.addDefaultProfile(app);
        Environment env = app.run(args).getEnvironment();
        logApplicationStartup(env);
    }

    private static void logApplicationStartup(Environment env) {
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        String serverPort = env.getProperty("server.port");
        String contextPath = env.getProperty("server.servlet.context-path");
        if (StringUtils.isBlank(contextPath)) {
            contextPath = "/";
        }
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("The host name could not be determined, using `localhost` as fallback");
        }
        log.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\t{}://localhost:{}{}\n\t" +
                        "External: \t{}://{}:{}{}\n\t" +
                        "Profile(s): \t{}\n----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                protocol,
                serverPort,
                contextPath,
                protocol,
                hostAddress,
                serverPort,
                contextPath,
                env.getActiveProfiles());
    }

    // we are using events instead of this; see ClientApplicationListener class for details
    /*@Bean
    public Application clientApplication() {
        return new ClientApplicationAdapter();
    }*/

    /*@Bean
    public Initiator clientInitiator(quickfix.Application clientApplication, MessageStoreFactory clientMessageStoreFactory,
        SessionSettings clientSessionSettings, LogFactory clientLogFactory,
        MessageFactory clientMessageFactory) throws ConfigError {

        return new ThreadedSocketInitiator(clientApplication, clientMessageStoreFactory, clientSessionSettings,
            clientLogFactory, clientMessageFactory);
    }*/
}
