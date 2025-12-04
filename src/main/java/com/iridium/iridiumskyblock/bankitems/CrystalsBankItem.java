package com.iridium.iridiumskyblock.bankitems;


import com.iridium.iridiumcore.Item;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumteams.IridiumTeams;
import com.iridium.iridiumteams.bank.BankItem;
import com.iridium.iridiumteams.bank.BankResponse;
import com.iridium.iridiumteams.database.TeamBank;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@NoArgsConstructor
public class CrystalsBankItem extends BankItem {

    public CrystalsBankItem(double defaultAmount, Item item) {
        super("Crystals", item, defaultAmount, true, true);
    }

    public CrystalsBankItem(double defaultAmount, Item item, boolean canTransact) {
        super("Crystals", item, defaultAmount, true, canTransact);
    }

    @Override
    public BankResponse withdraw(Player player, Number amount, TeamBank teamBank, IridiumTeams<?, ?> iridiumTeams) {
        if(!canTransact) return new BankResponse((int) teamBank.getNumber(), false);
        int crystals = Math.min(amount.intValue(), (int) teamBank.getNumber());

        if (crystals > 0) {
            player.getInventory().addItem(IridiumSkyblock.getInstance().getIslandManager().getIslandCrystal(crystals)).values().forEach(itemStack ->
                    player.getWorld().dropItem(player.getLocation(), itemStack)
            );
            teamBank.setNumber(teamBank.getNumber() - crystals);
            return new BankResponse(crystals, true);
        }
        return new BankResponse(crystals, false);
    }

    @Override
    public BankResponse deposit(Player player, Number amount, TeamBank teamBank, IridiumTeams<?, ?> iridiumTeams) {
        if(!canTransact) return new BankResponse((int) teamBank.getNumber(), false);

        int crystals = removeCrystals(player.getInventory());
        if (crystals > 0) {
            teamBank.setNumber(teamBank.getNumber() + crystals);
            return new BankResponse(crystals, true);
        }
        return new BankResponse(crystals, false);
    }

    private int removeCrystals(Inventory inventory) {
        int removedCrystals = 0;

        for (int slot = 0; slot < inventory.getContents().length; slot++) {
            ItemStack itemStack = inventory.getContents()[slot];
            if (itemStack == null) continue;
            int crystalsPerItem = IridiumSkyblock.getInstance().getIslandManager().getIslandCrystals(itemStack);
            int itemStackAmount = itemStack.getAmount();
            if (crystalsPerItem == 0) continue;
            inventory.setItem(slot, null);
            removedCrystals += crystalsPerItem * itemStackAmount;
        }
        return removedCrystals;
    }
}
