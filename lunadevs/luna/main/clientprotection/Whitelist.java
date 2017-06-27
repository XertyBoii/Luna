package lunadevs.luna.main.clientprotection;

import java.security.*;
import java.util.*;

import javax.swing.JOptionPane;

import java.io.*;
import net.minecraft.client.*;
import java.net.*;
import net.minecraft.client.Minecraft;

public class Whitelist {
	
    public static String getLicense() throws Exception {
        final String hwid = SHA1(String.valueOf(System.getenv("PROCESSOR_IDENTIFIER")) + System.getenv("COMPUTERNAME") + System.getProperty("user.name"));
        return hwid;
    }
    
    private static String SHA1(final String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        final MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] sha1hash = new byte[40];
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        sha1hash = md.digest();
        return convertToHex(sha1hash);
    }
    
    private static String convertToHex(final byte[] data) {
        final StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; ++i) {
            int halfbyte = data[i] >>> 4 & 0xF;
            int two_halfs = 0;
            do {
                if (halfbyte >= 0 && halfbyte <= 9) {
                    buf.append((char)(48 + halfbyte));
                }
                else {
                    buf.append((char)(97 + (halfbyte - 10)));
                }
                halfbyte = (data[i] & 0xF);
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }
    
    public static void whitelist() {
        try {
            final URL url = new URL("http://lunaclient.pw/api/hwid/HWID.php?id=" + getLicense());
            final ArrayList<Object> lines = new ArrayList<Object>();
            final URLConnection connection = url.openConnection();
            final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                lines.add(line);
            }
            if (!lines.contains(getLicense())) {
                System.out.print("ERROR: NOT_WHITELISTED, You are not allowed to use Luna! Purchase it at https://discord.gg/kGCRzgM \n");
                JOptionPane.showMessageDialog(null, "ERROR CODE: NOT_WHITELISTED");
                Minecraft.getMinecraft().shutdown();
                Minecraft.getMinecraft().shutdownMinecraftApplet();
                System.exit(0);
            }
        }
        catch (Exception e) {
            Minecraft.getMinecraft().shutdown();
            Minecraft.getMinecraft().shutdownMinecraftApplet();
            System.exit(0);
        }
    }
}