package com.histler.cosplay2.data.repository

import com.histler.cosplay2.data.model.ScheduleNodeEntity
import io.reactivex.Observable

/**
 * Created by abadretdinov
 * on 06.08.2018
 */
interface EventsRemote {
    fun getSchedule(): Observable<List<ScheduleNodeEntity>>
}