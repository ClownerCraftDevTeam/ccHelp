package net.clownercraft.cchelp.commands;

import net.clownercraft.cchelp.ccHelpPlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class cinforeloadCommand extends Command {

    public cinforeloadCommand() {
        super("cinforeload","cinfo.reload");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (hasPermission(commandSender)) {
            ccHelpPlugin.reload();

            BaseComponent[] message = TextComponent.fromLegacyText("§4InfoCmdCC Reloaded!");
            commandSender.sendMessage(message);
        } else {
            BaseComponent[] message = TextComponent.fromLegacyText("§4You do not have permission!");
            commandSender.sendMessage(message);
        }

    }
}
