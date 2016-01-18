package crazypants.structures.creator.block;

import crazypants.structures.creator.endercore.TileEntityBase;
import net.minecraft.nbt.NBTTagCompound;

public class AbstractResourceTile extends TileEntityBase {

  protected String name;
  protected String exportDir;
  
  @Override
  protected void writeCustomNBT(NBTTagCompound root) {
    if(name != null && name.length() > 0) {
      root.setString("name", name);
    }
    if(exportDir != null && exportDir.length() > 0) {
      root.setString("exportDir", exportDir);
    }
    
  }

  @Override
  protected void readCustomNBT(NBTTagCompound root) {
    name = root.getString("name");
    exportDir = root.getString("exportDir");
    if(exportDir != null && exportDir.length() == 0) {
      exportDir = null;
    }
    
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
    markDirty();
  }

  public String getExportDir() {
    return exportDir;
  }

  public void setExportDir(String exportDir) {
    this.exportDir = exportDir;
    markDirty();
  }
  
  

}
