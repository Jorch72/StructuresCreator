package crazypants.structures.creator.endercore.client.gui.button;

import crazypants.structures.creator.endercore.api.client.gui.IHideable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiButtonHideable extends GuiButton implements IHideable {

    public GuiButtonHideable(int buttonId, int x, int y, String buttonText) {
        super(buttonId, x, y, buttonText);
    }

    public GuiButtonHideable(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
    }

    @Override
    public void setIsVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    protected int getHoverState(boolean mouseOver) {
        int b = super.getHoverState(mouseOver);
        if (!isEnabled()) {
            b = 0;
        }
        return b;
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return isEnabled() && super.mousePressed(mc, mouseX, mouseY);
    }
}
