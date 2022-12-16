package jibbs.chatclip;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Keybind;

@ConfigGroup("chatclip")
public interface ChatClipConfig extends Config {
    @ConfigItem(
            keyName = "includename",
            name = "Include Name",
            description = "Includes name in copied message"
    )
    default boolean includename() {
        return false;
    }

    @ConfigItem(
            keyName = "keybind",
            name = "Keybind",
            description = "Copy last message when keybind is pressed"
    )
    default Keybind keybind() {
        return Keybind.NOT_SET;
    }

    @ConfigItem(
            keyName = "addtohistory",
            name = "Add to history",
            description = "Windows adds to clipboard history"
    )
    default boolean addtohistory() {
        return false;
    }

    @ConfigItem(
            keyName = "copyevery",
            name = "Copy time",
            description = "How often to copy last messages seconds"
    )
    default double copyevery() {
        return 1;
    }
}