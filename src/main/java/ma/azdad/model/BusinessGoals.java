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
@Table(name = "mypm_business_goals_new")
public class BusinessGoals extends GenericModel<Integer> {

	private String goalDetails;
	private String goalTitle;
	private double goalWeight;
	private Integer midYearReview;
	private Integer summaryRaiting;
	private String comment;
	private String commentEY;
	private String commentapp;
	private String commentappEY;
	private Sections sections;

	private Integer countFiles = 0;

	private List<BusinessGoalsFile> fileList = new ArrayList<>();
	private List<BusinessGoalsHistory> historyList = new ArrayList<>();
	private List<BusinessGoalsComment> commentList = new ArrayList<>();
	private List<CommentGroup<BusinessGoalsComment>> commentGroupList;

	public BusinessGoals(String goalDetails, String goalTitle, double goalWeight) {
		super();
		this.goalDetails = goalDetails;
		this.goalTitle = goalTitle;
		this.goalWeight = goalWeight;
	}
	public BusinessGoals(String goalDetails, String goalTitle, double goalWeight, Sections sections) {
		super();
		this.goalDetails = goalDetails;
		this.goalTitle = goalTitle;
		this.goalWeight = goalWeight;
		this.sections = sections;
	}
	
	

	public BusinessGoals(String goalDetails, String goalTitle, double goalWeight, Integer midYearReview,
			Integer summaryRaiting, Sections sections) {
		super();
		this.goalDetails = goalDetails;
		this.goalTitle = goalTitle;
		this.goalWeight = goalWeight;
		this.midYearReview = midYearReview;
		this.summaryRaiting = summaryRaiting;
		this.sections = sections;
	}
	
	public BusinessGoals() {
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

	@Column(name = "goalDetails")
	public String getGoalDetails() {
		return goalDetails;
	}

	public void setGoalDetails(String goalDetails) {
		this.goalDetails = goalDetails;
	}

	@Column(name = "goalTitle")
	public String getGoalTitle() {
		return goalTitle;
	}

	public void setGoalTitle(String goalTitle) {
		this.goalTitle = goalTitle;
	}

	@Column(name = "goalWeight")
	public double getGoalWeight() {
		return goalWeight;
	}

	public void setGoalWeight(double goalWeight) {
		this.goalWeight = goalWeight;
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

	@Column(name = "comment")
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Column(name = "commentEY")
	public String getCommentEY() {
		return commentEY;
	}

	public void setCommentEY(String commentEY) {
		this.commentEY = commentEY;
	}

	@Column(name = "commentapp")
	public String getCommentapp() {
		return commentapp;
	}

	public void setCommentapp(String commentapp) {
		this.commentapp = commentapp;
	}

	@Column(name = "commentappEY")
	public String getCommentappEY() {
		return commentappEY;
	}

	public void setCommentappEY(String commentappEY) {
		this.commentappEY = commentappEY;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
	public Sections getSections() {
		return sections;
	}

	public void setSections(Sections sections) {
		this.sections = sections;
	}

	@Transient
	public String getSectionsTitle() {
		return sections == null ? null : sections.getSectionsTitle();
	}

	// File

	public void calculateCountFiles() {
		countFiles = fileList.size();
	}

	@Transient
	public Boolean getHasFiles() {
		return countFiles > 0;
	}

	public void addFile(BusinessGoalsFile file) {
		file.setParent(this);
		fileList.add(file);
		calculateCountFiles();
	}

	public void removeFile(BusinessGoalsFile file) {
		file.setParent(null);
		fileList.remove(file);
		calculateCountFiles();
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<BusinessGoalsFile> getFileList() {
		return fileList;
	}

	public void setFileList(List<BusinessGoalsFile> fileList) {
		this.fileList = fileList;
	}

	public Integer getCountFiles() {
		return countFiles;
	}

	public void setCountFiles(Integer countFiles) {
		this.countFiles = countFiles;
	}

	// History
	public void addHistory(BusinessGoalsHistory history) {
		history.setParent(this);
		historyList.add(history);
	}

	public void removeHistory(BusinessGoalsHistory history) {
		history.setParent(null);
		historyList.remove(history);
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<BusinessGoalsHistory> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<BusinessGoalsHistory> historyList) {
		this.historyList = historyList;
	}

	// Comment
	public void addComment(BusinessGoalsComment comment) {
		comment.setParent(this);
		commentList.add(comment);
	}

	public void removeComment(BusinessGoalsComment comment) {
		comment.setParent(null);
		commentList.remove(comment);
	}

	private void generateCommentGroupList() {
		Map<String, List<BusinessGoalsComment>> map = new HashMap<>();
		for (BusinessGoalsComment comment : commentList) {
			String dateStr = UtilsFunctions.getFormattedDate(comment.getDate());
			map.putIfAbsent(dateStr, new ArrayList<BusinessGoalsComment>());
			map.get(dateStr).add(comment);
		}
		commentGroupList = new ArrayList<>();
		for (String dateStr : map.keySet())
			commentGroupList.add(new CommentGroup<>(UtilsFunctions.getDate(dateStr), map.get(dateStr)));
		Collections.sort(commentGroupList);
	}

	@Transient
	public List<CommentGroup<BusinessGoalsComment>> getCommentGroupList() {
		if (commentGroupList == null)
			generateCommentGroupList();
		return commentGroupList;
	}

	@Transient
	public void setCommentGroupList(List<CommentGroup<BusinessGoalsComment>> commentGroupList) {
		this.commentGroupList = commentGroupList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<BusinessGoalsComment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<BusinessGoalsComment> commentList) {
		this.commentList = commentList;
	}

}
