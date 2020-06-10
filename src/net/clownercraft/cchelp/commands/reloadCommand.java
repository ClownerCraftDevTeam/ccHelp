package net.clownercraft.cchelp.commands;

import net.clownercraft.cchelp.ccHelpPlugin;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class reloadCommand extends Command {

    public reloadCommand(String name) {
        super(name,"ccHelp.reload");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (hasPermission(commandSender)) {
            ccHelpPlugin.getInstance().reload();

            BaseComponent[] message =
                    TextComponent.fromLegacyText(
                            ChatColor.translateAlternateColorCodes(
                                    '&',ccHelpPlugin.getInstance().reloaded));
            commandSender.sendMessage(message);
        } else {
            BaseComponent[] message =
                    TextComponent.fromLegacyText(
                            ChatColor.translateAlternateColorCodes(
                                    '&',ccHelpPlugin.getInstance().noPermission));
            commandSender.sendMessage(message);
        }

    }
}
