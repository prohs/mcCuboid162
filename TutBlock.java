package tutorial.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import tutorial.lib.ModInfo;
import tutorial.lib.Names;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TutBlock extends Block {
	public int x;
	public int y;
	public int z;
	public int blk;

	public TutBlock(int id) {
		super(id, Material.rock);
		this.setUnlocalizedName(Names.tutBlock_unlocalizedName);
		this.setCreativeTab(CreativeTabs.tabBlock);
		this.setHardness(2F);
		this.setResistance(15F);
		this.setStepSound(Block.soundStoneFootstep);
		this.setLightValue(0.5F);
		reset();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister icon) {
		blockIcon = icon.registerIcon(ModInfo.ID.toLowerCase() + ":"
				+ Names.tutBlock_unlocalizedName);

	}

	/**
	 * Called when a block is placed using its ItemBlock. Args: World, X, Y, Z,
	 * side, hitX, hitY, hitZ, block metadata
	 **/

	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4,
			EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack) {
		if (!par1World.isRemote) {
			if (blk < 0) {
				if (y > -1) {
					par1World.setBlock(x, y, z, 0);
					System.out.printf("NULL:%s,%s,%s\r\n", par2, par3, par4);
				}
				x = par2;
				y = par3;
				z = par4;
				System.out.printf("Trigger:%s,%s,%s\r\n", x, y, z);
				return;
			}
			int fx, fy, fz;
			int sx, sy, sz;
			if (par2 < x) {
				fx = par2;
				sx = x;
			} else {
				fx = x;
				sx = par2;
			}
			if (par3 < y) {
				fy = par3;
				sy = y;
			} else {
				fy = y;
				sy = par3;
			}
			if (par4 < z) {
				fz = par4;
				sz = z;
			} else {
				fz = z;
				sz = par4;
			}
			int ilim = sx - fx + 1;
			int jlim = sy - fy + 1;
			int klim = sz - fz + 1;
			int istep = ilim < 0 ? -1 : 1;
			int jstep = jlim < 0 ? -1 : 1;
			int kstep = klim < 0 ? -1 : 1;
			for (int i1 = 0; i1 < ilim; i1 += istep) {
				for (int j1 = 0; j1 < jlim; j1 += jstep) {
					for (int k1 = 0; k1 < klim; k1 += kstep) {
						par1World.setBlock(fx + i1, fy + j1, fz + k1, blk);
					}
				}
			}
			reset();
			return;
		}
	}

	public void onBlockDestroyedByPlayer(World par1World, int par2, int par3,
			int par4, int par5) {
		reset();
	}

	public void onBlockDestroyedByExplosion(World par1World, int par2,
			int par3, int par4, Explosion par5Explosion) {
		reset();
	}

	public boolean onBlockActivated(World par1World, int par2, int par3,
			int par4, EntityPlayer par5EntityPlayer, int par6, float par7,
			float par8, float par9) {
		return clicked(par2, par3, par4, par5EntityPlayer);
	}

	public void onBlockClicked(World par1World, int par2, int par3, int par4,
			EntityPlayer par5EntityPlayer) {
		clicked(par2, par3, par4, par5EntityPlayer);
	}

	public boolean clicked(int par2, int par3, int par4,
			EntityPlayer par5EntityPlayer) {

		System.out.printf("-block CLICKED-\n");
		ItemStack currentitem = par5EntityPlayer.getCurrentEquippedItem();
		if (currentitem == null) {
			blk = 0;
			return true;
		}
		int di = currentitem.itemID;
		if ((di < 256) && (di != blockID)) {
			if (di == blockID) {
				return false;
			}
			blk = di;
			System.out.printf("-block id set-\n");
			return true;
		}
		return false;

	}

	public void reset() {
		x = -1;
		y = -1;
		z = -1;
		blk = -1;
	}
}
