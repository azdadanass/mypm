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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import ma.azdad.service.UtilsFunctions;

@Table(name = "mypm_appraisals_new")
@Entity
public class Appraisals extends GenericModel<Integer> {

	private Date creationDate;
	private Date endDate;
	private Date endYearSummaryEndDate;
	private Date endYearSummaryStartDate;
	private Date midYearReviewEndDate;
	private Date midYearReviewStartDate;
	private Date planningEndDate;
	private Date planningStartDate;
	private Date startDate;
	private int year;
	private String appraisalName;
	private String description;

	// Opened
	private Date dateStatsOpen;
	private User userStatsOpen;

	// midYearReview
	private Date dateStatsMid;
	private User userStatsMid;

	// finalYearReview
	private Date dateStatsFinal;
	private User userStatsFinal;

	// Closed
	private Date dateStatsClosed;
	private User userStatsClosed;

	private Integer countFiles = 0;

	private AppraisalsStatus appraisalsStatus = AppraisalsStatus.OPEN;
	private List<AppraisalsFile> fileList = new ArrayList<>();
	private List<AppraisalsHistory> historyList = new ArrayList<>();
	private List<AppraisalsComment> commentList = new ArrayList<>();
	private List<CommentGroup<AppraisalsComment>> commentGroupList;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Appraisal")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "creation_date")
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@Column(name = "End_date")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(name = "End_Year_summary_end_date")
	public Date getEndYearSummaryEndDate() {
		return endYearSummaryEndDate;
	}

	public void setEndYearSummaryEndDate(Date endYearSummaryEndDate) {
		this.endYearSummaryEndDate = endYearSummaryEndDate;
	}

	@Column(name = "End_Year_summary_start_date")
	public Date getEndYearSummaryStartDate() {
		return endYearSummaryStartDate;
	}

	public void setEndYearSummaryStartDate(Date endYearSummaryStartDate) {
		this.endYearSummaryStartDate = endYearSummaryStartDate;
	}

	@Column(name = "Mid_Year_Review_end_Date")
	public Date getMidYearReviewEndDate() {
		return midYearReviewEndDate;
	}

	public void setMidYearReviewEndDate(Date midYearReviewEndDate) {
		this.midYearReviewEndDate = midYearReviewEndDate;
	}

	@Column(name = "Mid_Year_Review_Start_Date")
	public Date getMidYearReviewStartDate() {
		return midYearReviewStartDate;
	}

	public void setMidYearReviewStartDate(Date midYearReviewStartDate) {
		this.midYearReviewStartDate = midYearReviewStartDate;
	}

	@Column(name = "Planning_end_date")
	public Date getPlanningEndDate() {
		return planningEndDate;
	}

	public void setPlanningEndDate(Date planningEndDate) {
		this.planningEndDate = planningEndDate;
	}

	@Column(name = "Planning_Start_Date")
	public Date getPlanningStartDate() {
		return planningStartDate;
	}

	public void setPlanningStartDate(Date planningStartDate) {
		this.planningStartDate = planningStartDate;
	}

	@Column(name = "Start_Date")
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Column(name = "Year")
	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	@Column(name = "appraisalname")
	public String getAppraisalName() {
		return appraisalName;
	}

	public void setAppraisalName(String appraisalName) {
		this.appraisalName = appraisalName;
	}

	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Enumerated(EnumType.STRING)
	public AppraisalsStatus getAppraisalsStatus() {
		return appraisalsStatus;
	}

	public void setAppraisalsStatus(AppraisalsStatus appraisalsStatus) {
		this.appraisalsStatus = appraisalsStatus;
	}

	// File

	public void calculateCountFiles() {
		countFiles = fileList.size();
	}

	@Transient
	public Boolean getHasFiles() {
		return countFiles > 0;
	}

	public void addFile(AppraisalsFile file) {
		file.setParent(this);
		fileList.add(file);
		calculateCountFiles();
	}

	public void removeFile(AppraisalsFile file) {
		file.setParent(null);
		fileList.remove(file);
		calculateCountFiles();
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<AppraisalsFile> getFileList() {
		return fileList;
	}

	public void setFileList(List<AppraisalsFile> fileList) {
		this.fileList = fileList;
	}

	public Integer getCountFiles() {
		return countFiles;
	}

	public void setCountFiles(Integer countFiles) {
		this.countFiles = countFiles;
	}

	// History
	public void addHistory(AppraisalsHistory history) {
		history.setParent(this);
		historyList.add(history);
	}

	public void removeHistory(AppraisalsHistory history) {
		history.setParent(null);
		historyList.remove(history);
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<AppraisalsHistory> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<AppraisalsHistory> historyList) {
		this.historyList = historyList;
	}

	// Comment
	public void addComment(AppraisalsComment comment) {
		comment.setParent(this);
		commentList.add(comment);
	}

	public void removeComment(AppraisalsComment comment) {
		comment.setParent(null);
		commentList.remove(comment);
	}

	private void generateCommentGroupList() {
		Map<String, List<AppraisalsComment>> map = new HashMap<>();
		for (AppraisalsComment comment : commentList) {
			String dateStr = UtilsFunctions.getFormattedDate(comment.getDate());
			map.putIfAbsent(dateStr, new ArrayList<AppraisalsComment>());
			map.get(dateStr).add(comment);
		}
		commentGroupList = new ArrayList<>();
		for (String dateStr : map.keySet())
			commentGroupList.add(new CommentGroup<>(UtilsFunctions.getDate(dateStr), map.get(dateStr)));
		Collections.sort(commentGroupList);
	}

	@Transient
	public List<CommentGroup<AppraisalsComment>> getCommentGroupList() {
		if (commentGroupList == null)
			generateCommentGroupList();
		return commentGroupList;
	}

	@Transient
	public void setCommentGroupList(List<CommentGroup<AppraisalsComment>> commentGroupList) {
		this.commentGroupList = commentGroupList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<AppraisalsComment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<AppraisalsComment> commentList) {
		this.commentList = commentList;
	}

	public Date getDateStatsMid() {
		return dateStatsMid;
	}

	public void setDateStatsMid(Date dateStatsMid) {
		this.dateStatsMid = dateStatsMid;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public User getUserStatsMid() {
		return userStatsMid;
	}

	public void setUserStatsMid(User userStatsMid) {
		this.userStatsMid = userStatsMid;
	}

	public Date getDateStatsFinal() {
		return dateStatsFinal;
	}

	public void setDateStatsFinal(Date dateStatsFinal) {
		this.dateStatsFinal = dateStatsFinal;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public User getUserStatsFinal() {
		return userStatsFinal;
	}

	public void setUserStatsFinal(User userStatsFinal) {
		this.userStatsFinal = userStatsFinal;
	}

	public Date getDateStatsClosed() {
		return dateStatsClosed;
	}

	public void setDateStatsClosed(Date dateStatsClosed) {
		this.dateStatsClosed = dateStatsClosed;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public User getUserStatsClosed() {
		return userStatsClosed;
	}

	public void setUserStatsClosed(User userStatsClosed) {
		this.userStatsClosed = userStatsClosed;
	}

	public Date getDateStatsOpen() {
		return dateStatsOpen;
	}

	public void setDateStatsOpen(Date dateStatsOpen) {
		this.dateStatsOpen = dateStatsOpen;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	public User getUserStatsOpen() {
		return userStatsOpen;
	}

	public void setUserStatsOpen(User userStatsOpen) {
		this.userStatsOpen = userStatsOpen;
	}
	
	

}