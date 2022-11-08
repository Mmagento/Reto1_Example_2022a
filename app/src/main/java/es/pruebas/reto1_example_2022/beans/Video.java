package es.pruebas.reto1_example_2022.beans;

import java.io.Serializable;

/**
 * A single video
 */
public class Video implements Serializable {

	private static final long serialVersionUID = -578858462965845200L;

	private int id;

    private String title;

    private String url;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
