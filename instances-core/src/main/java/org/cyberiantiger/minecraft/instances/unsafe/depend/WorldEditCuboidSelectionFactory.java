/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cyberiantiger.minecraft.instances.unsafe.depend;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.World;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.cyberiantiger.minecraft.Cuboid;
import org.cyberiantiger.minecraft.instances.Instances;
import org.cyberiantiger.minecraft.instances.command.InvocationException;
import org.cyberiantiger.minecraft.instances.util.DependencyFactory;

/**
 *
 * @author antony
 */
public class WorldEditCuboidSelectionFactory extends DependencyFactory<Instances, CuboidSelection> {

    public static final String PLUGIN_NAME = "WorldEdit";

    public WorldEditCuboidSelectionFactory(Instances thisPlugin) {
        super(thisPlugin, PLUGIN_NAME);
    }

    @Override
    public Class<CuboidSelection> getInterfaceClass() {
        return CuboidSelection.class;
    }

    @Override
    protected CuboidSelection createInterface(Plugin plugin) throws Exception {
        return new WorldEditCuboidSelection(plugin);
    }

    public static class WorldEditCuboidSelection implements CuboidSelection {

        private final WorldEditPlugin plugin;

        public WorldEditCuboidSelection(Plugin plugin) {
            this.plugin = (WorldEditPlugin) plugin;
        }

        public Cuboid getCurrentSelection(Player player) throws InvocationException {
            LocalSession session = this.plugin.getSession(player);
            World world = session.getSelectionWorld();
            try {
                Region selection = session.getSelection(world);
                if (selection instanceof CuboidRegion) {
                    CuboidRegion cubeSelection = (CuboidRegion) selection;
                    BlockVector3 pos1 = cubeSelection.getPos1();
                    BlockVector3 pos2 = cubeSelection.getPos2();
                    return new Cuboid(world.getName(), pos1.getBlockX(), pos2.getBlockX(), pos1.getBlockY(), pos2.getBlockY(), pos1.getBlockZ(), pos2.getBlockZ());
                } else {
                    throw new InvocationException("You can only create portals for cuboid regions.");
                }
            } catch (IncompleteRegionException ex) {
                if (ex.getLocalizedMessage() != null) 
                    throw new InvocationException(ex.getLocalizedMessage());
                if (ex.getMessage() != null)
                    throw new InvocationException(ex.getMessage());
                throw new InvocationException("Incomplete region");
            }
        }

        public boolean isNative() {
            return false;
        }

        public Plugin getPlugin() {
            return plugin;
        }
    }
}