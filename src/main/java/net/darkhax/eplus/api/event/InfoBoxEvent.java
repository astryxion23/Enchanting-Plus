package net.darkhax.eplus.api.event;

import java.util.List;

import net.darkhax.eplus.gui.GuiAdvancedTable;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public class InfoBoxEvent {

    public static final Event<InfoBoxCallback> EVENT = EventFactory.createArrayBacked(InfoBoxCallback.class, callbacks -> (gui, info) -> {
        for (InfoBoxCallback callback : callbacks) callback.onInfoBox(gui, info);
    });

    @FunctionalInterface
    public interface InfoBoxCallback {
        void onInfoBox(GuiAdvancedTable gui, List<String> info);
    }

    private final GuiAdvancedTable gui;
    private final List<String> info;

    public InfoBoxEvent(GuiAdvancedTable gui, List<String> info) {
        this.gui = gui;
        this.info = info;
    }

    public GuiAdvancedTable getGui() { return gui; }
    public List<String> getInfo() { return info; }
}
