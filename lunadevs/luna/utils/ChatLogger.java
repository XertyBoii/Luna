package lunadevs.luna.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

import org.apache.logging.log4j.Logger;

import lunadevs.luna.main.Parallaxa;

public class ChatLogger {
		
    private static final Minecraft MINECRAFT = Minecraft.getMinecraft();
    private static final String PREFIX = "\2478[\2477Parallaxa\2478] \2477";
    private static boolean enabled = true;
    
    public static void print(String message) {
        if (enabled) {
            if (MINECRAFT.thePlayer != null) {
                MINECRAFT.thePlayer.addChatComponentMessage(new ChatComponentText(PREFIX + message));
            } else {
                Parallaxa.getLogger().info(message);
            }
        }
    }
    public boolean isEnabled() {
        return enabled;
    }
    public static void setEnabled(boolean enable) {
        enabled = enable;
    }
}