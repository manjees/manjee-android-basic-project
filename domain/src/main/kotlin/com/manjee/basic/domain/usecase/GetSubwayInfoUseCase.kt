/**
 * Copyright 2025-02-19, 수, 20:5. Manjee. All rights reserved.
 *
 * @author JI UN MAN <manjee.official@gmail.com>
 * Description: 역 이름으로 해당 역 열차 도착 시간 가져오기
 *
 **/

package com.manjee.basic.domain.usecase

import com.manjee.basic.domain.repository.StationRepository
import javax.inject.Inject

class GetSubwayInfoUseCase @Inject constructor(
    private val stationRepository: StationRepository,
) {

    operator fun invoke(stationName: String) = stationRepository.getSubwayInfo(stationName)
}