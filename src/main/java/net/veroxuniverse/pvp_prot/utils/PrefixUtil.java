package net.veroxuniverse.pvp_prot.utils;

import net.minecraft.scoreboard.Team;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class PrefixUtil {
    public static void setTeamDisplayNameAndPrefix(Team team, String displayName) {
        Formatting color = team.getColor();
        Text prefix = Text.literal("[" + displayName + "] ").styled(style -> style.withColor(color));
        team.setDisplayName(Text.literal(displayName).styled(style -> style.withColor(color)));
        team.setPrefix(prefix);
    }
}