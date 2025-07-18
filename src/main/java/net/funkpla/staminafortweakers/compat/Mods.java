package net.funkpla.staminafortweakers.compat;
import net.fabricmc.loader.api.FabricLoader;

public class Mods {
    public static final Mod VC_GLIDERS = new Mod("vc_gliders");

    public record Mod (String id){
        public boolean isLoaded(){
            return FabricLoader.getInstance().isModLoaded(id);
        }
    }
}
