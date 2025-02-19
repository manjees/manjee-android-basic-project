/**
 * Copyright 2025-02-19, 수, 20:2. Manjee. All rights reserved.
 *
 * @author JI UN MAN <manjee.official@gmail.com>
 * Description: 역 이름 관련 레포지토리
 *
 **/

package com.manjee.basic.domain.repository

interface StationRepository {
    fun getSubwayInfo(stationName: String)
}