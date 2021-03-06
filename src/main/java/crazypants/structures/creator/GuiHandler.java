package crazypants.structures.creator;

import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {

  public static final int GUI_ID_COMPONENT_EDITOR = 1;  
  public static final int GUI_ID_TAG_EDITOR = 2;  
  public static final int GUI_ID_TEMPLATE_EDITOR = 3;
  public static final int GUI_ID_GENERATOR_EDITOR = 4;
  public static final int GUI_ID_VILLAGER_EDITOR = 5;
  public static final int GUI_ID_LOOT_EDITOR = 6;
  

  protected final Map<Integer, IGuiHandler> guiHandlers = new HashMap<Integer, IGuiHandler>();

  public void registerGuiHandler(int id, IGuiHandler handler) {
    guiHandlers.put(id, handler);
  }

  @Override
  public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
    IGuiHandler handler = guiHandlers.get(id);
    if(handler != null) {
      return handler.getServerGuiElement(id, player, world, x, y, z);
    }
    return null;
  }

  @Override
  public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
    IGuiHandler handler = guiHandlers.get(id);
    if(handler != null) {
      return handler.getClientGuiElement(id, player, world, x, y, z);
    }
    return null;
  }
}
