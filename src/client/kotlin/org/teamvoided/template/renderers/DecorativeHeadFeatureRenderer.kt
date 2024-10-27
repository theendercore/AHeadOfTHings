package org.teamvoided.template.renderers

import io.wispforest.accessories.api.AccessoriesCapability
import io.wispforest.accessories.api.slot.SlotTypeReference
import net.minecraft.block.AbstractSkullBlock
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.SkullBlockEntityRenderer
import net.minecraft.client.render.entity.feature.FeatureRenderer
import net.minecraft.client.render.entity.feature.FeatureRendererContext
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.render.entity.model.EntityModelLoader
import net.minecraft.client.render.entity.model.ModelWithHead
import net.minecraft.client.render.item.HeldItemRenderer
import net.minecraft.client.render.model.json.ModelTransformationMode
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.component.DataComponentTypes
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.BlockItem
import net.minecraft.util.math.Axis

class DecorativeHeadFeatureRenderer<T : LivingEntity, M>(
    context: FeatureRendererContext<T, M>, modelLoader: EntityModelLoader,
    private val scaleX: Float, private val scaleY: Float, private val scaleZ: Float,
    private val heldItemRenderer: HeldItemRenderer
) : FeatureRenderer<T, M>(context) where M : EntityModel<T> {
    private val headModels = SkullBlockEntityRenderer.getModels(modelLoader)

    constructor(
        context: FeatureRendererContext<T, M>, modelLoader: EntityModelLoader, heldItemRenderer: HeldItemRenderer
    ) : this(context, modelLoader, 1.0f, 1.0f, 1.0f, heldItemRenderer)

    init {
        if (contextModel !is ModelWithHead) error("DecorativeHeadFeatureRenderer: Context model is not a ModelWithHead!")
    }

    override fun render(
        matrices: MatrixStack, vertexConsumers: VertexConsumerProvider,
        light: Int, player: T?, limbAngle: Float, limbDistance: Float,
        tickDelta: Float, animationProgress: Float, headYaw: Float, headPitch: Float
    ) {
        if (player == null) return
        if (player !is PlayerEntity) return

        val capability = AccessoriesCapability.get(player) ?: return
        val headSlot = capability.getContainer(SlotTypeReference("head")) ?: return
        val itemStack = headSlot.accessories.firstOrNull()?.second ?: return
        if (itemStack.isEmpty) return

        val item = itemStack.item
        matrices.push()
        matrices.scale(this.scaleX, this.scaleY, this.scaleZ)
        (contextModel as ModelWithHead).head.rotate(matrices)

        if (item is BlockItem && item.block is AbstractSkullBlock) {
            matrices.scale(1.1875f, -1.1875f, -1.1875f)

            matrices.translate(-0.5, 0.0, -0.5)
            val skullType = (item.block as AbstractSkullBlock).skullType
            val renderLayer =
                SkullBlockEntityRenderer.getRenderLayer(skullType, itemStack.get(DataComponentTypes.PROFILE))
            val vehicle = player.vehicle
            val calcAngle = (if (vehicle is LivingEntity) vehicle else player).limbData.getLimbAngle(tickDelta)
            SkullBlockEntityRenderer.renderSkull(
                null, 180.0f, calcAngle, matrices, vertexConsumers, light,
                headModels[skullType], renderLayer
            )
        } else {
            translate(matrices)
            heldItemRenderer.renderItem(
                player,
                itemStack,
                ModelTransformationMode.HEAD,
                false,
                matrices,
                vertexConsumers,
                light
            )
        }
        matrices.pop()
    }

    companion object {
        fun translate(matrices: MatrixStack) {
            val scale = 0.625f
            matrices.translate(0.0f, -0.25f, 0.0f)
            matrices.rotate(Axis.Y_POSITIVE.rotationDegrees(180.0f))
            matrices.scale(scale, -scale, -scale)
        }
    }
}