package kishiko73.sao.ui.events;

import kishiko73.sao.ui.SAOUIMod;
import kishiko73.sao.ui.gui.*;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.NoteBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.IngameMenuScreen;
import net.minecraft.client.gui.screen.inventory.CraftingScreen;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.screen.inventory.MerchantScreen;
import net.minecraft.util.math.BlockPos;

import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
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
            if (event.getGui() instanceof CreativeScreen)
                System.out.println("creative");
            else if (event.getGui() instanceof InventoryScreen) // called on creativeScreen
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
     * Distinction between rightClick(block, empty, item) is clicking the block or
     * clicking the air with or without an item in hand
     * Now we can make blocks show custom gui's if we wish
     * Shift escapes all behaviour
     * @param event
     */
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onInteractBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!SAOUIMod.proxy.getClientWorld().isRemote)
            event.setCanceled(true); // disable gui on servers till we learn how to packet
        if (event.isCanceled())
            return;

        if (event.getPlayer().isShiftKeyDown())
            return;

        BlockPos pos = event.getPos();
        BlockState state = event.getWorld().getBlockState(pos);
        Block block = state.getBlock();

        if (block instanceof NoteBlock) {
            Minecraft.getInstance().displayGuiScreen(new SAOUINoteBlockScreen(event.getWorld(), pos, state));
            event.setCanceled(true);
        }
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

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Pre event) {
        if (Minecraft.getInstance().currentScreen instanceof SAOUIDeathScreen &&
                event.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
            event.setCanceled(true);
        }
    }
}
