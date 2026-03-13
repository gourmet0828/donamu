package com.handsup.donamu.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public class GoogleSheetsService {
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private final Sheets sheetsService;

    public GoogleSheetsService(Credential credentials) throws GeneralSecurityException, IOException {
        this.sheetsService = new Sheets.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credentials)
                .setApplicationName("Google Sheets API Java Quickstart")
                .build();
    }

    public List<List<Object>> getDataFromSheet(String spreadsheetId, String range) throws IOException {
        ValueRange response = sheetsService.spreadsheets().values().get(spreadsheetId, range).execute();
        return response.getValues();
    }

    public void updateDataInSheet(String spreadsheetId, String range, List<List<Object>> values) throws IOException {
        ValueRange body = new ValueRange().setValues(values);
        sheetsService.spreadsheets().values().update(spreadsheetId, range, body)
                .setValueInputOption("RAW")
                .execute();
    }
}
