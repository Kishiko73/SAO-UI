package kishiko73.sao.ui.gui;

import com.mojang.blaze3d.platform.GlStateManager;

import kishiko73.sao.ui.SAOUIMod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SAOUIDeathScreen extends Screen {

    private final int buttonW = 80, buttonH = 32;
    private final ResourceLocation death = new ResourceLocation(SAOUIMod.MODID, "textures/death.png");
    private SAOUIButton deathButton;

    public SAOUIDeathScreen() {
        super(new TranslationTextComponent(Minecraft.getInstance().world.getWorldInfo().isHardcore() ?
                "deathScreen.title.hardcore" : "deathScreen.title"));
        System.out.println("death open");
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }

    @Override
    protected void init() {
        super.init();
        deathButton = addButton(new SAOUIButton((width >> 1) - buttonW, (height >> 1) - buttonH,
                buttonW << 1, buttonH << 1, (id) -> {
            SAOUIMod.proxy.getClientPlayer().preparePlayerToSpawn();
            SAOUIMod.proxy.getClientPlayer().respawnPlayer();
            onClose();
        }));
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground();
        deathButton.updateHover(mouseX, mouseY);
        GlStateManager.pushMatrix(); {
            GlStateManager.enableAlphaTest();
            GlStateManager.enableBlend();
            GlStateManager.color3f(deathButton.isHovered() ? 1.0F : 0.8F, 0.0F, 0.0F);
            GlStateManager.colorMask(true, false, false, true);
            Minecraft.getInstance().getTextureManager().bindTexture(death);
            blit((width >> 1) - buttonW, (height >> 1) - buttonH - (deathButton.isHovered() ? 1 : 0),
                    0, 0, buttonW << 1, buttonH << 1, buttonW << 1, buttonH << 1);
        } GlStateManager.popMatrix();
        GlStateManager.pushMatrix(); {
            final float sf = 1.6F;
            GlStateManager.scalef(sf, sf, sf);
            drawCenteredString(font, title.getString(), (int)(width / 2 / sf),
                    (int)(height / 2 / sf - (deathButton.isHovered() ? 5 : 4)), 0xFFFFFF);
        } GlStateManager.popMatrix();
    }
}
