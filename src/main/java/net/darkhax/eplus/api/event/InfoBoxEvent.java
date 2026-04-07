package net.darkhax.eplus.api.event;

import java.util.List;

import net.darkhax.eplus.gui.GuiAdvancedTable;
import net.neoforged.bus.api.Event;

public class InfoBoxEvent extends Event {

    private final GuiAdvancedTable gui;
    private final List<String> info;

    public InfoBoxEvent(GuiAdvancedTable gui, List<String> info) {
        this.gui = gui;
        this.info = info;
    }

    public GuiAdvancedTable getGui() { return gui; }
    public List<String> getInfo() { return info; }
}
