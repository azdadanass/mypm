package ma.azdad.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import ma.azdad.service.UtilsFunctions;

@Entity

public class SupplierWarning extends GenericModel<Integer> {

	private Date date;
	private String type;
	private String subType;
	private Severity severity;
	private String way;
	private String disciplinaryAction;
	private String reason;

	private Supplier supplier;

	private Integer countFiles = 0;

	private List<SupplierWarningFile> fileList = new ArrayList<>();
	private List<SupplierWarningHistory> historyList = new ArrayList<>();
	private List<SupplierWarningComment> commentList = new ArrayList<>();

	private List<CommentGroup<SupplierWarningComment>> commentGroupList;

	@Transient
	@Override
	public String getIdentifierName() {
		return this.getIdStr();
	}

	public void calculateCountFiles() {
		countFiles = fileList.size();
	}

	@Transient
	public Boolean getHasFiles() {
		return countFiles > 0;
	}

	public void addFile(SupplierWarningFile file) {
		file.setParent(this);
		fileList.add(file);
		calculateCountFiles();
	}

	public void removeFile(SupplierWarningFile file) {
		file.setParent(null);
		fileList.remove(file);
		calculateCountFiles();
	}

	public void addHistory(SupplierWarningHistory history) {
		history.setParent(this);
		historyList.add(history);
	}

	public void removeHistory(SupplierWarningHistory history) {
		history.setParent(null);
		historyList.remove(history);
	}

	public void addComment(SupplierWarningComment comment) {
		comment.setParent(this);
		commentList.add(comment);
	}

	public void removeComment(SupplierWarningComment comment) {
		comment.setParent(null);
		commentList.remove(comment);
	}

	private void generateCommentGroupList() {
		Map<String, List<SupplierWarningComment>> map = new HashMap<>();
		for (SupplierWarningComment comment : commentList) {
			String dateStr = UtilsFunctions.getFormattedDate(comment.getDate());
			map.putIfAbsent(dateStr, new ArrayList<SupplierWarningComment>());
			map.get(dateStr).add(comment);
		}
		commentGroupList = new ArrayList<>();
		for (String dateStr : map.keySet())
			commentGroupList.add(new CommentGroup<>(UtilsFunctions.getDate(dateStr), map.get(dateStr)));
		Collections.sort(commentGroupList);
	}

	@Transient
	public List<CommentGroup<SupplierWarningComment>> getCommentGroupList() {
		if (commentGroupList == null)
			generateCommentGroupList();
		return commentGroupList;
	}

	@Transient
	public void setCommentGroupList(List<CommentGroup<SupplierWarningComment>> commentGroupList) {
		this.commentGroupList = commentGroupList;
	}

	// getters & setters

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Temporal(TemporalType.DATE)
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	@Enumerated(EnumType.STRING)
	public Severity getSeverity() {
		return severity;
	}

	public void setSeverity(Severity severity) {
		this.severity = severity;
	}

	public String getWay() {
		return way;
	}

	public void setWay(String way) {
		this.way = way;
	}

	@Column(columnDefinition = "TEXT")
	public String getDisciplinaryAction() {
		return disciplinaryAction;
	}

	public void setDisciplinaryAction(String disciplinaryAction) {
		this.disciplinaryAction = disciplinaryAction;
	}

	@Column(columnDefinition = "TEXT")
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "supplier_id")
	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<SupplierWarningFile> getFileList() {
		return fileList;
	}

	public void setFileList(List<SupplierWarningFile> fileList) {
		this.fileList = fileList;
	}

	public Integer getCountFiles() {
		return countFiles;
	}

	public void setCountFiles(Integer countFiles) {
		this.countFiles = countFiles;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<SupplierWarningHistory> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<SupplierWarningHistory> historyList) {
		this.historyList = historyList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<SupplierWarningComment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<SupplierWarningComment> commentList) {
		this.commentList = commentList;
	}

}
