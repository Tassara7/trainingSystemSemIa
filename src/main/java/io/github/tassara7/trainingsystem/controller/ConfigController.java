package io.github.tassara7.trainingsystem.controller;

import io.github.tassara7.trainingsystem.persistence.AppData;
import io.github.tassara7.trainingsystem.view.I18nManager;
import io.github.tassara7.trainingsystem.view.LanguageChangeListener;
import io.github.tassara7.trainingsystem.view.skin.ScreenType;
import io.github.tassara7.trainingsystem.view.skin.SkinManager;
import io.github.tassara7.trainingsystem.view.Skins;
import io.github.tassara7.trainingsystem.view.WallpaperUtil;
import io.github.tassara7.trainingsystem.view.skin.SkinnableScreen;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

// 1. Implementa a interface para "ouvir" as mudanças de idioma
public class ConfigController implements SkinnableScreen, LanguageChangeListener {

    @FXML private Pane rootPane;
    @FXML private ComboBox<Skins> skinComboBox;

    // 2. Adiciona os @FXML para os novos componentes da sua tela
    @FXML private ComboBox<String> languageComboBox;
    @FXML private Label changeSkinLabel;
    @FXML private Label changeLanguageLabel;

    // 3. Cria um mapa para associar nomes amigáveis (ex: "English") com os Locales do Java
    private final Map<String, Locale> supportedLanguages = new LinkedHashMap<>();

    public void initialize() {
        // Registra a tela para ouvir mudanças de skin e de idioma
        SkinManager.registerListener(this::onSkinChanged);
        I18nManager.registerListener(this);

        // --- Lógica das Skins (seu código existente) ---
        skinComboBox.getItems().addAll(Skins.values());
        skinComboBox.getSelectionModel().select(AppData.getInstance().getCurrentUser().getSkin());
        skinComboBox.valueProperty().addListener((obs, oldSkin, newSkin) -> {
            if (newSkin != null) {
                AppData.getInstance().getCurrentUser().setSkin(newSkin);
                SkinManager.updateSkinForAll(newSkin);
            }
        });

        // --- Lógica dos Idiomas (nova) ---
        setupLanguageSelector();
        updateTexts();
        // 2. Define o valor inicial do ComboBox sem disparar eventos desnecessários.
        setInitialLanguageSelection();

        // Chama uma vez para definir os textos no idioma correto ao abrir a tela

    }

    /**
     * Novo método para configurar o ComboBox de idiomas.
     */
    private void setupLanguageSelector() {
        // Preenche o mapa com os idiomas que o app suporta
        supportedLanguages.put("Português (BR)", new Locale("pt", "BR"));
        supportedLanguages.put("English (US)", new Locale("en", "US"));

        // Adiciona os nomes amigáveis ao ComboBox para o usuário ver
        languageComboBox.getItems().addAll(supportedLanguages.keySet());

        // Listener que age quando o usuário seleciona um novo idioma
        languageComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                // Pega o Locale correspondente ao nome selecionado (ex: "English (US)")
                Locale selectedLocale = supportedLanguages.get(newVal);
                // Avisa o I18nManager para trocar o idioma de toda a aplicação
                I18nManager.setLocale(selectedLocale);
            }
        });
    }

    /**
     * Este método é chamado pelo I18nManager sempre que o idioma muda.
     * Sua função é atualizar os textos DESTA tela.
     */
    @Override
    public void onLanguageChanged() {
        // Traduz as labels da tela de configurações
        changeSkinLabel.setText(I18nManager.getString("settings.view.skin"));
        changeLanguageLabel.setText(I18nManager.getString("settings.view.language"));

        // Garante que o ComboBox mostre o idioma que está ativo no momento
        Locale currentLocale = I18nManager.getLocale();
        for (Map.Entry<String, Locale> entry : supportedLanguages.entrySet()) {
            if (entry.getValue().equals(currentLocale)) {
                languageComboBox.getSelectionModel().select(entry.getKey());
                break;
            }
        }
    }

    private void setInitialLanguageSelection() {
        Locale currentLocale = I18nManager.getLocale();
        for (Map.Entry<String, Locale> entry : supportedLanguages.entrySet()) {
            if (entry.getValue().equals(currentLocale)) {
                // Seleciona a chave (ex: "Português (BR)") correspondente ao Locale ativo
                languageComboBox.getSelectionModel().select(entry.getKey());
                break;
            }
        }
    }

    private void updateTexts() {
        changeSkinLabel.setText(I18nManager.getString("settings.view.skin"));
        changeLanguageLabel.setText(I18nManager.getString("settings.view.language"));
    }


    // Seu onSkinChanged() não precisa de alterações
    public void onSkinChanged() {
        rootPane.setStyle(WallpaperUtil.buildWallpaperStyle());
    }

    @Override
    public ScreenType getScreenType() {
        return ScreenType.CONFIG;
    }
}