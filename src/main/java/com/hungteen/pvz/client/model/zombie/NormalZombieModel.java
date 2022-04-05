package com.hungteen.pvz.client.model.zombie;

import com.hungteen.pvz.common.entity.zombie.NormalZombie;
import net.minecraft.client.model.geom.ModelPart;

/**
 * @program: pvzmod-1.18.x
 * @author: HungTeen
 * @create: 2022-04-05 09:51
 **/
public class NormalZombieModel<T extends NormalZombie> extends PVZZombieModel<T> {
    public NormalZombieModel(ModelPart modelPart) {
        super(modelPart);
    }
}
