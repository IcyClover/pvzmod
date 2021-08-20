package com.hungteen.pvz.common.entity.zombie.poolday;

import com.hungteen.pvz.common.entity.zombie.base.SwimmerZombieEntity;
import com.hungteen.pvz.remove.Zombies;
import com.hungteen.pvz.utils.ZombieUtil;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.world.World;

public class DolphinRiderZombieEntity extends SwimmerZombieEntity{

	public DolphinRiderZombieEntity(EntityType<? extends MonsterEntity> type, World worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void updateAttributes() {
		super.updateAttributes();
		this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(ZombieUtil.WALK_VERY_FAST);
	}
	
	@Override
	public float getLife() {
		return 30;
	}

	@Override
	public Zombies getZombieEnumName() {
		return Zombies.DOLPHIN_RIDER_ZOMBIE;
	}

}
