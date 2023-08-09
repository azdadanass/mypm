package ma.azdad.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import ma.azdad.service.UtilsFunctions;

@Entity
@Table(name = "mypm_sections_new")
public class Sections extends GenericModel<Integer> {

	private int sectionsNumber;
	private String sectionsTitle;
	private Integer weight;
	private UserAppraisal userappraisal;
	private Integer summaryRaiting;
	private String midYearReview;
	private Boolean eligible;

	private Integer countFiles = 0;

	private List<SectionsFile> fileList = new ArrayList<>();
	private List<SectionsHistory> historyList = new ArrayList<>();
	private List<SectionsComment> commentList = new ArrayList<>();
	private List<CommentGroup<SectionsComment>> commentGroupList;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "sectionNUmber")
	public int getSectionsNumber() {
		return sectionsNumber;
	}


	public void setSectionsNumber(int sectionsNumber) {
		this.sectionsNumber = sectionsNumber;
	}
	
	@Column(name = "weight")
	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer integer) {
		this.weight = integer;
	}

	@Column(name = "sectionTitle")
	public String getSectionsTitle() {
		return sectionsTitle;
	}

	public void setSectionsTitle(String sectionsTitle) {
		this.sectionsTitle = sectionsTitle;
	}



	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public UserAppraisal getUserappraisal() {
		return userappraisal;
	}

	public void setUserappraisal(UserAppraisal userappraisal) {
		this.userappraisal = userappraisal;
	}

	@Column(name = "summaryRaiting")
	public Integer getSummaryRaiting() {
		return summaryRaiting;
	}

	public void setSummaryRaiting(Integer summaryRaiting) {
		this.summaryRaiting = summaryRaiting;
	}

	@Column(name = "midYearReview")
	public String getMidYearReview() {
		return midYearReview;
	}

	public void setMidYearReview(String midYearReview) {
		this.midYearReview = midYearReview;
	}
	
	@Column(name = "eligible")
	public Boolean getEligible() {
		return eligible;
	}

	public void setEligible(Boolean eligible) {
		this.eligible = eligible;
	}

	@Transient
	public int getUserAppraisalId() {
		return userappraisal == null ? null : userappraisal.getId();
	}

	// File

	public void calculateCountFiles() {
		countFiles = fileList.size();
	}

	@Transient
	public Boolean getHasFiles() {
		return countFiles > 0;
	}

	public void addFile(SectionsFile file) {
		file.setParent(this);
		fileList.add(file);
		calculateCountFiles();
	}

	public void removeFile(SectionsFile file) {
		file.setParent(null);
		fileList.remove(file);
		calculateCountFiles();
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<SectionsFile> getFileList() {
		return fileList;
	}

	public void setFileList(List<SectionsFile> fileList) {
		this.fileList = fileList;
	}

	public Integer getCountFiles() {
		return countFiles;
	}

	public void setCountFiles(Integer countFiles) {
		this.countFiles = countFiles;
	}

	// History
	public void addHistory(SectionsHistory history) {
		history.setParent(this);
		historyList.add(history);
	}

	public void removeHistory(SectionsHistory history) {
		history.setParent(null);
		historyList.remove(history);
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<SectionsHistory> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<SectionsHistory> historyList) {
		this.historyList = historyList;
	}

	// Comment
	public void addComment(SectionsComment comment) {
		comment.setParent(this);
		commentList.add(comment);
	}

	public void removeComment(SectionsComment comment) {
		comment.setParent(null);
		commentList.remove(comment);
	}

	private void generateCommentGroupList() {
		Map<String, List<SectionsComment>> map = new HashMap<>();
		for (SectionsComment comment : commentList) {
			String dateStr = UtilsFunctions.getFormattedDate(comment.getDate());
			map.putIfAbsent(dateStr, new ArrayList<SectionsComment>());
			map.get(dateStr).add(comment);
		}
		commentGroupList = new ArrayList<>();
		for (String dateStr : map.keySet())
			commentGroupList.add(new CommentGroup<>(UtilsFunctions.getDate(dateStr), map.get(dateStr)));
		Collections.sort(commentGroupList);
	}

	@Transient
	public List<CommentGroup<SectionsComment>> getCommentGroupList() {
		if (commentGroupList == null)
			generateCommentGroupList();
		return commentGroupList;
	}

	@Transient
	public void setCommentGroupList(List<CommentGroup<SectionsComment>> commentGroupList) {
		this.commentGroupList = commentGroupList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<SectionsComment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<SectionsComment> commentList) {
		this.commentList = commentList;
	}

}