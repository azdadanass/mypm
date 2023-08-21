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

@Entity
@Table(name="mypm_user_appraisal_new")
public class UserAppraisal extends GenericModel<Integer>{

	private Appraisals appraisal;
	private User employ;
	private User appraisee;
	private Integer  raiting;
	private String comment;
	private String commentApp;
	
	private Integer countFiles = 0;
	private UserAppraisalStatus userAppraisalStatus=UserAppraisalStatus.CREATED;

	private List<UserAppraisalFile> fileList = new ArrayList<>();
	private List<UserAppraisalHistory> historyList = new ArrayList<>();
	private List<UserAppraisalComment> commentList = new ArrayList<>();
	private List<CommentGroup<UserAppraisalComment>> commentGroupList;

	// Created
	private Date dateStatsCreated;
	private User userStatsCreated;

	// Edited
	private Date dateStatsEdited;
	private User userStatsEdited;
	
	// Submited
	private Date dateStatsSubmited;
	private User userStatsSubmited;

	// ApprovedLM
	private Date dateStatsApprovedLM;
	private User userStatsApprovedLM;

	// Approved
	private Date dateStatsApproved;
	private User userStatsApproved;
	
	// Rejected
	private Date dateStatsRejected;
	private User userStatsRejected;

	// Assessment Mid
	private Date dateStatsSelfAssessmentMidYear;
	private User userStatsSelfAssessmentMidYear;
	
	// Submited Mid
	private Date dateStatsSubmitedMidYear;
	private User userStatsSubmitedMidYear;

	// ApprovedLM mid year
	private Date dateStatsApprovedLMMidYear;
	private User userStatsApprovedLMMidYear;

	// Rejected Mid
	private Date dateStatsRejectedMidYear;
	private User userStatsRejectedMidYear;
	
	// Assessment Final
	private Date dateStatsSelfAssessmentFinalYear;
	private User userStatsSelfAssessmentFinalYear;
	
	
	// Submited Final
	private Date dateStatsSubmitedFinalYear;
	private User userStatsSubmitedFinalYear;

	// ApprovedLM Final year
	private Date dateStatsApprovedLMFinalYear;
	private User userStatsApprovedLMFinalYear;



	// Rejected Final
	private Date dateStatsRejectedFinalYear;
	private User userStatsRejectedFinalYear;
	
	// Closed
	private Date dateStatsClosed;
	private User userStatsClosed;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public User getAppraisee() {
		return appraisee;
	}

	public void setAppraisee(User appraisee) {
		this.appraisee = appraisee;
	}

	@Column(name = "comment")
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	@Column(name = "raiting")
	public Integer getRaiting() {
		return raiting;
	}

	public void setRaiting(Integer raiting) {
		this.raiting = raiting;
	}


	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public Appraisals getAppraisal() {
		return appraisal;
	}

	public void setAppraisal(Appraisals appraisal) {
		this.appraisal = appraisal;
	}
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public User getEmploy() {
		return employ;
	}

	public void setEmploy(User employ_idemploy) {
		this.employ = employ_idemploy;
	}
	@Column(name = "commentApp")
	public String getCommentApp() {
		return commentApp;
	}

	public void setCommentApp(String commentApp) {
		this.commentApp = commentApp;
	}

	
	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	public UserAppraisalStatus getUserAppraisalStatus() {
		return userAppraisalStatus;
	}

	public void setUserAppraisalStatus(UserAppraisalStatus userAppraisalStatus) {
		this.userAppraisalStatus = userAppraisalStatus;
	}
	
	
	@Transient
	public String getAppraisalName() {
		return appraisal == null ? null : appraisal.getAppraisalName();
	}
	
	@Transient
	public String getEmployName() {
		return employ == null ? null : employ.getFullName();
	}

	//File 

	public void calculateCountFiles() {
		countFiles = fileList.size();
	}
	
	@Transient
	public Boolean getHasFiles() {
		return countFiles > 0;
	}

	public void addFile(UserAppraisalFile file) {
		file.setParent(this);
		fileList.add(file);
		calculateCountFiles();
	}

	public void removeFile(UserAppraisalFile file) {
		file.setParent(null);
		fileList.remove(file);
		calculateCountFiles();
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<UserAppraisalFile> getFileList() {
		return fileList;
	}

	public void setFileList(List<UserAppraisalFile> fileList) {
		this.fileList = fileList;
	}

	public Integer getCountFiles() {
		return countFiles;
	}

	public void setCountFiles(Integer countFiles) {
		this.countFiles = countFiles;
	}
	
	//History
			public void addHistory(UserAppraisalHistory history) {
				history.setParent(this);
				historyList.add(history);
			}

			public void removeHistory(UserAppraisalHistory history) {
				history.setParent(null);
				historyList.remove(history);
			}
			
			@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
			public List<UserAppraisalHistory> getHistoryList() {
				return historyList;
			}

			public void setHistoryList(List<UserAppraisalHistory> historyList) {
				this.historyList = historyList;
			}
			
			//Comment
			public void addComment(UserAppraisalComment comment) {
				comment.setParent(this);
				commentList.add(comment);
			}

			public void removeComment(UserAppraisalComment comment) {
				comment.setParent(null);
				commentList.remove(comment);
			}

			private void generateCommentGroupList() {
				Map<String, List<UserAppraisalComment>> map = new HashMap<>();
				for (UserAppraisalComment comment : commentList) {
					String dateStr = UtilsFunctions.getFormattedDate(comment.getDate());
					map.putIfAbsent(dateStr, new ArrayList<UserAppraisalComment>());
					map.get(dateStr).add(comment);
				}
				commentGroupList = new ArrayList<>();
				for (String dateStr : map.keySet())
					commentGroupList.add(new CommentGroup<>(UtilsFunctions.getDate(dateStr), map.get(dateStr)));
				Collections.sort(commentGroupList);
			}

			@Transient
			public List<CommentGroup<UserAppraisalComment>> getCommentGroupList() {
				if (commentGroupList == null)
					generateCommentGroupList();
				return commentGroupList;
			}

			@Transient
			public void setCommentGroupList(List<CommentGroup<UserAppraisalComment>> commentGroupList) {
				this.commentGroupList = commentGroupList;
			}

			@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
			public List<UserAppraisalComment> getCommentList() {
				return commentList;
			}

			public void setCommentList(List<UserAppraisalComment> commentList) {
				this.commentList = commentList;
			}

			public Date getDateStatsCreated() {
				return dateStatsCreated;
			}

			public void setDateStatsCreated(Date dateStatsCreated) {
				this.dateStatsCreated = dateStatsCreated;
			}

			@ManyToOne(fetch = FetchType.LAZY)
			public User getUserStatsCreated() {
				return userStatsCreated;
			}

			public void setUserStatsCreated(User userStatsCreated) {
				this.userStatsCreated = userStatsCreated;
			}

			public Date getDateStatsSubmited() {
				return dateStatsSubmited;
			}

			public void setDateStatsSubmited(Date dateStatsSubmited) {
				this.dateStatsSubmited = dateStatsSubmited;
			}

			@ManyToOne(fetch = FetchType.LAZY)
			public User getUserStatsSubmited() {
				return userStatsSubmited;
			}

			public void setUserStatsSubmited(User userStatsSubmited) {
				this.userStatsSubmited = userStatsSubmited;
			}

			public Date getDateStatsApprovedLM() {
				return dateStatsApprovedLM;
			}

			public void setDateStatsApprovedLM(Date dateStatsApprovedLM) {
				this.dateStatsApprovedLM = dateStatsApprovedLM;
			}
			
			@ManyToOne(fetch = FetchType.LAZY)
			public User getUserStatsApprovedLM() {
				return userStatsApprovedLM;
			}

			public void setUserStatsApprovedLM(User userStatsApprovedLM) {
				this.userStatsApprovedLM = userStatsApprovedLM;
			}

			public Date getDateStatsApproved() {
				return dateStatsApproved;
			}

			public void setDateStatsApproved(Date dateStatsApproved) {
				this.dateStatsApproved = dateStatsApproved;
			}


			@ManyToOne(fetch = FetchType.LAZY)
			public User getUserStatsApproved() {
				return userStatsApproved;
			}

			public void setUserStatsApproved(User userStatsApproved) {
				this.userStatsApproved = userStatsApproved;
			}

			public Date getDateStatsSubmitedMidYear() {
				return dateStatsSubmitedMidYear;
			}

			public void setDateStatsSubmitedMidYear(Date dateStatsSubmitedMidYear) {
				this.dateStatsSubmitedMidYear = dateStatsSubmitedMidYear;
			}

			@ManyToOne(fetch = FetchType.LAZY)
			public User getUserStatsSubmitedMidYear() {
				return userStatsSubmitedMidYear;
			}

			public void setUserStatsSubmitedMidYear(User userStatsSubmitedMidYear) {
				this.userStatsSubmitedMidYear = userStatsSubmitedMidYear;
			}

			public Date getDateStatsApprovedLMMidYear() {
				return dateStatsApprovedLMMidYear;
			}

			public void setDateStatsApprovedLMMidYear(Date dateStatsApprovedLMMidYear) {
				this.dateStatsApprovedLMMidYear = dateStatsApprovedLMMidYear;
			}

			@ManyToOne(fetch = FetchType.LAZY)
			public User getUserStatsApprovedLMMidYear() {
				return userStatsApprovedLMMidYear;
			}

			public void setUserStatsApprovedLMMidYear(User userStatsApprovedLMMidYear) {
				this.userStatsApprovedLMMidYear = userStatsApprovedLMMidYear;
			}

			public Date getDateStatsSubmitedFinalYear() {
				return dateStatsSubmitedFinalYear;
			}

			public void setDateStatsSubmitedFinalYear(Date dateStatsSubmitedFinalYear) {
				this.dateStatsSubmitedFinalYear = dateStatsSubmitedFinalYear;
			}

			@ManyToOne(fetch = FetchType.LAZY)
			public User getUserStatsSubmitedFinalYear() {
				return userStatsSubmitedFinalYear;
			}

			public void setUserStatsSubmitedFinalYear(User userStatsSubmitedFinalYear) {
				this.userStatsSubmitedFinalYear = userStatsSubmitedFinalYear;
			}

			public Date getDateStatsApprovedLMFinalYear() {
				return dateStatsApprovedLMFinalYear;
			}

			public void setDateStatsApprovedLMFinalYear(Date dateStatsApprovedLMFinalYear) {
				this.dateStatsApprovedLMFinalYear = dateStatsApprovedLMFinalYear;
			}

			@ManyToOne(fetch = FetchType.LAZY)
			public User getUserStatsApprovedLMFinalYear() {
				return userStatsApprovedLMFinalYear;
			}

			public void setUserStatsApprovedLMFinalYear(User userStatsApprovedLMFinalYear) {
				this.userStatsApprovedLMFinalYear = userStatsApprovedLMFinalYear;
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

			public Date getDateStatsEdited() {
				return dateStatsEdited;
			}

			public void setDateStatsEdited(Date dateStatsEdited) {
				this.dateStatsEdited = dateStatsEdited;
			}

			@ManyToOne(fetch = FetchType.LAZY)
			public User getUserStatsEdited() {
				return userStatsEdited;
			}

			public void setUserStatsEdited(User userStatsEdited) {
				this.userStatsEdited = userStatsEdited;
			}

			public Date getDateStatsRejected() {
				return dateStatsRejected;
			}

			public void setDateStatsRejected(Date dateStatsRejected) {
				this.dateStatsRejected = dateStatsRejected;
			}
			
			@ManyToOne(fetch = FetchType.LAZY)
			public User getUserStatsRejected() {
				return userStatsRejected;
			}

			public void setUserStatsRejected(User userStatsRejected) {
				this.userStatsRejected = userStatsRejected;
			}

			public Date getDateStatsSelfAssessmentMidYear() {
				return dateStatsSelfAssessmentMidYear;
			}

			public void setDateStatsSelfAssessmentMidYear(Date dateStatsSelfAssessmentMidYear) {
				this.dateStatsSelfAssessmentMidYear = dateStatsSelfAssessmentMidYear;
			}
			
			@ManyToOne(fetch = FetchType.LAZY)
			public User getUserStatsSelfAssessmentMidYear() {
				return userStatsSelfAssessmentMidYear;
			}

			public void setUserStatsSelfAssessmentMidYear(User userStatsSelfAssessmentMidYear) {
				this.userStatsSelfAssessmentMidYear = userStatsSelfAssessmentMidYear;
			}

			public Date getDateStatsRejectedMidYear() {
				return dateStatsRejectedMidYear;
			}

			public void setDateStatsRejectedMidYear(Date dateStatsRejectedMidYear) {
				this.dateStatsRejectedMidYear = dateStatsRejectedMidYear;
			}

			@ManyToOne(fetch = FetchType.LAZY)
			public User getUserStatsRejectedMidYear() {
				return userStatsRejectedMidYear;
			}

			public void setUserStatsRejectedMidYear(User userStatsRejectedMidYear) {
				this.userStatsRejectedMidYear = userStatsRejectedMidYear;
			}

			public Date getDateStatsSelfAssessmentFinalYear() {
				return dateStatsSelfAssessmentFinalYear;
			}

			public void setDateStatsSelfAssessmentFinalYear(Date dateStatsSelfAssessmentFinalYear) {
				this.dateStatsSelfAssessmentFinalYear = dateStatsSelfAssessmentFinalYear;
			}
			
			@ManyToOne(fetch = FetchType.LAZY)
			public User getUserStatsSelfAssessmentFinalYear() {
				return userStatsSelfAssessmentFinalYear;
			}

			public void setUserStatsSelfAssessmentFinalYear(User userStatsSelfAssessmentFinalYear) {
				this.userStatsSelfAssessmentFinalYear = userStatsSelfAssessmentFinalYear;
			}

			public Date getDateStatsRejectedFinalYear() {
				return dateStatsRejectedFinalYear;
			}

			public void setDateStatsRejectedFinalYear(Date dateStatsRejectedFinalYear) {
				this.dateStatsRejectedFinalYear = dateStatsRejectedFinalYear;
			}
			
			@ManyToOne(fetch = FetchType.LAZY)
			public User getUserStatsRejectedFinalYear() {
				return userStatsRejectedFinalYear;
			}

			public void setUserStatsRejectedFinalYear(User userStatsRejectedFinalYear) {
				this.userStatsRejectedFinalYear = userStatsRejectedFinalYear;
			}
}
