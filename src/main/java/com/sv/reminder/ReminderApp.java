package com.sv.reminder;

import com.sv.core.Utils;
import com.sv.core.config.DefaultConfigs;
import com.sv.core.logger.MyLogger;
import com.sv.swingui.component.AppFrame;

import javax.swing.*;

/**
 * Java Utility to search big files.
 * Searched for a string files of size 1GB
 */
public class ReminderApp extends AppFrame {

    /**
     * This is config and program will search getter
     * of each enum to store in config file.
     * <p>
     * e.g. if enum is Xyz then when storing getXyz will be called
     */
    enum Configs {
        EventType
    }

    private MyLogger logger;
    private DefaultConfigs configs;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ReminderApp().initComponents());
    }

    public ReminderApp() {
        super("Reminder App");
    }

    /**
     * This method initializes the form.
     */
    private void initComponents() {
        logger = MyLogger.createLogger(getClass());
        configs = new DefaultConfigs(logger, Utils.getConfigsAsArr(Configs.class));

        setToCenter();
    }

}
