package me.konoplanyy.soulshard.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import me.konoplanyy.soulshard.data.ShardRecord;
import me.konoplanyy.soulshard.data.ShardStorage;
import me.konoplanyy.soulshard.entity.SoulShardEntity;
import me.konoplanyy.soulshard.registry.ModEntities;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class SoulShardCommands {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter
            .ofPattern("dd.MM.yyyy HH:mm:ss")
            .withZone(ZoneId.systemDefault());

    private SoulShardCommands() {}

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("SoulShard")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("list")
                        .executes(SoulShardCommands::listShards))
                .then(Commands.literal("restore")
                        .then(Commands.argument("id", StringArgumentType.string())
                                .suggests(SoulShardCommands::suggestShardIds)
                                .executes(SoulShardCommands::restoreShard))));
    }

    private static int listShards(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        ServerPlayer player = source.getPlayerOrException();
        ServerLevel level = source.getLevel();

        List<ShardRecord> Shards = ShardStorage.get(level).getShardsByOwner(player.getUUID());

        if (Shards.isEmpty()) {
            source.sendSuccess(() -> Component.literal("У тебе немає збережених могилок."), false);
            return 0;
        }

        for (ShardRecord Shard : Shards) {
            MutableComponent idComponent = createCopyableComponent(Shard.id().toString());
            MutableComponent dateComponent = createCopyableComponent(DATE_FORMAT.format(Instant.ofEpochMilli(Shard.createdAt())));

            Component line = Component.literal("ID: ").append(idComponent)
                    .append(Component.literal("  Час: ")).append(dateComponent);

            source.sendSuccess(() -> line, false);
        }

        return Shards.size();
    }

    private static CompletableFuture<Suggestions> suggestShardIds(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        ServerLevel level = context.getSource().getLevel();

        List<ShardRecord> shards = ShardStorage.get(level).getShardsByOwner(player.getUUID());

        for (ShardRecord shard : shards) {
            builder.suggest(shard.id().toString(), Component.literal(DATE_FORMAT.format(Instant.ofEpochMilli(shard.createdAt()))));
        }

        return builder.buildFuture();
    }

    private static int restoreShard(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        ServerPlayer player = source.getPlayerOrException();
        ServerLevel level = source.getLevel();

        String rawId = StringArgumentType.getString(context, "id");
        UUID ShardId;
        try {
            ShardId = UUID.fromString(rawId);
        } catch (IllegalArgumentException e) {
            source.sendFailure(Component.literal("Невірний формат ID."));
            return 0;
        }

        ShardStorage storage = ShardStorage.get(level);
        ShardRecord Shard = storage.getShard(ShardId);

        if (Shard == null) {
            source.sendFailure(Component.literal("Могилку з таким ID не знайдено."));
            return 0;
        }

        SoulShardEntity soulShard = new SoulShardEntity(ModEntities.SOUL_SHARD.get(), level);
        for (int i = 0; i < Shard.items().size(); i++) {
            soulShard.getInventory().setItem(i, Shard.items().get(i));
        }

        soulShard.setInventoryToPlayer(player);

        //storage.removeShard(ShardId);
        source.sendSuccess(() -> Component.literal("Могилку відновлено."), false);

        return 1;
    }

    private static MutableComponent createCopyableComponent(String value) {
        return Component.literal(value).setStyle(Style.EMPTY
                .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, value))
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Натисни щоб скопіювати"))));
    }
}