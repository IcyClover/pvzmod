package com.hungteen.pvz.misc.damage;

import com.hungteen.pvz.PVZMod;

import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public class PVZItemTags {

	//forge
	
	
	//pvz
	public static final Tag<Item> PLANT_SUMMON_CARDS = pvzTag("cards/plant_summon_card");
	public static final Tag<Item> PLANT_ENJOY_CARDS = pvzTag("cards/plant_enjoy_card");
	public static final Tag<Item> PLANT_CARDS = pvzTag("cards/plant_card");
	public static final Tag<Item> TEMPLATE_CARDS = pvzTag("cards/template_card");
		
	private static Tag<Item> pvzTag(String name){
        return new ItemTags.Wrapper(new ResourceLocation(PVZMod.MOD_ID, name));
    }
	
	@SuppressWarnings("unused")
	private static Tag<Item> forgeTag(String name){
        return new ItemTags.Wrapper(new ResourceLocation("forge", name));
    }
}
