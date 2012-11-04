/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.bangl.parties;

import de.bangl.parties.api.PartiesAPI;
import de.bangl.parties.commands.CommandManager;
import de.bangl.parties.listeners.PlayerListener;
import java.util.Date;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This class represents the main plugin. All actions related to the plugin are forwarded by this class
 * 
 * @author Fletch_to_99 <fletchto99@hotmail.com>
 * @author Jurre1996 <Jurre@koetse.eu>
 * @author BangL <henno.rickowski@gmail.com>
 * 
 */
public class PartiesPlugin extends JavaPlugin {

    // Invite timeout in seconds
    private final static int TIMEOUT = 10;

    private static CommandManager command = new CommandManager();

    private ExpireChecker checker;
    private int scheduledTaskId;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        checker = new ExpireChecker(this);
        scheduledTaskId = getServer().getScheduler().scheduleAsyncRepeatingTask(this, checker, 1200L, 300);
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        return getCommandManager().onGameCommand(sender, command, label, args);
    }

    protected CommandManager getCommandManager() {
        return command;
    }

    public static void debug(final Exception error) {
        Bukkit.getLogger().log(Level.SEVERE, "Critical error detected!", error);
    }

    public static void log(final String msg) {
        Bukkit.getLogger().info(msg);
    }

    private static class ExpireChecker implements Runnable {
        private final transient PartiesPlugin plugin;

        public ExpireChecker(final PartiesPlugin plugin) {
            this.plugin = plugin;
        }

        @Override
        public void run() {
            final Date now = new Date();
            for (Invite invite : PartiesAPI.getInstance().getInvites()) {
                final Player player = invite.getPlayer();
                final Player host = invite.getHost();
                if (invite.getTime().getTime() + (TIMEOUT * 1000) <= now.getTime()) {
                    host.sendMessage(ChatColor.GOLD + "The invitation of " + ChatColor.RESET + player.getDisplayName() + ChatColor.GOLD + " expired.");
                    player.sendMessage(ChatColor.GOLD + "The invitation by " + ChatColor.RESET + host.getDisplayName() + ChatColor.GOLD + " expired.");
                    PartiesAPI.getInstance().uninvite(invite.getPlayer());
                }
            }
        }
    }
}
