package com.hungteen.pvz.api.types.base;

import com.hungteen.pvz.api.types.*;
import com.hungteen.pvz.api.types.base.IIDType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.Optional;

/**
 * @program: pvzmod-1.18.x
 * @author: HungTeen
 * @create: 2022-03-26 15:17
 *
 * Why there is "PAZ" not "PVZ"? <br>
 * PAZ means Plants and Zombies, it stands for a common class for both plants and zombies.
 **/
public interface IPAZType extends IIDType {

    /**
     * get the sun cost of role.
     * how many sun amount will cost when use its card.
     */
    int getSunCost();

    /**
     * get the experience point of role.
     */
    int getXpPoint();

    /**
     * get the cool down of summon card of current type.
     */
    ICDType getCoolDown();

    /**
     * get the rank of type.
     */
    IRankType getRank();

    /**
     * get the essence type that the plant belongs to.
     */
    IEssenceType getEssence();

    /**
     * get the entity type of current type.
     */
    Optional<EntityType<? extends Mob>> getEntityType();

    /**
     * get summon card item of type.
     */
    Optional<? extends Item> getSummonCard();

    /**
     * get enjoy card item of type.
     */
    Optional<? extends Item> getEnjoyCard();

    /**
     * what type the plant upgrade from.
     */
    Optional<IPAZType> getUpgradeFrom();

    /**
     * what type the plant upgrade to.
     */
    Optional<IPAZType> getUpgradeTo();

    /**
     * all skills of type.
     */
    List<ISkillType> getSkills();

    /**
     * get type corresponding id in type list.
     * used to sort card item list.
     */
    int getId();

    /**
     * the priority of this group to stay forward in list. <br>
     * larger means higher priority. <br>
     * such as show front in Almanac.
     */
    int getSortPriority();

    /**
     * category of the group of type.
     */
    String getCategoryName();

    /**
     * default render texture.
     */
    @OnlyIn(Dist.CLIENT)
    ResourceLocation getDefaultResource();

    ResourceLocation getLootTable();

}