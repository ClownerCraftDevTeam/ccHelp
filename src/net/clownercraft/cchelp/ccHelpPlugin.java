package net.clownercraft.cchelp;

import net.clownercraft.cchelp.commands.cinfoCommand;
import net.clownercraft.cchelp.commands.cinforeloadCommand;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class ccHelpPlugin extends Plugin {
    public static HashMap<String,HashMap<String,String[]>> categories = new HashMap<>(); //Map of Category Name, with a Map of each page
    private static ccHelpPlugin instance;

    public void onEnable() {
        instance = this;
        loadFiles();
        registerCmds();
    }


    private static void loadFiles() {
        //Load all files in the plugin data folder
        File dataFolder = instance.getDataFolder();
        File[] files = dataFolder.listFiles();

        //Read them and add them to maps
        for (int i=0;i<files.length;i++) {
            String name = files[i].getName();
            String content;
            try {
                FileReader r = new FileReader(files[i]);
                BufferedReader br = new BufferedReader(r);
                content =  new String(Files.readAllBytes(files[i].toPath()));

                HashMap<String,String[]> pages = new HashMap<>();
                String[] split = content.split("#");
                for (int j=0;j<split.length-1;j+=2) {
                    String name2 = split[j];
                    String pageC = split[j+1];
                    String[] pageCs = pageC.split("\n");
                    pages.put(name2,pageCs);
                }
                categories.put(name, pages);
            } catch (IOException | IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
        //Log list of keys (categories)
        instance.getLogger().info("Files Loaded");
        Set<String> keys = categories.keySet();
        Iterator k = keys.iterator();
        String keyString = "";
        while (k.hasNext()) {keyString = keyString + k.next();}
        instance.getLogger().info(keyString);
    }

    private void registerCmds() {
        getProxy().getPluginManager().registerCommand(this, new cinfoCommand("help"));
        getProxy().getPluginManager().registerCommand(this, new cinfoCommand("vote"));
        getProxy().getPluginManager().registerCommand(this, new cinfoCommand("ranks"));
        getProxy().getPluginManager().registerCommand(this, new cinfoCommand("rules"));
        getProxy().getPluginManager().registerCommand(this, new cinfoCommand("discord"));
        getProxy().getPluginManager().registerCommand(this, new cinfoCommand("store"));
        getProxy().getPluginManager().registerCommand(this, new cinfoCommand("apply"));

        getProxy().getPluginManager().registerCommand(this, new cinforeloadCommand());




    }

    public static void reload() {
        loadFiles();
    }

}
