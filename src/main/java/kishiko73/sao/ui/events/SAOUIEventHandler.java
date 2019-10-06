package kishiko73.sao.ui.events;

import kishiko73.sao.ui.SAOUIMod;
import kishiko73.sao.ui.gui.SAOUIDeathScreen;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.IngameMenuScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SAOUIEventHandler {

    @SubscribeEvent
    public void onGUIOpen(GuiOpenEvent event) {
        if (event.getGui() == null)
            return;

        if (SAOUIMod.uiEnabled()) {
            if (event.getGui() instanceof InventoryScreen)
                System.out.println("inv"); // TODO: event.setGui(new SAOUIMenuScreen()); - open on inventory icon
            else if (event.getGui() instanceof DeathScreen)
                event.setGui(new SAOUIDeathScreen());
        } else if (event.getGui() instanceof IngameMenuScreen)
            // always shows our custom gui so options can be toggled back on
            System.out.println("menu"); // TODO: event.setGui(new SAOUIMenuScreen());
    }
}
