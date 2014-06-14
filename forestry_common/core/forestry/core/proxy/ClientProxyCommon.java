/*******************************************************************************
 * Copyright 2011-2014 by SirSengir
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/.
 ******************************************************************************/
package forestry.core.proxy;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.particle.EntityExplodeFX;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;

import forestry.apiculture.render.TextureBiomefinder;
import forestry.core.ForestryClient;
import forestry.core.TickHandlerCoreClient;
import forestry.core.config.Config;
import forestry.core.render.SpriteSheet;

public class ClientProxyCommon extends ProxyCommon {

	@Override
	public void bindTexture(ResourceLocation location) {
		getClientInstance().getTextureManager().bindTexture(location);
	}

	@Override
	public void bindTexture(SpriteSheet spriteSheet) {
		bindTexture(spriteSheet.getLocation());
	}

	@Override
	public void registerTickHandlers() {
		super.registerTickHandlers();

		new TickHandlerCoreClient();
	}

	@Override
	public IResourceManager getSelectedTexturePack(Minecraft minecraft) {
		return minecraft.getResourceManager();
	}

	@Override
	public void setBiomefinderCoordinates(EntityPlayer player, ChunkCoordinates coordinates) {
		if (isSimulating(player.worldObj))
			super.setBiomefinderCoordinates(player, coordinates);
		else
			TextureBiomefinder.getInstance().setTargetCoordinates(coordinates);
		// TextureHabitatLocatorFX.instance.setTargetCoordinates(coordinates);
	}

	@Override
	public File getForestryRoot() {
		return Minecraft.getMinecraft().mcDataDir;
	}

	@Override
	public World getRenderWorld() {
		return getClientInstance().theWorld;
	}

	@Override
	public int getBlockModelIdEngine() {
		return ForestryClient.blockModelIdEngine;
	}

	@Override
	public int getByBlockModelId() {
		return ForestryClient.byBlockModelId;
	}

	@Override
	public boolean isOp(EntityPlayer player) {
		return true;
	}

	@Override
	public double getBlockReachDistance(EntityPlayer entityplayer) {
		if (entityplayer instanceof EntityPlayerSP)
			return getClientInstance().playerController.getBlockReachDistance();
		else
			return 4f;
	}

	@Override
	public boolean isSimulating(World world) {
		return !world.isRemote;
	}

	@Override
	public boolean isShiftDown() {
		return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
	}

	@Override
	public String getItemStackDisplayName(Item item) {
		return item.getItemStackDisplayName(null);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return stack.getItem().getItemStackDisplayName(stack);
	}

	@Override
	public String getCurrentLanguage() {
		return Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().getLanguageCode();
	}

	@Override
	public String getDisplayName(ItemStack itemstack) {
		return itemstack.getItem().getItemStackDisplayName(itemstack);
	}

	@Override
	public void playSoundFX(World world, int x, int y, int z, Block block) {
		if (Proxies.common.isSimulating(world))
			super.playSoundFX(world, x, y, z, block);
		else
			playSoundFX(world, x, y, z, block.stepSound.getStepResourcePath(), block.stepSound.getVolume(), block.stepSound.getPitch());
	}

	@Override
	public void playBlockBreakSoundFX(World world, int x, int y, int z, Block block) {
		if (Proxies.common.isSimulating(world))
			super.playSoundFX(world, x, y, z, block);
		else
			playSoundFX(world, x, y, z, block.stepSound.getBreakSound(), block.stepSound.getVolume() / 4, block.stepSound.getPitch());
	}

	@Override
	public void playBlockPlaceSoundFX(World world, int x, int y, int z, Block block) {
		if (Proxies.common.isSimulating(world))
			super.playSoundFX(world, x, y, z, block);
		else
			playSoundFX(world, x, y, z, block.stepSound.getStepResourcePath(), block.stepSound.getVolume() / 4, block.stepSound.getPitch());
	}

	@Override
	public void playSoundFX(World world, int x, int y, int z, String sound, float volume, float pitch) {
		world.playSound(x + 0.5, y + 0.5, z + 0.5, sound, volume, (1.0f + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2f) * 0.7f, false);
	}

	/**
	 * Renders a EntityBiodustFX on client.
	 * 
	 * @param world
	 * @param d1
	 * @param d2
	 * @param d3
	 * @param f1
	 * @param f2
	 * @param f3
	 */
	// FIXME: This is causing crashes.
	@Override
	public void addEntityBiodustFX(World world, double d1, double d2, double d3, float f1, float f2, float f3) {
		if (!Config.enableParticleFX)
			return;

		// ModLoader.getMinecraftInstance().effectRenderer.addEffect(new EntityBiodustFX(world, d1, d2, d3, f1, f2, f3));
	}

	// FIXME: This is causing crashes.
	@Override
	public void addEntitySwarmFX(World world, double d1, double d2, double d3, float f1, float f2, float f3) {
		if (!Config.enableParticleFX)
			return;

		// ModLoader.getMinecraftInstance().effectRenderer.addEffect(new EntityHoneydustFX(world, d1, d2, d3, f1, f2, f3));
	}

	@Override
	public void addEntityExplodeFX(World world, double d1, double d2, double d3, float f1, float f2, float f3) {
		if (!Config.enableParticleFX)
			return;

		getClientInstance().effectRenderer.addEffect(new EntityExplodeFX(world, d1, d2, d3, f1, f2, f3));
	}

	@Override
	public void addBlockDestroyEffects(World world, int xCoord, int yCoord, int zCoord, Block block, int i) {
		if (!isSimulating(world))
			getClientInstance().effectRenderer.addBlockDestroyEffects(xCoord, yCoord, zCoord, block, i);
		else
			super.addBlockDestroyEffects(world, xCoord, yCoord, zCoord, block, i);
	}

	@Override
	public void addBlockPlaceEffects(World world, int xCoord, int yCoord, int zCoord, Block block, int i) {
		if (!isSimulating(world))
			playBlockPlaceSoundFX(world, xCoord, yCoord, zCoord, block);
		else
			super.addBlockPlaceEffects(world, xCoord, yCoord, zCoord, block, i);
	}

	@Override
	public EntityPlayer getPlayer() {
		return Minecraft.getMinecraft().thePlayer;
	}
}
