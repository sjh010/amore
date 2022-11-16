package com.apgroup.pms.thread;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 타이머 쓰레드
 * 
 * 기본 설정 : 2022년 08월 01일 09시 00분
 */
@Slf4j
@Component
public class WorkTimer implements Runnable {
	
	private static LocalDateTime time = LocalDateTime.of(2022, 8, 1, 9, 0, 0);

	@Override
	public void run() {
		
		while(true) {
			try {
				if (isWorkTime()) { // 근무시간동안은 1분을 1초라고 가정
					Thread.sleep(1000);
					time = time.plusMinutes(1);
				} else { // 비근무시간동안은 16시간을 30초로 가정
					Thread.sleep(30000);
					time = time.plusHours(16);
					ProductionLine.initTodayProduction();
				}
			} catch (InterruptedException e) {
				log.error("WorkTimer is dead", e);
			}	
		}
		
	}
	
	/**
	 * 근무시간 여부 확인
	 */
	public static boolean isWorkTime() {
		return (time.getHour() >= 9 && time.getHour() < 17);
	}
	
	/**
	 * 원료 충전 가능한지 여부
	 */	
	public static boolean isAbleCharge() {
		LocalDateTime futureTime = time.plusMinutes(40);
		LocalDateTime workEndTime = LocalDateTime.of(time.getYear(), time.getMonthValue(), time.getDayOfMonth(), 17, 0);
		
		return futureTime.isBefore(workEndTime);
	}
	
	/**
	 * 남아있는 작업 시간(분) 조회
	 * @return
	 */
	public static long remainWorkMinute() {
		LocalDateTime workEndTime = LocalDateTime.of(time.getYear(), time.getMonthValue(), time.getDayOfMonth(), 17, 0);
		
		Duration duration = Duration.between(time, workEndTime);
		
		return duration.toMinutes(); 
	}
	
	public static String getDate(int plusDay) {
		return time.plusDays(plusDay).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
	}
	
	public static String getCurrentTime() {
		return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}

}
