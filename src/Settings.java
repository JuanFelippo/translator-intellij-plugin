import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class Settings implements Configurable{

    private static final String LOCALE_TARGET = "language_target";
    private static final String DEFAULT_LOCALE = "en";

    private JPanel contentPane;
    private JTextField keyNameField;

    @Nls
    @Override
    public String getDisplayName() {
        return "Translator";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        createUIComponents();
        return contentPane;
    }

    private void createUIComponents() {
        displayLocale();
    }

    private void displayLocale() {
        PropertiesComponent component = PropertiesComponent.getInstance();
        keyNameField.setText(component.getValue(LOCALE_TARGET, DEFAULT_LOCALE));
    }

    @Override
    public boolean isModified() {
        return !getLocale().equals(keyNameField.getText().toLowerCase());
    }

    @Override
    public void apply() throws ConfigurationException {
        PropertiesComponent component = PropertiesComponent.getInstance();

        boolean validKey = !Utils.isEmptyOrBlankString(keyNameField.getText());
        component.setValue(LOCALE_TARGET, validKey? keyNameField.getText().toLowerCase() : DEFAULT_LOCALE);
    }

    @Override
    public void reset() {

    }

    @Override
    public void disposeUIResources() {

    }

    public static String getLocale() {
        return PropertiesComponent.getInstance().getValue(LOCALE_TARGET, DEFAULT_LOCALE);
    }
}
