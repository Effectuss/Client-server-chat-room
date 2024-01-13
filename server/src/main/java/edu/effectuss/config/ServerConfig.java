package edu.effectuss.config;

import edu.effectuss.config.exception.ServerConfigException;
import lombok.Getter;
import org.apache.commons.cli.CommandLine;
import edu.effectuss.chatserver.cmdparser.ServerOptions;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Getter
public class ServerConfig {
    private static final int MAX_PORT_VALUE = 65535;
    private static final int MIN_PORT_VALUE = 1;
    private static final String DEFAULT_CONFIG_FILE = "server.properties";

    private static final String PROPERTIES_KEY_FOR_PORT = "port";

    private static final String ERROR_PORT_VALUE = """
            Invalid port value: <%s>.
            The port value must be integer number from 0 to 65535!
            """;

    private static final String ERROR_KEY_PORT = """
            Failed to load port from *.properties file!
            The key for port must be <%s>!
            """;

    private static final String ERROR_LOAD_PROPERTIES_FILE = "Failed to load %s file!";

    private final Properties properties = new Properties();

    private int port;

    public ServerConfig(CommandLine commandLine) {
        if (commandLine.hasOption(ServerOptions.PORT_OPTION.getOption())) {
            port = parsePortFromString(commandLine.getOptionValue(
                    ServerOptions.PORT_OPTION.getOption()
            ));
        } else if (commandLine.hasOption(ServerOptions.FILE_OPTION.getOption())) {
            configureFromFile(commandLine.getOptionValue(
                    ServerOptions.FILE_OPTION.getOption()
            ));
        } else {
            configureDefault();
        }
    }

    private int parsePortFromString(String stringPort) {
        try {
            if (!isValidPortValue(Integer.parseInt(stringPort))) {
                throw new ServerConfigException(String.format(ERROR_PORT_VALUE, stringPort));
            }
            return Integer.parseInt(stringPort);
        } catch (NumberFormatException e) {
            throw new ServerConfigException(ERROR_PORT_VALUE, e);
        }
    }

    private void configureFromFile(String filePath) {
        try (InputStream is = new FileInputStream(filePath)) {
            loadProperties(is);
        } catch (IOException e) {
            throw new ServerConfigException(
                    String.format(ERROR_LOAD_PROPERTIES_FILE, filePath), e
            );
        }
    }

    private void configureDefault() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(DEFAULT_CONFIG_FILE)) {
            loadProperties(is);
        } catch (IOException e) {
            throw new ServerConfigException(
                    String.format(ERROR_LOAD_PROPERTIES_FILE, DEFAULT_CONFIG_FILE), e
            );
        }
    }

    private void loadProperties(InputStream is) throws IOException {
        if (is != null) {
            properties.load(is);
            if (!isValidKeyProperty()) {
                throw new ServerConfigException(
                        String.format(ERROR_KEY_PORT, PROPERTIES_KEY_FOR_PORT)
                );
            }
            port = parsePortFromString(properties.getProperty(PROPERTIES_KEY_FOR_PORT));
        } else {
            throw new ServerConfigException("InputStream is null");
        }
    }

    private boolean isValidKeyProperty() {
        return properties.containsKey(PROPERTIES_KEY_FOR_PORT);
    }

    private boolean isValidPortValue(int port) {
        return port >= MIN_PORT_VALUE && port <= MAX_PORT_VALUE;
    }
}
