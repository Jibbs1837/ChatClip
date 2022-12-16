package jibbs.chatclip;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.config.Keybind;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.HotkeyListener;

import javax.inject.Inject;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;

@Slf4j
@PluginDescriptor(
        name = "ChatClip",
        description = "Copy chat messages to clipboard",
        tags = {"chat", "clipboard", "copy"}
)
public class ChatClipPlugin extends Plugin {
    @Inject
    private Client client;

    @Inject
    private ChatClipConfig config;

    @Provides
    ChatClipConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(ChatClipConfig.class);
    }

    @Subscribe
    public void onChatMessage(ChatMessage event) {
        if (config.keybind() == Keybind.NOT_SET) {
            lastMessage = event.getMessage();
            lastPerson = event.getSender();
            return;
        }

        String string = "";
        if (config.includename() == true) {
            string = event.getSender() + " says " + event.getMessage();
        } else {
            string = event.getMessage();
        }

        exec(string);
    }

    public long last = 0;
    public String lastMessage = "";
    public String lastPerson = "";

    public void exec(String m) {
        if ((config.keybind() == Keybind.NOT_SET) == false) {
            if (Math.abs(last - System.currentTimeMillis()) <= config.copyevery() * (double)1000) {
                return;
            }

            last = System.currentTimeMillis();
        }

        try {
            if (config.addtohistory() == false) {
                StringSelection stringSelection = new StringSelection(m);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
            } else {
                new ProcessBuilder("cmd", "/c", "echo " + m + "|clip").start();
            }
        } catch (IOException ioException) {

        }
    }

    private final HotkeyListener hotkeyListener = new HotkeyListener(() -> this.config.keybind()) {
        @Override
        public void hotkeyPressed() {
            String string = "";
            if (config.includename() == true) {
                string = lastPerson + " says " + lastMessage;
            } else {
                string = lastMessage;
            }

            exec(string);
        }
    };
}