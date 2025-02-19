/**
 * Copyright 2025-02-19, 수, 20:2. Manjee. All rights reserved.
 *
 * @author JI UN MAN <manjee.official@gmail.com>
 * Description: 역 이름 관련 레포지토리
 *
 **/

package com.manjee.basic.domain.repository

import com.manjee.basic.domain.model.SubwayStation
import com.manjee.data_resource.DataResource
import kotlinx.coroutines.flow.Flow

interface StationRepository {
    fun getSubwayInfo(stationName: String): Flow<DataResource<List<SubwayStation>>>
}