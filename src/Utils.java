
import com.intellij.ui.JBColor;
import com.intellij.util.ui.JBUI;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

final class Utils {
    private static final SimpleAttributeSet ATTR_QUERY = new SimpleAttributeSet();
    private static final SimpleAttributeSet ATTR_EXPLAIN = new SimpleAttributeSet();
    private static final SimpleAttributeSet ATTR_PRE_EXPLAINS = new SimpleAttributeSet();
    private static final SimpleAttributeSet ATTR_EXPLAINS = new SimpleAttributeSet();
    private static final SimpleAttributeSet ATTR_WEB_EXPLAIN_TITLE = new SimpleAttributeSet();
    private static final SimpleAttributeSet ATTR_WEB_EXPLAIN_KEY = new SimpleAttributeSet();
    private static final SimpleAttributeSet ATTR_WEB_EXPLAIN_VALUES = new SimpleAttributeSet();


    static {
        StyleConstants.setItalic(ATTR_QUERY, true);
        StyleConstants.setBold(ATTR_QUERY, true);
        StyleConstants.setFontSize(ATTR_QUERY, JBUI.scaleFontSize(19));
        StyleConstants.setForeground(ATTR_QUERY, new JBColor(0xFFEE6000, 0xFFCC7832));

        StyleConstants.setForeground(ATTR_EXPLAIN, new JBColor(0xFF3E7EFF, 0xFF8CBCE1));

        StyleConstants.setItalic(ATTR_PRE_EXPLAINS, true);
        StyleConstants.setForeground(ATTR_PRE_EXPLAINS, new JBColor(0xFF7F0055, 0xFFEAB1FF));
        StyleConstants.setFontSize(ATTR_PRE_EXPLAINS, JBUI.scaleFontSize(16));

        StyleConstants.setForeground(ATTR_EXPLAINS, new JBColor(0xFF170591, 0xFFFFC66D));
        StyleConstants.setFontSize(ATTR_PRE_EXPLAINS, JBUI.scaleFontSize(16));

        StyleConstants.setForeground(ATTR_WEB_EXPLAIN_TITLE, new JBColor(0xFF707070, 0xFF808080));
        StyleConstants.setForeground(ATTR_WEB_EXPLAIN_KEY, new JBColor(0xFF4C4C4C, 0xFF77B767));
        StyleConstants.setForeground(ATTR_WEB_EXPLAIN_VALUES, new JBColor(0xFF707070, 0xFF6A8759));
    }

    private Utils() {
    }

    static boolean isEmptyOrBlankString(String str) {
        return null == str || str.trim().length() == 0;
    }

    static String splitWord(String input) {
        if (isEmptyOrBlankString(input))
            return input;

        return input.replace("_", " ")
                .replaceAll("([A-Z][a-z]+)|([0-9\\W]+)", " $0 ")
                .replaceAll("[A-Z]{2,}", " $0")
                .replaceAll("\\s{2,}", " ")
                .trim();
    }
}
