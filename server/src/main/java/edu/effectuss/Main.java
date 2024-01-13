package edu.effectuss;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import edu.effectuss.chatserver.Server;
import edu.effectuss.chatserver.cmdparser.ServerOptions;
import edu.effectuss.config.ServerConfig;
import edu.effectuss.chatserver.cmdparser.ServerParamsParser;
import edu.effectuss.config.exception.ServerConfigException;


@Log4j2
public class Main {
    public static void main(String[] args) {
        try {
            CommandLine commandLine = ServerParamsParser.parse(args);
            if (commandLine.hasOption(ServerOptions.HELP_OPTION.getOption())) {
                ServerParamsParser.printHelp();
                return;
            }
            ServerConfig serverConfig = new ServerConfig(commandLine);
            Server server = new Server(serverConfig);
            Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown));
            server.startServer();
        } catch (ServerConfigException | ParseException e) {
            log.error(e);
            ServerParamsParser.printHelp();
        }
    }
}