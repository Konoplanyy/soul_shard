package me.konoplanyy.soulshard.mixin;

import me.konoplanyy.soulshard.data.InventoryHistorySavedData;
import me.konoplanyy.soulshard.data.InventorySnapshot;
import me.konoplanyy.soulshard.entity.SoulShardEntity;
import me.konoplanyy.soulshard.registry.ModEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerEntityMixin {
    @Shadow
    protected abstract void destroyVanishingCursedItems();

    @Inject(method = "dropEquipment", at = @At("HEAD"), cancellable = true)
    private void onDropEquipment(ServerLevel serverLevel, CallbackInfo ci) {
        Player player = (Player) (Object) this;

        // keepInventory обробляє ванільний код
        if (serverLevel.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) return;

        // Прокляття зникнення, як і у ванілі
        destroyVanishingCursedItems();

        if (!player.getInventory().isEmpty()) {
            InventorySnapshot snapshot = InventorySnapshot.fromPlayer(player);
            InventoryHistorySavedData.get(serverLevel).addSnapshot(player.getUUID(), snapshot);
        }

        SoulShardEntity soulShard = new SoulShardEntity(ModEntities.SOUL_SHARD.get(), player.level());
        soulShard.setOwner(player);

        var playerInv = player.getInventory();
        var crystalInv = soulShard.getInventory();

        // Копіюємо основний інвентар (36 слотів)
        for (int i = 0; i < 36; i++) {
            ItemStack stack = playerInv.items.get(i);
            if (!stack.isEmpty()) {
                crystalInv.setItem(i, stack.copy());
            }
        }

        // Копіюємо броню (4 слоти: 36-39)
        for (int i = 0; i < 4; i++) {
            ItemStack stack = playerInv.armor.get(i);
            if (!stack.isEmpty()) {
                crystalInv.setItem(36 + i, stack.copy());
            }
        }

        // Копіюємо offhand (слот 40)
        ItemStack offhand = playerInv.offhand.get(0);
        if (!offhand.isEmpty()) {
            crystalInv.setItem(40, offhand.copy());
        }

        // Очищаємо інвентар гравця
        playerInv.clearContent();

        // Спавнимо кристал
        if (!crystalInv.isEmpty()) {
            soulShard.setPos(player.getX(), player.getY(), player.getZ());
            player.level().addFreshEntity(soulShard);
        }

        ci.cancel(); // Скасовуємо оригінальний drop
    }
}
