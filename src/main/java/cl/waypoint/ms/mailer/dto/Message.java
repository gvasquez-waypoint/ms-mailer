package cl.waypoint.ms.mailer.dto;

import java.util.Arrays;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class Message {
	
	private static final String EMAIL_REGEX = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\\\.[A-Z]{2,6}$";

	@Pattern(regexp = EMAIL_REGEX, flags = Pattern.Flag.CASE_INSENSITIVE)
	private String[] to;
	
	@Pattern(regexp = EMAIL_REGEX, flags = Pattern.Flag.CASE_INSENSITIVE)
	private String[] cc;
	
	@Pattern(regexp = EMAIL_REGEX, flags = Pattern.Flag.CASE_INSENSITIVE)
	private String[] bcc;
	
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

	public String[] getTo() {
		return to;
	}

	public void setTo(String[] to) {
		this.to = to;
	}

	public String[] getCc() {
		return cc;
	}

	public void setCc(String[] cc) {
		this.cc = cc;
	}

	public String[] getBcc() {
		return bcc;
	}

	public void setBcc(String[] bcc) {
		this.bcc = bcc;
	}

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
		return "Message [to=" + Arrays.toString(to) + ", cc=" + Arrays.toString(cc) + ", bcc=" + Arrays.toString(bcc)
				+ ", from=" + from + ", replyTo=" + replyTo + ", subject=" + subject + ", body=" + body
				+ ", attachments=" + Arrays.toString(attachments) + "]";
	}

}
