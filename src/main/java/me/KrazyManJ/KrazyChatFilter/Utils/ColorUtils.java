package me.KrazyManJ.KrazyChatFilter.Utils;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ColorUtils {
    private static final Pattern hex = Pattern.compile("(?i)&#[0-9a-f]{6}");

    public static String colorize(String text){
        Matcher m = hex.matcher(text);
        while (m.find()) text = text.replace(m.group(), ""+ChatColor.of(m.group().replace("&","")));
        return ChatColor.translateAlternateColorCodes('&',text);
    }

    public static String clearColors(Player player, String message) {
        String r = message;
//        if (!player.hasPermission("")) r = r.replaceAll("(?i)&x(&[0-9a-f]){6}|&#[0-9a-f]{6}","");
        return r;
    }
}
