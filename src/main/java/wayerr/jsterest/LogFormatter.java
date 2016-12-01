package wayerr.jsterest;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 */
class LogFormatter extends Formatter {

    static final LogFormatter INSTANCE = new LogFormatter();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy.MM.dd hh:mm:ss.SSS", Locale.ROOT);

    @Override
    public String format(LogRecord record) {
        StringBuilder sb = new StringBuilder();
        long millis = record.getMillis();
        String date = formatter.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault()));
        sb.append(date).append(' ');

        String className = record.getSourceClassName();
        if (className != null) {
            printClassName(sb, className);
            if (record.getSourceMethodName() != null) {
                sb.append('.').append(record.getSourceMethodName());
            }
        } else {
            sb.append(record.getLoggerName());
        }
        sb.append(' ');
        sb.append(record.getLevel()).append(": ");

        String message = formatMessage(record);
        sb.append(message).append(' ');

        Throwable th = record.getThrown();
        if (th != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            pw.println();
            th.printStackTrace(pw);
            pw.close();
            sb.append(sw);
        }
        sb.append('\n');
        return sb.toString();
    }

    private void printClassName(StringBuilder sb, String className) {
        int pos = 0;
        int newPos;
        while((newPos = className.indexOf('.', pos)) > 0) {
            sb.append(className.charAt(pos)).append('.');
            if(className.length() < newPos) {
                break;
            }
            newPos++;
            pos = newPos;
        }
        sb.append(className, pos, className.length());
    }
}
