package net.betterverse.remotifier;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Remotifier extends JavaPlugin {

    public static Remotifier Instance;
    private static boolean hasCreditsShop = false;
    private static Long lastCreditsCheck = null;
    public Database DB;
    public static Economy economy = null;

    @Override
    public void onDisable() {
        System.out.println(this + " is now disabled!");
    }

    @Override
    public void onEnable() {
        setupEconomy();
        Instance = this;
        Config.Load();
        DB = new Database();
        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new PollTask(), 20L * 60, 20L * 60);
        this.getServer().getPluginManager().registerEvents(new PlayerEvents(), this);
        checkCreditsShop();
        System.out.println(this + " is now enabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            return false;
        }
        if (!(sender.hasPermission("remotifier.reward"))) {
            return false;
        }
        DB.Insert(args[0], Bukkit.getPlayer(args[0]).getAddress().getAddress().getHostAddress());
        sender.sendMessage("Inserted row into table. Should trigger reward soon.");
        return false;
    }

    private Boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }

    /**
     * Return true if server has CreditsShop, false if not.
     *
     * @return
     */
    public static boolean hasCreditsShop() {
        checkCreditsShop();
        return hasCreditsShop;
    }

    /**
     * Checks if the server is running CreditsShop.
     */
    private static void checkCreditsShop() {
        if(!hasCreditsShop && lastCreditsCheck == null ||
                (lastCreditsCheck + 30 < (System.currentTimeMillis() / 1000))) {
            Plugin check = Instance.getServer().getPluginManager().getPlugin("Spout");
            if (check != null && check.isEnabled()) {
                hasCreditsShop = true;
            }
            lastCreditsCheck = System.currentTimeMillis() / 1000;
        }
    }
}