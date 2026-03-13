package com.handsup.donamu.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class InsaHandler implements SpreadsheetHandler {
    private final SpreadsheetService spreadsheetService;

    public InsaHandler(SpreadsheetService spreadsheetService) {
        this.spreadsheetService = spreadsheetService;
    }

    @Override
    public List<List<Object>> read(String spreadsheetId, String range) throws IOException {
        // 인사평가 시트에서 데이터를 읽는 로직
        System.out.printf("Reading data for 인사평가: Range: %s%n", range);
        List<List<Object>> data = spreadsheetService.fetchDataFromSpreadsheet(spreadsheetId, range);
        System.out.printf("Fetched data: %s%n", data);
        return data;
    }

    @Override
    public void update(String spreadsheetId, String range, List<List<Object>> values) throws IOException {
        // 인사평가 시트 업데이트 로직
        System.out.printf("Updating 인사평가: Range: %s, Values: %s%n", range, values);
        // 실제 업데이트 로직 작성
    }

    @Override
    public void notifyChange(String range, Object value) {
        // 범위에서 행 번호 추출
        String rowNumber = range.replaceAll("[^0-9]", ""); // H15에서 15만 추출
        int row = Integer.parseInt(rowNumber); // 문자열을 정수로 변환

        // 체크할 열의 범위 (B, C, D, E, F) - 상반기
        String[] columnsBCF = {"B", "C", "D", "E", "F"};
        List<Object> rowDataBCF = new ArrayList<>();

        // 체크할 열의 범위 (H, I, J, K, L) - 하반기
        String[] columnsHCL = {"H", "I", "J", "K", "L"};
        List<Object> rowDataHCL = new ArrayList<>();

        // 여기서 시트 이름을 지정합니다.
        String sheetName = "참고. 인사평가"; // 사용할 실제 시트 이름으로 변경

        try {
            // 수정된 열이 상반기인지 하반기인지 확인
            boolean isUpperHalf = range.matches("[B-E][0-9]+") || range.matches("F[0-9]+");
            boolean isLowerHalf = range.matches("[H-L][0-9]+");

            // 상반기 데이터 가져오기
            if (isUpperHalf) {
                String dataRange1 = String.format("%s!%s%d:%s%d", sheetName, columnsBCF[0], row, columnsBCF[columnsBCF.length - 1], row);
                rowDataBCF = read("1SrjzmAbwZTSM95gedJGKP0xkCkGsh7Sxjg8TfyQvO3Q", dataRange1).get(0); // 첫 번째 행 데이터 가져오기

                // 수정된 값도 출력
                System.out.printf("Value modified at %s: %s%n", range, value);

                // 널 값 체크: B~E 열 (F는 체크하지 않음)
                boolean hasNullBCDE = rowDataBCF.stream()
                        .limit(3) // B, C, D, E 열만 체크
                        .anyMatch(cell -> cell == null || cell.toString().trim().isEmpty());

                // B~F 열 체크 결과 출력
                if (!hasNullBCDE) {
                    System.out.printf("Row %d B~F Data: %s%n", row, rowDataBCF);
                } else {
                    System.out.printf("Row %d B~E has null values, ignoring.%n", row);
                }
            }

            // 하반기 데이터 가져오기
            if (isLowerHalf) {
                String dataRange2 = String.format("%s!%s%d:%s%d", sheetName, columnsHCL[0], row, columnsHCL[columnsHCL.length - 1], row);
                rowDataHCL = read("1SrjzmAbwZTSM95gedJGKP0xkCkGsh7Sxjg8TfyQvO3Q", dataRange2).get(0); // H~L 열 데이터 가져오기

                // 수정된 값도 출력
                System.out.printf("Value modified at %s: %s%n", range, value);

                // 널 값 체크: H~L 열
                boolean hasNullHCL = rowDataHCL.stream()
                        .limit(3)
                        .anyMatch(cell -> cell == null || cell.toString().trim().isEmpty());

                // H~L 열 체크 결과 출력
                if (!hasNullHCL) {
                    System.out.printf("Row %d H~L Data: %s%n", row, rowDataHCL);
                } else {
                    System.out.printf("Row %d H~L has null values, ignoring.%n", row);
                }
            }

        } catch (IOException e) {
            System.err.printf("Error while reading data for row %d: %s%n", row, e.getMessage());
        } catch (IndexOutOfBoundsException e) {
            System.err.printf("No data found for row %d: %s%n", row, e.getMessage());
        }
    }
}

