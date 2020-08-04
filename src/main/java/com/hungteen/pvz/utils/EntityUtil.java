package com.hungteen.pvz.utils;

import com.hungteen.pvz.PVZConfig;
import com.hungteen.pvz.entity.plant.PlantEntity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.Team;
import net.minecraft.world.World;

public class EntityUtil {

	public static boolean checkCanEntityAttack(Entity attacker,Entity target)
	{
		if(attacker==null||target==null) {//prevent crash
			return false;
		}
		World world=attacker.world;
		if(target instanceof PlayerEntity) {
			if(((PlayerEntity) target).isCreative()||target.isSpectator()) return false;
		}
		if(PVZConfig.COMMON_CONFIG.ENTITY_SETTINGS.canPlantAttackOtherTeam.get()) {
			Team team1=getEntityTeam(world,attacker);
			Team team2=getEntityTeam(world,target);
			if(team1!=null&&team2!=null) {
				boolean change=getIsEntityCharmed(attacker)^getIsEntityCharmed(target);
				if(change) return team1.isSameTeam(team2);
				else return !team1.isSameTeam(team2);
			}
		}
		int attackerGroup=getEntityGroup(attacker);
		int targetGroup=getEntityGroup(target);
		if(attackerGroup*targetGroup<0) {
			return true;
		}
		return false;
	}
	
	public static int getEntityGroup(Entity entity)
	{
		int group=0;
		if(entity instanceof PlayerEntity) return 2;
		if(entity instanceof PlantEntity) {
			group=((PlantEntity) entity).getIsCharmed()?-1:1;
		}else if(entity instanceof MonsterEntity) {
			group=-1;
		}
		return group;
	}
	
	public static Team getEntityTeam(World world,Entity entity)
	{
		if(entity instanceof PlayerEntity) {
			return entity.getTeam();
		}
		if(entity instanceof PlantEntity) {
			PlayerEntity player = world.getPlayerByUuid(((PlantEntity) entity).getOwnerUUID());
			if(player==null) return null;
			return player.getTeam();
		}
	    return null;
	}
	
	public static boolean getIsEntityCharmed(Entity entity)
	{
		if(entity instanceof PlantEntity) return ((PlantEntity) entity).getIsCharmed();
		return false;
	}
}
