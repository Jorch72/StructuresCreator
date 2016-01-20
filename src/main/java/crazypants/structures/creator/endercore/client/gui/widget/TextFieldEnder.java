package crazypants.structures.creator.endercore.client.gui.widget;

import java.lang.reflect.Field;

import com.google.common.base.Strings;

import crazypants.structures.creator.endercore.api.client.gui.IGuiScreen;
import crazypants.structures.creator.endercore.api.client.gui.IHideable;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class TextFieldEnder extends GuiTextField implements IHideable {

    public interface ICharFilter {

        boolean passesFilter(TextFieldEnder tf, char c);
    }

    public static final ICharFilter FILTER_NUMERIC = new ICharFilter() {

        @Override
        public boolean passesFilter(TextFieldEnder tf, char c) {
            return Character.isDigit(c) || c == '-' && Strings.isNullOrEmpty(tf.getText());
        }
    };

    public static ICharFilter FILTER_ALPHABETICAL = new ICharFilter() {

        @Override
        public boolean passesFilter(TextFieldEnder tf, char c) {
            return Character.isLetter(c);
        }
    };

    public static ICharFilter FILTER_ALPHANUMERIC = new ICharFilter() {

        @Override
        public boolean passesFilter(TextFieldEnder tf, char c) {
            return FILTER_NUMERIC.passesFilter(tf, c) || FILTER_ALPHABETICAL.passesFilter(tf, c);
        }
    };

    private final int xOrigin;
    private final int yOrigin;
    private ICharFilter filter;

    private static Field canLoseFocus;

    static {
        try {
            canLoseFocus = ReflectionHelper.findField(GuiTextField.class, "canLoseFocus", "field_146212_n", "n");
            canLoseFocus.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public TextFieldEnder(FontRenderer fnt, int x, int y, int width, int height) {
        this(fnt, x, y, width, height, null);
    }

    public TextFieldEnder(FontRenderer fnt, int x, int y, int width, int height, ICharFilter charFilter) {
        super(0, fnt, x, y, width, height);
        xOrigin = x;
        yOrigin = y;
        filter = charFilter;
    }

    public void init(IGuiScreen gui) {
        xPosition = xOrigin + gui.getGuiLeft();
        yPosition = yOrigin + gui.getGuiTop();
    }

    public TextFieldEnder setCharFilter(ICharFilter filter) {
        this.filter = filter;
        return this;
    }

    @Override
    public boolean textboxKeyTyped(char c, int key) {
        if (filter == null || filter.passesFilter(this, c) || isSpecialChar(c, key)) {
            return super.textboxKeyTyped(c, key);
        }
        return false;
    }

    public static boolean isSpecialChar(char c, int key) {
        // taken from the giant switch statement in GuiTextField
        return c == 1 || c == 3 || c == 22 || c == 24 || key == 14 || key == 199 || key == 203 || key == 205 || key == 207 || key == 211;
    }

    public boolean getCanLoseFocus() {
        try {
            return canLoseFocus.getBoolean(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean contains(int x, int y) {
        return x >= xPosition && x < xPosition + width && y >= yPosition && y < yPosition + height;
    }

    @Override
    public boolean isVisible() {
        return getVisible();
    }

    @Override
    public void setIsVisible(boolean visible) {
        setVisible(visible);
    }
}
