package me.konoplanyy.soulshard.entity;

import me.konoplanyy.soulshard.config.SoulShardConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.core.UUIDUtil;
import net.minecraft.world.ItemStackWithSlot;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SoulShardEntity extends PathfinderMob implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");

    private final SimpleContainer inventory = new SimpleContainer(41);

    private UUID ownerUUID;
    private int lifeTicks = 0;
    public SoulShardEntity(EntityType<? extends PathfinderMob> type, Level level){
        super(type, level);
        if (!level.isClientSide()) {
            int crystalHealth = Math.max(1, SoulShardConfig.getConfig().getCrystalHealth());
            var maxHealth = this.getAttribute(Attributes.MAX_HEALTH);
            if (maxHealth != null) {
                maxHealth.setBaseValue(crystalHealth);
            }
            this.setHealth(crystalHealth);
        }
        this.setPersistenceRequired();
    }

    public SimpleContainer getInventory(){
        return inventory;
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public boolean isOwner(Player player) {
        return this.ownerUUID != null && this.ownerUUID.equals(player.getUUID());
    }

    public void setOwner(Player player){
        ownerUUID = player.getUUID();
    }

    @Override
    public boolean causeFallDamage(double fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    public void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit){
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);

        if (SoulShardConfig.getConfig().dropItemsOnBreak()){

            for (int i = 0; i < inventory.getContainerSize(); i++){
                ItemStack stack = inventory.getItem(i);
                if (!stack.isEmpty()){
                    this.spawnAtLocation(level, stack);
                }
            }
        }

        inventory.clearContent();
    }

    public void addItem(ItemStack stack){
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            if (inventory.getItem(i).isEmpty()){
                inventory.setItem(i, stack.copy());
                break;
            }
        }
    }

    public void setInventoryToPlayer(Player player){
        if (player.level().isClientSide()) return;

        var playerInv = player.getInventory();

        // Зберігаємо старі речі гравця
        List<ItemStack> oldItems = new ArrayList<>();
        for (int i = 0; i < playerInv.getContainerSize(); i++){
            ItemStack stack = playerInv.getItem(i);
            if (!stack.isEmpty()) {
                oldItems.add(stack.copy());
            }
        }

        // Очищаємо інвентар
        playerInv.clearContent();

        // Копіюємо речі з кристала на точні позиції
        // (0-35 основний, 36-39 броня, 40 offhand)
        for (int i = 0; i < 41; i++){
            ItemStack stack = inventory.getItem(i);
            playerInv.setItem(i, stack.isEmpty() ? ItemStack.EMPTY : stack.copy());
        }

        // Додаємо старі речі у вільні слоти
        for (ItemStack oldStack : oldItems) {
            boolean added = false;

            // Шукаємо вільний слот
            for (int i = 0; i < 36; i++) {
                if (playerInv.getItem(i).isEmpty()) {
                    playerInv.setItem(i, oldStack);
                    added = true;
                    break;
                }
            }

            // Якщо немає місця - викидаємо під гравцем
            if (!added) {
                player.drop(oldStack, false);
            }
        }

        // Синхронізуємо
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.inventoryMenu.broadcastChanges();
        }

        playerInv.setChanged();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10D)
                .add(Attributes.MOVEMENT_SPEED, 0.0D)
                .add(Attributes.SCALE, 5.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }
    @Override
    protected void pushEntities(){}
    @Override
    public boolean isPushable() {return false;}
    @Override
    public boolean canBeCollidedWith(Entity entity){return false;}
    @Override
    public void doPush(Entity entity) {}
    @Override
    public void push(Entity entity) {}
    @Override
    protected boolean isImmobile() {return true;}
    @Override
    public boolean canCollideWith(Entity entity) {return false;}

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide()) {
            int lifeTimeSeconds = SoulShardConfig.getConfig().getCrystalLifeTime();

            if (lifeTimeSeconds >= 0) {
                lifeTicks++;
                if (lifeTicks >= lifeTimeSeconds * 20L) {
                    if (SoulShardConfig.getConfig().DespawnChatNotify()){
                        if (ownerUUID != null)
                        {
                            var Player = this.level().getPlayerByUUID(ownerUUID);
                            if (Player != null) {
                                Player.displayClientMessage(Component.literal("Your Soul Shard has despawned :(").withStyle(ChatFormatting.RED)
                                        , false);
                            }
                        }
                    }
                    this.discard();
                }
            }
        }
    }
    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        if (!SoulShardConfig.getConfig().canDie() && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return false;
        }
        return super.hurtServer(level, source, amount);
    }

    @Override
    public void die(DamageSource damageSource) {
        super.die(damageSource);

        if (SoulShardConfig.getConfig().DeadChatNotify()){
            if (ownerUUID == null) return;
            var Player = this.level().getPlayerByUUID(ownerUUID);
            if (Player != null) {
                Player.displayClientMessage(Component.literal("Your Soul Shard has died :(").withStyle(ChatFormatting.RED)
                        , false);
            }
        }
    }

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);

        if (ownerUUID != null) {
            output.store("Owner", UUIDUtil.CODEC, ownerUUID);
        }
        output.putInt("LifeTicks", lifeTicks);

        ValueOutput.TypedOutputList<ItemStackWithSlot> itemsList = output.list("Items", ItemStackWithSlot.CODEC);
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty()) {
                itemsList.add(new ItemStackWithSlot(i, stack));
            }
        }
    }

    @Override
    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);

        ownerUUID = input.read("Owner", UUIDUtil.CODEC).orElse(null);
        lifeTicks = input.getIntOr("LifeTicks", 0);

        inventory.clearContent();
        for (ItemStackWithSlot item : input.listOrEmpty("Items", ItemStackWithSlot.CODEC)) {
            if (item.isValidInContainer(inventory.getContainerSize())) {
                inventory.setItem(item.slot(), item.stack());
            }
        }
    }

    @Override
    public InteractionResult interactAt(Player player, Vec3 hitPos, InteractionHand hand)
    {
        if (!level().isClientSide() && isOwner(player)){
            setInventoryToPlayer(player);
            inventory.clearContent();
            this.discard();
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers)
    {
        controllers.add(new AnimationController<SoulShardEntity>("controller", 0, state -> {
            state.setAnimation(IDLE);
            return state.setAndContinue(IDLE);
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
