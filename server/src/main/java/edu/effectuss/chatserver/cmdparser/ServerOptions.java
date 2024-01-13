package edu.effectuss.chatserver.cmdparser;

import lombok.Getter;

@Getter
public enum ServerOptions {
    HELP_OPTION("h"),
    HELP_OPTION_LONG("help"),
    PORT_OPTION("p"),
    PORT_OPTION_LONG("port"),
    DEFAULT_PORT_OPTION("dp"),
    DEFAULT_PORT_OPTION_LONG("default-port"),
    FILE_OPTION("f"),
    FILE_OPTION_LONG("file"),
    ;

    private final String option;

    ServerOptions(String option) {
        this.option = option;
    }
}
