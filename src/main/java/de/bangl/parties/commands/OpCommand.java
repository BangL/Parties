/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bangl.parties.commands;

import de.bangl.parties.Party;
import de.bangl.parties.api.PartiesAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * 
 * @author Fletch_to_99 <fletchto99@hotmail.com>
 * @author Jurre1996 <Jurre@koetse.eu>
 * @author BangL <henno.rickowski@gmail.com>
 * 
 */
public class OpCommand extends AbstractCommand {

    @Override
    public boolean canExecute(final CommandSender sender, final String[] split) {
        return split.length == 3
                && split[0].equalsIgnoreCase("party")
                && split[1].equalsIgnoreCase("op");
    }

    @Override
    public boolean execute(CommandSender sender, String[] split) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            final Party party = PartiesAPI.getInstance().getParty(player);
            if (party == null) {
                player.sendMessage(ChatColor.RED + "You're not in a party!");
            } else {
                if (party.getLeader().equals(player)
                        || party.isOp(player)) {
                    final Player op = Bukkit.getPlayer(split[2]);
                    if (op == null) {
                        player.sendMessage(ChatColor.RED + "This player does not exist.");
                    } else if (PartiesAPI.getInstance().inParty(op)
                            && PartiesAPI.getInstance().getParty(op).equals(party)) {
                        if (party.isLeader(op)) {
                            player.sendMessage(op.getDisplayName() + ChatColor.RED + " is the already the leader of this party.");
                        } else if (party.isOp(op)) {
                            player.sendMessage(op.getDisplayName() + ChatColor.RED + " is already an operator of this party.");
                        } else {
                            party.op(op);
                            party.sendPartyMessage(op.getDisplayName() + ChatColor.GOLD + " is now an operator of this party.");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "This player is not part of your party.");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "You're not allowed to op a member of this party.");
                }
            }
        } else {
            sender.sendMessage("You can't op a member of any party!");
        }
        return true;
    }

    @Override
    public String[] getPermission() {
        return new String[] { "parties.use" };
    }
}
