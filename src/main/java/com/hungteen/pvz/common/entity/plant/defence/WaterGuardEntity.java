package com.hungteen.pvz.common.entity.plant.defence;

import com.hungteen.pvz.common.core.PlantType;
import com.hungteen.pvz.common.entity.plant.base.PlantDefenderEntity;
import com.hungteen.pvz.common.impl.plant.CustomPlants;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.world.World;

public class WaterGuardEntity extends PlantDefenderEntity{

	public WaterGuardEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(2, new SwimGoal(this));
	}
	
	@Override
	public float getPlantHealth() {
		return this.getAverageProgress(250F, 500F);
	}
	
	@Override
	public float getSuperLife() {
		return this.getThreeStage(400, 600, 800);
	}
	
	@Override
	public int getSuperTimeLength() {
		return 20;
	}
	
	@Override
	public EntitySize getDimensions(Pose poseIn) {
		return new EntitySize(0.8f, 0.8f, false);
	}

	@Override
	public PlantType getPlantType() {
		return CustomPlants.WATER_GUARD;
	}

}
