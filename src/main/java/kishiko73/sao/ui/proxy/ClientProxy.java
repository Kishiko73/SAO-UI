package kishiko73.sao.ui.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy implements IProxy {

    public static KeyBinding[] keyBindings;

    @Override
    public void init() {
        keyBindings = new KeyBinding[1];
        keyBindings[0] = new KeyBinding("key.kiko-sao-ui.respawn", 340, "key.kiko-sao-ui.hotkeys"); // L Shift

        for (KeyBinding keyBinding : keyBindings)
            ClientRegistry.registerKeyBinding(keyBinding);


    }

    @Override
    public World getClientWorld() {
        return Minecraft.getInstance().world;
    }

    @Override
    public PlayerEntity getClientPlayer() {
        return Minecraft.getInstance().player;
    }
}
