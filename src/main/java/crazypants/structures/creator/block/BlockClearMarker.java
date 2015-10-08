package crazypants.structures.creator.block;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import crazypants.structures.creator.EnderStructuresCreator;
import crazypants.structures.creator.EnderStructuresCreatorTab;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class BlockClearMarker extends Block {

  public static final String NAME = "blockClearMarker";

  public static BlockClearMarker create() {
    BlockClearMarker res = new BlockClearMarker();
    res.init();
    return res;
  }

  protected BlockClearMarker() {
    super(Material.rock);
    setHardness(0.5F);
    setBlockName(NAME);
    setStepSound(Block.soundTypeStone);
    setHarvestLevel("pickaxe", 0);
    setCreativeTab(EnderStructuresCreatorTab.tabEnderStructures);
    setLightOpacity(0);
  }

  protected void init() {
    GameRegistry.registerBlock(this, NAME);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void registerBlockIcons(IIconRegister iIconRegister) {
    blockIcon = iIconRegister.registerIcon(EnderStructuresCreator.MODID.toLowerCase() + ":" + NAME);
  }

  public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_) {
    return null;
  }

  public boolean isOpaqueCube() {
    return false;
  }

  public boolean renderAsNormalBlock() {
    return false;
  }

}
