package ma.azdad.view;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import ma.azdad.model.BankAccount;
import ma.azdad.model.Company;
import ma.azdad.model.CompanyComment;
import ma.azdad.model.CompanyFile;
import ma.azdad.model.CompanyHistory;
import ma.azdad.model.CompanyProfile;
import ma.azdad.repos.CompanyRepos;
import ma.azdad.service.AccountingSystemService;
import ma.azdad.service.BankAccountService;
import ma.azdad.service.CompanyService;
import ma.azdad.service.RestTemplateService;
import ma.azdad.service.UserService;
import ma.azdad.service.UtilsFunctions;
import ma.azdad.utils.App;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Scope("view")
public class CompanyView extends GenericView<Integer, Company, CompanyRepos, CompanyService> {

	@Autowired
	private SessionView sessionView;

	@Autowired
	UserService userService;

	@Autowired
	RestTemplateService restTemplateService;

	@Autowired
	BankAccountService bankAccountService;

	@Autowired
	AccountingSystemService accountingSystemService;

	private CompanyProfile profile;

	@Override
	@PostConstruct
	public void init() {
		super.init();
		time();
	}

	@Override
	protected void initParameters() {
		super.initParameters();
	}

	@Override
	protected Boolean canAccess() {
		return sessionView.getIsInternalAdmin();
	}

	// save
	public Boolean canSave() {
		return sessionView.getIsInternalAdmin();
	}

	public String save() {
		if (!canSave())
			return addParameters(listPage, "faces-redirect=true");
		if (!validate())
			return null;

		model.addHistory(new CompanyHistory(getIsAddPage() ? "Created" : "Edited", sessionView.getUser(), getIsAddPage() ? null : UtilsFunctions.getChanges(model, old)));

		model = service.save(model);
		if (isEditPage && !model.getName().equals(old.getName())) {
			restTemplateService.consumRest(App.COMPTA.getIp() + "/rest/exercise/generateName");
			bankAccountService.generateFullNameByCompany(model.getId());
		}

		return addParameters(viewPage, "faces-redirect=true", "id=" + model.getId());
	}

	public Boolean validate() {
		return true;
	}

	// delete
	public Boolean canDelete() {
		return sessionView.getIsInternalAdmin();
	}

	public String delete() {
		if (!canDelete())
			return null;
		try {
			service.delete(model);
		} catch (DataIntegrityViolationException e) {
			FacesContextMessages.ErrorMessages("Can not delete this item (contains childs)");
			log.error(e.getMessage());
			return null;
		} catch (Exception e) {
			FacesContextMessages.ErrorMessages("Error !");
			e.printStackTrace();
			log.error(e.getMessage());
			return null;
		}
		return addParameters(listPage, "faces-redirect=true");
	}

	// files
	private CompanyFile file;
	private String fileType;

	public Boolean canAddFile() {
		return sessionView.getIsInternalAdmin();
	}

	public void handleFileUpload(FileUploadEvent event) throws IOException {
		if (!canAddFile())
			return;
		File file = fileUploadView.handleFileUpload(event, getClassName2());
		CompanyFile modelFile = new CompanyFile(file, fileType, event.getFile().getFileName(), sessionView.getUser());
		model.addFile(modelFile);
		synchronized (CompanyView.class) {
			model.calculateCountFiles();
			model = service.saveAndRefresh(model);
		}
	}

	public Boolean canDeleteFile() {
		return sessionView.getIsInternalAdmin();
	}

	public void deleteFile() {
		if (!canDeleteFile())
			return;
		model.removeFile(file);
		model = service.saveAndRefresh(model);
	}

	// photos
	public Boolean canUploadPhoto() {
		return sessionView.getIsInternalAdmin();
	}

	public void handlePhotoUpload(FileUploadEvent event) throws IOException {
		File file = fileUploadView.handlePhotoUpload(event, getClassName2(), 400 * 1024);
		model.setPhoto("files/" + getClassName2() + "/" + file.getName());
		synchronized (CompanyView.class) {
			model = service.saveAndRefresh(model);
		}
	}

	// comments
	private CompanyComment comment = new CompanyComment();

	public Boolean canAddComment() {
		return sessionView.getIsInternalAdmin();
	}

	public void addComment() {
		if (!canAddComment())
			return;
		comment.setDate(new Date());
		comment.setUser(sessionView.getUser());
		model.addComment(comment);
		model = service.saveAndRefresh(model);
	}

	// company profile

	public Boolean canAddProfile() {
		return sessionView.getIsInternalAdmin();
	}

	public void initProfile() {
		profile = new CompanyProfile();
	}

	public void addProfile() {
		if (!canAddProfile() || !validateAddProfile())
			return;
		model.addProfile(profile);
		model = service.saveAndRefresh(model);
	}

	private Boolean validateAddProfile() {
		if (model.getProfileList().stream().filter(
				p -> p.getErp().contentEquals(profile.getErp()) && p.getProfile().equals(profile.getProfile()) && p.getUserUsername().contentEquals(profile.getUserUsername()))
				.count() > 0)
			return FacesContextMessages.ErrorMessages("Line alreday exists !");
		return true;
	}

	public Boolean canDeleteProfile() {
		return sessionView.getIsInternalAdmin();
	}

	public void deleteProfile(CompanyProfile profile) {
		if (!canDeleteProfile())
			return;
		model.removeProfile(profile);
		model = service.saveAndRefresh(model);
	}

	// bankaccounts
	private BankAccount bankAccount;

	public void initBankAccount() {
		bankAccount = new BankAccount();
	}

	public Boolean canAddBankAccount() {
		return sessionView.getIsInternalAdmin();
	}

	public Boolean validateBankAccount() {
//		if (model.getAccounting() && String.valueOf(bankAccount.getAccountingCode()).length() != model.getAccountNumberOfDigit())
//			return FacesContextMessages.ErrorMessages("Accouting Code Number of digits should be equals to : " + model.getAccountNumberOfDigit());

		if (model.getAccounting())
			if (!accountingSystemService.isNumeroExists(bankAccount.getAccountingCode(), model.getId()))
				return FacesContextMessages.ErrorMessages("Accounting Code non-existent in accounting system");
			else if (!accountingSystemService.isNumeroExists(bankAccount.getBankChargeAccountingCode(), model.getId()))
				return FacesContextMessages.ErrorMessages("Bank Charge Accounting Code non-existent in accounting system");
			else if (!accountingSystemService.isNumeroExists(bankAccount.getVatAccountingCode(), model.getId()))
				return FacesContextMessages.ErrorMessages("Vat Accounting Code  non-existent in accounting system");
			else if (!accountingSystemService.isNumeroExists(bankAccount.getCashAccountingCode(), model.getId()))
				return FacesContextMessages.ErrorMessages("Cash Accounting Code non-existent in accounting system");
			else if (!accountingSystemService.isNumeroExists(bankAccount.getCaisseAccountingCode(), model.getId()))
				return FacesContextMessages.ErrorMessages("Caisse Accounting Code non-existent in accounting system");

		return true;
	}

	public void addBankaccount() {
		if (!canAddBankAccount())
			return;
		model.addBankAccount(bankAccount);
	}

	public void saveBankAccount() {
		if (!canAddBankAccount() || !validateBankAccount())
			return;
		if (bankAccount.getId() == null) {
			model.addBankAccount(bankAccount);
			bankAccount.generateFullName();
		}
		model = service.saveAndRefresh(model);
		execJavascript("PF('addBankAccountDlg').hide()");
	}

	public Boolean canDeleteBankAccount() {
		return sessionView.getIsInternalAdmin();
	}

	public void deleteBankAccount() {
		if (!canDeleteBankAccount())
			return;
		model.removeBankAccount(bankAccount);
		model = service.saveAndRefresh(model);
		execJavascript("PF('deleteBankAccountDlg').hide()");
	}

	// photos
	public Boolean canUploadBankAccountPhoto() {
		return sessionView.getIsInternalAdmin();
	}

	public void handleBankAccountPhotoUpload(FileUploadEvent event) throws IOException {
		System.out.println(bankAccount.getFullName());
		File file = fileUploadView.handlePhotoUpload(event, "bankAccount", 400 * 1024);
		bankAccount.setLogo("files/bankAccount/" + file.getName());
		synchronized (CompanyView.class) {
			model = service.saveAndRefresh(model);
		}
	}

	// generic

	@Cacheable("companyView.findLight")
	public List<Company> findLight() {
		return service.findLight();
	}

	public List<Company> findAll() {
		return service.findAll();
	}

	// getters & setters
	public Company getModel() {
		return model;
	}

	public void setModel(Company model) {
		this.model = model;
	}

	public CompanyFile getFile() {
		return file;
	}

	public void setFile(CompanyFile file) {
		this.file = file;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public CompanyComment getComment() {
		return comment;
	}

	public void setComment(CompanyComment comment) {
		this.comment = comment;
	}

	public BankAccount getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(BankAccount bankAccount) {
		this.bankAccount = bankAccount;
	}

	public CompanyProfile getProfile() {
		return profile;
	}

	public void setProfile(CompanyProfile profile) {
		this.profile = profile;
	}

}
