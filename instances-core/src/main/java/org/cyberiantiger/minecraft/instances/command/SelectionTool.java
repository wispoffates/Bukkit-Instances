/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cyberiantiger.minecraft.instances.command;

import java.util.List;
import org.bukkit.entity.Player;
import org.cyberiantiger.minecraft.instances.Instances;

/**
 *
 * @author antony
 */
public class SelectionTool extends AbstractCommand {

    public SelectionTool() {
        super(SenderType.PLAYER);
    }

    @Override
    public List<String> execute(Instances instances, Player sender, String[] args) {
        if (args.length != 0) {
            return null;
        }
        if (!instances.getCuboidSelection().isNative()) {
            throw new InvocationException("Using WorldEdit selection tool.");
        }
        Player player = (Player) sender;
        instances.setSelectionTool(player.getInventory().getItemInMainHand());
        return msg("Selection tool set.");
    }
}
