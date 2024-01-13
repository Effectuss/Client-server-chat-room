package edu.effectuss.chatserver.cmdparser;

import org.apache.commons.cli.*;

public final class ServerParamsParser {
    private static final Options OPTIONS = new Options();

    public static CommandLine parse(String[] args) throws ParseException {
        if (OPTIONS.getOptions().isEmpty()) {
            buildOptions();
        }
        CommandLine cmd = new DefaultParser().parse(OPTIONS, args);
        validateCommandLine(cmd);
        return cmd;
    }

    public static void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar <jarfile>", OPTIONS);
    }

    private static void validateCommandLine(CommandLine cmd) throws ParseException {
        if (!cmd.getArgList().isEmpty()) {
            throw new ParseException("The application does not accept any arguments!");
        }
    }

    private static void buildOptions() {
        OPTIONS.addOption(createHelpOption())
                .addOption(createPortOption())
                .addOption(createFileOption())
                .addOption(createDefaultPortOption());
    }

    private static Option createPortOption() {
        return Option.builder(ServerOptions.PORT_OPTION.getOption())
                .longOpt(ServerOptions.PORT_OPTION_LONG.getOption())
                .argName("int number from 0 to 65535")
                .desc("Start server on specified port")
                .hasArg(true)
                .build();
    }

    private static Option createHelpOption() {
        return Option.builder(ServerOptions.HELP_OPTION.getOption())
                .longOpt(ServerOptions.HELP_OPTION_LONG.getOption())
                .desc("Display help information")
                .build();
    }

    private static Option createFileOption() {
        return Option.builder(ServerOptions.FILE_OPTION.getOption())
                .longOpt(ServerOptions.FILE_OPTION_LONG.getOption())
                .argName("*.properties file")
                .desc("Start server on specified port, from properties file")
                .hasArg(true)
                .build();
    }

    private static Option createDefaultPortOption() {
        return Option.builder(ServerOptions.DEFAULT_PORT_OPTION.getOption())
                .longOpt(ServerOptions.DEFAULT_PORT_OPTION_LONG.getOption())
                .desc("Start server on default port 6666")
                .build();
    }

    private ServerParamsParser() {

    }
}
