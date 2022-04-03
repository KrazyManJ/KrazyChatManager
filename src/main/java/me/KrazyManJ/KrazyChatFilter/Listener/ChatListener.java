package me.KrazyManJ.KrazyChatFilter.Listener;

import me.KrazyManJ.KrazyChatFilter.Core.ChatComponent;
import me.KrazyManJ.KrazyChatFilter.Utils.ColorUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public final class ChatListener implements Listener {
    private final Pattern regex = Pattern.compile(
            "(?<name>"+String.join("|",getPlayerNames())+")|(?<command>\\{/[a-z]+})|(?<text>(?:(?!KrazyManJ|\\{/[a-z]+}).)+)"
    );

    ChatColor defaultChatColor = ChatColor.GRAY;

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);
        String clearedMsg = ColorUtils.colorize(defaultChatColor+ColorUtils.clearColors(event.getPlayer(),event.getMessage()));
        Matcher m = regex.matcher(clearedMsg);

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
        Bukkit.getConsoleSender().sendMessage(TextComponent.toLegacyText(c.toArray(new BaseComponent[0])));
    }
    private List<String> getPlayerNames(){
        List<String> s = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers()) s.add(p.getName());
        return s;
    }
}
