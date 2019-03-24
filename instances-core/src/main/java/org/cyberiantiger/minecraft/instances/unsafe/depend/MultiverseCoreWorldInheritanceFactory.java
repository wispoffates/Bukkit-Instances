/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cyberiantiger.minecraft.instances.unsafe.depend;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import com.onarandombox.MultiverseCore.utils.WorldManager;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.logging.Level;
import org.bukkit.plugin.Plugin;
import org.cyberiantiger.minecraft.instances.Instances;
import org.cyberiantiger.minecraft.instances.util.DependencyFactory;

/**
 *
 * @author antony
 */
public class MultiverseCoreWorldInheritanceFactory extends DependencyFactory<Instances, WorldInheritance> {
    public static final String PLUGIN_NAME = "Multiverse-Core";

    public MultiverseCoreWorldInheritanceFactory(Instances thisPlugin) {
        super(thisPlugin, PLUGIN_NAME);
    }

    @Override
    public Class<WorldInheritance> getInterfaceClass() {
        return WorldInheritance.class;
    }

    @Override
    protected WorldInheritance createInterface(Plugin plugin) throws Exception {
        return new MultiverseCoreWorldInheritance(plugin);
    }
    
    private class MultiverseCoreWorldInheritance implements WorldInheritance {
        private final MultiverseCore plugin;
        private final MVWorldManager worldManager;
        private final Field worlds;
        private final Field worldsFromTheConfig;

        public MultiverseCoreWorldInheritance(Plugin plugin) throws Exception {
            this.plugin = (MultiverseCore) plugin;
            this.worldManager = this.plugin.getMVWorldManager();
            worlds = WorldManager.class.getDeclaredField("worlds");
            worldsFromTheConfig = WorldManager.class.getDeclaredField("worldsFromTheConfig");
            worlds.setAccessible(true);
            worldsFromTheConfig.setAccessible(true);
        }

        public void preAddInheritance(String parent, String child) {
        }

        public void postAddInheritance(String parent, String child) {
            MultiverseWorld parentWorld = worldManager.getMVWorld(parent);
            if (parentWorld != null) {
                if (worldManager.addWorld(child, parentWorld.getEnvironment(), String.valueOf(parentWorld.getSeed()), parentWorld.getWorldType(), false, null, parentWorld.getAdjustSpawn())) {
                    MultiverseWorld childWorld = worldManager.getMVWorld(child);
                    // Do not set the alias or MV-Core has multiple worlds
                    // under the same alias.
                    // childWorld.setAlias(parentWorld.getAlias());
                    childWorld.setAllowAnimalSpawn(parentWorld.canAnimalsSpawn());
                    childWorld.setAllowFlight(parentWorld.getAllowFlight());
                    childWorld.setAllowMonsterSpawn(parentWorld.canMonstersSpawn());
                    childWorld.setAutoHeal(parentWorld.getAutoHeal());
                    // Never autoload instance worlds.
                    childWorld.setAutoLoad(false);
                    childWorld.setBedRespawn(parentWorld.getBedRespawn());
                    if (parentWorld.getColor() != null) {
                        childWorld.setColor(parentWorld.getColor().name());
                    }
                    childWorld.setCurrency(parentWorld.getCurrency());
                    // We have our own difficulty management.
                    // Do not inherit parent setting for this.
                    // childWorld.setDifficulty(parentWorld.getDifficulty());
                    childWorld.setEnableWeather(parentWorld.isWeatherEnabled());
                    childWorld.setGameMode(parentWorld.getGameMode());
                    childWorld.setHidden(parentWorld.isHidden());
                    childWorld.setHunger(parentWorld.getHunger());
                    // Do not keep spawn in memory for instanced worlds.
                    childWorld.setKeepSpawnInMemory(false);
                    childWorld.setPVPMode(parentWorld.isPVPEnabled());
                    childWorld.setPlayerLimit(parentWorld.getPlayerLimit());
                    childWorld.setPrice(parentWorld.getPrice());
                    if (parentWorld.getRespawnToWorld() != null) {
                        childWorld.setRespawnToWorld(parentWorld.getRespawnToWorld().getName());
                    }
                    childWorld.setScaling(parentWorld.getScaling());
                    childWorld.setSpawnLocation(parentWorld.getSpawnLocation());
                    if (parentWorld.getStyle() != null) {
                        childWorld.setStyle(parentWorld.getStyle().name());
                    }
                    childWorld.setTime(parentWorld.getTime());
                }
            }
        }

        public void preRemoveInheritance(String parent, String child) {
            try {
                Map worldsMap = (Map) worlds.get(plugin.getMVWorldManager());
                Map worldsFromTheConfigMap = (Map) worldsFromTheConfig.get(plugin.getMVWorldManager());
                worldsMap.remove(child);
                worldsFromTheConfigMap.remove(child);
            } catch (IllegalArgumentException ex) {
                getThisPlugin().getLogger().log(Level.WARNING, null, ex);
            } catch (IllegalAccessException ex) {
                getThisPlugin().getLogger().log(Level.WARNING, null, ex);
            }
        }

        public void postRemoveInheritance(String parent, String child) {
        }

        public Plugin getPlugin() {
            return plugin;
        }
    }
}
