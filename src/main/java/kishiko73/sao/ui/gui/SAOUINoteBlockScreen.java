package kishiko73.sao.ui.gui;

import kishiko73.sao.ui.SAOUIMod;
import kishiko73.sao.ui.utils.Pair;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SAOUINoteBlockScreen extends Screen {

    private World world;
    private BlockPos pos;
    private BlockState state;
    private final ResourceLocation keyboard = new ResourceLocation(SAOUIMod.MODID, "textures/keyboard.png");

    private int mouseXBuff, mouseYBuff;

    public SAOUINoteBlockScreen(World world, BlockPos pos, BlockState state) {
        super(new TranslationTextComponent("gui.kiko-sao-ui.noteBlockScreen.title"));
        this.world = world;
        this.pos = pos;
        this.state = state;
    }

    @Override // defaults to true
    public boolean isPauseScreen() {
        return false;
    }

    private void tune(int note) {
        world.setBlockState(pos, state.with(BlockStateProperties.NOTE_0_24, note), 3);

        world.playSound(null, pos,
                state.get(BlockStateProperties.NOTE_BLOCK_INSTRUMENT).getSound(),
                SoundCategory.RECORDS, 3.0F, (float)Math.pow(2.0D, (double)(note - 12) / 12.0D));
        world.addParticle(ParticleTypes.NOTE,
                (double)pos.getX() + 0.5D, (double)pos.getY() + 1.2D, (double)pos.getZ() + 0.5D,
                (double)note / 24.0D, 0.0D, 0.0D);
    }

    @Override
    protected void init() {
        super.init();
        final int keyboardW = 295, keyboardH = 85;
        addButton(new Button(width - keyboardW >> 1, height - keyboardH >> 1, keyboardW, keyboardH, "",
                id -> {
                    Key key = Key.collidingKey(
                            mouseXBuff - (width -  keyboardW >> 1),
                            mouseYBuff - (height - keyboardH >> 1)
                    );
                    if (key != null)
                        tune(key.ordinal());
                    onClose();
                }) {
            @Override
            public void renderButton(int mouseX, int mouseY, float partialTicks) {
                Minecraft.getInstance().getTextureManager().bindTexture(keyboard);

                RenderSystem.pushMatrix();
                    mouseXBuff = mouseX;
                    mouseYBuff = mouseY;
                    blit(x, y, 0, 0, keyboardW, keyboardH, keyboardW, keyboardH);
                RenderSystem.popMatrix();
            }
        });
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground();
        super.render(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean keyPressed(int keyCode, int p_keyPressed_2_, int p_keyPressed_3_) {
        /*if (keyCode == KeyBinds.RESPAWN.getKeyCode())
            respawn();*/
        return super.keyPressed(keyCode, p_keyPressed_2_, p_keyPressed_3_);
    }

    private enum Key {
        F_SHARP_0(  0, KeyType.BLACK),
        G_0      (  7, KeyType.WHITE_MID),
        G_SHARP_0( 20, KeyType.BLACK),
        A_0      ( 27, KeyType.WHITE_MID),
        A_SHARP_0( 40, KeyType.BLACK),
        B_0      ( 47, KeyType.WHITE_RIGHT),
        C_0      ( 67, KeyType.WHITE_LEFT),
        C_SHARP_0( 80, KeyType.BLACK),
        D_0      ( 87, KeyType.WHITE_MID),
        D_SHARP_0(100, KeyType.BLACK),
        E_0      (107, KeyType.WHITE_RIGHT),
        F_0      (127, KeyType.WHITE_LEFT),
        F_SHARP_1(140, KeyType.BLACK),
        G_1      (147, KeyType.WHITE_MID),
        G_SHARP_1(160, KeyType.BLACK),
        A_1      (167, KeyType.WHITE_MID),
        A_SHARP_1(180, KeyType.BLACK),
        B_1      (187, KeyType.WHITE_RIGHT),
        C_1      (207, KeyType.WHITE_LEFT),
        C_SHARP_1(220, KeyType.BLACK),
        D_1      (227, KeyType.WHITE_MID),
        D_SHARP_1(240, KeyType.BLACK),
        E_1      (247, KeyType.WHITE_RIGHT),
        F_1      (267, KeyType.WHITE_LEFT),
        F_SHARP_2(280, KeyType.BLACK);

        private int x;
        private KeyType keyType;

        Key(int x, KeyType keyType) {
            this.x = x;
            this.keyType = keyType;
        }

        public static Key collidingKey(int mouseX, int mouseY) {
            int tempX, tempY;
            for (Key key : values()) {
                for (Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> dimension : key.keyType.dimensions) {
                    tempX = mouseX - dimension.getFirst().getFirst() - key.x;
                    tempY = mouseY - dimension.getFirst().getSecond();
                    if (0 <= tempX && tempX <= dimension.getSecond().getFirst() &&
                        0 <= tempY && tempY <= dimension.getSecond().getSecond())
                    return key;
                }
            }
            return null;
        }
    }
    private enum KeyType {
        BLACK(Collections.singletonList(
            new Pair<>(new Pair<>(0, 0), new Pair<>(15, 50))
        )),
        WHITE_LEFT(Arrays.asList(
            new Pair<>(new Pair<>(0,  0), new Pair<>(12, 50)),
            new Pair<>(new Pair<>(0, 50), new Pair<>(20, 35))
        )),
        WHITE_MID(Arrays.asList(
            new Pair<>(new Pair<>(8,  0), new Pair<>( 5, 50)),
            new Pair<>(new Pair<>(0, 50), new Pair<>(20, 35))
        )),
        WHITE_RIGHT(Arrays.asList(
                new Pair<>(new Pair<>(8,  0), new Pair<>(12, 50)),
                new Pair<>(new Pair<>(0, 50), new Pair<>(20, 35))
        ));

        //             x        y              dx       dy
        List<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>> dimensions;

        KeyType(List<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>> dimensions) {
            this.dimensions = dimensions;
        }
    }
}
