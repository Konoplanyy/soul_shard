package me.konoplanyy.soulshard.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerEntityMixin {
    @Inject(method = "dropEquipment", at = @At("HEAD"), cancellable = true)
    private void onDropEquipment(CallbackInfo ci) {
        System.out.println("Речі не падають)");

        Player player = (Player) (Object) this;
        java.util.List<net.minecraft.world.item.ItemStack> itemsToSave = new java.util.ArrayList<>();

        for (int i = 0; i < player.getInventory().getContainerSize(); i++)
        {
            net.minecraft.world.item.ItemStack stack = player.getInventory().getItem(i);
            if (!stack.isEmpty())
            {
                itemsToSave.add(stack.copy());
                player.getInventory().setItem(i, ItemStack.EMPTY);
            }
        }

        System.out.println("Збережено предметів: " + itemsToSave.size());

        ci.cancel();
    }
}
