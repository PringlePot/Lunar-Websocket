package me.moose.websocket.server.server.logger;

import codes.rbuh.gLogger.logger.AbstractLogger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class WebLogger extends AbstractLogger {
    private final File dir;
    private final File logFile;

    public WebLogger(String file) {
        super("[WS] (%type% | %size%): %msg%");
        this.dir = new File(file + File.separator + "logs");
        this.logFile = new File(this.dir + File.separator + new Date().toInstant().toString());

        System.out.println("logFileName=" + this.logFile.getName());
    }

    @Override
    public void info(String msg) {
        super.info(msg);
        System.out.println(formatLog(msg, "INFO"));
    }

    @Override
    public void error(String msg) {
        super.error(msg);
        System.out.println(formatLog(msg, "ERROR"));
    }

    @Override
    public void success(String msg) {
        super.success(msg);
        System.out.println(formatLog(msg, "SUCCESS"));
    }

    public String formatLog(String msg, String type) {
        return this.getTextFormat().replace("%size%", "" + this.getLogs().size()).replace("%type%", type).replace("%msg%", msg);
    }

    public void dump() {
        try {
            if (this.dir.exists() || this.dir.mkdirs()) {
                if (this.logFile.exists() || this.logFile.createNewFile()) {
                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(this.logFile));
                    for (String line : this.getLogs()) {
                        bufferedWriter.write(line);
                        bufferedWriter.newLine();
                    }
                    bufferedWriter.close();
                }
            }
        } catch (IOException ignored) {}
    }
}
