package crazypants.structures.creator.endercore.client.gui.button;

import java.util.List;

import crazypants.structures.creator.endercore.api.client.gui.IGuiScreen;
import crazypants.structures.creator.endercore.client.gui.button.CycleButton.ICycleEnum;
import crazypants.structures.creator.endercore.client.render.IWidgetIcon;
import net.minecraft.client.Minecraft;

/**
 * A button which automatically parses enum constants and cycles between them when clicked.
 *
 * @param <T>
 *            The enum type for this button.
 */
public class CycleButton<T extends Enum<T> & ICycleEnum> extends IconButton {

    public interface ICycleEnum {

        /**
         * @return The icon to display when the button has selected this mode.
         */
        IWidgetIcon getIcon();

        /**
         * @return Localized tooltip lines.
         */
        List<String> getTooltipLines();
    }

    private final T[] modes;
    
    private T mode;

    public CycleButton(IGuiScreen gui, int id, int x, int y, Class<T> enumClass) {
        super(gui, id, x, y, null);
        modes = enumClass.getEnumConstants();
        setMode(modes[0]);
    }

    @Override
    public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3) {
        boolean result = super.mousePressed(par1Minecraft, par2, par3);
        if (result) {
            nextMode();
        }
        return result;
    }

    @Override
    public boolean mousePressedButton(Minecraft mc, int x, int y, int button) {
        boolean result = button == 1 && super.checkMousePress(mc, x, y);
        if (result) {
            prevMode();
        }
        return result;
    }

    private void nextMode() {
        setMode(modes[(mode.ordinal() + 1) % modes.length]);
    }

    private void prevMode() {
        int ord = mode.ordinal() - 1;
        if (ord < 0) {
            ord = modes.length - 1;
        }
        setMode(modes[ord]);
    }

    public void setMode(T newMode) {
        if (mode == newMode) {
            return;
        }
        mode = newMode;
        List<String> tooltip = mode.getTooltipLines();
        setToolTip(tooltip.toArray(new String[tooltip.size()]));

        icon = mode.getIcon();
    }
}
