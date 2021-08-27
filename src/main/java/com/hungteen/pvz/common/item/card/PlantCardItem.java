package com.hungteen.pvz.common.item.card;

import java.util.List;
import java.util.function.Consumer;

import com.hungteen.pvz.PVZMod;
import com.hungteen.pvz.common.advancement.trigger.PlayerPlacePlantTrigger;
import com.hungteen.pvz.common.capability.CapabilityHandler;
import com.hungteen.pvz.common.capability.player.PlayerDataManager;
import com.hungteen.pvz.common.core.PlantType;
import com.hungteen.pvz.common.enchantment.EnchantmentUtil;
import com.hungteen.pvz.common.entity.plant.PVZPlantEntity;
import com.hungteen.pvz.common.entity.plant.base.PlantDefenderEntity;
import com.hungteen.pvz.common.event.events.SummonCardUseEvent;
import com.hungteen.pvz.common.impl.plant.PVZPlants;
import com.hungteen.pvz.register.EffectRegister;
import com.hungteen.pvz.register.EnchantmentRegister;
import com.hungteen.pvz.register.GroupRegister;
import com.hungteen.pvz.register.SoundRegister;
import com.hungteen.pvz.utils.EntityUtil;
import com.hungteen.pvz.utils.PlantUtil;
import com.hungteen.pvz.utils.enums.Resources;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;

/**
 * Normal plant type : place on top face of block.
 * Outer plant type : place on plant entity. 
 * Pumpkin heal type : heal pumpkin outer plant. 
 * Defender plant heal type : Heal Defender Plant. 
 * Upgrade plant type : base plant upgrade. 
 * CatTail upgrade type : cattail upgrade on lilypad. 
 * CoffeeBean place type : coffee bean ride on plant entity. 
 * GraveBuster place type : gravebuster ride on tombstone. 
 * Place block in water type : lilypad place in water. 
 * Place block on ground type : flower pot place on ground. 
 */
public class PlantCardItem extends SummonCardItem {

	public final PlantType plantType;

	public PlantCardItem(PlantType plant, boolean isFragment) {
		super(isFragment);
		this.plantType = plant;
	}
	
	public PlantCardItem(Properties properties, PlantType plant, boolean isFragment) {
		super(properties, isFragment);
		this.plantType = plant;
	}
	
	@Override
	public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> list) {
		if(this.allowdedIn(group)) {
			list.add(new ItemStack(this));
			if(group == GroupRegister.PVZ_PLANT_CARD) {
				// insert sort.
				for(int i = list.size() - 1; i >= 1; -- i) {
					final ItemStack pre = list.get(i - 1);
					final ItemStack now = list.get(i);
					if(needExchange(pre, now)) {
						final ItemStack tmp = pre.copy();
						list.set(i - 1, now);
						list.set(i, tmp);
					} else {
						break;
					}
				}
			}
		}
	}
	
	/**
	 * first check plantType ID, then check enjoy card. <br>
	 * large id put forward and enjoy card put backward. <br>
	 */
	public static boolean needExchange(ItemStack pre, ItemStack now) {
		//both are plant card.
		if(pre.getItem() instanceof PlantCardItem && now.getItem() instanceof PlantCardItem) {
			if(((PlantCardItem) pre.getItem()).isEnjoyCard && ! ((PlantCardItem) now.getItem()).isEnjoyCard) {
				return true;
			}
			if(! ((PlantCardItem) pre.getItem()).isEnjoyCard && ((PlantCardItem) now.getItem()).isEnjoyCard) {
				return false;
			}
			PlantType preType = ((PlantCardItem) pre.getItem()).plantType;
			PlantType nowType = ((PlantCardItem) now.getItem()).plantType;
			return preType.getId() > nowType.getId();
		}
		if(pre.getItem() instanceof PlantCardItem) {
			return false;
		}
		return true;
	}

	@Override
	public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand handIn) {
		ItemStack stack = player.getItemInHand(handIn);
		if(! this.plantType.isWaterPlant() || plantType == PVZPlants.CAT_TAIL) {
			return ActionResult.pass(stack);
		}
		RayTraceResult raytraceresult = getPlayerPOVHitResult(world, player, RayTraceContext.FluidMode.SOURCE_ONLY);
		if (raytraceresult.getType() != RayTraceResult.Type.BLOCK) {
			return ActionResult.pass(stack);
		} else if (world.isClientSide) {
			return ActionResult.success(stack);
		} else {
			BlockRayTraceResult blockraytraceresult = (BlockRayTraceResult) raytraceresult;
			BlockPos pos = blockraytraceresult.getBlockPos();
			if (!(world.getBlockState(pos).getBlock() instanceof FlowingFluidBlock)) {
				return ActionResult.pass(stack);
			}
			if (world.mayInteract(player, pos) && player.mayUseItemAt(pos, blockraytraceresult.getDirection(), stack)) {
				checkSunAndSummonPlant(player, stack, this, pos, (l)->{});
				return ActionResult.success(stack);
			} else {
				return ActionResult.fail(stack);
			}
		}
	}

	@Override
	public ActionResultType useOn(ItemUseContext context) {
		final World world = context.getLevel();
		final PlayerEntity player = context.getPlayer();
		final Hand hand = context.getHand();
		final ItemStack stack = player.getItemInHand(hand);
		BlockPos pos = context.getClickedPos();
		if (world.isClientSide) {
			return ActionResultType.SUCCESS;
		}
		//ignore upgrade plant.
		if(plantType.getUpgradeFrom().isPresent()) {
			return ActionResultType.FAIL;
		}
		// can only plant in water.
		if (plantType.isWaterPlant()) {
			return ActionResultType.PASS;
		}
		//is suitable block.
		if(! plantType.getPlacement().canPlaceOnBlock(world.getBlockState(pos).getBlock())) {
			return ActionResultType.FAIL;
		}
//		if (plantType.isOuterPlant() || plantType.isUpgradePlant()) {//need place on entity.
//			if(plantType == Plants.CAT_TAIL && world.getBlockState(pos).getBlock() == BlockRegister.LILY_PAD.get()) {
//				checkSunAndSummonPlant(player, stack, this, pos.below(), (l)->{
//					world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
//				});
//				return ActionResultType.SUCCESS;
//			}
//			return ActionResultType.FAIL;
//		}
		BlockPos spawnPos = pos;
		if(! world.getBlockState(pos).getCollisionShape(world, pos).isEmpty()) {
			spawnPos = pos.relative(context.getClickedFace());
		}
		if (context.getClickedFace() == Direction.UP && world.isEmptyBlock(pos.above())) {// can plant here
			checkSunAndSummonPlant(player, stack, this, spawnPos, (l)->{});
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.FAIL;
	}

	/**
	 * check sunCost and spawn plantEntity.
	 */
	public static void checkSunAndSummonPlant(PlayerEntity player, ItemStack stack, PlantCardItem cardItem, BlockPos pos, Consumer<PVZPlantEntity> consumer) {
		if(player.getCooldowns().isOnCooldown(cardItem)) return ;
		PlantType plantType = cardItem.plantType;
		player.getCapability(CapabilityHandler.PLAYER_DATA_CAPABILITY).ifPresent((l) -> {
			PlayerDataManager manager = l.getPlayerData();
			int num = manager.getPlayerStats().getPlayerStats(Resources.SUN_NUM);
			int sunCost = SummonCardItem.getItemStackSunCost(stack);
			if (num >= sunCost && plantType.getEntityType().isPresent()) { // sun is enough
				EntityType<? extends PVZPlantEntity> entityType = plantType.getEntityType().get();
				PVZPlantEntity plantEntity = (PVZPlantEntity) entityType.spawn((ServerWorld) player.level, stack, player, pos, SpawnReason.SPAWN_EGG, true, true);
				if (plantEntity == null) {
					PVZMod.LOGGER.debug("no such plant");
					return;
				}
				l.getPlayerData().getPlayerStats().addPlayerStats(Resources.SUN_NUM, - sunCost);
				int lvl = manager.getPlantStats().getPlantLevel(plantType);
				plantEntity.onSpawnedByPlayer(player, lvl);//update level health and owner.
				plantEntity.plantSunCost = sunCost;// use for sun shovel
				PlantCardItem.onUsePlantCard(player, stack, cardItem, lvl);//handle CD.
				summonPlantEntityByCard(plantEntity, stack);
				consumer.accept(plantEntity);
			}
		});
	}
	
	public static void summonPlantEntityByCard(PVZPlantEntity plantEntity, ItemStack stack) {
		if (canPlantBreakOut(stack)) {// break out enchantment
			if (plantEntity.canStartSuperMode()) {
				plantEntity.startSuperMode(false);
			}
		}
		if(EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegister.CHARM.get(), stack) > 0) {
			plantEntity.onCharmedBy(null);
		}
		if(EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegister.SOILLESS_PLANT.get(), stack) > 0) {
			plantEntity.setImmunneToWeak(true);
		}
	}

	/**
	 * check sunCost and place plantBlock.
	 */
	public static void checkSunAndPlaceBlock(PlayerEntity player, BlockPlantCardItem cardItem, ItemStack stack, BlockPos pos) {
		if(player.getCooldowns().isOnCooldown(cardItem)) return ;
		PlantType plantType = cardItem.plantType;
		player.getCapability(CapabilityHandler.PLAYER_DATA_CAPABILITY).ifPresent((l) -> {
			PlayerDataManager manager = l.getPlayerData();
			int num = manager.getPlayerStats().getPlayerStats(Resources.SUN_NUM);
			int sunCost = SummonCardItem.getItemStackSunCost(stack);
			if (num >= sunCost) { // sun is enough
				l.getPlayerData().getPlayerStats().addPlayerStats(Resources.SUN_NUM, - sunCost);
				onUsePlantCard(player, stack, cardItem, manager.getPlantStats().getPlantLevel(plantType));
				BlockState state = BlockPlantCardItem.getBlockState(player, plantType);
			    if(state == null) return;
				player.level.setBlock(pos, state, 11);
			    if (player instanceof ServerPlayerEntity) {
			        CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity) player, pos, stack);
		        }
			    player.level.playSound((PlayerEntity) null, pos.getX(), pos.getY(), pos.getZ(), plantType.isWaterPlant() ? SoundRegister.PLANT_IN_WATER.get() : SoundRegister.PLANT_ON_GROUND.get(), SoundCategory.BLOCKS, 1F, 1F);
			}
		});
	}
	
	/**
	 * check sunCost and add outerplant for plantEntity
	 */
	public static void checkSunAndOuterPlant(PlayerEntity player, PVZPlantEntity plantEntity, PlantCardItem cardItem,
			ItemStack stack) {
		if(player.getCooldowns().isOnCooldown(cardItem)) return ;
		PlantType plantType = cardItem.plantType;
		player.getCapability(CapabilityHandler.PLAYER_DATA_CAPABILITY).ifPresent((l) -> {
			PlayerDataManager manager = l.getPlayerData();
			int num = manager.getPlayerStats().getPlayerStats(Resources.SUN_NUM);
			int sunCost = SummonCardItem.getItemStackSunCost(stack);
			if (num >= sunCost) { // sun is enough
				l.getPlayerData().getPlayerStats().addPlayerStats(Resources.SUN_NUM, - sunCost);
				onUsePlantCard(player, stack, cardItem, manager.getPlantStats().getPlantLevel(plantType));
				plantEntity.outerSunCost = sunCost;
				placeOuterPlant(plantEntity, plantType, stack);
			}
		});
	}
	
	public static void placeOuterPlant(PVZPlantEntity plantEntity, PlantType plantType, ItemStack stack) {
		if(plantType == PVZPlants.PUMPKIN) {
			float life = PlantUtil.PUMPKIN_LIFE;
		    if (canPlantBreakOut(stack)) {// break out enchantment
			    life += PlantUtil.PUMPKIN_SUPER_LIFE;
		    }
		    plantEntity.setPumpkinLife(life);
		}
		plantEntity.setOuterPlantType(plantType);
		EntityUtil.playSound(plantEntity, SoundRegister.PLANT_ON_GROUND.get());
	}
	
	/**
	 * check sunCost and heal defender plantEntity.
	 */
	public static void checkSunAndHealPlant(PlayerEntity player, PVZPlantEntity plantEntity, PlantCardItem cardItem,
			ItemStack stack) {
		if(player.getCooldowns().isOnCooldown(cardItem)) return ;
		PlantType plantType = cardItem.plantType;
		player.getCapability(CapabilityHandler.PLAYER_DATA_CAPABILITY).ifPresent((l) -> {
			PlayerDataManager manager = l.getPlayerData();
			int num = manager.getPlayerStats().getPlayerStats(Resources.SUN_NUM);
			int sunCost = SummonCardItem.getItemStackSunCost(stack);
			if (num >= sunCost) { // sun is enough
				l.getPlayerData().getPlayerStats().addPlayerStats(Resources.SUN_NUM, - sunCost);
				onUsePlantCard(player, stack, cardItem, manager.getPlantStats().getPlantLevel(plantType));
				if(cardItem.plantType == PVZPlants.PUMPKIN) {
					float life = PlantUtil.PUMPKIN_LIFE;
				    if (canPlantBreakOut(stack)) {// break out enchantment
					    life += PlantUtil.PUMPKIN_SUPER_LIFE;
				    }
				    plantEntity.setPumpkinLife(life);
				} else {
					plantEntity.heal(plantEntity.getMaxHealth());
					if(plantEntity instanceof PlantDefenderEntity) {
						if (canPlantBreakOut(stack)) {// break out enchantment
							((PlantDefenderEntity) plantEntity).setDefenceLife(((PlantDefenderEntity) plantEntity).getSuperLife());
					    }
					}
				}
				EntityUtil.playSound(plantEntity, SoundRegister.PLANT_ON_GROUND.get());
			}
		});
	}
	
    public static void onUsePlantCard(PlayerEntity player, ItemStack stack, PlantCardItem item, int plantLvl) {
		MinecraftForge.EVENT_BUS.post(new SummonCardUseEvent(player, stack));
		if (item.isEnjoyCard) {
			if(! player.abilities.instabuild) {
				stack.shrink(1);
			}
		} else {
			handlePlantCardCoolDown(player, stack, item, plantLvl);
		}
		if(player instanceof ServerPlayerEntity) {
		    PlayerPlacePlantTrigger.INSTANCE.trigger((ServerPlayerEntity) player, item.plantType.getId());
		}
		player.awardStat(Stats.ITEM_USED.get(item));
	}
    
	/**
	 * set player-item cool down
	 */
	public static void handlePlantCardCoolDown(PlayerEntity player, ItemStack stack, PlantCardItem item, int plantLvl) {
		final int cd = getPlantCardCD(player, stack, item, plantLvl);
		player.getCapability(CapabilityHandler.PLAYER_DATA_CAPABILITY).ifPresent((l)->{
			l.getPlayerData().getItemCDStats().setPlantCardCD(item.plantType, cd);
		});
		player.getCooldowns().addCooldown(stack.getItem(), cd);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		tooltip.add(new TranslationTextComponent("tooltip.pvz.plant_card").withStyle(TextFormatting.GREEN));
		PlantCardItem item = (PlantCardItem) stack.getItem();
		if(item == null) {
			System.out.println("ERROR : Wrong Plant Card Item !");
			return ;
		}
		PlantType plant = item.plantType;
		if(plant.getUpgradeFrom().isPresent()) {
			tooltip.add(new TranslationTextComponent("tooltip.pvz." + plant.toString().toLowerCase() + "_card").withStyle(TextFormatting.RED));
		} else if(plant == PVZPlants.DOOM_SHROOM) {
			tooltip.add(new TranslationTextComponent("tooltip.pvz." + plant.toString().toLowerCase() + "_card").withStyle(TextFormatting.DARK_RED));
		}
	}
	
	/**
	 * get cooldown for current plant
	 */
	public static int getPlantCardCD(PlayerEntity player, ItemStack stack, PlantCardItem item, int plantLvl) {
		int cd = item.getPlantCardCD(player, stack, item.plantType, plantLvl);
		if (player.hasEffect(EffectRegister.EXCITE_EFFECT.get())) {
			int lvl = player.getEffect(EffectRegister.EXCITE_EFFECT.get()).getAmplifier();
			float mult = Math.max(0, 0.9f - 0.1f * lvl);
			cd = (int) Math.floor(cd * mult);
		}
		return cd;
	}
	
	public int getPlantCardCD(PlayerEntity player, ItemStack stack, PlantType plant, int lvl) {
		return plant.getCD().getCD(lvl);
	}

	public static boolean canPlantBreakOut(ItemStack stack) {
		return random.nextInt(100) < EnchantmentUtil.getPlantBreakOutChance(stack);
	}
	
	@Override
	public int getEnchantmentValue() {
		return plantType.getRank().enchantPoint;
	}
	
}
