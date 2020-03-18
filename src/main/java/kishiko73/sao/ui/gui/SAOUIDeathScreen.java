package kishiko73.sao.ui.gui;

import kishiko73.sao.ui.KeyBinds;
import kishiko73.sao.ui.SAOUIMod;
import kishiko73.sao.ui.proxy.ClientProxy;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SAOUIDeathScreen extends Screen {

    private final int buttonW = 160, buttonH = 64;
    private final ResourceLocation death = new ResourceLocation(SAOUIMod.MODID, "textures/death.png");

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
        addButton(new Button(width - buttonW >> 1, height - buttonH >> 1, buttonW, buttonH, "",
                id -> respawn()) {
            @Override
            public void renderButton(int mouseX, int mouseY, float partialTicks) {
                Minecraft.getInstance().getTextureManager().bindTexture(death);

                RenderSystem.pushMatrix();
                    RenderSystem.colorMask(true, false, false, true);
                    if (isHovered) {
                        RenderSystem.color4f(1.0F, 0.0F, 0.0F, 0.3F);
                        blit(x, y - 1, 0, 0, buttonW, buttonH, buttonW, buttonH);
                    } else {
                        RenderSystem.color4f(0.8F, 0.0F, 0.0F, 0.3F);
                        blit(x, y, 0, 0, buttonW, buttonH, buttonW, buttonH);
                    }
                RenderSystem.popMatrix();

                RenderSystem.pushMatrix();
                    final float sf = 1.6F;
                    RenderSystem.scalef(sf, sf, sf);
                    drawCenteredString(font, title.getString(), (int)((x + (width >> 1)) / sf),
                            (int)((y + (height >> 1)) / sf) - (isHovered ? 5 : 4), 0xFFFFFF);
                    RenderSystem.colorMask(true, true, true, true);
                RenderSystem.popMatrix();
            }
        });
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        drawCenteredString(font, I18n.format("deathScreen.score") + ": " +
                TextFormatting.YELLOW + Integer.toString(SAOUIMod.proxy.getClientPlayer().getScore()),
                width >> 1, (height - buttonH >> 1) - 16,
                0xFFFFFF);
        drawCenteredString(font, I18n.format("gui.kiko-sao-ui.respawn_keybind",
                I18n.format(ClientProxy.keyBindings[0].getTranslationKey())),
                width >> 1, (height + buttonH >> 1) + 16,
                0xFFFFFF);
    }

    @Override
    public boolean keyPressed(int keyCode, int p_keyPressed_2_, int p_keyPressed_3_) {
        if (keyCode == KeyBinds.RESPAWN.getKeyCode())
            respawn();
        return super.keyPressed(keyCode, p_keyPressed_2_, p_keyPressed_3_);
    }
}
