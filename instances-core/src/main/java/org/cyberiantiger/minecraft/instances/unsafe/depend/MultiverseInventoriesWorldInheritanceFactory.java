/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cyberiantiger.minecraft.instances.unsafe.depend;

import org.cyberiantiger.minecraft.instances.util.DependencyFactory;
import com.onarandombox.multiverseinventories.MultiverseInventories;
import com.onarandombox.multiverseinventories.WorldGroup;
import org.bukkit.plugin.Plugin;
import org.cyberiantiger.minecraft.instances.Instances;

/**
 *
 * @author antony
 */
public class MultiverseInventoriesWorldInheritanceFactory extends DependencyFactory<Instances, WorldInheritance> {
    public static final String PLUGIN_NAME = "Multiverse-Inventories";

    public MultiverseInventoriesWorldInheritanceFactory(Instances thisPlugin) {
        super(thisPlugin, PLUGIN_NAME);
    }

    @Override
    public Class<WorldInheritance> getInterfaceClass() {
        return WorldInheritance.class;
    }

    @Override
    protected WorldInheritance createInterface(Plugin plugin) throws Exception {
        return new MultiverseInventoriesWorldInheritance(plugin);
    }

    private static class MultiverseInventoriesWorldInheritance implements WorldInheritance {
        private MultiverseInventories plugin;

        public MultiverseInventoriesWorldInheritance(Plugin plugin) {
            this.plugin = (MultiverseInventories) plugin;
        }

        public void preAddInheritance(String parent, String child) {
        }

        public void postAddInheritance(String parent, String child) {
            for (WorldGroup profile : plugin.getGroupManager().getGroupsForWorld(parent)) {
                profile.addWorld(child);
            }
        }

        public void preRemoveInheritance(String parent, String child) {
            for (WorldGroup profile : plugin.getGroupManager().getGroupsForWorld(parent)) {
                profile.removeWorld(child);
            }
        }

        public void postRemoveInheritance(String parent, String child) {
        }

        public Plugin getPlugin() {
            return plugin;
        }
    }
}
