package com.example.b2bfront.util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Adaptateur Gson pour LocalDateTime qui supporte plusieurs formats
 */
public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {

    private static final DateTimeFormatter FORMATTER_DATE_TIME = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final DateTimeFormatter FORMATTER_DATE = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public void write(JsonWriter out, LocalDateTime value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value.format(FORMATTER_DATE_TIME));
        }
    }

    @Override
    public LocalDateTime read(JsonReader in) throws IOException {
        if (in.peek() == com.google.gson.stream.JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        String dateTimeString = in.nextString();

        // Essayer de parser comme LocalDateTime d'abord
        try {
            return LocalDateTime.parse(dateTimeString, FORMATTER_DATE_TIME);
        } catch (DateTimeParseException e1) {
            // Si ça échoue, essayer comme LocalDate (ex: "2024-11-01")
            try {
                LocalDate date = LocalDate.parse(dateTimeString, FORMATTER_DATE);
                return date.atStartOfDay(); // Convertir en LocalDateTime à minuit
            } catch (DateTimeParseException e2) {
                System.err.println("Impossible de parser la date: " + dateTimeString);
                return null; // Retourner null au lieu de lancer une exception
            }
        }
    }
}
