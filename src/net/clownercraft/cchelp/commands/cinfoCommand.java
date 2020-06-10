package net.clownercraft.cchelp.commands;

import net.clownercraft.cchelp.ccHelpPlugin;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class cinfoCommand extends Command implements TabExecutor {

    public cinfoCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        String page = "main";
        String cat = "";

        /*
        Find the category and page
         */
        if (getName().equalsIgnoreCase("rules")) {
            cat = "rules";
            if (args.length!=0) {
                page = args[0];
            }
        } else if (getName().equalsIgnoreCase("ranks")) {
            cat = "ranks";
            if (args.length!=0) {
                page = args[0];
            }
        }  else if (getName().equalsIgnoreCase("vote")) {
            cat = "vote";
            if (args.length!=0) {
                page = args[0];
            }
        }  else if (getName().equalsIgnoreCase("discord")) {
            cat = "discord";
            if (args.length!=0) {
                page = args[0];
            }
        } else if (getName().equalsIgnoreCase("store")) {
            cat = "store";
            if (args.length!=0) {
                page = args[0];
            }
        } else if (getName().equalsIgnoreCase("apply")) {
            cat = "apply";
            if (args.length != 0) {
                page = args[0];
            }
        } else if (getName().equalsIgnoreCase("help")) {
            if (args.length>=2) {
                cat = args[0];
                page = args[1];
            } else if (args.length==1) {
                cat = args[0];
            } else {
                cat = "help";
            }
        }

        /*
        Get and send the text to player
         */
        try {

            String[] text = ccHelpPlugin.categories.get(cat).get(page);
            for (String s : text) {
                BaseComponent[] message = TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', s));
                commandSender.sendMessage(message);
            }
        } catch (NullPointerException e) {
            BaseComponent[] message = TextComponent.fromLegacyText("§4Page not found!");
            commandSender.sendMessage(message);
            e.printStackTrace();
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] args) {
        if (getName().equalsIgnoreCase("help")) {
            if (args.length==1) {

                return filterSet(ccHelpPlugin.categories.keySet(),"^"+args[0]);
            } else if (args.length==2) {
                String cat = args[0];
                HashMap<String,String[]> map = ccHelpPlugin.categories.getOrDefault(cat, new HashMap<>());

                return filterSet(map.keySet(),"^"+args[1]);
            }
        } else {
            if (args.length==1) {
                String cat = getName();
                HashMap<String,String[]> map = ccHelpPlugin.categories.getOrDefault(cat, new HashMap<>());

                return filterSet(map.keySet(),"^"+args[0]);
            }
        }
        return new HashSet<>();
    }


    public Set<String> filterSet(Set<String> set, String regex) {
        Pattern filter = Pattern.compile(regex);
        return set.stream()
                .filter(filter.asPredicate())
                .collect(Collectors.toSet());
    }
}
