package com.handsup.donamu.controller;

import com.handsup.donamu.service.SpreadsheetService;
import com.handsup.donamu.service.SpreadsheetHandler;
import com.handsup.donamu.service.InsaHandler;
import com.handsup.donamu.service.LevelExperienceHandler;
import com.handsup.donamu.service.MemberInformationHandler;
import com.handsup.donamu.service.YearExperienceHandler;
import com.handsup.donamu.service.QuestJobHandler;
import com.handsup.donamu.service.LeaderQuestHandler;
import com.handsup.donamu.service.WorkHandler;
import com.handsup.donamu.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;


@RestController
@RequestMapping("/api/spreadsheet")
public class SpreadsheetController {
    private final SpreadsheetService spreadsheetService; // SpreadsheetService 추가
    private final Map<String, SpreadsheetHandler> handlers = new HashMap<>();

    @Autowired
    public SpreadsheetController(SpreadsheetService spreadsheetService, InsaHandler insaHandler,
                                 LevelExperienceHandler levelExperienceHandler,
                                 MemberInformationHandler memberInformationHandler,
                                 YearExperienceHandler yearExperienceHandler,
                                 QuestJobHandler questJobHandler,
                                 LeaderQuestHandler leaderQuestHandler,
                                 WorkHandler workHandler) {
        this.spreadsheetService = spreadsheetService; // 초기화
        // 핸들러 매핑
        handlers.put("참고. 인사평가", insaHandler);
        handlers.put("참고. 레벨별 경험치", levelExperienceHandler);
        handlers.put("참고. 구성원 정보", memberInformationHandler);
        handlers.put("참고. 올해 경험치", yearExperienceHandler);
        handlers.put("참고. 직무별 퀘스트", questJobHandler);
        handlers.put("참고. 리더부여 퀘스트", leaderQuestHandler);
        handlers.put("참고. 전사 프로젝트", workHandler);
        // 추가 핸들러를 여기에 추가하면 됩니다.
    }

    @GetMapping("/read")
    public List<List<Object>> readSpreadsheet(@RequestParam String spreadsheetId, @RequestParam String range) {
        try {
            return spreadsheetService.fetchDataFromSpreadsheet(spreadsheetId, range); // 수정된 부분
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/update")
    public void updateSpreadsheet(@RequestParam String spreadsheetId, @RequestParam String range, @RequestBody List<List<Object>> values) {
        String sheetName = extractSheetName(range); // range에서 시트 이름을 추출
        SpreadsheetHandler handler = handlers.get(sheetName);

        if (handler != null) {
            try {
                handler.update(spreadsheetId, range, values);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.printf("Unknown sheet: %s%n", sheetName);
        }
    }

    @PostMapping("/notify")
    public void notifySpreadsheetChange(@RequestBody Map<String, Object> notification) {
        System.out.println("notification : " + notification);
        if (notification == null) {
            System.out.println("Received null notification");
            return;
        }

        String sheetName = (String) notification.get("sheetName");
        String range = (String) notification.get("range");
        Object value = notification.get("value");

        SpreadsheetHandler handler = handlers.get(sheetName);
        if (handler != null) {
            handler.notifyChange(range, value);
        } else {
            System.out.printf("Unknown sheet: %s, Range: %s, New Value: %s%n", sheetName, range, value);
        }
    }

    private String extractSheetName(String range) {
        String sheetName = range.split("!")[0]; // 예: "참고. 인사평가!A1:A10" -> "참고. 인사평가"
        System.out.println("Extracted sheet name: " + sheetName); // 로그 추가
        return sheetName;
    }

}