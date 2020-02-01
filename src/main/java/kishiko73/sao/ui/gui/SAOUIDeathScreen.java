package kishiko73.sao.ui.gui;

import kishiko73.sao.ui.KeyBinds;
import kishiko73.sao.ui.SAOUIMod;
import kishiko73.sao.ui.proxy.ClientProxy;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SAOUIDeathScreen extends Screen {

    private final int buttonW = 160, buttonH = 64;
    private final ResourceLocation death = new ResourceLocation(SAOUIMod.MODID, "textures/death.png");
    private SAOUIButton deathButton;

    public SAOUIDeathScreen() {
        super(new TranslationTextComponent(Minecraft.getInstance().world.getWorldInfo().isHardcore() ?
                "deathScreen.title.hardcore" : "deathScreen.title"));
    }

    @Override // defaults to true
    public boolean isPauseScreen() {
        return false;
    }

    private void respawn() {
        SAOUIMod.proxy.getClientPlayer().preparePlayerToSpawn();
        SAOUIMod.proxy.getClientPlayer().respawnPlayer();
        onClose();
    }

    @Override
    protected void init() {
        super.init();
        deathButton = addButton(new SAOUIButton(width - buttonW >> 1, height - buttonH >> 1, buttonW, buttonH,
                id -> respawn()));
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground();
        deathButton.updateHover(mouseX, mouseY);
        drawCenteredString(font, I18n.format("deathScreen.score") + ": " +
                TextFormatting.YELLOW + Integer.toString(SAOUIMod.proxy.getClientPlayer().getScore()),
                width >> 1, (height - buttonH >> 1) - 16,
                0xFFFFFF);
        drawCenteredString(font, I18n.format("gui.kiko-sao-ui.respawn_keybind",
                I18n.format(InputMappings.getInputByCode(ClientProxy.keyBindings[0].getKey().getKeyCode(), -1)
                        .getTranslationKey())),
                width >> 1, (height + buttonH >> 1) + 16,
                0xFFFFFF);
        GlStateManager.pushMatrix(); {
            GlStateManager.colorMask(true, false, false, true);
            Minecraft.getInstance().getTextureManager().bindTexture(death);
            if (deathButton.isHovered()) {
                GlStateManager.color3f(1.0F, 0.0F, 0.0F);
                blit(width - buttonW >> 1, (height - buttonH >> 1) - 1,
                        0, 0, buttonW, buttonH, buttonW, buttonH);
            } else {
                GlStateManager.color3f(0.8F, 0.0F, 0.0F);
                blit(width - buttonW >> 1, height - buttonH >> 1,
                        0, 0, buttonW, buttonH, buttonW, buttonH);
            }
        } GlStateManager.popMatrix();
        GlStateManager.pushMatrix(); {
            final float sf = 1.6F;
            GlStateManager.scalef(sf, sf, sf);
            drawCenteredString(font, title.getString(), (int)(width / 2 / sf),
                    (int)(height / 2 / sf - (deathButton.isHovered() ? 5 : 4)), 0xFFFFFF);
        } GlStateManager.popMatrix();
    }



    @Override
    public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
        //System.out.println(p_keyPressed_1_ + " " + p_keyPressed_2_ + " " + p_keyPressed_3_);
        if (p_keyPressed_1_ == KeyBinds.RESPAWN.getKeyCode())
            respawn();
        return super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
    }
}
