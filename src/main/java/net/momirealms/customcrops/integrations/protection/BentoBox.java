package net.momirealms.customcrops.integrations.protection;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import world.bentobox.bentobox.api.flags.Flag;
import world.bentobox.bentobox.database.objects.Island;

import java.util.Optional;
import java.util.UUID;


public class BentoBox  implements Integration {

    @Override
    public boolean canBreak(Location location, Player player) {
       Optional<Island> islandOption= world.bentobox.bentobox.BentoBox.getInstance().getIslandsManager().getProtectedIslandAt(location);
        OfflinePlayer player1=(OfflinePlayer) player;
       // System.out.println("玩家"+player1.getUniqueId());
        for (UUID uuid : islandOption.get().getMemberSet()) {
        //    System.out.println(uuid);
        }
       // System.out.println(islandOption.isPresent()&&islandOption.get().getMemberSet().contains(player1.getUniqueId()));
       if(islandOption.isPresent()&&islandOption.get().getMemberSet().contains(player1.getUniqueId())){
           return true;
       }
        return false;
    }

    @Override
    public boolean canPlace(Location location, Player player) {
        Optional<Island> islandOption= world.bentobox.bentobox.BentoBox.getInstance().getIslandsManager().getProtectedIslandAt(location);
        OfflinePlayer player1=(OfflinePlayer) player;
      //  System.out.println("玩家"+player1.getUniqueId());
        for (UUID uuid : islandOption.get().getMemberSet()) {
         //   System.out.println(uuid);
        }
      //  System.out.println(islandOption.isPresent()&&islandOption.get().getMemberSet().contains(player1.getUniqueId()));
        if(islandOption.isPresent()&&islandOption.get().getMemberSet().contains(player1.getUniqueId())){
            return true;
        }
        return false;
    }
}
