package com.yourserver.classsystem.classes;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.NamespacedKey;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

/**
 * Berserker: a blood-hungry warrior that doesn't think twice before
 * stepping into battle.
 *
 * +3 Attack Damage
 * -2 Max Health
 * Permanent Speed I
 * Cannot use a shield
 */
public class BerserkerClass implements PlayerClass {

    // Fixed, stable keys for the attribute modifiers this class applies.
    // Using a fixed NamespacedKey (not a random UUID) means we can always
    // find-and-remove our own modifier later, even after a server restart,
    // without accidentally touching modifiers from other plugins.
    private static final NamespacedKey ATTACK_DAMAGE_KEY =
            new NamespacedKey("classsystem", "berserker_attack_damage");
    private static final NamespacedKey MAX_HEALTH_KEY =
            new NamespacedKey("classsystem", "berserker_max_health");

    private static final double ATTACK_DAMAGE_BONUS = 3.0;
    private static final double MAX_HEALTH_PENALTY = -2.0;

    @Override
    public String getId() {
        return "berserker";
    }

    @Override
    public String getDisplayName() {
        return "Berserker";
    }

    @Override
    public Material getIcon() {
        return Material.IRON_SWORD;
    }

    @Override
    public String getDescription() {
        return "A blood-hungry warrior that doesn't think twice before stepping into battle.";
    }

    @Override
    public List<String> getStatLines() {
        return List.of(
                "+3 Attack Damage",
                "-2 Max Health",
                "Permanent Speed I",
                "Cannot use a Shield"
        );
    }

    @Override
    public boolean blocksShieldUse() {
        return true;
    }

    @Override
    public void apply(Player player) {
        applyModifier(player, Attribute.GENERIC_ATTACK_DAMAGE, ATTACK_DAMAGE_KEY, ATTACK_DAMAGE_BONUS);
        applyModifier(player, Attribute.GENERIC_MAX_HEALTH, MAX_HEALTH_KEY, MAX_HEALTH_PENALTY);

        // Clamp current health down if the max-health reduction put it above the new cap.
        AttributeInstance maxHealthAttr = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealthAttr != null && player.getHealth() > maxHealthAttr.getValue()) {
            player.setHealth(maxHealthAttr.getValue());
        }

        // Permanent Speed I: ambient (no swirling particles spam) and not shown as an icon-only
        // debuff-looking effect. Duration is re-applied on respawn by PlayerRespawnListener since
        // potion effects are cleared on death.
        player.addPotionEffect(new PotionEffect(
                PotionEffectType.SPEED,
                PotionEffect.INFINITE_DURATION,
                0,      // amplifier 0 = Speed I
                true,   // ambient
                false,  // particles
                true    // icon
        ));
    }

    @Override
    public void remove(Player player) {
        removeModifier(player, Attribute.GENERIC_ATTACK_DAMAGE, ATTACK_DAMAGE_KEY);
        removeModifier(player, Attribute.GENERIC_MAX_HEALTH, MAX_HEALTH_KEY);
        player.removePotionEffect(PotionEffectType.SPEED);
    }

    private void applyModifier(Player player, Attribute attribute, NamespacedKey key, double amount) {
        AttributeInstance instance = player.getAttribute(attribute);
        if (instance == null) return;

        // Remove any existing copy first so re-applying (e.g. on join) never stacks.
        instance.getModifiers().stream()
                .filter(mod -> mod.getKey() != null && mod.getKey().equals(key))
                .toList()
                .forEach(instance::removeModifier);

        AttributeModifier modifier = new AttributeModifier(
                key,
                amount,
                AttributeModifier.Operation.ADD_NUMBER
        );
        instance.addModifier(modifier);
    }

    private void removeModifier(Player player, Attribute attribute, NamespacedKey key) {
        AttributeInstance instance = player.getAttribute(attribute);
        if (instance == null) return;

        instance.getModifiers().stream()
                .filter(mod -> mod.getKey() != null && mod.getKey().equals(key))
                .toList()
                .forEach(instance::removeModifier);
    }
}
