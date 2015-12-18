package crazypants.structures.creator.block.villager;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import org.apache.commons.io.IOUtils;

import crazypants.structures.Log;
import crazypants.structures.api.gen.IVillagerGenerator;
import crazypants.structures.api.util.Point3i;
import crazypants.structures.creator.block.AbstractResourceDialog;
import crazypants.structures.creator.block.AbstractResourceTile;
import crazypants.structures.creator.block.FileControls;
import crazypants.structures.creator.block.tree.EditorTreeControl;
import crazypants.structures.creator.block.tree.Icons;
import crazypants.structures.creator.item.ExportManager;
import crazypants.structures.gen.StructureGenRegister;
import crazypants.structures.gen.io.VillagerParser;
import crazypants.structures.gen.io.resource.StructureResourceManager;
import crazypants.structures.gen.villager.VillagerTemplate;
import net.minecraft.client.Minecraft;

public class DialogVillagerEditor extends AbstractResourceDialog {

  
  private static final long serialVersionUID = 1L;

  private static Map<Point3i, DialogVillagerEditor> openDialogs = new HashMap<Point3i, DialogVillagerEditor>();

  public static void openDialog(TileVillagerEditor tile) {
    Point3i key = new Point3i(tile.xCoord, tile.yCoord, tile.zCoord);
    DialogVillagerEditor res = openDialogs.get(key);
    if(res == null) {
      res = new DialogVillagerEditor(tile);
      openDialogs.put(key, res);
    }
    res.openDialog();
  }

  private final TileVillagerEditor tile;
  private final Point3i position;  
  private VillagerTemplate curTemplate;
  
  private FileControls fileControls;
  private EditorTreeControl treeControl;

  public DialogVillagerEditor(TileVillagerEditor tile) {
    this.tile = tile;
    position = new Point3i(tile.xCoord, tile.yCoord, tile.zCoord);
    setIconImage(Icons.GENERATOR.getImage());    
    setTitle("Generator Editor");

    initComponents();
    addComponents();
    addListeners();

    if(tile.getName() != null && tile.getName().trim().length() > 0 && tile.getExportDir() != null) {
      try {
        curTemplate = loadFromFile(new File(tile.getExportDir(), tile.getName() + StructureResourceManager.VILLAGER_EXT));
      } catch (Exception e) {
        tile.setName("NewGenerator");
        e.printStackTrace();
      }

    } else {
      tile.setName("NewGenerator");
    }
    buildTree();
  }

  private void buildTree() {
    String name = tile.getName();
    if(curTemplate == null) {
      VillagerTemplate tmpl = new VillagerTemplate();
      tmpl.setUid(name);
      curTemplate = tmpl;       
    }
    treeControl.buildTree(curTemplate);    
    
  }

  @Override
  protected void createNewResource() {
    if(!treeControl.isDirty() || checkClear()) {
      tile.setName("NewVillagerGen");
      sendUpdatePacket();
      curTemplate = null;
      buildTree();
    }    
  }

  @Override
  protected void openResource() {
    if(treeControl.isDirty() && !checkClear()) {
      return;
    }

    StructureResourceManager resMan = StructureGenRegister.instance.getResourceManager();
    List<File> files = resMan.getFilesWithExt(getResourceExtension());

    JPopupMenu menu = new JPopupMenu();
    for (File file : files) {

      final String uid = file.getName().substring(0, file.getName().length() - getResourceExtension().length());
      final VillagerTemplate gen = loadFromFile(file);
      if(gen != null) {
        JMenuItem mi = new JMenuItem(file.getName());
        mi.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            openGenerator(uid, gen);
          }
        });
        menu.add(mi);
      } else {
        System.out.println("DialogVillagerEditor.openResource: Could not load template from file: " + file.getAbsolutePath());
      }

    }

    JMenuItem mi = new JMenuItem("...");
    mi.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        openFromFile();
      }
    });
    menu.add(mi);

    menu.show(fileControls.getOpenB(), 0, 0);
  }

  @Override
  public String getResourceUid() {
    if(curTemplate == null || curTemplate.getUid() == null) {
      return null;
    }    
    return curTemplate.getUid().trim();
  }
  
  @Override
  public String getResourceExtension() {
    return StructureResourceManager.VILLAGER_EXT;
  }

  @Override
  public AbstractResourceTile getTile() {
    return tile;
  }
  
  @Override
  protected void onDialogClose() {
    openDialogs.remove(position);
    super.onDialogClose();
  }

  @Override
  public void onDirtyChanged(boolean dirty) {
    super.onDirtyChanged(dirty);
    fileControls.getSaveB().setEnabled(dirty);
  }
  
  @Override
  protected void writeToFile(File file, String newUid) {
    if(ExportManager.writeToFile(file, curTemplate, Minecraft.getMinecraft().thePlayer)) {
      Log.info("DialogTemplateEditor.save: Saved template to " + file.getAbsolutePath());
      if(!newUid.equals(curTemplate.getUid())) {
        curTemplate.setUid(newUid);
        tile.setName(newUid);
        sendUpdatePacket();
        buildTree();
        treeControl.setDirty(true);
      }
      treeControl.setDirty(false);   
      registerCurrentTemplate();
    }    
  }
  
  private void openGenerator(String name, VillagerTemplate gen) {
    if(name == null || gen == null) {
      return;
    }   
    tile.setName(name);
    sendUpdatePacket();
    curTemplate = gen;
    onDirtyChanged(false);
    buildTree();
  }
  
  private void openFromFile() {
    File file = selectFileToOpen();
    if(file == null) {
      return;
    }
    VillagerTemplate vilTmpl = loadFromFile(file);
    if(vilTmpl != null) {      
      String name = vilTmpl.getUid();
      openGenerator(name, vilTmpl);
      registerCurrentTemplate();
    } else {
      JOptionPane.showMessageDialog(this, "Could not load template.", "Bottoms", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void registerCurrentTemplate() {
    if(curTemplate != null && curTemplate.isValid()) {
      IVillagerGenerator gen = curTemplate.createGenerator();
      StructureGenRegister.instance.registerVillagerGenerator(gen);
      gen.onReload();
    }
  }
  
  private VillagerTemplate loadFromFile(File file) {
    String name = file.getName();
    if(name.endsWith(StructureResourceManager.VILLAGER_EXT)) {
      name = name.substring(0, name.length() - StructureResourceManager.VILLAGER_EXT.length());
    }

    InputStream stream = null;
    try {
      stream = new FileInputStream(file);
      String json = StructureGenRegister.instance.getResourceManager().loadText(name, stream);
      StructureGenRegister.instance.getResourceManager().addResourceDirectory(file.getParentFile());      
      VillagerTemplate res = VillagerParser.parseVillagerTemplate(name, json);      
      if(res != null) {
        tile.setExportDir(file.getParentFile().getAbsolutePath());
        sendUpdatePacket();
      }
      return res;
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      IOUtils.closeQuietly(stream);
    }
    return null;
  }

  private void initComponents() {
    fileControls = new FileControls(this);
    treeControl = new EditorTreeControl(this);
  }

  private void addComponents() {
    Container cp = getContentPane();
    cp.setLayout(new BorderLayout());
    cp.add(fileControls.getPanel(), BorderLayout.NORTH);
    cp.add(treeControl.getRoot(), BorderLayout.CENTER);
  }

  private void addListeners() {
    
  }
}

