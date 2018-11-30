package br.com.food4fit.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import br.com.food4fit.component.MaskedTextField;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class FormHelper {
	public static final int REQUIRED = 1 << 1;
	public static final int VALID_MASK = 1 << 2;
	public static final int VALID_EMAIL = 1 << 3;
	public static final int VALID_DATE = 1 << 4;
	public static final int VALID_DOUBLE = 1 << 5;
	public static final int VISIBLE_ONLY = 1 << 6;
	
	private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$");
	private final Map<Object, Integer> nodes = new HashMap<>();
	private Object objectData;

	public static FormHelper getInstance() {
		return new FormHelper();
	}
	
	private FormHelper() {}
	
	public void addValidation(Object object, int flags) {
		nodes.put(object, flags);
		if (object instanceof Node) {
			((Node) object).focusedProperty().addListener(observable -> ((Node) object).getStyleClass().remove("invalid"));
		}
	}
	
	public boolean validate() {
		boolean allValid = true;
		for (Entry<Object, Integer> entry : nodes.entrySet()) {
			int flags = entry.getValue();
			
			if (entry.getKey() instanceof Node) {
				Node node = (Node) entry.getKey();
				Object value = fetchValue(node);
				boolean invalid = false;
				
				if ((flags & VISIBLE_ONLY) == VISIBLE_ONLY ? node.isVisible() : true) {
					if ((flags & REQUIRED) == REQUIRED && !invalid) {	
						if (value instanceof String) {
							invalid = value == null || ((String) value).trim().isEmpty();
						} else {
							invalid = value == null;
						}
					}
	
					if ((flags & VALID_MASK) == VALID_MASK && !invalid) {
						if (node instanceof MaskedTextField) {
							invalid = !((MaskedTextField) node).isValid();
						}
					}
					
					if ((flags & VALID_EMAIL) == VALID_EMAIL && !invalid) {
						if (value instanceof String) {
							invalid = !EMAIL_PATTERN.matcher((String) value).matches();
						}
					}
					
					if ((flags & VALID_DATE) == VALID_DATE && !invalid) {
						if (node instanceof TextField) {
							SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
							try {
								long time = sdf.parse(((TextField) node).getText()).getTime();
								invalid = time < 0;
							} catch (ParseException e) {
								invalid = true;
							}
						}
					}
					
					if ((flags & VALID_DOUBLE) == VALID_DOUBLE && !invalid) {
						if (value instanceof String) {
							try {
								Double.parseDouble((String) value);
							} catch (NumberFormatException e) {
								invalid = true;
							}
						}
					}
				}
				
				if (invalid) {
					node.getStyleClass().add("invalid");
					allValid = false;
				} else {
					node.getStyleClass().remove("invalid");
				}
				
			} else if (entry.getKey() instanceof ToggleGroup) {
				if ((flags & REQUIRED) == REQUIRED) {
					if (((ToggleGroup) entry.getKey()).getSelectedToggle() == null) {
						allValid = false;
					}
				}
			}
		}
		
		return allValid;
	}
	
	private Object fetchValue(Node node) {
		if (node instanceof MaskedTextField) {
			return ((MaskedTextField) node).getPlainText();
		}
		
		if (node instanceof TextField) {
			return ((TextField) node).getText();
		}
		
		if (node instanceof TextArea) {
			return ((TextArea) node).getText();
		}
		
		if (node instanceof ComboBox<?>) {
			return ((ComboBox<?>) node).getValue();
		}
		
		return null;
	}

	public Object getObjectData() {
		return this.objectData;
	}

	public void setObjectData(Object objectData) {
		this.objectData = objectData;
	}
}
