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

public class ccHelpCommand extends Command implements TabExecutor {
    String category;

    //For the default command
    public ccHelpCommand(String name) {
        super(name);
        this.category = "";
    }

    //for category commands
    public ccHelpCommand(String name, String category) {
        super(name);
        this.category = category;
    }


    @Override
    public void execute(CommandSender commandSender, String[] args) {
        String page = "main";

        /*
        Find the category and page
         */
        //Main Command
        if (category.equals("")) {
            if (args.length<1) {
                category = "main";
            } else {
                category = args[0];
                if (args.length>1) {
                    page = args[1];
                } else {
                    page = "main";
                }
            }
        } else {
            if (args.length<1) {
                page = "main";
            } else {
                page = args[0];
            }
        }


        /*
        Get and send the text to player
         */
        try {

            String[] text = ccHelpPlugin.getInstance().categories.get(category).get(page);
            for (String s : text) {
                BaseComponent[] message = TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', s));
                commandSender.sendMessage(message);
            }
        } catch (NullPointerException e) {
            BaseComponent[] message = TextComponent.fromLegacyText(
                    ChatColor.translateAlternateColorCodes(
                            '&',ccHelpPlugin.getInstance().pageNotFound));

            commandSender.sendMessage(message);
            e.printStackTrace();
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] args) {
        if (category.equals("")) {
            if (args.length==1) {

                return filterSet(ccHelpPlugin.getInstance().categories.keySet(),args[0]);
            } else if (args.length==2) {
                String cat = args[0];
                HashMap<String,String[]> map = ccHelpPlugin.getInstance().categories.getOrDefault(cat, new HashMap<>());

                return filterSet(map.keySet(),args[1]);
            }
        } else {
            if (args.length==1) {
                String cat = getName();
                HashMap<String,String[]> map = ccHelpPlugin.getInstance().categories.getOrDefault(cat, new HashMap<>());

                return filterSet(map.keySet(),args[0]);
            }
        }
        return new HashSet<>();
    }

    /**
     * Filter list of items based on what the user has started typing
     * @param set = the list to be filtered
     * @param currStr = what the user has typed so far
     * @return the filtered set
     */
    public Set<String> filterSet(Set<String> set, String currStr) {
        Pattern filter = Pattern.compile("^" + currStr, Pattern.CASE_INSENSITIVE);
        return set.stream()
                .filter(filter.asPredicate())
                .collect(Collectors.toSet());
    }
}
