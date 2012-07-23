
public class HtmlLink {
	String link;
	String linkText;

	HtmlLink(String link, String linkText) {
		this.link = link;
		this.linkText = linkText;
	}

	public String getLink() {
		return link.substring(1, link.length() - 1);
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getLinkText() {
		return linkText;
	}

	public void setLinkText(String linkText) {
		this.linkText = linkText;
	}

	@Override
	public String toString() {
		return new StringBuffer("Link : ").append(this.link)
				.append(" Link Text : ").append(this.linkText).toString();
	}

}
