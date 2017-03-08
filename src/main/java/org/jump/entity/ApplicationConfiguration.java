package org.jump.entity;

import lombok.Getter;
import lombok.Setter;

public class ApplicationConfiguration {

    @Getter
    @Setter
    private String fileName;

    @Getter
    @Setter
    private String database;

    @Getter
    @Setter
    private String host;

    @Getter
    @Setter
    private Integer port;

    @Getter
    @Setter
    private String user;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private boolean logSql = false;

    @Getter
    @Setter
    private boolean verbose = false;

    @Getter
    @Setter
    private boolean dryRun = false;

    @Getter
    @Setter
    private boolean success = false;
}
