package com.hungteen.pvz.data;

import com.hungteen.pvz.PVZMod;
import com.hungteen.pvz.api.types.IPlantType;
import com.hungteen.pvz.common.block.BlockRegister;
import com.hungteen.pvz.common.item.ItemRegister;
import com.hungteen.pvz.common.item.misc.PVZSpawnEggItem;
import com.hungteen.pvz.common.item.spawn.card.PlantCardItem;
import com.hungteen.pvz.utils.StringUtil;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ItemModelGenerator extends ItemModelProvider{

	private Set<Item> addedItems = new HashSet<>();
	
	public ItemModelGenerator(DataGenerator generator, ExistingFileHelper helper) {
		super(generator, PVZMod.MOD_ID, helper);
	}

	@Override
	protected void registerModels() {
		//special model generated by blockbench.
		addedItems.addAll(Arrays.asList(ItemRegister.PEA_GUN.get(), ItemRegister.ZOMBIE_FLAG.get(), ItemRegister.BOBSLE_CAR.get(),
				ItemRegister.SCREEN_DOOR.get(), ItemRegister.BOWLING_GLOVE.get(), ItemRegister.JACK_BOX.get(), ItemRegister.BALLOON.get(),
				ItemRegister.WARNING_SIGN.get(), ItemRegister.ZOMBIE_DOLL.get(), ItemRegister.POLE.get(), ItemRegister.CONE_HEAD.get(),
				ItemRegister.BUCKET_HEAD.get(), ItemRegister.RESOURCE_COLLECTOR.get()
				));
		//single add
		Arrays.asList(ItemRegister.PEA.get(), ItemRegister.SPORE.get(), ItemRegister.CABBAGE_SEEDS.get(), ItemRegister.CORN_SEEDS.get()
				
				).forEach(i -> {
			genNormalModel(i);
			this.addedItems.add(i);
		});
		Arrays.asList(BlockRegister.NUT_SAPLING.get(), BlockRegister.STEEL_LADDER.get()).forEach(i -> {
			genItemModelWithBlock(i.asItem());
			this.addedItems.add(i.asItem());
		});
		//for mostly common items.
		for (Item i : ForgeRegistries.ITEMS) {
			if(! i.getRegistryName().getNamespace().equals(PVZMod.MOD_ID) || addedItems.contains(i)) continue ;
			if (i instanceof PVZSpawnEggItem) {//for spawn eggs
				addedItems.add(i);
				getBuilder(i.getRegistryName().getPath()).parent(getExistingFile(new ResourceLocation("item/template_spawn_egg")));
			} else if(i instanceof PlantCardItem) {//for plant cards
				IPlantType plant = ((PlantCardItem) i).plantType;
				ResourceLocation plantResource = new ResourceLocation(plant.getModID(), "screenshot/plant/" + plant.toString());
				addedItems.add(i);
				if(((PlantCardItem) i).isEnjoyCard) {
					ResourceLocation r = StringUtil.prefix("item/red_card");
					genNormal(i.getRegistryName().getPath(), r, plantResource);
				} else {
					ResourceLocation r = plant.getRank().getTemplateCard().getRegistryName();
					genNormal(i.getRegistryName().getPath(), new ResourceLocation(r.getNamespace(), "item/" + r.getPath()), plantResource);
				}
			} else if(i instanceof BlockItem) {
				addedItems.add(i);
				genBlockModel(((BlockItem) i).getBlock());
			}
		}
		//for hand held item
		Arrays.asList(ItemRegister.ORIGIN_AXE.get(), ItemRegister.ORIGIN_HOE.get(), ItemRegister.ORIGIN_PICKAXE.get(), ItemRegister.ORIGIN_SHOVEL.get(),
				ItemRegister.ORIGIN_SWORD.get()
				).forEach(i -> {
			addedItems.add(i);
			genHeld(i.getRegistryName().getPath(), StringUtil.prefix("item/" + i.getRegistryName().getPath()));
		});
		//3 types of sun storage sapling.
		genSameModelsWithAdd(ItemRegister.SUN_STORAGE_SAPLING.get(), ItemRegister.SMALL_SUN_STORAGE_SAPLING.get(), ItemRegister.LARGE_STORAGE_SAPLING.get());
		//last step for all normal item models.
		for(Item i : ForgeRegistries.ITEMS) {
			if(i.getRegistryName().getNamespace().equals(PVZMod.MOD_ID) && ! addedItems.contains(i)) {
				genNormal(i.getRegistryName().getPath(), StringUtil.prefix("item/" + i.getRegistryName().getPath()));
			
			}
		}
	}
	
	/**
	 * for items with the same texture.
	 */
	private void genSameModelsWithAdd(Item... items) {
		final Item first = items[0];
		for(Item i : items) {
			genNormal(i.getRegistryName().getPath(), StringUtil.prefix("item/" + first.getRegistryName().getPath()));
		    this.addedItems.add(i);
		}
	}
	
	private void genNormalModel(Item i) {
		genNormal(i.getRegistryName().getPath(), StringUtil.prefix("item/" + i.getRegistryName().getPath()));
	}
	
	private ItemModelBuilder genNormal(String name, ResourceLocation... layers) {
		return gen(name, "item/generated", layers);
	}
	
	private ItemModelBuilder genHeld(String name, ResourceLocation... layers) {
		return gen(name, "item/handheld", layers);
	}
	
	private ItemModelBuilder gen(String name, String parent, ResourceLocation... layers) {
		ItemModelBuilder builder = withExistingParent(name, parent);
		for (int i = 0; i < layers.length; i++) {
			builder = builder.texture("layer" + i, layers[i]);
		}
		return builder;
	}
	
	private void genBlockModel(Block b) {
		withExistingParent(b.getRegistryName().getPath(), StringUtil.prefix("block/" + b.getRegistryName().getPath()));
	}
	
	private void genItemModelWithBlock(Item i) {
		genNormal(i.getRegistryName().getPath(), StringUtil.prefix("block/" + i.getRegistryName().getPath()));
	}
	
	@Override
	public String getName() {
		return "Plants vs Zombies item and itemblock models";
	}

}
