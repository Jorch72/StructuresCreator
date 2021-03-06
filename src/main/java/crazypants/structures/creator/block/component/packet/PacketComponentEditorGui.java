package crazypants.structures.creator.block.component.packet;

import com.enderio.core.common.network.MessageTileEntity;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import crazypants.structures.creator.block.component.TileComponentEditor;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class PacketComponentEditorGui extends MessageTileEntity<TileComponentEditor> implements IMessageHandler<PacketComponentEditorGui, IMessage> {

  
  private NBTTagCompound data;
  
  public PacketComponentEditorGui() {    
    
  }
  
  public PacketComponentEditorGui(TileComponentEditor tile) {
    super(tile);
    data = new NBTTagCompound();
    tile.writeCustomNBT(data);
  }

  @Override
  public void toBytes(ByteBuf buf) {  
    super.toBytes(buf);
    ByteBufUtils.writeTag(buf, data);
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    super.fromBytes(buf);
    data = ByteBufUtils.readTag(buf);
  }

  @Override
  public IMessage onMessage(PacketComponentEditorGui message, MessageContext ctx) {
    EntityPlayer player = ctx.getServerHandler().playerEntity;
    TileComponentEditor tile = message.getTileEntity(player.worldObj);
    if(tile == null) {
      return null;
    }
    tile.readCustomNBT(message.data);
    return null;
    
  }

}
