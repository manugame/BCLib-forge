package org.betterx.bclib.items;

import org.betterx.bclib.BCLib;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.RecordItem;

import java.lang.reflect.Constructor;

public class BaseDiscItem {
    public static RecordItem create(
            int comparatorOutput,
            SoundEvent sound,
            Item.Properties settings,
            int lengthInSeconds
    ) {
        for (Constructor<?> c : RecordItem.class.getDeclaredConstructors()) {
            if (c.getParameterCount() == 4) {
                var types = c.getParameterTypes();
                if (types.length == 4) { //1.19.1 Constructor
                    if (
                            types[0].isAssignableFrom(int.class)
                                    && types[1].isAssignableFrom(SoundEvent.class)
                                    && types[2].isAssignableFrom(Item.Properties.class)
                                    && types[3].isAssignableFrom(int.class)
                    ) {
                        c.setAccessible(true);
                        try {
                            return (RecordItem) c.newInstance(comparatorOutput, sound, settings, lengthInSeconds);
                        } catch (Exception e) {
                            BCLib.LOGGER.error("Failed to instantiate RecordItem", e);
                        }
                    }
                }
            } else if (c.getParameterCount() == 3) {
                var types = c.getParameterTypes();
                if (types.length == 3) { //1.19 constructor
                    if (
                            types[0].isAssignableFrom(int.class)
                                    && types[1].isAssignableFrom(SoundEvent.class)
                                    && types[2].isAssignableFrom(Item.Properties.class)
                    ) {
                        c.setAccessible(true);
                        try {
                            return (RecordItem) c.newInstance(comparatorOutput, sound, settings);
                        } catch (Exception e) {
                            BCLib.LOGGER.error("Failed to instantiate RecordItem", e);
                        }
                    }
                }
            }
        }
        BCLib.LOGGER.error("No Constructor for RecordItems found:");
        for (Constructor<?> c : RecordItem.class.getDeclaredConstructors()) {
            BCLib.LOGGER.error("    - " + c);
        }
        return null;
    }
}
