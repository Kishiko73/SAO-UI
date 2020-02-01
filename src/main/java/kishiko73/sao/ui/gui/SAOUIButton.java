package kishiko73.sao.ui.gui;

import net.minecraft.client.gui.widget.button.Button;

/**
 * Class used purely for the clickable hit box and hover event, not for render
 */

public class SAOUIButton extends Button {

    public SAOUIButton(int x, int y, IPressable handler) {
        super(x, y, 20, 20, "", handler);
    }
    public SAOUIButton(int x, int y, int w, int h, IPressable handler) {
        super(x, y, w, h, "", handler);
    }
    public SAOUIButton(int x, int y, int w, int h, String text, IPressable handler) {
        super(x, y, w, h, text, handler);
    }

    public void updateHover(int mouseX, int mouseY) {
        isHovered = visible && (x <= mouseX && mouseX <= x + width &&
                                y <= mouseY && mouseY <= y + height);
    }
}
