package com.nrk.ltu;

import java.io.Serializable;

public class TagedImage implements Serializable {
	private static final long serialVersionUID = 1L;
	private final String imageUrl;
	private final String contactUrl;

	public TagedImage(String imageUrl, String contactUrl) {
		this.imageUrl = imageUrl;
		this.contactUrl = contactUrl;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public String getContactUrl() {
		return contactUrl;
	}

}