/**
 * Copyright 2025-02-19, 수, 19:52. Manjee. All rights reserved.
 *
 * @author JI UN MAN <manjee.official@gmail.com>
 * Description: 지하철 데이터 클래스 (Domain)
 *
 **/
package com.manjee.basic.domain.model

/**
 * @param isUpLine 상행선 여부 (true: 상행, false: 하행)
 * @param trainLineName 열차 노선명 (ex: "신사행 - 신논현방면")
 * @param stationName 역 이름 (ex: "강남")
 * @param trainCategory 열차 종류 (ex: "급행", "완행", 등등)
 * @param arrivedTime 도착 시간(초 단위) (ex: "160")
 * @param lastStation 종착역 (ex: "신사")
 * @param arrivedCode 도착 코드 (0:진입, 1:도착, 2:출발, 3:전역출발, 4:전역진입, 5:전역도착, 99:운행중)
 * @param isLastTrain 마지막 열차 (true: 마지막, false: 마지막 아님)
 */
data class SubwayStation(
    val subwayId: String,
    val isUpLine: Boolean,
    val trainLineName: String,
    val stationName: String,
    val trainCategory: String,
    val arrivedTime: String,
    val lastStation: String,
    val arrivedCode: String,
    val isLastTrain: Boolean
)