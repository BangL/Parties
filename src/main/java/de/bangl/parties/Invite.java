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

import java.util.Date;
import org.bukkit.entity.Player;

/**
 * 
 * @author Fletch_to_99 <fletchto99@hotmail.com>
 * @author Jurre1996 <Jurre@koetse.eu>
 * @author BangL <henno.rickowski@gmail.com>
 * 
 */
public class Invite {

    private final transient Player host;
    private final transient Player player;
    private final transient Date time;

    public Invite(final Player host, final Player player) {
        this.host = host;
        this.player = player;
        this.time = new Date();
    }

    public Player getHost() {
        return this.host;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Date getTime() {
        return this.time;
    }
}
