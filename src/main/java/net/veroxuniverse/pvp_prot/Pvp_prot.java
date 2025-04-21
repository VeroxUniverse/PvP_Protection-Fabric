package net.veroxuniverse.pvp_prot;

import net.fabricmc.api.ModInitializer;
import net.veroxuniverse.pvp_prot.config.ProtectionConfig;
import net.veroxuniverse.pvp_prot.setup.CommandRegistry;
import net.veroxuniverse.pvp_prot.setup.ProtectionHandler;

public class Pvp_prot implements ModInitializer {

    public static final String MOD_ID = "pvp_prot";

    @Override
    public void onInitialize() {
        ProtectionConfig.load();
        ProtectionHandler.register();

        CommandRegistry.registerCommands();
    }

}
