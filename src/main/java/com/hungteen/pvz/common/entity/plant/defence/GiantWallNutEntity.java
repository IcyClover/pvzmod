package com.hungteen.pvz.common.entity.plant.defence;

import com.hungteen.pvz.api.types.IPlantType;
import com.hungteen.pvz.common.impl.plant.PVZPlants;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.world.World;

public class GiantWallNutEntity extends TallNutEntity {

	public GiantWallNutEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
		super(type, worldIn);
	}

	@Override
	public int getArmor() {
		return 20;
	}

	@Override
	public int getArmorToughness() {
		return 20;
	}

	@Override
	public float getSuperLife() {
		return 600;
	}

	@Override
	public float getAttractRange() {
		return 5;
	}

	@Override
	public boolean canPlaceOuterPlant() {
		return false;
	}
	
	@Override
	public EntitySize getDimensions(Pose poseIn) {
		return EntitySize.scalable(2F, 3F);
	}
	
	@Override
	public int getSuperTimeLength() {
		return 30;
	}
	
	@Override
	public IPlantType getPlantType() {
		return PVZPlants.GIANT_WALL_NUT;
	}

}
