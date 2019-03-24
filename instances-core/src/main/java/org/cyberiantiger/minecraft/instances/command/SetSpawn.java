/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cyberiantiger.minecraft.instances.command;

import java.util.List;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.cyberiantiger.minecraft.instances.Instances;

/**
 *
 * @author antony
 */
public class SetSpawn extends AbstractCommand {

    public SetSpawn() {
        super(SenderType.PLAYER);
    }

    @Override
    public List<String> execute(Instances instances, Player player, String[] args) {
        if (args.length != 0 && args.length != 1) {
            return null;
        }
        if (args.length == 1 && !"none".equals(args[0])) {
            return null;
        }
        if (args.length == 1) {
            instances.setSpawn(null);
            return msg("Spawn unset.");
        } else {
            World world = player.getWorld();
            if (instances.isInstance(world)) {
                throw new InvocationException("Cannot set the spawn world to an instance.");
            }
            instances.setSpawn(world);
            return msg("Spawn set to " + player.getWorld().getName() + '.');
        }
    }
}
