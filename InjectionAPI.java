import net.minecraft.client.main.Main;

import java.io.File;

public class InjectionAPI {
    public static void main(String[] args) {
        System.out.println("Loading Luna.........................................................................................................................................................Nuddles is a skid / Nigger Jim");
        getPlatform();
            try {
                String userHome = System.getProperty("user.home", ".");
                File workingDirectory;
                switch(getPlatform()) {
                    case LINUX:
                        workingDirectory = new File(userHome, ".minecraft/");
                        break;
                    case WINDOWS:
                        final String applicationData = System.getenv("APPDATA");
                        final String folder = (applicationData != null) ? applicationData : userHome;
                        workingDirectory = new File(folder, ".minecraft/");
                        break;
                    case MACOS:
                        workingDirectory = new File(userHome, "Library/Application Support/minecraft");
                        break;
                    default:
                        workingDirectory = new File(userHome, "minecraft/");
                }
                Main.main(new String[]{"--version", "Luna",
                        "--accessToken", "0",
                        "--assetIndex", "1.8",
                        "--userProperties", "{}",
                        "--gameDir", new File(workingDirectory, ".").getAbsolutePath(),
                        "--assetsDir", new File(workingDirectory, "assets/").getAbsolutePath()});
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public static OS getPlatform() {
        String s = System.getProperty("os.name").toLowerCase();
        return s.contains("win") ? OS.WINDOWS : (s.contains("mac") ? OS.MACOS : (s.contains("solaris") ? OS.SOLARIS : (s.contains("sunos") ? OS.SOLARIS : (s.contains("linux") ? OS.LINUX : (s.contains("unix") ? OS.LINUX : OS.UNKNOWN)))));
    }
}
