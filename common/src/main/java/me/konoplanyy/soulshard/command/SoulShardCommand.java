package me.konoplanyy.soulshard.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import me.konoplanyy.soulshard.data.InventoryHistorySavedData;
import me.konoplanyy.soulshard.data.InventorySnapshot;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SoulShardCommand {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").withZone(ZoneId.systemDefault());

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("SoulShard")
                .then(Commands.literal("restore")
                        .then(Commands.argument("index", IntegerArgumentType.integer(1, 5))
                                .suggests(SoulShardCommand::suggestSnapshots)
                                .executes(ctx -> restore(ctx, IntegerArgumentType.getInteger(ctx, "index")))
                        )
                )
        );
    }

    private static CompletableFuture<Suggestions> suggestSnapshots(
            CommandContext<CommandSourceStack> ctx,
            SuggestionsBuilder builder
    ) throws CommandSyntaxException {
        ServerPlayer player = ctx.getSource().getPlayerOrException();
        List<InventorySnapshot> snapshots = InventoryHistorySavedData
                .get(player.serverLevel())
                .getSnapshots(player.getUUID());

        for (int i = 0; i < snapshots.size(); i++) {
            InventorySnapshot snapshot = snapshots.get(i);
            long itemCount = snapshot.items().stream().filter(s -> !s.isEmpty()).count();
            String tooltip = FORMATTER.format(Instant.ofEpochMilli(snapshot.timestamp()))
                    + " - " + itemCount + " предметів";
            builder.suggest(i + 1, Component.literal(tooltip));
        }

        return builder.buildFuture();
    }

    private static int restore(CommandContext<CommandSourceStack> ctx, int index) throws CommandSyntaxException {
        ServerPlayer player = ctx.getSource().getPlayerOrException();
        List<InventorySnapshot> snapshots = InventoryHistorySavedData
                .get(player.serverLevel())
                .getSnapshots(player.getUUID());

        if (index > snapshots.size()) {
            ctx.getSource().sendFailure(Component.literal("Снапшот #" + index + " не існує."));
            return 0;
        }

        InventorySnapshot snapshot = snapshots.get(index - 1);
        applySnapshot(player, snapshot);

        ctx.getSource().sendSuccess(
                () -> Component.literal("Інвентар відновлено зі снапшота #" + index),
                false
        );
        return 1;
    }

    private static void applySnapshot(ServerPlayer player, InventorySnapshot snapshot) {
        var inv = player.getInventory();
        List<ItemStack> items = snapshot.items();

        for (int i = 0; i < 36; i++)
            inv.items.set(i, items.get(i).copy());
        for (int i = 0; i < 4; i++)
            inv.armor.set(i, items.get(36 + i).copy());
        inv.offhand.set(0, items.get(40).copy());

        player.inventoryMenu.broadcastChanges();
        inv.setChanged();
    }
}