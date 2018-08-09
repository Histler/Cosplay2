package com.histler.cosplay2.cache.model

import android.arch.persistence.room.Embedded

/**
 * Created by abadretdinov
 * on 09.08.2018
 */
data class CachedParticipantWithNomination(
        @Embedded
        var participant: CachedParticipant,
        @Embedded
        var participantNomination: CachedParticipantNomination,
        @Embedded
        var nomination: CachedNomination
)