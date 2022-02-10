package com.hungteen.pvz.common.entity.zombie.zombotany;

import com.hungteen.pvz.common.impl.zombie.ZombieType;
import com.hungteen.pvz.common.impl.zombie.Zombotanies;
import com.hungteen.pvz.common.misc.PVZLoot;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class WallNutZombieEntity extends AbstractZombotanyEntity {

	public WallNutZombieEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
		super(type, worldIn);
	}

	@Override
	public float getLife() {
		return 160;
	}
	
	@Override
	public ZombieType getZombieType() {
		return Zombotanies.WALLNUT_ZOMBIE;
	}

}
