package me.KrazyManJ.KrazyChatFilter.Listener;

import me.KrazyManJ.KrazyChatFilter.Core.ChatComponent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.intellij.lang.annotations.Language;
import org.intellij.lang.annotations.RegExp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatListener implements Listener {

    ChatColor DEFAULT_COLOR = ChatColor.GRAY;

    @EventHandler
    public void on(AsyncPlayerChatEvent event) {
        event.setCancelled(true);

        String clearedMsg = colorize(clearColors(event.getPlayer(),event.getMessage()));

        Matcher m = Pattern.compile("(?<name>"+String.join("|",getPlayerNames())+")|(?<command>\\{/[a-z]+})|(?<text>(?:(?!KrazyManJ|\\{/[a-z]+}).)+)").matcher(clearedMsg);
        List<BaseComponent> c = new ArrayList<>();
        c.addAll(List.of(TextComponent.fromLegacyText(event.getPlayer().getDisplayName())));
        c.addAll(List.of(TextComponent.fromLegacyText("§8§l » ")));
        String lastColor = "";

        while (m.find()){
            String group = "";

            for (String g : Arrays.asList("text","command","name")) if (m.group(g) != null) group = g;
            switch (group){
                case "name" -> c.add(ChatComponent.MentionComponent(m.group()));
                case "command" -> c.add(ChatComponent.CommandComponent(m.group().replaceAll("[{}]","")));
                case "text" -> {
                    c.addAll(List.of(TextComponent.fromLegacyText(lastColor+m.group())));
                    String l = org.bukkit.ChatColor.getLastColors(m.group());
                    if (l.length() > 0) lastColor = l;
                }
            }
        }
        Bukkit.spigot().broadcast(c.toArray(new BaseComponent[0]));
    }

    private String clearColors(Player player,String message) {
        String r = message;
//        if (!player.hasPermission("")) r = r.replaceAll("&x(&[0-9a-f]){6}","");
        return r;
    }

    private List<String> getPlayerNames(){
        List<String> s = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers()) s.add(p.getName());
        return s;
    }

    private String colorize(String text){
        return ChatColor.translateAlternateColorCodes('&',text);
    }
}
