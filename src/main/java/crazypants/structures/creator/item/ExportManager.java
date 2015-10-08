package crazypants.structures.creator.item;

import java.io.File;
import java.io.FileOutputStream;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

import org.apache.commons.io.IOUtils;

import crazypants.structures.Log;
import crazypants.structures.gen.StructureRegister;
import crazypants.structures.gen.io.StructureResourceManager;
import crazypants.structures.gen.structure.StructureComponent;

public class ExportManager {

//  private static String MIN_TEMPLATE = "{" +
//      "\"StructureGenerator\" : { \"uid\" : \"$GEN_UID\"," +
//      "\"templates\" : [" +
//      "{\"uid\" : \"$TEMPLATE_UID\"}" +
//      "]," +
//      "\"LocationSampler\" : { \"type\" : \"SurfaceSampler\"} }" +      
//      "}";

  static final String STRUCT_NAME = "structure";

  static final File EXPORT_DIR = new File("exportedStructureData");

  public static final ExportManager instance = new ExportManager();

  public ExportManager() {
    StructureRegister.instance.getResourceManager().addResourcePath(EXPORT_DIR);            
  }

  public void loadExportFolder() {
    if(!EXPORT_DIR.exists()) {
      return;
    }
    File[] files = EXPORT_DIR.listFiles();
    if(files == null) {
      return;
    }
//    for(File f : files) {
//      if(f != null && f.getName().endsWith(StructureResourceManager.GENERATOR_EXT)) {
//        try {
//          StructureGenerator gen = StructureRegister.instance.getResourceManager().loadGenerator(f);
//          if(gen != null && gen.isValid()) {
//            StructureRegister.instance.registerGenerator(gen);
//          }
//        } catch (Exception e) {
//          Log.warn("Could not load exported generator: " + f.getAbsolutePath() + " Ex: " + e);
//        }    
//      }
//    }
    
    for(File f : files) {
      if(f != null && f.getName().endsWith(StructureResourceManager.COMPONENT_EXT)) {
        try {
          StructureComponent comp = StructureRegister.instance.getResourceManager().loadStructureComponent(f.getName());
          if(comp != null) {
            StructureRegister.instance.registerStructureComponent(comp);
          }
        } catch (Exception e) {
          Log.warn("Could not load exported componnet: " + f.getAbsolutePath() + " Ex: " + e);
        }    
      }
    }
  }

  public String getNextExportUid() {
    String res = STRUCT_NAME;
    File file = new File(EXPORT_DIR, res + StructureResourceManager.COMPONENT_EXT);
    int num = 1;
    while (file.exists() && num < 100) {
      res = STRUCT_NAME + "_" + num;
      file = new File(EXPORT_DIR, res + StructureResourceManager.COMPONENT_EXT);
      num++;
    }
    return res;
  }

  public static void writeToFile(EntityPlayer entityPlayer, StructureComponent st, boolean createDefaultGenerator) {
    EXPORT_DIR.mkdir();
    if(!EXPORT_DIR.exists()) {
      entityPlayer.addChatComponentMessage(new ChatComponentText("Could not make folder " + EXPORT_DIR.getAbsolutePath()));
      return;
    }
    boolean saved = doWriteToFile(entityPlayer, st);
    if(!createDefaultGenerator || !saved) {
      return;
    }
//    String txt = createDefaultGenFor(st.getUid());
//    File f = new File(EXPORT_DIR, st.getUid() + StructureResourceManager.GENERATOR_EXT);
//    if(txt != null) {
//      try {
//        IoUtil.writeToFile(txt, f);
//        entityPlayer.addChatComponentMessage(new ChatComponentText("Saved to " + f.getAbsolutePath()));
//      } catch (IOException e) {
//        entityPlayer.addChatComponentMessage(new ChatComponentText("Could not write generator to " + f.getAbsolutePath()));
//        e.printStackTrace();
//      }
//    }

  }

//  private static String createDefaultGenFor(String uid) {
//    String res = MIN_TEMPLATE;
//    
//    InputStream stream = ExportManager.class.getResourceAsStream(DefaultStructures.RESOURCE_PATH + "defaultTemplate.gen");
//    if(stream == null) {
//      return res;
//    }    
//    try {
//      res = IoUtil.readStream(stream);
//    } catch (IOException e) {    
//      e.printStackTrace();
//    }
//    if(res != null) {
//      res = res.replaceAll("TEMPLATE_UID", uid);
//      res = res.replaceAll("GENERATOR_UID", uid);
//    }    
//    return res;
//  }

  private static boolean doWriteToFile(EntityPlayer entityPlayer, StructureComponent st) {
    boolean saved = false;
    File file = new File(EXPORT_DIR, st.getUid() + StructureResourceManager.COMPONENT_EXT);
    FileOutputStream fos = null;
    try {
      fos = new FileOutputStream(file, false);
      st.write(fos);
      fos.flush();
      fos.close();
      saved = true;
      entityPlayer.addChatComponentMessage(new ChatComponentText("Saved to " + file.getAbsolutePath()));
    } catch (Exception e) {
      e.printStackTrace();
      entityPlayer.addChatComponentMessage(new ChatComponentText("Could not save to " + file.getAbsolutePath()));
    } finally {
      IOUtils.closeQuietly(fos);
    }
    return saved;
  }

}
