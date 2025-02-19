package com.manjee.basic.domain.usecase

import com.manjee.basic.domain.repository.StationRepository
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class GetSubwayInfoUseCaseTest {

    private lateinit var stationRepository: StationRepository
    private lateinit var getSubwayInfoUseCase: GetSubwayInfoUseCase

    @Before
    fun setup() {
        stationRepository = mockk(relaxed = true)
        getSubwayInfoUseCase = GetSubwayInfoUseCase(stationRepository)
    }

    @Test
    fun `invoke should call stationRepository getSubwayInfo`() {
        // given
        val stationName = "Some Station"

        // when
        getSubwayInfoUseCase(stationName)

        // then
        verify(exactly = 1) { stationRepository.getSubwayInfo(stationName) }
    }
}