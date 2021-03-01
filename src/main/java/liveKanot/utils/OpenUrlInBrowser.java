package liveKanot.utils;

import java.io.IOException;

public class OpenUrlInBrowser {
    private static final String OS = System.getProperty("os.name").toLowerCase();

    public static void openURL(String domain) {
        String url = domain;
        Runtime rt = Runtime.getRuntime();
        try {
            if (OpenUrlInBrowser.isWindows()) {
                rt.exec("rundll32 url.dll,FileProtocolHandler " + url).waitFor();
                log("Browser: " + url);
            } else if (OpenUrlInBrowser.isMac()) {
                String[] cmd = {"open", url};
                rt.exec(cmd).waitFor();
                log("Browser: " + url);
            } else if (OpenUrlInBrowser.isUnix()) {
                String[] cmd = {"xdg-open", url};
                rt.exec(cmd).waitFor();
                log("Browser: " + url);
            } else {
                try {
                    throw new IllegalStateException();
                } catch (IllegalStateException e1) {
//                    MUtils.alertMessage(Lang.get("desktop.not.supported"), MainPn.getMainPn());
                    e1.printStackTrace();
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void log(String logMessage) {
        System.out.println(logMessage);
    }

    public static boolean isWindows() {
        return OS.contains("win");
    }

    public static boolean isMac() {
        return OS.contains("mac");
    }

    public static boolean isUnix() {
        return OS.contains("nix") || OS.contains("nux") || OS.indexOf("aix") > 0;
    }
}
