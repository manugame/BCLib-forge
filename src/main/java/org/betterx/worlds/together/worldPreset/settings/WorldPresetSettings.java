package org.betterx.worlds.together.worldPreset.settings;

import org.betterx.worlds.together.WorldsTogether;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.levelgen.WorldGenSettings;

import java.util.Set;
import java.util.function.Function;

public abstract class WorldPresetSettings {
    public static WorldPresetSettings DEFAULT = VanillaWorldPresetSettings.DEFAULT;
    public static final ResourceKey<Registry<Codec<? extends WorldPresetSettings>>> WORLD_PRESET_SETTINGS_REGISTRY =
            createRegistryKey(WorldsTogether.makeID("worldgen/world_preset_settings"));

    public static final Registry<Codec<? extends WorldPresetSettings>> WORLD_PRESET_SETTINGS =
            registerSimple(WORLD_PRESET_SETTINGS_REGISTRY);

    public static final Codec<WorldPresetSettings> CODEC = WORLD_PRESET_SETTINGS
            .byNameCodec()
            .dispatchStable(WorldPresetSettings::codec, Function.identity());


    private static <T> ResourceKey<Registry<T>> createRegistryKey(ResourceLocation location) {

        return ResourceKey.createRegistryKey(location);
    }

    private static <T> Registry<T> registerSimple(ResourceKey<? extends Registry<T>> resourceKey) {
        return new MappedRegistry<>(resourceKey, Lifecycle.stable(), null);
    }

    public static Codec<? extends WorldPresetSettings> register(
            ResourceLocation loc,
            Codec<? extends WorldPresetSettings> codec
    ) {
        return Registry.register(WORLD_PRESET_SETTINGS, loc, codec);
    }

    public static void bootstrap() {
        register(WorldsTogether.makeID("vanilla_world_preset_settings"), VanillaWorldPresetSettings.CODEC);
    }

    public abstract Codec<? extends WorldPresetSettings> codec();
    public abstract WorldGenSettings repairSettingsOnLoad(RegistryAccess registryAccess, WorldGenSettings settings);
    public abstract BiomeSource fixBiomeSource(BiomeSource biomeSource, Set<Holder<Biome>> datapackBiomes);
}