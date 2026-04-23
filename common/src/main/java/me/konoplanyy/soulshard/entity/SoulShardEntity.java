package me.konoplanyy.soulshard.entity;

import me.konoplanyy.soulshard.config.SoulShardConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SoulShardEntity extends PathfinderMob implements IGeoAnimatable{
    private final SimpleContainer inventory = new SimpleContainer(41);
    private UUID ownerUUID;
    private int lifeTicks = 0;

    public SoulShardEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
        if (!level.isClientSide) {
            this.setHealth(SoulShardConfig.getConfig().getCrystalHealth());
        }
    }

    public SimpleContainer getInventory() {
        return inventory;
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public boolean isOwner(Player player) {
        return this.ownerUUID == player.getUUID();
    }

    public void setOwner(Player player) {
        ownerUUID = player.getUUID();
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource damageSource, int lootingMultiplier, boolean recentlyHit) {
        super.dropCustomDeathLoot(damageSource, lootingMultiplier, recentlyHit);

        if (SoulShardConfig.getConfig().dropItemsOnBreak()) {
            for (int i = 0; i < inventory.getContainerSize(); i++) {
                ItemStack stack = inventory.getItem(i);
                if (!stack.isEmpty()) {
                    this.spawnAtLocation(stack);
                }
            }
        }

        inventory.clearContent();
    }

    public void addItem(ItemStack stack) {
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            if (inventory.getItem(i).isEmpty()) {
                inventory.setItem(i, stack.copy());
                break;
            }
        }
    }

    public void setInventoryToPlayer(Player player) {
        if (player.level().isClientSide) return;

        var playerInv = player.getInventory();

        List<ItemStack> oldItems = new ArrayList<>();
        for (int i = 0; i < playerInv.getContainerSize(); i++) {
            ItemStack stack = playerInv.getItem(i);
            if (!stack.isEmpty()) {
                oldItems.add(stack.copy());
            }
        }

        playerInv.clearContent();

        for (int i = 0; i < 36; i++) {
            ItemStack stack = inventory.getItem(i);
            playerInv.items.set(i, stack.isEmpty() ? ItemStack.EMPTY : stack.copy());
        }

        for (int i = 0; i < 4; i++) {
            ItemStack stack = inventory.getItem(36 + i);
            playerInv.armor.set(i, stack.isEmpty() ? ItemStack.EMPTY : stack.copy());
        }

        ItemStack offhandStack = inventory.getItem(40);
        playerInv.offhand.set(0, offhandStack.isEmpty() ? ItemStack.EMPTY : offhandStack.copy());

        for (ItemStack oldStack : oldItems) {
            boolean added = false;
            for (int i = 0; i < 36; i++) {
                if (playerInv.items.get(i).isEmpty()) {
                    playerInv.items.set(i, oldStack);
                    added = true;
                    break;
                }
            }
            if (!added) {
                player.drop(oldStack, false);
            }
        }

        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.inventoryMenu.broadcastChanges();
        }

        playerInv.setChanged();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10D)
                .add(Attributes.MOVEMENT_SPEED, 0.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    protected void pushEntities() {}
    @Override
    public boolean isPushable() { return false; }
    @Override
    public boolean canBeCollidedWith() { return false; }
    @Override
    public void doPush(Entity entity) {}
    @Override
    public void push(Entity entity) {}
    @Override
    protected boolean isImmobile() { return true; }
    @Override
    public boolean canCollideWith(Entity entity) { return false; }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) {
            int lifeTimeSeconds = SoulShardConfig.getConfig().getCrystalLifeTime();

            if (lifeTimeSeconds != -1) {
                lifeTicks++;
                if (lifeTicks >= lifeTimeSeconds * 20) {
                    this.discard();
                }
            }
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (!SoulShardConfig.getConfig().canDie()) {
            return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    public void die(DamageSource damageSource) {
        super.die(damageSource);
    }

    @Override
    public InteractionResult interactAt(Player player, Vec3 hitPos, InteractionHand hand) {
        if (!level().isClientSide && isOwner(player)) {
            setInventoryToPlayer(player);
            inventory.clearContent();
            this.discard();
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}