/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cyberiantiger.minecraft.instances.unsafe.depend;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.cyberiantiger.minecraft.instances.Instances;
import org.cyberiantiger.minecraft.instances.util.DependencyFactory;

/**
 *
 * @author antony
 */
public class VaultBankFactory extends DependencyFactory<Instances, Bank> {
    public static final String PLUGIN_NAME = "Vault";

    public VaultBankFactory(Instances thisPlugin) {
        super(thisPlugin, PLUGIN_NAME);
    }

    @Override
    public Class<Bank> getInterfaceClass() {
        return Bank.class;
    }

    @Override
    protected Bank createInterface(Plugin plugin) throws Exception {
        try {
            return new VaultBank(plugin);
        } catch (NullPointerException e) {
            // Currently no economies loaded
            return null;
        }
    }

    private static class VaultBank implements Bank {

        private final RegisteredServiceProvider<Economy> provider;
        private final Economy economy;

        public VaultBank(Plugin plugin) {
            this.provider = plugin.getServer().getServicesManager().getRegistration(Economy.class);
            this.economy = provider.getProvider();
            if (economy == null) {
                throw new NullPointerException();
            }
        }

        public boolean deduct(Player player, double amount) {
            EconomyResponse status = economy.bankWithdraw(player.getName(), amount);
            return status.transactionSuccess();
        }

        public Plugin getPlugin() {
            return null;
        }
    }
}
