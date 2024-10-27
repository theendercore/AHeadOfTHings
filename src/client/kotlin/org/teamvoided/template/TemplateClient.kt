package org.teamvoided.template

import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback
import net.minecraft.client.render.entity.feature.FeatureRendererContext
import net.minecraft.entity.EntityType
import org.teamvoided.template.Template.log
import org.teamvoided.template.renderers.DecorativeHeadFeatureRenderer

@Suppress("unused")
object TemplateClient {
    fun init() {
        log.info("Hello from Client")

        LivingEntityFeatureRendererRegistrationCallback.EVENT.register { entityType, entityRenderer, registrationHelper, context ->
            if (entityType != EntityType.PLAYER) return@register
            if (entityRenderer !is FeatureRendererContext<*, *>) return@register
            registrationHelper.register(
                DecorativeHeadFeatureRenderer(entityRenderer, context.modelLoader, context.heldItemRenderer)
            )
        }
    }
}
