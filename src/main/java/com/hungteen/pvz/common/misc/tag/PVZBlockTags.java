package com.hungteen.pvz.common.misc.tag;

import com.hungteen.pvz.utils.StringUtil;

import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.util.ResourceLocation;

public class PVZBlockTags {

	//forge
	public static final INamedTag<Block> AMETHYST_ORES = forgeTag("ores/amethyst");
	public static final INamedTag<Block> PLANT_SUIT_BLOCKS = forgeTag("plant_suit_block");
	
	//pvz
	public static final INamedTag<Block> ESSENCE_ORES = pvzTag("ores/essence");
	public static final INamedTag<Block> GOLD_TILES = pvzTag("gold_tile");
		
	private static INamedTag<Block> pvzTag(String name){
        return BlockTags.createOptional(StringUtil.prefix(name));
    }
	
	private static INamedTag<Block> forgeTag(String name){
        return BlockTags.createOptional(new ResourceLocation("forge", name));
    }
}
