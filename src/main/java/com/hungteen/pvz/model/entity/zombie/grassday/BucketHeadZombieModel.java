package com.hungteen.pvz.model.entity.zombie.grassday;

import com.hungteen.pvz.entity.zombie.grassday.BucketHeadZombieEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

// Made with Blockbench 3.7.1
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


public class BucketHeadZombieModel extends EntityModel<BucketHeadZombieEntity> {
	private final ModelRenderer total;
	private final ModelRenderer head;
	private final ModelRenderer bucket_r1;
	private final ModelRenderer bucket;
	private final ModelRenderer defence1;
	private final ModelRenderer bar_r1;
	private final ModelRenderer bar;
	private final ModelRenderer defence2;
	private final ModelRenderer defence3;
	private final ModelRenderer right_hand;
	private final ModelRenderer left_hand;
	private final ModelRenderer body;
	private final ModelRenderer right_leg;
	private final ModelRenderer left_leg;

	public BucketHeadZombieModel() {
		texWidth = 256;
		texHeight = 256;

		total = new ModelRenderer(this);
		total.setPos(0.0F, 24.0F, 0.0F);
		

		head = new ModelRenderer(this);
		head.setPos(0.0F, -49.0F, 0.0F);
		total.addChild(head);
		head.texOffs(16, 96).addBox(-8.0F, -15.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, false);

		bucket_r1 = new ModelRenderer(this);
		bucket_r1.setPos(0.0F, -13.0F, 0.0F);
		head.addChild(bucket_r1);
		setRotationAngle(bucket_r1, -0.0873F, 0.0F, 0.0F);
		

		bucket = new ModelRenderer(this);
		bucket.setPos(0.0F, -13.0F, 0.0F);
		head.addChild(bucket);
		setRotationAngle(bucket, -0.0873F, 0.0F, 0.0F);
		

		defence1 = new ModelRenderer(this);
		defence1.setPos(0.0F, 0.0F, 0.0F);
		bucket.addChild(defence1);
		defence1.texOffs(137, 2).addBox(-9.0F, -2.0F, -9.0F, 18.0F, 2.0F, 1.0F, 0.0F, false);
		defence1.texOffs(138, 8).addBox(-9.0F, -2.0F, 8.0F, 18.0F, 2.0F, 1.0F, 0.0F, false);
		defence1.texOffs(138, 15).addBox(-9.0F, -2.0F, -8.0F, 1.0F, 2.0F, 16.0F, 0.0F, true);
		defence1.texOffs(138, 38).addBox(8.0F, -2.0F, -8.0F, 1.0F, 2.0F, 16.0F, 0.0F, false);
		defence1.texOffs(184, 2).addBox(-9.0F, -7.0F, -9.0F, 18.0F, 5.0F, 18.0F, 0.0F, false);

		bar_r1 = new ModelRenderer(this);
		bar_r1.setPos(0.0F, -2.0F, -1.0F);
		defence1.addChild(bar_r1);
		setRotationAngle(bar_r1, 0.7854F, 0.0F, 0.0F);
		

		bar = new ModelRenderer(this);
		bar.setPos(0.0F, -2.0F, -1.0F);
		defence1.addChild(bar);
		setRotationAngle(bar, 0.7854F, 0.0F, 0.0F);
		bar.texOffs(182, 31).addBox(-11.0F, -1.0F, -15.0F, 22.0F, 1.0F, 1.0F, 0.0F, false);
		bar.texOffs(138, 60).addBox(-11.0F, -1.0F, -14.0F, 1.0F, 1.0F, 16.0F, 0.0F, false);
		bar.texOffs(137, 82).addBox(10.0F, -1.0F, -14.0F, 1.0F, 1.0F, 16.0F, 0.0F, false);
		bar.texOffs(181, 38).addBox(9.0F, -2.0F, 0.0F, 1.0F, 3.0F, 3.0F, 0.0F, false);
		bar.texOffs(195, 38).addBox(-10.0F, -2.0F, 0.0F, 1.0F, 3.0F, 3.0F, 0.0F, false);

		defence2 = new ModelRenderer(this);
		defence2.setPos(0.0F, 0.0F, 0.0F);
		bucket.addChild(defence2);
		defence2.texOffs(181, 48).addBox(-9.0F, -14.0F, -9.0F, 18.0F, 7.0F, 18.0F, 0.0F, false);

		defence3 = new ModelRenderer(this);
		defence3.setPos(0.0F, 0.0F, 0.0F);
		bucket.addChild(defence3);
		defence3.texOffs(182, 76).addBox(-9.0F, -20.0F, -9.0F, 18.0F, 6.0F, 18.0F, 0.0F, false);

		right_hand = new ModelRenderer(this);
		right_hand.setPos(8.0F, -48.0F, 0.0F);
		total.addChild(right_hand);
		right_hand.texOffs(96, 0).addBox(-24.0F, 0.0F, -4.0F, 8.0F, 24.0F, 8.0F, 0.0F, false);

		left_hand = new ModelRenderer(this);
		left_hand.setPos(-8.0F, -48.0F, 0.0F);
		total.addChild(left_hand);
		left_hand.texOffs(96, 60).addBox(16.0F, 0.0F, -4.0F, 8.0F, 24.0F, 8.0F, 0.0F, false);

		body = new ModelRenderer(this);
		body.setPos(0.0F, -25.0F, 0.0F);
		total.addChild(body);
		body.texOffs(0, 41).addBox(-8.0F, -23.0F, -4.0F, 16.0F, 24.0F, 8.0F, 0.0F, false);

		right_leg = new ModelRenderer(this);
		right_leg.setPos(4.0F, -24.0F, 0.0F);
		total.addChild(right_leg);
		right_leg.texOffs(44, 0).addBox(-12.0F, 0.0F, -4.0F, 8.0F, 24.0F, 8.0F, 0.0F, false);

		left_leg = new ModelRenderer(this);
		left_leg.setPos(-4.0F, -24.0F, 0.0F);
		total.addChild(left_leg);
		left_leg.texOffs(0, 0).addBox(4.0F, 0.0F, -4.0F, 8.0F, 24.0F, 8.0F, 0.0F, false);
	}

	@Override
	public void setupAnim(BucketHeadZombieEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		this.head.yRot = netHeadYaw / (180F / (float)Math.PI);
        this.head.xRot = headPitch / (180F / (float)Math.PI);
        this.left_leg.xRot = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.right_leg.xRot = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.right_hand.xRot = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.left_hand.xRot = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        float percent = entity.getDefenceLife() / BucketHeadZombieEntity.BUCKET_HEALTH;
        this.defence3.visible = percent > 2.0f / 3;
		this.defence2.visible = percent > 1.0f / 3;
		this.defence1.visible = percent > 0;
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		total.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}

}