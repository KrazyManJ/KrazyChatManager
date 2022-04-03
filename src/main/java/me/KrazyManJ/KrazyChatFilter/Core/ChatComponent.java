package me.KrazyManJ.KrazyChatFilter.Core;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Text;

public final class ChatComponent {
    public static BaseComponent CommandComponent(String command){
        return new ComponentBuilder(new TextComponent(command))
                .color(ChatColor.RED)
                .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,command))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new Text(TextComponent.fromLegacyText("§cCommand here!"))))
                .create()[0];
    }
    public static BaseComponent MentionComponent(String name){
        return new ComponentBuilder(
                new TextComponent(name))
                .color(ChatColor.BLUE)
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new Text(TextComponent.fromLegacyText("§9Player mentioned you!"))))
                .create()[0];
    }
}
