
package com.handsup.donamu.service;

import java.io.IOException;
import java.util.List;

public interface SpreadsheetHandler {
    List<List<Object>> read(String spreadsheetId, String range) throws IOException;
    void update(String spreadsheetId, String range, List<List<Object>> values) throws IOException;
    void notifyChange(String range, Object value);
}
