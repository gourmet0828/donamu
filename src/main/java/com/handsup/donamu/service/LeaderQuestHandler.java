package com.handsup.donamu.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service

public class LeaderQuestHandler implements SpreadsheetHandler{

    private final SpreadsheetService spreadsheetService;
    public LeaderQuestHandler(SpreadsheetService spreadsheetService){
        this.spreadsheetService = spreadsheetService;
    }

    @Override
    public List<List<Object>> read(String spreadsheetId, String range) throws IOException {
        // 레벨별 경험치 시트에서 데이터를 읽는 로직
        System.out.printf("Reading data for 리더부여 퀘스트: Range: %s%n", range);
        List<List<Object>> data = spreadsheetService.fetchDataFromSpreadsheet(spreadsheetId, range);
        System.out.printf("Fetched data: %s%n", data);
        return data;
    }

    @Override
    public void update(String spreadsheetId, String range, List<List<Object>> values) throws IOException {
        // 레벨별 경험치 시트 업데이트 로직
        System.out.printf("Updating 리더부여 퀘스트: Range: %s, Values: %s%n", range, values);
        // 실제 업데이트 로직 작성
    }

    @Override
    public void notifyChange(String range, Object value) {
        // 범위에서 행 번호 추출
        String rowNumber = range.replaceAll("[^0-9]", ""); // H15에서 15만 추출
        int row = Integer.parseInt(rowNumber); // 문자열을 정수로 변환

        // 체크할 열의 범위 (B, C, D, E, F, G, H)
        String[] columns = {"B", "C", "D", "E", "F", "G", "H", "I"};
        List<Object> rowData = new ArrayList<>();

        // 여기서 시트 이름을 지정합니다.
        String sheetName = "참고. 리더부여 퀘스트"; // 사용할 실제 시트 이름으로 변경

        try {
            // 각 열의 데이터를 가져오기 위해 range 설정 (시트 이름 포함)
            String dataRange = String.format("%s!%s%d:%s%d", sheetName, columns[0], row, columns[columns.length - 1], row);

            // read 메서드 호출
            rowData = read("1SrjzmAbwZTSM95gedJGKP0xkCkGsh7Sxjg8TfyQvO3Q", dataRange).get(0); // 첫 번째 행 데이터 가져오기

            // 수정된 값도 출력
            System.out.printf("Value modified at %s: %s%n", range, value);

            // 널 값 체크: rowData의 모든 값이 널이나 빈 문자열인지 확인
            boolean hasNull = rowData.stream()
                    .limit(7) // B~G열만 체크
                    .anyMatch(cell -> cell == null || cell.toString().trim().isEmpty());

            if (!hasNull) {
                // 널 값이 없을 경우 데이터 출력
                System.out.printf("Row %d Data: %s%n", row, rowData);
            } else {
                System.out.printf("Row %d has null values, ignoring.%n", row);
            }
        } catch (IOException e) {
            System.err.printf("Error while reading data for row %d: %s%n", row, e.getMessage());
        } catch (IndexOutOfBoundsException e) {
            System.err.printf("No data found for row %d: %s%n", row, e.getMessage());
        }
    }

}
