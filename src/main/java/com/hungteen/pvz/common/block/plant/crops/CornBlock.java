package com.hungteen.pvz.common.block.plant.crops;

import com.hungteen.pvz.common.item.PVZItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

/**
 * @program: pvzmod-1.18.x
 * @author: HungTeen
 * @create: 2022-04-16 21:52
 **/
public class CornBlock extends CropBlock {

    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    private static final VoxelShape[] LOWER_SHAPE = new VoxelShape[] {
            Block.box(4.0D, 0.0D, 4.0D, 12.0D, 8.0D, 12.0D),
            Block.box(4.0D, 0.0D, 4.0D, 12.0D, 14.0D, 12.0D),
            Block.box(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D),
            Block.box(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D)
    };
    private static final VoxelShape[] UPPER_SHAPE = new VoxelShape[] {
            Block.box(2.0D, 0.0D, 2.0D, 14.0D, 0.0D, 14.0D),
            Block.box(2.0D, 0.0D, 2.0D, 14.0D, 0.0D, 14.0D),
            Block.box(2.0D, 0.0D, 2.0D, 14.0D, 8.0D, 14.0D),
            Block.box(2.0D, 0.0D, 2.0D, 14.0D, 14.0D, 14.0D)
    };

    public CornBlock() {
        super(Block.Properties.copy(Blocks.WHEAT));
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(this.getAgeProperty(), Integer.valueOf(0))
                .setValue(HALF, DoubleBlockHalf.LOWER)
        );
    }

    /**
     * modify from super method.
     */
    @Override
    public void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, Random random) {
        if (!serverLevel.isAreaLoaded(blockPos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
        if (! isUpperState(blockState) && serverLevel.getRawBrightness(blockPos, 0) >= 9 && random.nextFloat() < 0.25F) {
            int i = this.getAge(blockState);
            if (i < this.getMaxAge()) {
                float f = getGrowthSpeed(this, serverLevel, blockPos);
                if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(serverLevel, blockPos, blockState, random.nextInt((int)(25.0F / f) + 1) == 0)) {
                    this.onGrow(serverLevel, blockState, blockPos, i + 1);
                    net.minecraftforge.common.ForgeHooks.onCropsGrowPost(serverLevel, blockPos, blockState);
                }
            }
        }
    }

    /**
     * Copy from {@link net.minecraft.world.level.block.DoublePlantBlock}.
     */
    @Override
    public BlockState updateShape(BlockState selfState, Direction direction, BlockState blockState, LevelAccessor levelAccessor, BlockPos selfPos, BlockPos blockPos) {
        DoubleBlockHalf blockHalf = selfState.getValue(HALF);
        if (direction.getAxis() != Direction.Axis.Y || blockHalf == DoubleBlockHalf.LOWER != (direction == Direction.UP) || blockState.is(this) && blockState.getValue(HALF) != blockHalf) {
            return blockHalf == DoubleBlockHalf.LOWER && direction == Direction.DOWN && !selfState.canSurvive(levelAccessor, selfPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(selfState, direction, blockState, levelAccessor, selfPos, blockPos);
        } else {
            return Blocks.AIR.defaultBlockState();
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockpos = context.getClickedPos();
        Level level = context.getLevel();
        return blockpos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(blockpos.above()).canBeReplaced(context) ? super.getStateForPlacement(context) : null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState state, LivingEntity entity, ItemStack itemStack) {
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos blockPos, BlockState state, Player player) {
        if (!level.isClientSide) {
            if (player.isCreative()) {
                preventCreativeDropFromBottomPart(level, blockPos, state, player);
            } else {
                dropResources(state, level, blockPos, null, player, player.getMainHandItem());
            }
        }

        super.playerWillDestroy(level, blockPos, state, player);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos blockPos, BlockState state, @javax.annotation.Nullable BlockEntity blockEntity, ItemStack stack) {
        super.playerDestroy(level, player, blockPos, Blocks.AIR.defaultBlockState(), blockEntity, stack);
    }

    protected static void preventCreativeDropFromBottomPart(Level level, BlockPos blockPos, BlockState state, Player player) {
        DoubleBlockHalf doubleblockhalf = state.getValue(HALF);
        if (doubleblockhalf == DoubleBlockHalf.UPPER) {
            BlockPos blockpos = blockPos.below();
            BlockState blockstate = level.getBlockState(blockpos);
            if (blockstate.is(state.getBlock()) && blockstate.getValue(HALF) == DoubleBlockHalf.LOWER) {
                BlockState blockstate1 = blockstate.hasProperty(BlockStateProperties.WATERLOGGED) && blockstate.getValue(BlockStateProperties.WATERLOGGED) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
                level.setBlock(blockpos, blockstate1, 35);
                level.levelEvent(player, 2001, blockpos, Block.getId(blockstate));
            }
        }

    }

    public static void placeAt(LevelAccessor levelAccessor, BlockState blockState, BlockPos blockPos, int flag) {
        BlockPos blockpos = blockPos.above();
        levelAccessor.setBlock(blockPos, copyWaterloggedFrom(levelAccessor, blockPos, blockState.setValue(HALF, DoubleBlockHalf.LOWER)), flag);
        levelAccessor.setBlock(blockpos, copyWaterloggedFrom(levelAccessor, blockpos, blockState.setValue(HALF, DoubleBlockHalf.UPPER)), flag);
    }

    public static BlockState copyWaterloggedFrom(LevelReader reader, BlockPos blockPos, BlockState blockState) {
        return blockState.hasProperty(BlockStateProperties.WATERLOGGED) ? blockState.setValue(BlockStateProperties.WATERLOGGED, reader.isWaterAt(blockPos)) : blockState;
    }

    /**
     * Modify from {@link net.minecraft.world.level.block.DoublePlantBlock}.
     */
    @Override
    public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
        if (! isUpperState(state)) {
            return super.canSurvive(state, reader, pos);
        } else {
            BlockState blockstate = reader.getBlockState(pos.below());
            System.out.println("Why ?");
            if (state.getBlock() != this) return super.canSurvive(state, reader, pos); //Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
            return blockstate.is(this) && blockstate.getValue(HALF) == DoubleBlockHalf.LOWER;
        }
    }

    @Override
    public boolean isValidBonemealTarget(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState, boolean flag) {
        return !isUpperState(blockState) && super.isValidBonemealTarget(blockGetter, blockPos, blockState, flag);
    }

    /**
     * modify from super method.
     */
    @Override
    public void growCrops(Level level, BlockPos blockPos, BlockState blockState) {
        int i = this.getAge(blockState) + this.getBonemealAgeIncrease(level);
        int j = this.getMaxAge();
        if (i > j) {
            i = j;
        }
        this.onGrow(level, blockState, blockPos, i);
    }

    @Override
    protected int getBonemealAgeIncrease(Level level) {
        return super.getBonemealAgeIncrease(level) / 3;
    }

    protected void onGrow(Level level, BlockState state, BlockPos pos, int age){
        if(! isUpperState(state)){
            level.setBlock(pos, this.getStateForAge(age, false), 2);
            if(canGrowUpper(level.getBlockState(pos))) {
            	level.setBlock(pos.above(), this.getStateForAge(age, true), 2);
            }
        }
    }

    public static boolean isUpperState(BlockState state){
        return state.getValue(HALF) == DoubleBlockHalf.UPPER;
    }

    protected boolean canGrowUpper(BlockState state){
        return state.getValue(AGE) >= 2;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE, HALF);
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return 3;
    }

    @Override
    public OffsetType getOffsetType() {
        return OffsetType.NONE;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        final int age = state.getValue(this.getAgeProperty());
        return isUpperState(state) ? UPPER_SHAPE[age] : LOWER_SHAPE[age];
    }

    public BlockState getStateForAge(int age, boolean upper) {
        return this.defaultBlockState()
                .setValue(this.getAgeProperty(), Integer.valueOf(age))
                .setValue(HALF, upper ? DoubleBlockHalf.UPPER : DoubleBlockHalf.LOWER);
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return PVZItems.CORN_SEEDS.get();
    }
}
