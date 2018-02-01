package cl.waypoint.ms.mailer.dto;

import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import cz.jirutka.validator.collection.constraints.EachPattern;

public class Message {
	
	private static final String EMAIL_REGEX = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

	@EachPattern(regexp = EMAIL_REGEX, flags = Pattern.Flag.CASE_INSENSITIVE)
	private List<String>to;
	
	@EachPattern(regexp = EMAIL_REGEX, flags = Pattern.Flag.CASE_INSENSITIVE)
	private List<String> cc;
	
	@EachPattern(regexp = EMAIL_REGEX, flags = Pattern.Flag.CASE_INSENSITIVE)
	private List<String> bcc;
	
	@NotNull
	@Pattern(regexp = EMAIL_REGEX, flags = Pattern.Flag.CASE_INSENSITIVE)
	private String from;
	
	@Pattern(regexp = EMAIL_REGEX, flags = Pattern.Flag.CASE_INSENSITIVE)
	private String replyTo;

	@NotNull
	private String subject;

	@NotNull
	private String body;
	private Attachment[] attachments;


	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Attachment[] getAttachments() {
		return attachments;
	}

	public void setAttachments(Attachment[] attachments) {
		this.attachments = attachments;
	}

	@Override
	public String toString() {
		return "Message [to=" + to + ", cc=" + cc + ", bcc=" + bcc + ", from=" + from + ", replyTo=" + replyTo
				+ ", subject=" + subject + ", body=" + body + ", attachments=" + Arrays.toString(attachments) + "]";
	}

	public List<String> getTo() {
		return to;
	}

	public void setTo(List<String> to) {
		this.to = to;
	}

	public List<String> getCc() {
		return cc;
	}

	public void setCc(List<String> cc) {
		this.cc = cc;
	}

	public List<String> getBcc() {
		return bcc;
	}

	public void setBcc(List<String> bcc) {
		this.bcc = bcc;
	}



}
