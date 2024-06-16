package com.han.towersofivory.ui.businesslayer.services.windows.agent;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.TextBox;
import com.han.towersofivory.agent.dto.AgentConfigurationText;
import com.han.towersofivory.agent.exceptions.AgentCompilerException;
import com.han.towersofivory.agent.interfacelayer.interfaces.IAgent;
import com.han.towersofivory.agent.interfacelayer.mappers.ConcreteAgent;
import com.han.towersofivory.ui.businesslayer.services.WindowRouter;
import com.han.towersofivory.ui.businesslayer.services.windows.AbstractWindow;
import org.antlr.v4.runtime.misc.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import static com.han.towersofivory.ui.businesslayer.services.BindASCIItoPanel.bindASCIItoPanel;

public class ConfigurationDetailWindow extends AbstractWindow {
    private static final Logger LOGGER = LogManager.getLogger(ConfigurationDetailWindow.class);
    public static final String AGENT_CONFIGURATIONS = "agentConfigurationsWindow";
    private static final String ERROR = "Error";
    private final WindowRouter router;
    private final IAgent agent = new ConcreteAgent();
    private final File file;
    private final String title;
    private static final String CHECKER_ERROR = "Er ging iets mis, check je grammatica en probeer opnieuw.";

    public ConfigurationDetailWindow(WindowRouter router, File file, String title) {
        super(title);
        this.router = router;
        this.file = file;
        this.title = title;
        initializeComponents();
    }

    @Override
    @SuppressWarnings("Not my code: copied from the original project, should be fixed in the future.")
    protected void initializeComponents() {
        bindASCIItoPanel(contentPanel);

        Pair<TextBox, TextBox> textBoxes = setupTextBoxes();
        TextBox agentConfiguration = textBoxes.a;
        TextBox titleBox = textBoxes.b;

        renderTitleAndConfigurationTextBox(titleBox, agentConfiguration);

        renderCheckAndSaveButton(titleBox, agentConfiguration);

        renderCheckButton(titleBox, agentConfiguration);

        renderDeleteButton();

        renderBackButton();
    }

    private Pair<TextBox, TextBox> setupTextBoxes() {
        TextBox agentConfiguration = new TextBox(new TerminalSize(100, 8));
        TextBox titleBox = new TextBox();

        if (file != null) {
            try {
                String content = new String(Files.readAllBytes(file.toPath()));
                agentConfiguration.setText(content);
                titleBox.setText(title);
            } catch (IOException e) {
                LOGGER.error("An error occurred while reading the file.", e);
            }
        }

        return new Pair<>(agentConfiguration, titleBox);
    }

    private void renderTitleAndConfigurationTextBox(TextBox titleBox, TextBox agentConfiguration) {
        contentPanel.addComponent(new Label("Title:"));
        contentPanel.addComponent(titleBox);
        contentPanel.addComponent(new Label("Configuration:"));
        contentPanel.addComponent(agentConfiguration);
    }

    private void renderBackButton() {
        contentPanel.addComponent(new Button("Back", () -> router.navigateTo("agentWindow")));
    }

    private void saveConfiguration(File oldFile, File newFile, AgentConfigurationText agentConfigurationText) {
        if (oldFile != null && !newFile.equals(oldFile)) {
            try {
                Files.move(oldFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                showDialog(ERROR, e.getMessage(), DialogType.ERROR, null);
                LOGGER.error("Gefaald om het configuratiebestand op te slaan.", e);
                return;
            }
        }

        agent.saveConfiguration(agentConfigurationText);
    }

    private void renderCheckButton(TextBox titleBox, TextBox agentConfiguration) {
        contentPanel.addComponent(new Button("Check", () -> {
            LOGGER.info("Check configuration");
            if (checkConfiguration(new AgentConfigurationText(titleBox.getText(), agentConfiguration.getText()))) {
                return;
            }
            showDialog("Success", "Configuratie check was successful.", DialogType.INFO, null);
        }));
    }

    private void renderDeleteButton() {
        if(file != null) {
            contentPanel.addComponent(new Button("Delete", () -> {
                agent.deleteConfiguration(file.getName());
                router.navigateTo("agentWindow");
            }));
        }
    }

    private void renderCheckAndSaveButton(TextBox titleBox, TextBox agentConfiguration) {
        contentPanel.addComponent(new Button("Check & save", () -> {
            if (titleBox.getText().isEmpty() || agentConfiguration.getText().isEmpty()) {
                showDialog(ERROR, "Titel en/of configuratie kan niet leeg zijn.", DialogType.ERROR, null);
                return;
            }
            AgentConfigurationText agentConfigurationText = new AgentConfigurationText(titleBox.getText(), agentConfiguration.getText());

            if (checkConfiguration(agentConfigurationText)) {
                return;
            }
            String directoryPath = "src/main/resources/agentConfigurations/";
            File newFile = new File((file != null ? file.getParent() : directoryPath), titleBox.getText() + ".txt");

            if (newFile.exists() && (!newFile.equals(file))) {
                showDialog("Waarschuwing", "Een configuratie met deze titel bestaat al. Wilt u deze overschrijven?", DialogType.WARNING, () -> {
                    saveConfiguration(file, newFile, agentConfigurationText);
                    close();
                    router.navigateTo(AGENT_CONFIGURATIONS);
                });
            } else {
                saveConfiguration(file, newFile, agentConfigurationText);
                router.navigateTo(AGENT_CONFIGURATIONS);
            }
        }));
    }

    private void showDialog(String title, String message, DialogType type, Runnable onConfirm) {
        router.dialog("agentDialogWindow", title, message, type, onConfirm);
    }


    private boolean checkConfiguration(AgentConfigurationText agentConfigurationText){
        try {
            agent.checkConfiguration(agentConfigurationText);
            return false;
        } catch (AgentCompilerException e) {
            showDialog(ERROR, e.getMessage(), DialogType.ERROR, null);
            return true;
        } catch (NumberFormatException e) {
            showDialog(ERROR, "Een nummer mist in de grammatica, check je grammatica en probeer opnieuw.", DialogType.ERROR, null);
            return true;
        } catch (Exception e) {
            showDialog(ERROR, CHECKER_ERROR, DialogType.ERROR, null);
            return true;
        }
    }
}
