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
@Table(name = "mypm_supplementary_goals_new")
public class SupplementaryGoals extends GenericModel<Integer> {

	private Integer midYearReview;
	private Integer summaryRaiting;
	private Integer weight;
	private Sections sections;
	private SectionsData sectionsData;

	private Integer countFiles = 0;

	private List<SupplementaryGoalsFile> fileList = new ArrayList<>();
	private List<SupplementaryGoalsHistory> historyList = new ArrayList<>();
	private List<SupplementaryGoalsComment> commentList = new ArrayList<>();
	private List<CommentGroup<SupplementaryGoalsComment>> commentGroupList;

	

	public SupplementaryGoals(Integer midYearReview, Integer summaryRaiting, Integer weight, Sections sections,
			SectionsData sectionsData) {
		super();
		this.midYearReview = midYearReview;
		this.summaryRaiting = summaryRaiting;
		this.weight = weight;
		this.sections = sections;
		this.sectionsData = sectionsData;
	}
	public SupplementaryGoals(SectionsData sectionsData) {
		super();
		this.sectionsData = sectionsData;
	}
	
	public SupplementaryGoals(Sections sections, SectionsData sectionsData) {
		super();
		this.sections = sections;
		this.sectionsData = sectionsData;
	}
	
	public SupplementaryGoals(Sections sections, SectionsData sectionsData,Integer weight) {
		super();
		this.sections = sections;
		this.sectionsData = sectionsData;
		this.weight=weight;
	}
	
	public SupplementaryGoals() {
		super();
	}


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "midYearReview")
	public Integer getMidYearReview() {
		return midYearReview;
	}

	public void setMidYearReview(Integer midYearReview) {
		this.midYearReview = midYearReview;
	}

	@Column(name = "summaryRaiting")
	public Integer getSummaryRaiting() {
		return summaryRaiting;
	}

	public void setSummaryRaiting(Integer summaryRaiting) {
		this.summaryRaiting = summaryRaiting;
	}

	@Column(name = "weight")
	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public Sections getSections() {
		return sections;
	}

	public void setSections(Sections sections) {
		this.sections = sections;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public SectionsData getSectionsData() {
		return sectionsData;
	}

	public void setSectionsData(SectionsData sectionsData) {
		this.sectionsData = sectionsData;
	}

	@Transient
	public String getSectionsTitle() {
		return sections == null ? null : sections.getSectionsTitle();
	}

	@Transient
	public String getGoalTitle() {
		return sectionsData == null ? null : sectionsData.getGoalTitle();
	}

	// File

	public void calculateCountFiles() {
		countFiles = fileList.size();
	}

	@Transient
	public Boolean getHasFiles() {
		return countFiles > 0;
	}

	public void addFile(SupplementaryGoalsFile file) {
		file.setParent(this);
		fileList.add(file);
		calculateCountFiles();
	}

	public void removeFile(SupplementaryGoalsFile file) {
		file.setParent(null);
		fileList.remove(file);
		calculateCountFiles();
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<SupplementaryGoalsFile> getFileList() {
		return fileList;
	}

	public void setFileList(List<SupplementaryGoalsFile> fileList) {
		this.fileList = fileList;
	}

	public Integer getCountFiles() {
		return countFiles;
	}

	public void setCountFiles(Integer countFiles) {
		this.countFiles = countFiles;
	}

	// History
	public void addHistory(SupplementaryGoalsHistory history) {
		history.setParent(this);
		historyList.add(history);
	}

	public void removeHistory(SupplementaryGoalsHistory history) {
		history.setParent(null);
		historyList.remove(history);
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<SupplementaryGoalsHistory> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<SupplementaryGoalsHistory> historyList) {
		this.historyList = historyList;
	}

	// Comment
	public void addComment(SupplementaryGoalsComment comment) {
		comment.setParent(this);
		commentList.add(comment);
	}

	public void removeComment(SupplementaryGoalsComment comment) {
		comment.setParent(null);
		commentList.remove(comment);
	}

	private void generateCommentGroupList() {
		Map<String, List<SupplementaryGoalsComment>> map = new HashMap<>();
		for (SupplementaryGoalsComment comment : commentList) {
			String dateStr = UtilsFunctions.getFormattedDate(comment.getDate());
			map.putIfAbsent(dateStr, new ArrayList<SupplementaryGoalsComment>());
			map.get(dateStr).add(comment);
		}
		commentGroupList = new ArrayList<>();
		for (String dateStr : map.keySet())
			commentGroupList.add(new CommentGroup<>(UtilsFunctions.getDate(dateStr), map.get(dateStr)));
		Collections.sort(commentGroupList);
	}

	@Transient
	public List<CommentGroup<SupplementaryGoalsComment>> getCommentGroupList() {
		if (commentGroupList == null)
			generateCommentGroupList();
		return commentGroupList;
	}

	@Transient
	public void setCommentGroupList(List<CommentGroup<SupplementaryGoalsComment>> commentGroupList) {
		this.commentGroupList = commentGroupList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<SupplementaryGoalsComment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<SupplementaryGoalsComment> commentList) {
		this.commentList = commentList;
	}

}
