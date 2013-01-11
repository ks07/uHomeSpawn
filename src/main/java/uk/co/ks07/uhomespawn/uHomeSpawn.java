package uk.co.ks07.uhomespawn;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import uk.co.ks07.uhome.HomeConfig;
import uk.co.ks07.uhome.uHome;

public class uHomeSpawn extends JavaPlugin implements CommandExecutor {
    private uHome uh;
    private static final String spawnPlayer = "__spawn__";
    
    @Override
    public void onEnable() {
        Plugin uhome = this.getServer().getPluginManager().getPlugin("uHome");
        if (uhome != null && uhome.isEnabled() && uhome instanceof uHome) {
            this.uh = (uHome)uhome;
            if (HomeConfig.enableUnlock) {
                this.getCommand("setspawn").setExecutor(this);
                this.getCommand("spawn").setExecutor(this);
            } else {
                this.getLogger().severe("Could not load uHomeSpawn, enableUnlock is disabled in uHome/config.yml!");
                this.getPluginLoader().disablePlugin(this);
            }
        } else {
            this.getLogger().severe("Could not load uHomeSpawn, uHome is not running on the server!");
            this.getPluginLoader().disablePlugin(this);
        }
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            
            if ("spawn".equals(commandLabel)) {
                this.uh.getHomeList().warpToExact(spawnPlayer, player.getWorld().getName(), player, this);
            } else if ("setspawn".equals(commandLabel)) {
                this.uh.getHomeList().adminAddHome(player.getLocation(), spawnPlayer, player.getWorld().getName(), this.getLogger());
                if (!this.uh.getHomeList().toggleHomeLock(spawnPlayer, player.getWorld().getName())) {
                    this.uh.getHomeList().toggleHomeLock(spawnPlayer, player.getWorld().getName());
                }
            }
        } else {
            sender.sendMessage("You must be in-game to use this command!");
        }
        
        return true;
    }
    
    @Override
    public void onDisable() {
        this.uh = null;
    }
}
