package project.model;

public class ErrorMessage extends Exception {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private String message;
private String messageType;
private String messageAlertType;
public String getMessage() {
	return message;
}
public void setMessage(String message) {
	this.message = message;
}
public String getMessageType() {
	return messageType;
}
public void setMessageType(String messageType) {
	this.messageType = messageType;
}
public String getMessageAlertType() {
	return messageAlertType;
}
public void setMessageAlertType(String messageAlertType) {
	this.messageAlertType = messageAlertType;
}
public ErrorMessage(String message, String messageType, String messageAlertType) {
	super();
	this.message = message;
	this.messageType = messageType;
	this.messageAlertType = messageAlertType;
}
}
