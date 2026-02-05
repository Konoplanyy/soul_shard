package me.konoplanyy.soulshard.entity;

import net.minecraft.server.level.ServerLevel;
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
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.UUID;

public class SoulShardEntity extends PathfinderMob implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");

    private final SimpleContainer inventory = new SimpleContainer(41);

    private UUID ownerUUID;

    public SoulShardEntity(EntityType<? extends PathfinderMob> type, Level level){
        super(type, level);
    }

    public SimpleContainer getInventory(){
        return inventory;
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public boolean isOwner(Player player) {
        return this.ownerUUID == player.getUUID();
    }

    public void setOwner(Player player){
        ownerUUID = player.getUUID();
    }

    @Override
    public void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit){
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);

        for (int i = 0; i < inventory.getContainerSize(); i++){
            ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty()){
                this.spawnAtLocation(stack);
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

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    protected void pushEntities(){}
    @Override
    public boolean isPushable() {return false;}
    @Override
    public boolean canBeCollidedWith(){return false;}
    @Override
    public void doPush(Entity entity) {}
    @Override
    public void push(Entity entity) {}
    @Override
    protected boolean isImmobile() {return true;}
    @Override
    public boolean canCollideWith(Entity entity) {return false;}

    @Override
    public InteractionResult interactAt(Player player, Vec3 hitPos, InteractionHand hand)
    {
        if (!level().isClientSide && isOwner(player)){
            this.kill();
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers)
    {
        controllers.add(new AnimationController<>(this, "controller", 0, state -> {
            state.setAnimation(IDLE);
            return state.setAndContinue(IDLE);
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
