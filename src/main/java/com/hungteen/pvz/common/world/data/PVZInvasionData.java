package com.hungteen.pvz.common.world.data;

import java.util.HashSet;

import com.hungteen.pvz.PVZConfig;
import com.hungteen.pvz.common.cache.InvasionCache;
import com.hungteen.pvz.common.core.InvasionType;
import com.hungteen.pvz.common.core.PlantType;
import com.hungteen.pvz.common.core.ZombieType;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

public class PVZInvasionData extends WorldSavedData {

	private static final String DATA_NAME = "WorldEventData";
	private HashSet<InvasionType> events = new HashSet<>();
	private HashSet<ZombieType> zombies = new HashSet<>();
	private HashSet<PlantType> plants = new HashSet<>();
	private boolean changed = false;
	private int countDownDay = 0;
	private int currentDifficulty = 0;

	public PVZInvasionData() {
		super(DATA_NAME);
	}

	public PVZInvasionData(String name) {
		super(name);
	}

	// getter setter for zombie spawns.
	public boolean hasZombieSpawnEntry(ZombieType zombie) {
		return zombies.contains(zombie);
	}

	public void addZombieSpawnEntry(ZombieType zombie) {
		zombies.add(zombie);
		this.setDirty();
	}

	public void removeZombieSpawnEntry(ZombieType zombie) {
		zombies.remove(zombie);
		this.setDirty();
	}

	// getter setter for plant spawns.
	public boolean hasPlantSpawnEntry(PlantType plant) {
		return plants.contains(plant);
	}

	public void addPlantSpawnEntry(PlantType plant) {
		plants.add(plant);
		this.setDirty();
	}

	public void removePlantSpawnEntry(PlantType plant) {
		plants.remove(plant);
		this.setDirty();
	}

	// getter setter for invasion events.
	public boolean hasEvent(InvasionType ev) {
		return events.contains(ev);
	}

	public void addEvent(InvasionType ev) {
		events.add(ev);
		this.setDirty();
	}

	public void removeEvent(InvasionType ev) {
		events.remove(ev);
		this.setDirty();
	}

	// getter setter for change flag.
	public void setChanged(boolean is) {
		this.changed = is;
		this.setDirty();
	}

	public boolean hasChanged() {
		return this.changed;
	}

	// getter setter for count down day.
	public void setCountDownDay(int day) {
		this.countDownDay = day;
		this.setDirty();
	}

	public void decCountDownDay() {
		if(this.countDownDay <= 0) {
			this.countDownDay = PVZConfig.COMMON_CONFIG.InvasionSettings.InvasionIntervalLength.get();
		} else {
			-- this.countDownDay;
		}
		this.setDirty();
	}

	public int getCountDownDay() {
		return this.countDownDay;
	}
	
	public boolean hasCountDownDay() {
		return this.getCountDownDay() > 0;
	}

	// getter setter for count down day.
	public void setCurrentDifficulty(int dif) {
		this.currentDifficulty = Math.max(0, dif);
		InvasionCache.InvasionDifficulty = this.currentDifficulty;
		this.setDirty();
	}
	
	public void addCurrentDifficulty(int dif) {
		this.currentDifficulty = Math.max(0, this.currentDifficulty + dif);
		InvasionCache.InvasionDifficulty = this.currentDifficulty;
		this.setDirty();
	}

	public int getCurrentDifficulty() {
		return this.currentDifficulty;
	}

	@Override
	public void load(CompoundNBT nbt) {
		/* restore event */
		this.events.clear();
		if(nbt.contains("invasion_events")) {
			CompoundNBT tag = nbt.getCompound("invasion_events");
			for(InvasionType type : InvasionType.getInvasionEvents()) {
				if(tag.contains("has_" + type.getIdentity()) && tag.getBoolean("has_" + type.getIdentity())) {
					this.addEvent(type);
				}
			}
		}
		/* restore zombie spawn entry */
		this.zombies.clear();
		if (nbt.contains("zombie_spawn_entries")) {
			CompoundNBT tag = nbt.getCompound("zombie_spawn_entries");
			for (ZombieType zombie : ZombieType.getZombies()) {
				if (tag.contains("has_" + zombie.getIdentity())) {
					this.addZombieSpawnEntry(zombie);
				}
			}
		}
		/* restore zombie spawn entry */
		this.plants.clear();
		if (nbt.contains("plant_spawn_entries")) {
			CompoundNBT tag = nbt.getCompound("plant_spawn_entries");
			for (PlantType plant : PlantType.getPlants()) {
				if (tag.contains(plant.getIdentity())) {
					this.addPlantSpawnEntry(plant);
				}
			}
		}
		/* others */
		this.changed = nbt.getBoolean("changed");
		this.countDownDay = nbt.getInt("count_down_day");
		this.currentDifficulty = nbt.getInt("pvz_current_difficulty");
	}

	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		/* save events */
		CompoundNBT event_list = new CompoundNBT();
		for(InvasionType type : InvasionType.getInvasionEvents()) {
			event_list.putBoolean("has_" + type.getIdentity(), this.hasEvent(type));
		}
		nbt.put("invasion_events", event_list);
		/* save zombies */
		CompoundNBT zombie_list = new CompoundNBT();
		for (ZombieType zombie : ZombieType.getZombies()) {
			zombie_list.putBoolean("has_" + zombie.getIdentity(), this.hasZombieSpawnEntry(zombie));
		}
		nbt.put("zombie_spawn_entries", zombie_list);
		/* save plants */
		CompoundNBT plant_list = new CompoundNBT();
		for (PlantType plant : PlantType.getPlants()) {
			plant_list.putBoolean(plant.getIdentity(), this.hasPlantSpawnEntry(plant));
		}
		nbt.put("plant_spawn_entries", plant_list);
		/* others */
		nbt.putBoolean("changed", this.changed);
		nbt.putInt("count_down_day", this.countDownDay);
		nbt.putInt("pvz_current_difficulty", this.currentDifficulty);
		return nbt;
	}

	/*
	 * there is no world has invasion except overworld.
	 */
//	public static PVZInvasionData getInvasionData(World worldIn) {
//		if (!(worldIn instanceof ServerWorld)) {
//			throw new RuntimeException("Attempted to get the data from a client world. This is wrong.");
//		}
//		ServerWorld world = (ServerWorld) worldIn;
//		DimensionSavedDataManager storage = world.getDataStorage();
//		return storage.computeIfAbsent(PVZInvasionData::new, DATA_NAME);
//	}
	
	public static PVZInvasionData getOverWorldInvasionData(World worldIn) {
		if (!(worldIn instanceof ServerWorld)) {
			throw new RuntimeException("Attempted to get the data from a client world. This is wrong.");
		}
		ServerWorld world = worldIn.getServer().getLevel(World.OVERWORLD);
		DimensionSavedDataManager storage = world.getDataStorage();
		return storage.computeIfAbsent(PVZInvasionData::new, DATA_NAME);
	}

}
