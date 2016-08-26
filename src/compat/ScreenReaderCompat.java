package compat;

import com.intellij.util.ui.accessibility.ScreenReader;

public final class ScreenReaderCompat {

    private ScreenReaderCompat() {
    }

    public static boolean isActive() {
        return IdeaCompat.BUILD_NUMBER >= IdeaCompat.Version.IDEA2016_2 && ScreenReader.isActive();
    }

}
