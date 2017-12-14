package cl.waypoint.ms.mailer.dto;

import java.util.Arrays;

import javax.validation.constraints.NotNull;

public class Attachment {

	@NotNull
	private byte[] data;
	
	@NotNull
	private String filename;
	
	@NotNull
	private String contentType;

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@Override
	public String toString() {
		return "Attachment [data=" + Arrays.toString(data) + ", filename=" + filename + ", contentType=" + contentType
				+ "]";
	}
}
