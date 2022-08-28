/*
 *  Copyright (C) <2022> <XiaoMoMi>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.momirealms.customcrops.requirements;

import net.momirealms.customcrops.ConfigReader;
import net.momirealms.customcrops.utils.AdventureManager;

import java.util.List;

public record World(List<String> worlds) implements Requirement {

    public List<String> getWorlds() {
        return this.worlds;
    }

    @Override
    public boolean canPlant(PlantingCondition plantingCondition) {
        org.bukkit.World world = plantingCondition.getLocation().getWorld();
        if (worlds.contains(world.getName())) return true;
        AdventureManager.playerMessage(plantingCondition.player(), ConfigReader.Message.prefix +ConfigReader.Message.badWorld);
        return false;
    }
}
