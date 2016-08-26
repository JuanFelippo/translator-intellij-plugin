import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;

public class TranslationDialogManager {

    private final static TranslationDialogManager sInstance = new TranslationDialogManager();

    private TranslationDialog myShowingDialog;

    private TranslationDialogManager() {
    }

    public static TranslationDialogManager getInstance() {
        return sInstance;
    }

    public void show(@Nullable Project project) {
        if (myShowingDialog == null) {
            myShowingDialog = new TranslationDialog(project);
            myShowingDialog.setOnDisposeListener(new TranslationDialog.OnDisposeListener() {
                @Override
                public void onDispose() {
                    myShowingDialog = null;
                }
            });
        }

        myShowingDialog.show();
    }

}
