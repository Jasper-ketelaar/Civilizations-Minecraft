package org.macroprod.civilization.util;

import net.minecraft.server.v1_11_R1.EntityTypes;
import net.minecraft.server.v1_11_R1.EntityVillager;
import net.minecraft.server.v1_11_R1.MinecraftKey;
import net.minecraft.server.v1_11_R1.RegistryMaterials;
import org.bukkit.entity.EntityType;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.macroprod.civilization.resident.types.Miner;
import org.macroprod.civilization.resident.types.Settler;

import java.lang.reflect.Field;
import java.util.Set;

/**
 * Takes care of registring and unregistring entities.
 */
public enum CustomEntities {

    /**
     * Replace the default Minecraft civilization with our simpleton settlers
     */
    SETTLER("Settler", 121, EntityType.VILLAGER, EntityVillager.class, Settler.class),
    MINER("Miner", 122, EntityType.VILLAGER, EntityVillager.class, Miner.class);

    private String name;
    private int id;
    private EntityType entityType;
    private Class<?> nmsClass;
    private Class<?> customClass;
    private MinecraftKey key;
    private MinecraftKey oldKey;

    /**
     * Constructor for enum
     *
     * @param name        entity name; refer to {@link EntityTypes} for proper names
     * @param id          entity id; refer to {@link EntityTypes} for proper ids
     * @param entityType  type of entity
     * @param nmsClass    original minecraft server class
     * @param customClass custom entity class
     */
    CustomEntities(String name, int id, EntityType entityType, Class<?> nmsClass, Class<?> customClass) {
        this.name = name;
        this.id = id;
        this.entityType = entityType;
        this.nmsClass = nmsClass;
        this.customClass = customClass;
        this.key = new MinecraftKey(name);
        this.oldKey = ((RegistryMaterials<MinecraftKey, Class<?>>) getPrivateStatic(EntityTypes.class, "b")).b(nmsClass);
    }

    /**
     * Registers all entities in our enum
     */
    public static void registerEntities() {
        for (CustomEntities ce : CustomEntities.values()) ce.register();
    }

    /**
     * Unregisters all entities in our enum
     */
    public static void unregisterEntities() {
        for (CustomEntities ce : CustomEntities.values()) ce.unregister();
    }

    /**
     * Returns private static field content
     */
    private Object getPrivateStatic(final Class<?> clazz, final String f) {
        try {
            Field field = clazz.getDeclaredField(f);
            field.setAccessible(true);
            return field.get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Register our key
     */
    private void register() {
        ((Set<MinecraftKey>) getPrivateStatic(EntityTypes.class, "d")).add(key);
        ((RegistryMaterials<MinecraftKey, Class<?>>) getPrivateStatic(EntityTypes.class, "b")).a(id, key, customClass);
    }

    /**
     * Unregister our key and replace with default
     */
    private void unregister() {
        ((Set<MinecraftKey>) getPrivateStatic(EntityTypes.class, "d")).remove(key);
        ((RegistryMaterials<MinecraftKey, Class<?>>) getPrivateStatic(EntityTypes.class, "b")).a(id, oldKey, nmsClass);
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return id;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public Class<?> getCustomClass() {
        return customClass;
    }
}