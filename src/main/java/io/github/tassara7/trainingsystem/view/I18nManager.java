package io.github.tassara7.trainingsystem.view;

import io.github.tassara7.trainingsystem.model.BodyParts;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class I18nManager {

    private static final String BUNDLE_PATH = "io.github.tassara7.trainingsystem.i18n.messages";
    private static final List<LanguageChangeListener> listeners = new ArrayList<>();

    private static Locale currentLocale = new Locale("pt", "BR");
    private static ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_PATH, currentLocale);

    public static String getString(String key) {
        return bundle.getString(key);
    }

    public static String get(BodyParts part) {
        return getString("bodypart." + part.name());
    }

    public static void setLocale(Locale locale) {
        currentLocale = locale;
        bundle = ResourceBundle.getBundle(BUNDLE_PATH, currentLocale);
        notifyListeners();
    }


    public static void registerListener(LanguageChangeListener listener) {
        if(!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    private static void notifyListeners() {
        for(LanguageChangeListener listener : listeners) {
            listener.onLanguageChanged();
        }
    }

    public static Locale getLocale() {
        return currentLocale;
    }

    public static ResourceBundle getBundle(){
        return bundle;
    }
}
