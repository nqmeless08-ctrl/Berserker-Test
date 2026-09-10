package com.yourserver.classsystem.gui;

import com.yourserver.classsystem.ClassSystemPlugin;
import com.yourserver.classsystem.classes.PlayerClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.floodgate.api.FloodgateApi;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Sends a native Bedrock "Simple Form" (button list) for class selection.
 * Only ever called after confirming the player is a Floodgate/Bedrock player
 * (see ClassCommand), so it's safe for this class to reference Floodgate/Cumulus
 * classes directly - it will simply never be loaded on servers without Geyser.
 *
 * NOTE: Floodgate/Cumulus form APIs have changed across versions. This targets
 * Floodgate 2.2.x's Cumulus 1.1.x form builder. If your server runs a different
 * Floodgate version, the exact method chain below may need small adjustments -
 * the shape (title -> content -> buttons -> validResultHandler) has been stable
 * for a long time, but method names have shifted slightly between releases.
 */
public class BedrockClassForm {

    public static void send(Player player, List<PlayerClass> classes) {
        UUID uuid = player.getUniqueId();

        SimpleForm.Builder builder = SimpleForm.builder()
                .title("Choose Your Class")
                .content("Pick a class. This is separate from your Origin and can be changed later with /class.");

        List<PlayerClass> ordered = new ArrayList<>(classes);
        for (PlayerClass playerClass : ordered) {
            StringBuilder buttonText = new StringBuilder();
            buttonText.append(playerClass.getDisplayName()).append("\n");
            for (String stat : playerClass.getStatLines()) {
                buttonText.append(stat).append("  ");
            }
            builder.button(buttonText.toString().trim());
        }

        builder.validResultHandler(response -> {
            int index = response.clickedButtonId();
            if (index < 0 || index >= ordered.size()) return;

            PlayerClass chosen = ordered.get(index);

            // Form callbacks run off the main server thread - hop back before
            // touching Bukkit API (attributes, potion effects, PDC, etc.).
            Bukkit.getScheduler().runTask(ClassSystemPlugin.getInstance(), () -> {
                ClassSystemPlugin.getInstance().getClassManager().setClass(player, chosen);
                player.sendMessage(Component.text(
                        "You are now a " + chosen.getDisplayName() + "!", NamedTextColor.GREEN));
            });
        });

        FloodgateApi.getInstance().sendForm(uuid, builder);
    }

    public static boolean isBedrockPlayer(Player player) {
        try {
            return FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId());
        } catch (Throwable t) {
            // Floodgate not installed - treat everyone as Java.
            return false;
        }
    }
}
