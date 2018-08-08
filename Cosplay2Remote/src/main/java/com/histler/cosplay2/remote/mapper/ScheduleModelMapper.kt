package com.histler.cosplay2.remote.mapper

import com.histler.base.remote.mapper.ModelMapper
import com.histler.cosplay2.data.repository.UrlProvider
import com.histler.cosplay2.domain.model.ScheduleCard
import com.histler.cosplay2.domain.model.ScheduleCardType
import com.histler.cosplay2.remote.model.ListTopicCard
import com.histler.cosplay2.remote.model.ScheduleNode
import com.histler.cosplay2.remote.model.ScheduleType
import javax.inject.Inject

/**
 * Created by abadretdinov
 * on 08.08.2018
 */
class ScheduleModelMapper @Inject constructor(private val urlProvider: UrlProvider) : ModelMapper<Pair<ScheduleNode, ListTopicCard?>, ScheduleCard> {
    override fun mapFromModel(model: Pair<ScheduleNode, ListTopicCard?>): ScheduleCard {
        val node = model.first
        val card = model.second
        return ScheduleCard(
                card?.id ?: 0L,
                node.title,
                if (card != null) getImageUrl(card) else null,
                when (node.type) {
                    ScheduleType.DAY -> ScheduleCardType.DAY
                    ScheduleType.PLACE -> ScheduleCardType.BLOCK
                    ScheduleType.EVENT -> ScheduleCardType.PARTICIPANT
                    ScheduleType.REQUEST -> TODO()
                },
                node.startTime
        )
    }

    private fun getImageUrl(card: ListTopicCard): String {
        return if (card.image != null) {
            urlProvider.getBaseFestivalUrl() + "uploads/" + urlProvider.getBaseFestivalUrl() + "/" + card.id + "/" + card.image.fileName + "_large.jpg"
        } else {
            urlProvider.getBaseFestivalUrl() + "images/avatars/" + card.userId + "_full.png"
        }
    }
}