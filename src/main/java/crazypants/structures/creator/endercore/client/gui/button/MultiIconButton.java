package crazypants.structures.creator.endercore.client.gui.button;

import crazypants.structures.creator.endercore.api.client.gui.IGuiScreen;
import crazypants.structures.creator.endercore.client.render.EnderWidget;
import crazypants.structures.creator.endercore.client.render.IWidgetIcon;

public class MultiIconButton extends IconButton {

    private final IWidgetIcon unpressed;
    private final IWidgetIcon pressed;
    private final IWidgetIcon hover;

    public MultiIconButton(IGuiScreen gui, int id, int x, int y, IWidgetIcon unpressed, IWidgetIcon pressed, IWidgetIcon hover) {
        super(gui, id, x, y, null);
        this.unpressed = unpressed;
        this.pressed = pressed;
        this.hover = hover;
        setSize(unpressed.getWidth(), unpressed.getHeight());
    }

    @Override
    protected IWidgetIcon getIconForHoverState(int hoverState) {
        if (hoverState == 0) {
            return pressed;
        }
        if (hoverState == 2) {
            return hover;
        }
        return unpressed;
    }

    public static MultiIconButton createRightArrowButton(IGuiScreen gui, int id, int x, int y) {
        return new MultiIconButton(gui, id, x, y, EnderWidget.RIGHT_ARROW, EnderWidget.RIGHT_ARROW_PRESSED, EnderWidget.RIGHT_ARROW_HOVER);
    }

    public static MultiIconButton createLeftArrowButton(IGuiScreen gui, int id, int x, int y) {
        return new MultiIconButton(gui, id, x, y, EnderWidget.LEFT_ARROW, EnderWidget.LEFT_ARROW_PRESSED, EnderWidget.LEFT_ARROW_HOVER);
    }

    public static MultiIconButton createAddButton(IGuiScreen gui, int id, int x, int y) {
        return new MultiIconButton(gui, id, x, y, EnderWidget.ADD_BUT, EnderWidget.ADD_BUT_PRESSED, EnderWidget.ADD_BUT_HOVER);
    }

    public static MultiIconButton createMinusButton(IGuiScreen gui, int id, int x, int y) {
        return new MultiIconButton(gui, id, x, y, EnderWidget.MINUS_BUT, EnderWidget.MINUS_BUT_PRESSED, EnderWidget.MINUS_BUT_HOVER);
    }
}
