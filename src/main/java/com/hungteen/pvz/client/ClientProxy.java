package com.hungteen.pvz.client;

import com.hungteen.pvz.client.render.layer.fullskin.ColdLayer;
import com.hungteen.pvz.common.CommonProxy;
import com.hungteen.pvz.common.item.ItemRegister;
import com.hungteen.pvz.common.item.armor.BucketArmorItem;
import com.hungteen.pvz.common.item.armor.ConeArmorItem;
import com.hungteen.pvz.common.item.armor.FootballArmorItem;
import com.hungteen.pvz.common.item.armor.GigaArmorItem;
import com.hungteen.pvz.utils.StringUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemModelsProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientProxy extends CommonProxy{

	public static final Minecraft MC = Minecraft.getInstance();
	
	@Override
	public void init() {
	};
	
	@Override
	public void postInit() {
		this.addLayersForRender();
	};
	
	@Override
	public void setUpClient() {
		ConeArmorItem.initArmorModel();
		BucketArmorItem.initArmorModel();
		FootballArmorItem.initArmorModel();
		GigaArmorItem.initArmorModel();
		KeyBindRegister.init();
		ItemModelsProperties.register(ItemRegister.SCREEN_DOOR.get(), StringUtil.prefix("blocking"), (stack, world, entity) -> {
	         return entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F;
	    });
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addLayersForRender() {
		MC.getEntityRenderDispatcher().renderers.values().forEach(r -> {
			if (r instanceof LivingRenderer) {
				((LivingRenderer) r).addLayer(new ColdLayer<>((LivingRenderer) r));
			}
		});
	}

	@Override
	public PlayerEntity getPlayer() {
		return MC.player;
	}
}
