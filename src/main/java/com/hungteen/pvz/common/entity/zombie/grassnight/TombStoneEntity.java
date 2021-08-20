package com.hungteen.pvz.common.entity.zombie.grassnight;

import java.util.List;
import java.util.Optional;

import com.hungteen.pvz.common.cache.InvasionCache;
import com.hungteen.pvz.common.entity.plant.assist.GraveBusterEntity;
import com.hungteen.pvz.common.entity.zombie.other.NobleZombieEntity;
import com.hungteen.pvz.common.entity.zombie.roof.ZomBossEntity;
import com.hungteen.pvz.common.world.invasion.WaveManager;
import com.hungteen.pvz.register.EntityRegister;
import com.hungteen.pvz.remove.Zombies;
import com.hungteen.pvz.utils.EntityUtil;
import com.hungteen.pvz.utils.MathUtil;
import com.hungteen.pvz.utils.ZombieUtil;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TombStoneEntity extends AbstractTombStoneEntity {

	protected int waveSummonTick = 0;
	protected int currentSummonCD;
	private final int MinSummonCD = 360;
	private final int MaxSummonCD = 1200;
	
	public TombStoneEntity(EntityType<? extends MonsterEntity> type, World worldIn) {
		super(type, worldIn);
	}
	
	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new TombStoneSummonZombieGoal(this));
		this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, NobleZombieEntity.class, true));
		this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, ZomBossEntity.class, true));
	}

	/**
	 * {@link WaveManager#activateTombStone()}
	 */
	public void activateByWave() {
		this.waveSummonTick = 40;
	}
	
	public static void spawnTombStone(World world, BlockPos pos) {
		TombStoneEntity tomb = EntityRegister.TOMB_STONE.get().create(world);
		tomb.setZombieRising();
		EntityUtil.onEntitySpawn(world, tomb, pos);
	}
	
	@Override
	public void normalZombieTick() {
		super.normalZombieTick();
		if(! level.isClientSide) {//update wave spawn.
			if(this.waveSummonTick > 0) {
				-- this.waveSummonTick;
				if(this.waveSummonTick == 1) {
					this.summonZombie();
				}
			}
		}
	}
	
	/**
	 * tombstone summon zombies.
	 * it will select suitable zombie for that day.
	 * {@link TombStoneSummonZombieGoal} and {@link #normalZombieTick()}
	 */
	public void summonZombie() {
		final List<Zombies> list = InvasionCache.getOrDefaultZombieList(Zombies.DEFAULT_ZOMBIES);
		final Zombies zombieType = list.get(this.random.nextInt(list.size()));
		Optional.ofNullable(ZombieUtil.getZombieEntity(level, zombieType)).ifPresent(zombie -> {
			zombie.setZombieRising();
			ZombieUtil.copySummonZombieData(this, zombie);
			EntityUtil.onEntitySpawn(level, zombie, blockPosition());
		});
	}
	
	/**
	 * check can summon zombie or not.
	 * it need a valid boss target.
	 */
	protected boolean canSummonZombie() {
		return EntityUtil.isEntityValid(this.getTarget());
	}
	
	@Override
	protected boolean isZombieInvulnerableTo(DamageSource source) {
		return super.isZombieInvulnerableTo(source) || ! (source.getDirectEntity() instanceof GraveBusterEntity);
	}
	
	/**
	 * next summon zombie cd for next time.
	 */
	protected int genSummonCD() {
		return MathUtil.getRandomMinMax(getRandom(), MinSummonCD, MaxSummonCD);
	}
	
	@Override
	public Zombies getZombieEnumName() {
		return Zombies.TOMB_STONE;
	}
	
	@Override
	public void readAdditionalSaveData(CompoundNBT compound) {
		super.readAdditionalSaveData(compound);
		if(compound.contains("wave_summon_tick")) {
			this.waveSummonTick = compound.getInt("wave_summon_tick");
		}
	}
	
	@Override
	public void addAdditionalSaveData(CompoundNBT compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("wave_summon_tick", this.waveSummonTick);
	}
	
	static class TombStoneSummonZombieGoal extends Goal{

		protected final TombStoneEntity tomb;
		
		public TombStoneSummonZombieGoal(TombStoneEntity tomb) {
			this.tomb = tomb;
		}
		
		@Override
		public boolean canUse() {
			if(this.tomb.getAttackTime() > 0) {
				return true;
			}
			if(this.tomb.random.nextInt(10) == 0 && this.tomb.canSummonZombie()) {
				this.tomb.setAttackTime(1);
				this.tomb.currentSummonCD = this.tomb.genSummonCD();
				return true;
			}
			return false;
		}
		
		@Override
		public void start() {
			
		}
		
		@Override
		public boolean canContinueToUse() {
			return this.tomb.getAttackTime() > 0 && this.tomb.canSummonZombie();
		}
		
		@Override
		public void tick() {
			int time = this.tomb.getAttackTime();
			if(time >= this.tomb.currentSummonCD) {
				this.tomb.summonZombie();
				this.tomb.setAttackTime(0);
			} else {
				this.tomb.setAttackTime(time + 1);
			}
		}
		
		@Override
		public void stop() {
			this.tomb.setAttackTime(0);
		}
		
	}
	
}
