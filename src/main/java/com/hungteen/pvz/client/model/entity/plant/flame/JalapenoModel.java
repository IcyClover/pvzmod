package com.hungteen.pvz.client.model.entity.plant.flame;

import com.hungteen.pvz.client.model.entity.plant.PVZPlantModel;
import com.hungteen.pvz.common.entity.plant.flame.JalapenoEntity;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

// Made with Blockbench 3.6.6
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


public class JalapenoModel extends PVZPlantModel<JalapenoEntity> {
	private final ModelRenderer body;
	private final ModelRenderer bone;
	private final ModelRenderer bone2;
	private final ModelRenderer bone3;
	private final ModelRenderer bone4;
	private final ModelRenderer bone5;
	private final ModelRenderer bone6;
	private final ModelRenderer bone7;

	public JalapenoModel() {
		texWidth = 128;
		texHeight = 128;

		body = new ModelRenderer(this);
		body.setPos(0.0F, 24.0F, 2.0F);
		body.texOffs(63, 94).addBox(-8.0F, -40.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, false);
		body.texOffs(69, 76).addBox(-7.0F, -24.0F, -7.0F, 14.0F, 2.0F, 14.0F, 0.0F, false);
		body.texOffs(78, 58).addBox(-6.0F, -22.0F, -6.0F, 12.0F, 3.0F, 12.0F, 0.0F, false);
		body.texOffs(88, 42).addBox(-5.0F, -19.0F, -5.0F, 10.0F, 4.0F, 10.0F, 0.0F, false);
		body.texOffs(95, 24).addBox(-4.0F, -15.0F, -5.0F, 8.0F, 5.0F, 8.0F, 0.0F, false);
		body.texOffs(103, 9).addBox(-3.0F, -10.0F, -6.0F, 5.0F, 4.0F, 7.0F, 0.0F, false);
		body.texOffs(75, 2).addBox(-3.0F, -6.0F, -8.0F, 6.0F, 3.0F, 7.0F, 0.0F, false);
		body.texOffs(67, 15).addBox(-3.0F, -3.0F, -12.0F, 6.0F, 3.0F, 8.0F, 0.0F, false);
		body.texOffs(0, 0).addBox(-7.0F, -41.0F, -7.0F, 14.0F, 1.0F, 14.0F, 0.0F, false);
		body.texOffs(0, 0).addBox(-6.0F, -42.0F, -6.0F, 12.0F, 1.0F, 12.0F, 0.0F, false);

		bone = new ModelRenderer(this);
		bone.setPos(0.0F, -3.0F, -12.0F);
		body.addChild(bone);
		setRotationAngle(bone, 1.0472F, 0.0F, 0.0F);
		bone.texOffs(76, 30).addBox(-2.0F, -3.0F, -1.0F, 4.0F, 4.0F, 1.0F, 0.0F, false);

		bone2 = new ModelRenderer(this);
		bone2.setPos(0.0F, -3.0F, 0.0F);
		bone.addChild(bone2);
		setRotationAngle(bone2, 0.6981F, 0.0F, 0.0F);
		bone2.texOffs(68, 42).addBox(-2.0F, -0.6428F, -0.766F, 4.0F, 1.0F, 4.0F, 0.0F, false);

		bone3 = new ModelRenderer(this);
		bone3.setPos(0.0F, -0.0152F, 2.8264F);
		bone2.addChild(bone3);
		setRotationAngle(bone3, 0.9599F, 0.0F, 0.0F);
		bone3.texOffs(75, 52).addBox(-1.0F, -0.0261F, -0.2521F, 2.0F, 4.0F, 1.0F, 0.0F, false);

		bone4 = new ModelRenderer(this);
		bone4.setPos(0.0F, -42.0F, 0.0F);
		body.addChild(bone4);
		setRotationAngle(bone4, 0.0F, 0.0F, 0.1745F);
		bone4.texOffs(0, 0).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 7.0F, 2.0F, 0.0F, false);

		bone5 = new ModelRenderer(this);
		bone5.setPos(1.2247F, -5.2929F, 0.0F);
		bone4.addChild(bone5);
		setRotationAngle(bone5, 0.0F, 0.0F, -0.4363F);
		bone5.texOffs(0, 0).addBox(-1.7175F, -1.5811F, -1.0F, 6.0F, 2.0F, 2.0F, 0.0F, false);

		bone6 = new ModelRenderer(this);
		bone6.setPos(4.6375F, 2.1433F, 0.0F);
		bone5.addChild(bone6);
		setRotationAngle(bone6, 0.0F, 0.0F, -0.6109F);
		bone6.texOffs(0, 0).addBox(-0.1546F, -3.2544F, -1.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);

		bone7 = new ModelRenderer(this);
		bone7.setPos(0.7071F, -0.1213F, 0.0F);
		bone6.addChild(bone7);
		setRotationAngle(bone7, 0.0F, 0.0F, -1.9199F);
		bone7.texOffs(0, 0).addBox(-1.204F, -3.2268F, -1.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);
	}

	@Override
	public void setupAnim(JalapenoEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
	}

	@Override
	public ModelRenderer getPlantWholeBody() {
		return this.body;
	}

	@Override
	public EntityModel<JalapenoEntity> getPlantModel() {
		return this;
	}
}