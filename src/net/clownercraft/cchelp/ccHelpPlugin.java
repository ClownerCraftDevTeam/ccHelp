package net.clownercraft.cchelp;

import net.clownercraft.cchelp.commands.ccHelpCommand;
import net.clownercraft.cchelp.commands.reloadCommand;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class ccHelpPlugin extends Plugin {
    //Map of Category Name, with a Map of each page
    public HashMap<String,HashMap<String,String[]>> categories;

    //Map Command label, category name
    public HashMap<String,String> commandMap;

    //Command labels
    public String generalCommandLabel,reloadCommandLabel;

    //Messages
    public String pageNotFound,reloaded,noPermission;

    public static ccHelpPlugin getInstance() {
        return instance;
    }

    private static ccHelpPlugin instance;

    public void onEnable() {
        instance = this;
        loadFiles();
        registerCmds();
    }


    private void loadFiles() {
        //Load all files in the plugin data folder
        File dataFolder = instance.getDataFolder();
        File categoryFolder = new File(dataFolder,"categories");

        //If the category folder doesn't exist, create it and save the default files
        if (categoryFolder.mkdir()){
            InputStream mainIn = this.getResourceAsStream("main.txt");
            InputStream rulesIn = this.getResourceAsStream("rules.txt");
            File mainFile = new File(categoryFolder,"main.txt");
            File rulesFile = new File(categoryFolder,"rules.txt");

            try {
                Files.copy(mainIn,mainFile.toPath());
                Files.copy(rulesIn,rulesFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        //load config.yml
        File commandsFile = new File(dataFolder,"config.yml");

        //Save default if it doesn't exist
        if (!commandsFile.exists()) {
            InputStream commandsIn = this.getResourceAsStream("config.yml");
            try {
                Files.copy(commandsIn,commandsFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Configuration commandsConf;
        commandMap = new HashMap<>();
        try {
            commandsConf = ConfigurationProvider.getProvider(YamlConfiguration.class).load(commandsFile);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        //Load command settings
        generalCommandLabel = commandsConf.getString("GeneralCommand");
        reloadCommandLabel = commandsConf.getString("ReloadCommand");

        pageNotFound = commandsConf.getString("Messages.pageNotFound");
        reloaded = commandsConf.getString("Messages.reloaded");
        noPermission = commandsConf.getString("Messages.noPermission");

        Configuration catConf = commandsConf.getSection("CategoriesCommands");
        for (String key: catConf.getKeys()) {
            String cat = catConf.getString(key);
            commandMap.put(key,cat);
        }

        /* Load category files */
        File[] files = categoryFolder.listFiles();

        categories = new HashMap<>();

        //Read them and add them to maps
        for (File file : files) {
            String name = file.getName().replaceAll("\\.txt","");
            String content;
            try {
                FileReader r = new FileReader(file);
                BufferedReader br = new BufferedReader(r);
                content = new String(Files.readAllBytes(file.toPath()));

                HashMap<String, String[]> pages = new HashMap<>();
                String[] split = content.split("(?<!\\\\)#");
                for (int j = 1; j < split.length - 1; j += 2) {
                    String name2 = split[j];
                    String pageC = split[j + 1];
                    String[] pageCs = pageC.split("\n");
                    pages.put(name2, pageCs);
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
        //Unregister any existing commands, for if we reload the plugin
        getProxy().getPluginManager().unregisterCommands(this);

        //General Commands
        getProxy().getPluginManager().registerCommand(this, new reloadCommand(reloadCommandLabel));
        getProxy().getPluginManager().registerCommand(this, new ccHelpCommand(generalCommandLabel));

        //Category Custom Commands
        for (String key: commandMap.keySet()) {
            getProxy().getPluginManager().registerCommand(this, new ccHelpCommand(key,commandMap.get(key)));
        }
    }

    public void reload() {
        //load the files again
        loadFiles();
        //register the commands again in case any changed
        registerCmds();
    }

}
