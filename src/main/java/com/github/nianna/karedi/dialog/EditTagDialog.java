package com.github.nianna.karedi.dialog;

import com.github.nianna.karedi.I18N;
import com.github.nianna.karedi.control.RestrictedTextField;
import com.github.nianna.karedi.song.tag.FormatSpecification;
import com.github.nianna.karedi.song.tag.Tag;
import com.github.nianna.karedi.song.tag.TagKey;
import com.github.nianna.karedi.song.tag.TagValueValidators;
import com.github.nianna.karedi.util.ValidationUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.Validator;
import org.controlsfx.validation.decoration.GraphicValidationDecoration;
import org.controlsfx.validation.decoration.ValidationDecoration;

import java.util.Arrays;
import java.util.List;

public class EditTagDialog extends Dialog<Tag> {
	@FXML
	private TextField keyField;
	@FXML
	private RestrictedTextField valueField;

	private ValidationDecoration valueDecoration = new GraphicValidationDecoration();
	private ValidationDecoration keyDecoration = new GraphicValidationDecoration();
	private Validator<String> valueValidator = TagValueValidators.defaultValidator();
	private Validator<String> keyValidator = Validator
			.createEmptyValidator(I18N.get("dialog.tag.key_required"));

	private Node okButton;

	private final FormatSpecification formatSpecification;

	public EditTagDialog(FormatSpecification formatSpecification) {
		this.formatSpecification = formatSpecification;

		DialogUtils.loadPane(this, getClass().getResource("/fxml/EditTagDialogPaneLayout.fxml"));
		setTitle(I18N.get("dialog.new_tag.title"));

		getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		okButton = getDialogPane().lookupButton(ButtonType.OK);

		setResultConverter(type -> {
			if (type == ButtonType.OK) {
				return new Tag(keyField.getText(), valueField.getText());
			}
			return null;
		});

	}

	@FXML
	public void initialize() {
		List<TagKey> suggestedKeys = Arrays.stream(TagKey.values())
				.filter(key -> formatSpecification == null || formatSpecification.supports(key))
				.toList();
		TextFields.bindAutoCompletion(keyField, suggestedKeys);
		keyField.textProperty().addListener(obs -> onKeyFieldTextChanged());
		valueField.textProperty().addListener(obs -> refreshValueFieldDecoration());

		Platform.runLater(() -> {
			refreshValueFieldDecoration();
			refreshKeyFieldDecoration();
		});
	}

	private void onKeyFieldTextChanged() {
		TagValueValidators.forbiddenCharacterRegex(keyField.getText()).ifPresent(regex -> {
			valueField.setForbiddenCharacterRegex(regex);
		});
		valueValidator = TagValueValidators.forKey(keyField.getText());
		refreshValueFieldDecoration();
		refreshKeyFieldDecoration();
	}

	private void refreshValueFieldDecoration() {
		refreshDecoration(valueValidator, valueDecoration, valueField);
	}

	private void refreshKeyFieldDecoration() {
		refreshDecoration(keyValidator, keyDecoration, keyField);
	}

	private void refreshDecoration(Validator<String> validator, ValidationDecoration decoration,
			TextField field) {
		ValidationResult result = validator.apply(field, field.getText());
		decoration.removeDecorations(field);
		ValidationUtils.getHighestPriorityMessage(result)
				.ifPresent(msg -> decoration.applyValidationDecoration(msg));

		if (result.getErrors().size() > 0) {
			okButton.setDisable(true);
		} else {
			refreshOkButtonDisable();
		}
	}

	private void refreshOkButtonDisable() {
		okButton.setDisable(errorsPresent());
	}

	private boolean errorsPresent() {
		return countErrors(keyValidator, keyField) + countErrors(valueValidator, valueField) > 0;
	}

	private int countErrors(Validator<String> validator, TextField field) {
		return validator.apply(field, field.getText()).getErrors().size();
	}
}
