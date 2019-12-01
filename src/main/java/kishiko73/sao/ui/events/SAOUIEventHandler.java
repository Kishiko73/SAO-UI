package kishiko73.sao.ui.events;

import kishiko73.sao.ui.SAOUIMod;
import kishiko73.sao.ui.gui.SAOUIDeathScreen;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.IngameMenuScreen;
import net.minecraft.client.gui.screen.inventory.CraftingScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.screen.inventory.MerchantScreen;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SAOUIEventHandler {

    /**
     * Called when a gui is opened (null on close)
     * Separate from the hud which contains the hot-bar
     * @param event
     */
    @SubscribeEvent
    public void onGUIOpen(GuiOpenEvent event) {
        System.out.println(event.getGui());
        if (event.getGui() == null) // called in Screen#onClose
            return;

        if (SAOUIMod.uiEnabled()) {
            // TODO: replace prints with appropriate ui
            if (event.getGui() instanceof InventoryScreen) // called on creativeScreen
                System.out.println("inventory");
            else if (event.getGui() instanceof DeathScreen)
                event.setGui(new SAOUIDeathScreen());
            else if (event.getGui() instanceof CraftingScreen)
                System.out.println("crafting");
            else if (event.getGui() instanceof MerchantScreen)
                System.out.println("trading");
        } else if (event.getGui() instanceof IngameMenuScreen)
            // always shows our custom gui so options can be toggled back on
            System.out.println("menu");
    }

    /**
     * Only registers block right clicks
     * Distinction between rightclick(block,empty,item) is clicking the block or
     * clicking the air without or with an item in hand
     * Now we can make blocks show custom gui's if we wish
     * Shift escapes all behaviour
     * @param event
     */
    @SubscribeEvent
    public void onInteractBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getPlayer().isSneaking())
            return;

        Block block = event.getWorld().getBlockState(event.getPos()).getBlock();
        if (block == Blocks.NOTE_BLOCK)
            System.out.println("tuning");

    }


    /**
     * Only registers entity right clicks
     * Now we can make entities show custom gui's if we wish
     * Shift does not escape this behaviour as this is the vanilla behaviour
     * @param event
     */
    @SubscribeEvent
    public void onInteractEntity(PlayerInteractEvent.EntityInteract event) {
        System.out.println("entity");
    }
}
