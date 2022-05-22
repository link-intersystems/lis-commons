package com.link_intersystems.sql.hibernate;

import com.link_intersystems.sql.format.SqlFormatter;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.hibernate.engine.jdbc.internal.Formatter;

import java.io.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class HibernateSqlFormatter implements SqlFormatter {

    private final Formatter formatter;

    public HibernateSqlFormatter() {
        formatter = FormatStyle.BASIC.getFormatter();
    }

    @Override
    public String format(String sql) {
        String formatted = formatter.format(sql);

        BufferedReader reader = new BufferedReader(new StringReader(formatted));
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        String line;
        String mainIndention = null;
        try {
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    continue;
                }
                if (mainIndention == null) {
                    mainIndention = "";
                    for (int i = 0; i < line.length(); i++) {
                        if (line.charAt(i) != ' ') {
                            break;
                        }
                        mainIndention += " ";
                    }
                }

                if (line.startsWith(mainIndention)) {
                    line = line.substring(mainIndention.length());
                }

                pw.println(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        pw.flush();
        pw.close();

        return sw.toString().trim();
    }
}
