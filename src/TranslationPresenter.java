import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

class TranslationPresenter implements TranslationMVP.Presenter {

    private static final int HISTORY_SIZE = 50;
    private static final List<String> sHistory = new ArrayList<>(HISTORY_SIZE);

    private final TranslationMVP.View mTranslationView;

    private String currentQuery;

    public TranslationPresenter(@NotNull TranslationMVP.View view) {
        this.mTranslationView = Objects.requireNonNull(view, "view cannot be null.");
    }

    @NotNull
    @Override
    public List<String> getHistory() {
        return Collections.unmodifiableList(sHistory);
    }

    @Override
    public void query(@Nullable String query) {
        if (Utils.isEmptyOrBlankString(query) || query.equals(currentQuery))
            return;

        query = query.trim();

        List<String> history = TranslationPresenter.sHistory;
        int index = history.indexOf(query);
        if (index != 0) {
            if (index > 0) {
                history.remove(index);
            }
            if (history.size() >= HISTORY_SIZE) {
                history.remove(HISTORY_SIZE - 1);
            }

            history.add(0, query);
            mTranslationView.updateHistory();
        }

        currentQuery = query;
        Translator.get().query(query, new Translator.Callback() {
            @Override
            public void onQuery(String query, String result) {
                onPostResult(query, result);
            }
        });
    }

    private void onPostResult(String query, String result) {
        if (Utils.isEmptyOrBlankString(query) || Utils.isEmptyOrBlankString(result)) {
            currentQuery = null;
            mTranslationView.showError("Something went wrong");
            return;
        }

        if(!query.equals(currentQuery) ){
            return;
        }
        currentQuery = null;
        mTranslationView.showResult(query, result);
    }
}
