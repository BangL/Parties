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
package de.bangl.parties.listeners;

import de.bangl.parties.Party;
import de.bangl.parties.api.PartiesAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * 
 * @author Fletch_to_99 <fletchto99@hotmail.com>
 * @author Jurre1996 <Jurre@koetse.eu>
 * @author BangL <henno.rickowski@gmail.com>
 * 
 */
public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerChat(final AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();
        Party party;
        if (player != null) {
            party = PartiesAPI.getInstance().getParty(player);
            if (party != null
                    && PartiesAPI.getInstance().inParty(player)
                    && PartiesAPI.getInstance().getPartyChatMode(player)) {
                party.sendPartyChat(player, event.getMessage());
                event.setCancelled(true);
                event.setMessage("");
            }
        }
    }

    public void onPlayerQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        if (PartiesAPI.getInstance().inParty(player)) {
            final Party party = PartiesAPI.getInstance().getParty(player);
            if (party != null) {
                party.removeMember(player);
                party.sendPartyMessage(ChatColor.GOLD + player.getDisplayName() + " has left the party!");
                if (party.isEmpty()) {
                    party.sendPartyMessage(ChatColor.GOLD + "Your party dissolved.");
                    PartiesAPI.getInstance().removeParty(party);
                } else {
                    if (party.getLeader().equals(player)) {
                        party.setRandomLeader();
                        party.sendPartyMessage(ChatColor.GOLD + party.getLeader().getDisplayName() + " is the new party leader!");
                    }
                    if (party.isOp(player)) {
                        party.deop(player);
                    }
                }
            }
        } else if (PartiesAPI.getInstance().isInvited(player)) {
            PartiesAPI.getInstance().uninvite(player);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerDamage(final EntityDamageEvent event) {
        
        Player hurt = null;
        final DamageCause cause = event.getCause();
        final Entity hurtEntity = event.getEntity();
        if (hurtEntity instanceof Tameable) {
            final Tameable pet = (Tameable) hurtEntity;
            if (pet.getOwner() instanceof Player) {
                hurt = (Player) pet.getOwner();
            }
        } else if (hurtEntity instanceof Player) {
            hurt = (Player) hurtEntity;
        }
        
        Player attacker = null;
        if (event instanceof EntityDamageByEntityEvent) {
            if (cause == DamageCause.ENTITY_ATTACK) {
                final Entity attackEntity = ((EntityDamageByEntityEvent) event).getDamager();
                if (attackEntity instanceof Tameable) {
                    final Tameable pet = (Tameable) attackEntity;
                    if (pet.getOwner() instanceof Player) {
                        attacker = (Player) pet.getOwner();
                    }
                } else if (attackEntity instanceof Player) {
                    attacker = (Player) attackEntity;
                }
            } else if (cause == DamageCause.PROJECTILE) {
                final Projectile arrow = (Projectile) ((EntityDamageByEntityEvent) event).getDamager();
                if (arrow.getShooter() instanceof Player) {
                    attacker = (Player) arrow.getShooter();
                }
            }
        }
        
        if (hurt != null) {
            Party party = null;
            party = PartiesAPI.getInstance().getParty(hurt);
            if (attacker != null
                    && party != null
                    && PartiesAPI.getInstance().inParty(hurt)
                    && party.isMember(attacker)) {
                event.setDamage(0);
                event.setCancelled(true);
                attacker.sendMessage(ChatColor.RED + "You can't hurt members of the same party!");
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onCommand(final PlayerCommandPreprocessEvent event) {
        final String msg = event.getMessage().toLowerCase();
        if (msg.startsWith("/ptp")) {
            event.setMessage(event.getMessage().replace("/ptp",
                    "/party teleport"));
        } else if (msg.startsWith("/p ")) {
            event.setMessage(event.getMessage().replace("/p ", "/party "));
        }
    }
}
