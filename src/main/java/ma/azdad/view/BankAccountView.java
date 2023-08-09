package ma.azdad.view;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import ma.azdad.model.BankAccount;
import ma.azdad.model.BankAccountComment;
import ma.azdad.model.BankAccountFile;
import ma.azdad.model.BankAccountHistory;
import ma.azdad.repos.BankAccountRepos;
import ma.azdad.service.BankAccountService;
import ma.azdad.service.CurrencyService;
import ma.azdad.service.SupplierService;
import ma.azdad.service.UtilsFunctions;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Scope("view")
public class BankAccountView extends GenericView<Integer, BankAccount, BankAccountRepos, BankAccountService> {

	@Autowired
	private SessionView sessionView;

	@Autowired
	private SupplierService supplierService;

	@Autowired
	private CurrencyService currencyService;

	private Integer supplierId;

	@Override
	@PostConstruct
	public void init() {
		super.init();
		time();
	}

	@Override
	protected Boolean canAccess() {
		return sessionView.isBuyer() || sessionView.isCfo();
	}

	@Override
	public void refreshList() {
		if (isListPage)
			list2 = list1 = service.findAll();
	}

	@Override
	protected void addPage() {
		super.addPage();
		model.setSupplier(supplierService.findOne(supplierId));
	}

	@Override
	protected void initParameters() {
		super.initParameters();
		supplierId = UtilsFunctions.getIntegerParameter("supplierId");
	}

	// save
	public Boolean canSave() {
		if (isAddPage || isListPage)
			return sessionView.isBuyer();
		if (isEditPage || isViewPage)
			return sessionView.isBuyer();
		return false;
	}

	public String save() {
		if (!canSave())
			return addParameters(listPage, "faces-redirect=true");
		if (!validate())
			return null;

		model.setCurrency(currencyService.findOne(model.getCurrencyId()));
		model.generateFullName();

		model.addHistory(new BankAccountHistory(isAddPage ? "Created" : "Edited", sessionView.getUser(), isAddPage ? null : UtilsFunctions.getChanges(model, old)));
		model = service.save(model);
		return addParameters(viewPage, "faces-redirect=true", "id=" + model.getId());
	}

	public Boolean validate() {
		if (model.getRib().isEmpty() && model.getAccountNumber().isEmpty())
			return FacesContextMessages.ErrorMessages("RIB and Account Number should not be both null");

		return true;
	}

	public Boolean canEditCurrency() {
		return false;
	}

	// toggle status
	public Boolean canToggle() {
		return sessionView.isBuyer();
	}

	public void toggle() {
		if (!canToggle())
			return;
		model.setActive(!model.getActive());
		model = service.saveAndRefresh(model);
		refreshList();
	}

	// delete
	public Boolean canDelete() {
		return sessionView.isBuyer();
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
	private BankAccountFile file;
	private String fileType;

	public Boolean canAddFile() {
		return sessionView.isBuyer();
	}

	public Boolean validateAddFile() {
		return model.getFileList().stream().filter(i -> fileType.equals(i.getType())).count() == 0;
	}

	public void handleFileUpload(FileUploadEvent event) throws IOException {
		if (!canAddFile())
			return;
		if (!validateAddFile()) {
			FacesContextMessages.ErrorMessages("File Type (" + fileType + ") already added !");
			return;
		}
		File file = fileUploadView.handleFileUpload(event, getClassName2());
		BankAccountFile modelFile = new BankAccountFile(file, fileType, event.getFile().getFileName(), sessionView.getUser());
		model.addFile(modelFile);
		synchronized (BankAccountView.class) {
			model.calculateCountFiles();
			model = service.saveAndRefresh(model);
		}
	}

	public Boolean canDeleteFile() {
		return canAddFile();
	}

	public void deleteFile() {
		if (!canDeleteFile())
			return;
		model.removeFile(file);
		model = service.saveAndRefresh(model);
	}

	// comments
	private BankAccountComment comment = new BankAccountComment();

	public Boolean canAddComment() {
		return canAccess();
	}

	public void addComment() {
		if (!canAddComment())
			return;
		comment.setDate(new Date());
		comment.setUser(sessionView.getUser());
		model.addComment(comment);
		model = service.saveAndRefresh(model);
	}

	// getters & setters
	public BankAccount getModel() {
		return model;
	}

	public void setModel(BankAccount model) {
		this.model = model;
	}

	public BankAccountFile getFile() {
		return file;
	}

	public void setFile(BankAccountFile file) {
		this.file = file;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public BankAccountComment getComment() {
		return comment;
	}

	public void setComment(BankAccountComment comment) {
		this.comment = comment;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

}
