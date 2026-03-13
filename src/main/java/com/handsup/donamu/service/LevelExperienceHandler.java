
package com.handsup.donamu.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class LevelExperienceHandler implements SpreadsheetHandler {
    @Override
    public List<List<Object>> read(String spreadsheetId, String range) throws IOException {
        // 레벨별 경험치 시트에서 데이터를 읽는 로직
        System.out.printf("Reading data for 레벨별 경험치: Range: %s%n", range);
        // 실제 데이터 반환 로직 작성
        return null; // 실제 반환할 데이터로 교체
    }

    @Override
    public void update(String spreadsheetId, String range, List<List<Object>> values) throws IOException {
        // 레벨별 경험치 시트 업데이트 로직
        System.out.printf("Updating 레벨별 경험치: Range: %s, Values: %s%n", range, values);
        // 실제 업데이트 로직 작성
    }

    @Override
    public void notifyChange(String range, Object value) {
        // 레벨별 경험치 시트에서의 변경 사항 처리
        System.out.printf("Notifying 레벨별 경험치: Range: %s, New Value: %s%n", range, value);
    }
}


