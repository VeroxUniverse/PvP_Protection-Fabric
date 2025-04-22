package net.veroxuniverse.pvp_prot.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.veroxuniverse.pvp_prot.config.ProtectionConfig;

public class ProtectionConfigCommand {

    public static LiteralCommandNode<ServerCommandSource> register() {
        return CommandManager.literal("set")
                .then(CommandManager.literal("time")
                        .then(CommandManager.argument("time", StringArgumentType.string())
                                .executes(context -> {
                                    ServerCommandSource source = context.getSource();

                                    if (source.hasPermissionLevel(4)) {
                                        String timeArg = StringArgumentType.getString(context, "time");

                                        try {
                                            int protectionTime = Integer.parseInt(timeArg);
                                            ProtectionConfig.protectionMinutes = protectionTime;
                                            ProtectionConfig.save();
                                            source.sendMessage(Text.literal("Protection time set to " + protectionTime + " minutes"));

                                            return 1;
                                        } catch (NumberFormatException e) {
                                            source.sendMessage(Text.literal("Invalid time format. Please provide a valid number."));
                                            return 0;
                                        }
                                    } else {
                                        source.sendMessage(Text.literal("You do not have permission to use this command.").styled(style -> style.withColor(Formatting.RED)));
                                        return 0;
                                    }
                                })
                        )
                ).build();
    }
}
