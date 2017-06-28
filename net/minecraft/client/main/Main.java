package net.minecraft.client.main; 

import java.io.BufferedReader;
 import java.io.File;
 import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
 import java.net.Authenticator;
 import java.net.InetSocketAddress;
 import java.net.PasswordAuthentication;
 import java.net.Proxy;
 import java.net.Proxy.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
 import java.net.URL;
 import java.net.URLConnection;
 import java.util.ArrayList;
 import joptsimple.ArgumentAcceptingOptionSpec;
 import joptsimple.NonOptionArgumentSpec;
 import joptsimple.OptionParser;
 import joptsimple.OptionSet;
import lunadevs.luna.main.clientprotection.Whitelist;
 import lunadevs.luna.utils.license;
 import net.minecraft.client.Minecraft;
 import net.minecraft.util.Session;
 
 
     public static void main(String[] p_main_0_)
     {
		//Whitelist.whitelist();
		Whitelist.whitelist();
         System.setProperty("java.net.preferIPv4Stack", "true");
         System.setProperty("lwgjl", "true");
         OptionParser var1 = new OptionParser();
         var1.allowsUnrecognizedOptions();
         var1.accepts("demo");
        whitelist();
         var1.accepts("fullscreen");
         var1.accepts("checkGlErrors");
        whitelist();
         ArgumentAcceptingOptionSpec var2 = var1.accepts("server").withRequiredArg();
         ArgumentAcceptingOptionSpec var3 = var1.accepts("port").withRequiredArg().ofType(Integer.class).defaultsTo(Integer.valueOf(25565), new Integer[0]);
         ArgumentAcceptingOptionSpec var4 = var1.accepts("gameDir").withRequiredArg().ofType(File.class).defaultsTo(new File("."), new File[0]);
         ArgumentAcceptingOptionSpec var5 = var1.accepts("assetsDir").withRequiredArg().ofType(File.class);
         ArgumentAcceptingOptionSpec var6 = var1.accepts("resourcePackDir").withRequiredArg().ofType(File.class);
       //  Minecraft.hwid1();
         ArgumentAcceptingOptionSpec var7 = var1.accepts("proxyHost").withRequiredArg();
         ArgumentAcceptingOptionSpec var8 = var1.accepts("proxyPort").withRequiredArg().defaultsTo("8080", new String[0]).ofType(Integer.class);
         ArgumentAcceptingOptionSpec var9 = var1.accepts("proxyUser").withRequiredArg();
         ArgumentAcceptingOptionSpec var10 = var1.accepts("proxyPass").withRequiredArg();
        ArgumentAcceptingOptionSpec var11 = var1.accepts("username").withRequiredArg().defaultsTo("NiGHTMiST_IRC", new String[0]);
         ArgumentAcceptingOptionSpec var11 = var1.accepts("username").withRequiredArg().defaultsTo("LunaUser", new String[0]);
         ArgumentAcceptingOptionSpec var12 = var1.accepts("uuid").withRequiredArg();
         ArgumentAcceptingOptionSpec var13 = var1.accepts("accessToken").withRequiredArg().required();
         ArgumentAcceptingOptionSpec var14 = var1.accepts("version").withRequiredArg().required();
 
         (new Minecraft(var42)).run();
     }
 
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
            final URL url = new URL("http://lunaurlservers.x10.mx/connector/HWID/");
            final ArrayList<Object> lines = new ArrayList<Object>();
            final URLConnection connection = url.openConnection();
            final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                lines.add(line);
            }
            if (!lines.contains(getLicense())) {
                System.out.print("ERROR: NOT_WHITELISTED, You are not allowed to use Luna! Purchase it at https://discord.gg/kGCRzgM \n");
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
 
                ArrayList<Object> lines = new ArrayList();
            URLConnection connection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null)
            {
                lines.add(line);
            }
            if (!lines.contains("true")) {
                Minecraft.getMinecraft().shutdown();
                Minecraft.getMinecraft().shutdownMinecraftApplet();
                System.exit(0);
            }
        }
        catch (Exception e)
        {
            Minecraft.getMinecraft().shutdown();
            Minecraft.getMinecraft().shutdownMinecraftApplet();
            System.exit(0);
        }
    }

    private static boolean func_110121_a(String p_110121_0_)
    {
        return p_110121_0_ != null && !p_110121_0_.isEmpty();
    }
}