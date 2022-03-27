package com.hungteen.pvz.common.impl.type;

import com.hungteen.pvz.PVZMod;
import com.hungteen.pvz.api.types.ICDType;
import com.hungteen.pvz.api.types.IRankType;
import com.hungteen.pvz.api.types.ISkillType;
import com.hungteen.pvz.api.types.base.IPAZType;
import com.hungteen.pvz.utils.AlgorithmUtil;
import com.hungteen.pvz.utils.Util;
import com.mojang.datafixers.util.Pair;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.RegistryEvent;

import java.util.*;
import java.util.function.Supplier;

/**
 * @program: pvzmod-1.18.x
 * @author: HungTeen
 * @create: 2022-03-26 15:53
 **/
public abstract class PAZType implements IPAZType {
    /* all registered types */
    private static final List<IPAZType> PAZS = new ArrayList<>();
    /* category -> paz type list */
    private static final Map<String, List<IPAZType>> CATEGORY_MAP = new HashMap<>();
    /* used to confirm no duplicate */
    private static final Set<IPAZType> PAZ_SET = new HashSet<>();
    /* paz type -> unique id.(dynamic each loading) */
    private static final Map<IPAZType, Integer> BY_ID = new HashMap<>();
    /* get type by name */
    private static final Map<String, IPAZType> BY_NAME = new HashMap<>();
    /* entity type -> paz type */
    private static final Map<EntityType<? extends Mob>, IPAZType> BY_ENTITY_TYPE = new HashMap<>();
    private final String name;
    protected int sunCost = 9999;
    protected int xpPoint = 0;
    protected ICDType coolDown = CDTypes.DEFAULT;
    protected IRankType rankType = RankTypes.GRAY;
    protected ResourceLocation entityRenderResource;
    protected ResourceLocation lootTable;
    protected Supplier<? extends Item> summonCardSup;
    protected Supplier<? extends Item> enjoyCardSup;
    protected Supplier<EntityType<? extends Mob>> entitySup;
    protected List<ISkillType> skills;

    protected PAZType(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public String getIdentity() {
        return Util.identify(this.getModID(), this.toString());
    }

    @Override
    public MutableComponent getText() {
        return new TranslatableComponent("entity." + this.getModID() + "." + this.toString());
    }

    @Override
    public int getSunCost() {
        return this.sunCost;
    }

    @Override
    public int getXpPoint() {
        return this.xpPoint;
    }

    @Override
    public ICDType getCoolDown() {
        return this.coolDown;
    }

    @Override
    public IRankType getRank() {
        return this.rankType;
    }

    @Override
    public Optional<EntityType<? extends Mob>> getEntityType() {
        return this.entitySup == null ? Optional.empty() : Optional.ofNullable(this.entitySup.get());
    }

    @Override
    public Optional<? extends Item> getSummonCard() {
        return this.summonCardSup == null ? Optional.empty() : Optional.ofNullable(this.summonCardSup.get());
    }

    @Override
    public Optional<? extends Item> getEnjoyCard() {
        return this.enjoyCardSup == null ? Optional.empty() : Optional.ofNullable(this.enjoyCardSup.get());
    }

    @Override
    public List<ISkillType> getSkills() {
        return skills;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public ResourceLocation getDefaultResource() {
        return this.entityRenderResource;
    }

    @Override
    public ResourceLocation getLootTable() {
        return lootTable;
    }

    public static List<IPAZType> getPAZs(){
        return Collections.unmodifiableList(PAZS);
    }

    /**
     * register type.
     */
    public static void registerPAZType(IPAZType type) {
        if(! PAZ_SET.contains(type)) {
            PAZ_SET.add(type);
            if(CATEGORY_MAP.containsKey(type.getCategoryName())) {
                CATEGORY_MAP.get(type.getCategoryName()).add(type);
            } else {
                CATEGORY_MAP.put(type.getCategoryName(), new ArrayList<>(Arrays.asList(type)));
            }
        } else {
            Util.warn("PAZ Register : already add {}.", type);
        }
    }

    /**
     * sort plants or zombies type by priority.
     * {@link com.hungteen.pvz.common.item.PVZItems#registerCards(RegistryEvent.Register)}
     */
    public static void initPAZs() {
        //clear list.
        PAZS.clear();
        //get priority category list.
        final List<Pair<String, Integer>> categoryList = new ArrayList<>();
        CATEGORY_MAP.keySet().forEach(l -> {
            final IPAZType tmp = CATEGORY_MAP.get(l).get(0);
            categoryList.add(Pair.of(l, tmp.getSortPriority()));
        });
        //sort category by priority.
        Collections.sort(categoryList, new AlgorithmUtil.PairSorter<>());
        //deal with each category list one by one.
        for(Pair<String, Integer> category : categoryList) {
            //get priority category list.
            final List<Pair<IPAZType, Integer>> tmp = new ArrayList<>();
            CATEGORY_MAP.get(category.getFirst()).forEach(l -> tmp.add(Pair.of(l, l.getSortPriority())));
            //sort list by priority.
            Collections.sort(tmp, new AlgorithmUtil.PairSorter<>());
            Util.debug("PAZ Register : sort category [{}] found {} types.", category.getFirst(), tmp.size());
            //add to the final result list.
            tmp.forEach(pair -> PAZS.add(pair.getFirst()));
        }
        for(int i = 0; i < PAZS.size(); ++ i) {
            BY_ID.put(PAZS.get(i), i);
            BY_NAME.put(PAZS.get(i).getIdentity(), PAZS.get(i));
        }
    }

    /**
     * to update the map from entity type to type.
     */
    public static void postInit() {
        PAZS.forEach(type -> {
            type.getEntityType().ifPresent(l -> {
                BY_ENTITY_TYPE.put(l, type);
            });
        });
    }

}
