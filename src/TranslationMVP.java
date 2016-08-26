import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface TranslationMVP {

    interface Presenter {
        @NotNull
        List<String> getHistory();

        void query(@Nullable String query);
    }

    interface View {

        void updateHistory();

        void showResult(@NotNull String query, @NotNull String result);

        void showError(@NotNull String error);
    }
}
