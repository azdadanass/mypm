package ma.azdad.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import ma.azdad.service.UtilsFunctions;

@SuppressWarnings({ "serial", "rawtypes" })
public class CommentGroup<A> implements Serializable, Comparable<CommentGroup> {

	protected Date date;
	protected List<A> commentList;

	public CommentGroup(Date date, List<A> commentList) {
		super();
		this.date = date;
		this.commentList = commentList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : getFormattedDate().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CommentGroup other = (CommentGroup) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!getFormattedDate().equals(other.getFormattedDate()))
			return false;
		return true;
	}

	public String getFormattedDate() {
		return UtilsFunctions.getFormattedDate(date);
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<A> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<A> commentList) {
		this.commentList = commentList;
	}

	@Override
	public int compareTo(CommentGroup o) {
		return date.compareTo(o.getDate());
	}

}
