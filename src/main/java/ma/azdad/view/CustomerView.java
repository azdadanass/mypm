package ma.azdad.view;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.Customer;
import ma.azdad.model.CustomerAccountingCode;
import ma.azdad.model.CustomerCategory;
import ma.azdad.model.CustomerComment;
import ma.azdad.model.CustomerCompany;
import ma.azdad.model.CustomerContact;
import ma.azdad.model.CustomerCurrency;
import ma.azdad.model.CustomerFile;
import ma.azdad.model.CustomerHistory;
import ma.azdad.model.CustomerIncoterms;
import ma.azdad.model.CustomerRevenueType;
import ma.azdad.model.CustomerServiceCode;
import ma.azdad.model.InvoiceTerm;
import ma.azdad.model.InvoiceTermTemplate;
import ma.azdad.repos.CustomerRepos;
import ma.azdad.service.CompanyService;
import ma.azdad.service.CustomerService;
import ma.azdad.service.UserService;
import ma.azdad.service.UtilsFunctions;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Scope("view")
public class CustomerView extends GenericView<Integer, Customer, CustomerRepos, CustomerService> {

	@Autowired
	private SessionView sessionView;

	@Autowired
	FileUploadView fileUploadView;

	@Autowired
	CompanyService companyService;

	@Autowired
	UserService userService;

	private int step = 1;
	private Boolean editContact = false;
	private Boolean editCompany = false;
	private Boolean editCurrency = false;
	private Boolean editIncoterms = false;
	private Boolean editAccountingCode = false;
	private Boolean editServiceCode = false;
	private Boolean editRevenueType = false;
	private Boolean editInvoiceTermTemplate = false;
	private Boolean editOldInvoiceTerm = false;
	private CustomerCategory category;
	private InvoiceTerm oldInvoiceTerm;

	@Override
	@PostConstruct
	public void init() {
		super.init();
		time();
	}

	@Override
	public void refreshList() {
		if (isListPage) {
			if (category == null)
				return;
			else if ("All".equals(category.getName()))
				list2 = list1 = service.find();
			else
				list2 = list1 = service.findByCategory(category.getId());
		} else if ("/customerList2.xhtml".equals(currentPath))
			switch (pageIndex) {
			case 1:
				list2 = list1 = service.findWithouActiveBankAccount();
				break;
			default:
				break;
			}
	}

	@Override
	protected void initParameters() {
		super.initParameters();
	}

	@Override
	public void setSearchBean(String searchBean) {
		this.searchBean = searchBean;

		// force all category
		if (!searchBean.isEmpty() && category == null) {
			category = new CustomerCategory("All");
			refreshList();
		}

		filterBean(searchBean);
	}

	// save
	public Boolean canSave() {
		return sessionView.getIsInternalAdmin() && sessionView.getInternal();
	}

	@Transactional
	public String nextStep() {
		if (!canSave())
			return addParameters(listPage, "faces-redirect=true");

		switch (step) {
		case 1:
			if (!validateStep1())
				break;
			step++;
			break;
		case 2:
			step++;
			break;
		case 3:
			if (!validateStep3())
				break;
			step++;
			break;
		case 4:
			return save();
		}
		return null;
	}

	private Boolean validateStep1() {
		if (service.isNameExists(model))
			return FacesContextMessages.ErrorMessages("Name already exists");
		if (service.isAccountingCodeExists(model))
			return FacesContextMessages.ErrorMessages("Accounting Code already exists");
		return true;
	}

	private Boolean validateStep3() {
		if (model.getCompanyList().isEmpty()) {
			FacesContextMessages.ErrorMessages("Company list should not be empty");
			return false;
		}
		return true;
	}

	public void previousStep() {
		step--;
	}

	@Transactional
	public String save() {
		if (!canSave())
			return addParameters(listPage, "faces-redirect=true");
		if (!validate())
			return null;

		if (isAddPage)
			generateCode();

		model.setOldCategory(model.getCategory().getName());
		model.setManager(userService.findOne(model.getManagerUsername()));

		model.addHistory(new CustomerHistory(isAddPage ? "Created" : "Edited", sessionView.getUser(), isAddPage ? null : UtilsFunctions.getChanges(model, old)));
		model = service.save(model);
		return addParameters(viewPage, "faces-redirect=true", "id=" + model.getId());
	}

	private void generateCode() {
		model.setCode(service.findMaxCode() + 1);
	}

	public Boolean validate() {
		return true;
	}

	// delete
	public Boolean canDelete() {
		return sessionView.getIsInternalAdmin() && sessionView.getInternal();
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

	// companys
	public Boolean canAddCompany() {
		return sessionView.getIsInternalAdmin() && sessionView.getInternal();
	}

	public void addCompany() {
		if (canAddCompany())
			model.addCompany(new CustomerCompany());
	}

	public Boolean canSaveCompanyList() {
		return canAddCompany() && editCompany;
	}

	public void saveCompanyList() {
		if (!canSaveCompanyList())
			return;
		if (!validateCompanyList())
			return;
		model = service.saveAndRefresh(model);
		editCompany = false;
	}

	private Boolean validateCompanyList() {
		Set<Integer> keySet = new HashSet<>();
		for (CustomerCompany sc : model.getCompanyList()) {
			if (sc.getCompany() == null)
				return FacesContextMessages.ErrorMessages("Company should not be null");
			keySet.add(sc.getCompany().getId());
		}

		if (model.getCompanyList().size() > keySet.size())
			return FacesContextMessages.ErrorMessages("Duplicate Line !");

		return true;
	}

	public Boolean canDeleteCompany() {
		return sessionView.getIsInternalAdmin() && sessionView.getInternal();
	}

	public void deleteCompany(CustomerCompany company) {
		if (canDeleteCompany())
			model.removeCompany(company);
	}

	// contacts
	public Boolean canAddContact() {
		return sessionView.getIsInternalAdmin() && sessionView.getInternal();
	}

	public void addContact() {
		if (canAddContact())
			model.addContact(new CustomerContact());
	}

	public Boolean canSaveContactList() {
		return canAddContact() && editContact;
	}

	public void saveContactList() {
		if (!canSaveContactList())
			return;
		model = service.saveAndRefresh(model);
		editContact = false;
	}

	public Boolean canDeleteContact() {
		return sessionView.getIsInternalAdmin() && sessionView.getInternal();
	}

	public void deleteContact(CustomerContact contact) {
		if (canDeleteContact())
			model.removeContact(contact);
	}

	// currencys
	public Boolean canAddCurrency() {
		return sessionView.getIsInternalAdmin() && sessionView.getInternal();
	}

	public void addCurrency() {
		if (canAddCurrency())
			model.addCurrency(new CustomerCurrency());
	}

	public Boolean canSaveCurrencyList() {
		return canAddCurrency() && editCurrency;
	}

	public void saveCurrencyList() {
		if (!canSaveCurrencyList())
			return;
		if (!validateCurrencyList())
			return;
		model = service.saveAndRefresh(model);
		editCurrency = false;
	}

	private Boolean validateCurrencyList() {
		Set<String> keySet = new HashSet<>();
		for (CustomerCurrency sc : model.getCurrencyList()) {
			if (sc.getCurrency() == null)
				return FacesContextMessages.ErrorMessages("Currency should not be null");
			else if (sc.getCompany() == null)
				return FacesContextMessages.ErrorMessages("Company should not be null");
			keySet.add(sc.getCurrency().getId() + ";" + sc.getCompany().getId());
		}

		if (model.getCurrencyList().size() > keySet.size())
			return FacesContextMessages.ErrorMessages("Duplicate Line !");

		return true;
	}

	public Boolean canDeleteCurrency() {
		return sessionView.getIsInternalAdmin() && sessionView.getInternal();
	}

	public void deleteCurrency(CustomerCurrency currency) {
		if (canDeleteCurrency())
			model.removeCurrency(currency);
	}

	// incotermss
	public Boolean canAddIncoterms() {
		return sessionView.getIsInternalAdmin();
	}

	public void addIncoterms() {
		if (canAddIncoterms())
			model.addIncoterms(new CustomerIncoterms());
	}

	public Boolean canSaveIncotermsList() {
		return canAddIncoterms() && editIncoterms;
	}

	public void saveIncotermsList() {
		if (!canSaveIncotermsList())
			return;
		if (!validateIncotermsList())
			return;
		model = service.saveAndRefresh(model);
		editIncoterms = false;
	}

	private Boolean validateIncotermsList() {
		Set<String> keySet = new HashSet<>();
		for (CustomerIncoterms sc : model.getIncotermsList()) {
			if (sc.getIncoterms() == null)
				return FacesContextMessages.ErrorMessages("Incoterms should not be null");
			else if (sc.getCompany() == null)
				return FacesContextMessages.ErrorMessages("Company should not be null");
			keySet.add(sc.getIncoterms() + ";" + sc.getCompany().getId());
		}

		if (model.getIncotermsList().size() > keySet.size())
			return FacesContextMessages.ErrorMessages("Duplicate Line !");

		return true;
	}

	public Boolean canDeleteIncoterms() {
		return sessionView.getIsInternalAdmin() && sessionView.getInternal();
	}

	public void deleteIncoterms(CustomerIncoterms incoterms) {
		if (canDeleteIncoterms())
			model.removeIncoterms(incoterms);
	}

	// accountingCodes
	public Boolean canAddAccountingCode() {
		return sessionView.getIsInternalAdmin() && sessionView.getInternal();
	}

	public void addAccountingCode() {
		if (canAddAccountingCode())
			model.addAccountingCode(new CustomerAccountingCode());
	}

	public void changeAccountingCodeCompanyListener(CustomerAccountingCode customerAccountingCode) {
		if (customerAccountingCode.getCompany() != null && customerAccountingCode.getAccountingCode() == null)
			customerAccountingCode.setAccountingCode(service.findLastAccountingCodeByCompany(customerAccountingCode.getCompany().getId()) + 1);
	}

	public Boolean canSaveAccountingCodeList() {
		return canAddAccountingCode() && editAccountingCode;
	}

	public void saveAccountingCodeList() {
		if (!canSaveAccountingCodeList())
			return;
		if (!validateAccountingCodeList())
			return;
		model = service.saveAndRefresh(model);
		editAccountingCode = false;
	}

	private Boolean validateAccountingCodeList() {
		Set<String> keySet = new HashSet<>();
		Set<Integer> companyIdSet = new HashSet<>();
		for (CustomerAccountingCode sc : model.getAccountingCodeList()) {
			if (sc.getAccountingCode() == null)
				return FacesContextMessages.ErrorMessages("Accounting Code should not be null");
			else if (sc.getCompany() == null)
				return FacesContextMessages.ErrorMessages("Company should not be null");

			if (!service.isUniqueAccountingCode(sc.getCompany().getId(), sc.getAccountingCode(), sc.getId()))
				return FacesContextMessages.ErrorMessages("Accounting code already exists on this company");

			companyIdSet.add(sc.getCompany().getId());
			keySet.add(sc.getAccountingCode() + ";" + sc.getCompany().getId());
		}

		if (model.getAccountingCodeList().size() > keySet.size())
			return FacesContextMessages.ErrorMessages("Duplicate Line !");

		if (model.getAccountingCodeList().size() > companyIdSet.size())
			return FacesContextMessages.ErrorMessages("Customer should have one accounting code by company");

		return true;
	}

	public Boolean canDeleteAccountingCode() {
		return sessionView.getIsInternalAdmin() && sessionView.getInternal();
	}

	public void deleteAccountingCode(CustomerAccountingCode accountingCode) {
		if (canDeleteAccountingCode())
			model.removeAccountingCode(accountingCode);
	}

	// serviceCodes
	public Boolean canAddServiceCode() {
		return sessionView.getIsInternalAdmin() && sessionView.getInternal();
	}

	public void addServiceCode() {
		if (canAddServiceCode())
			model.addServiceCode(new CustomerServiceCode());
	}

	public Boolean canSaveServiceCodeList() {
		return canAddServiceCode() && editServiceCode;
	}

	public void saveServiceCodeList() {
		if (!canSaveServiceCodeList())
			return;
		if (!validateServiceCodeList())
			return;
		model = service.saveAndRefresh(model);
		editServiceCode = false;
	}

	private Boolean validateServiceCodeList() {
		Set<String> keySet = new HashSet<>();
		Set<Integer> companyIdSet = new HashSet<>();
		for (CustomerServiceCode sc : model.getServiceCodeList()) {
			if (sc.getServiceCode() == null)
				return FacesContextMessages.ErrorMessages("Service Code should not be null");
			else if (sc.getCompany() == null)
				return FacesContextMessages.ErrorMessages("Company should not be null");

			companyIdSet.add(sc.getCompany().getId());
			keySet.add(sc.getServiceCode() + ";" + sc.getCompany().getId());
		}

		if (model.getServiceCodeList().size() > keySet.size())
			return FacesContextMessages.ErrorMessages("Duplicate Line !");

		return true;
	}

	public Boolean canDeleteServiceCode() {
		return sessionView.getIsInternalAdmin() && sessionView.getInternal();
	}

	public void deleteServiceCode(CustomerServiceCode serviceCode) {
		if (canDeleteServiceCode())
			model.removeServiceCode(serviceCode);
	}

	// revenueTypes
	public Boolean canAddRevenueType() {
		return sessionView.getIsInternalAdmin() && sessionView.getInternal();
	}

	public void addRevenueType() {
		if (canAddRevenueType())
			model.addRevenueType(new CustomerRevenueType());
	}

	public Boolean canSaveRevenueTypeList() {
		return canAddRevenueType() && editRevenueType;
	}

	public void saveRevenueTypeList() {
		if (!canSaveRevenueTypeList())
			return;
		if (!validateRevenueTypeList())
			return;
		model = service.saveAndRefresh(model);
		editRevenueType = false;
	}

	private Boolean validateRevenueTypeList() {
		Set<String> keySet = new HashSet<>();
		for (CustomerRevenueType sc : model.getRevenueTypeList()) {
			if (sc.getRevenueCategory() == null)
				return FacesContextMessages.ErrorMessages("Revenue Category should not be null");
			if (sc.getRevenueType() == null)
				return FacesContextMessages.ErrorMessages("Revenue Type should not be null");
			else if (sc.getCompany() == null)
				return FacesContextMessages.ErrorMessages("Company should not be null");
			keySet.add(sc.getRevenueCategory() + ";" + sc.getRevenueType().ordinal() + ";" + sc.getCompany().getId());
		}

		if (model.getRevenueTypeList().size() > keySet.size())
			return FacesContextMessages.ErrorMessages("Duplicate Line !");

		return true;
	}

	public Boolean canDeleteRevenueType() {
		return sessionView.getIsInternalAdmin() && sessionView.getInternal();
	}

	public void deleteRevenueType(CustomerRevenueType revenueType) {
		if (canDeleteRevenueType())
			model.removeRevenueType(revenueType);
	}

	// invoiceTermTemplates
	public Boolean canSaveInvoiceTermTemplateList() {
		return sessionView.getIsInternalAdmin() && sessionView.getInternal() && editInvoiceTermTemplate;
	}

	public void saveInvoiceTermTemplateList() {
		if (!canSaveInvoiceTermTemplateList())
			return;
		model = service.saveAndRefresh(model);
		editInvoiceTermTemplate = false;
	}

	public Boolean canDeleteInvoiceTermTemplate() {
		return true;
	}

	public void deleteInvoiceTermTemplate(InvoiceTermTemplate invoiceTermTemplate) {
		if (canDeleteInvoiceTermTemplate())
			model.removeInvoiceTermTemplate(invoiceTermTemplate);
	}

	// invoiceTermTemplate
	private InvoiceTermTemplate invoiceTermTemplate = new InvoiceTermTemplate(false);

	public Boolean canAddInvoiceTermTemplate() {
		return sessionView.getIsInternalAdmin();
	}

//	public void refreshOldInvoiceTerm() {
//		oldInvoiceTerm = invoiceTermService.findOne(oldInvoiceTerm.getId());
//	}

//	public Boolean canAddOldInvoiceTerm() {
//		return true;
//	}
//
//	public void addOldInvoiceTerm() {
//		if (canAddOldInvoiceTerm())
//			model.addOldInvoiceTerm(new InvoiceTerm(model.getOldInvoiceTermList().stream().mapToInt(i -> i.getReferenceId()).max().orElse(0) + 1, 1));
//	}
//
//	public void changePercentage(InvoiceTerm row, int index) {
//		if (row.getPercentage() < row.getTmpPercentage()) {
//			InvoiceTerm newRow = new InvoiceTerm(row.getReferenceId(), row.getTmpPercentage() - row.getPercentage(), row.getTmpPercentage() - row.getPercentage(), row.getCompany());
//			model.addOldInvoiceTerm(++index, newRow);
//
//			int numero = 1;
//			for (InvoiceTerm it : model.getOldInvoiceTermList())
//				if (row.getReferenceId().equals(it.getReferenceId()))
//					it.setNumero(numero++);
//
//			row.setTmpPercentage(row.getPercentage());
//			model.generateOldInvoiceTermReference(row.getReferenceId());
//		}
//	}
//
//	public void changeOldInvoiceTermCompany(InvoiceTerm row) {
//		model.getOldInvoiceTermList().stream().filter(i -> i.getReferenceId().equals(row.getReferenceId())).forEach(i -> i.setCompany(row.getCompany()));
//	}
//
//	public void cancelChangePercentage(InvoiceTerm row) {
//		row.setPercentage(row.getTmpPercentage());
//	}
//
//	public void changePaymentTermPercentage(PaymentTerm row, int index) {
//		if (row.getPercentage() < row.getTmpPercentage()) {
//			PaymentTerm newRow = new PaymentTerm(row.getReferenceId(), row.getTmpPercentage() - row.getPercentage(), row.getTmpPercentage() - row.getPercentage());
//			oldInvoiceTerm.addDetail(++index, newRow);
//			int numero = 1;
//			for (PaymentTerm itd : oldInvoiceTerm.getDetailList())
//				if (row.getReferenceId().equals(itd.getReferenceId()))
//					itd.setNumero(numero++);
//			row.setTmpPercentage(row.getPercentage());
//		}
//	}
//
//	public void cancelChangePaymentTermPercentage(PaymentTerm row) {
//		row.setPercentage(row.getTmpPercentage());
//	}
//
//	public void addPaymentTerm() {
//		PaymentTerm paymentTerm = new PaymentTerm(oldInvoiceTerm.getDetailList().stream().mapToInt(i -> i.getReferenceId()).max().orElse(0) + 1, 1);
//		oldInvoiceTerm.addDetail(paymentTerm);
//	}
//
//	public Boolean canSaveOldInvoiceTermList() {
//		return canAddOldInvoiceTerm() && editOldInvoiceTerm;
//	}
//
//	public void saveOldInvoiceTermList() {
//		if (!canSaveOldInvoiceTermList())
//			return;
//		if (!validateOldInvoiceTermList())
//			return;
//
//		Collections.sort(model.getOldInvoiceTermList(), new Comparator<InvoiceTerm>() {
//			@Override
//			public int compare(InvoiceTerm o1, InvoiceTerm o2) {
//				return o1.getReference().compareTo(o2.getReference());
//			}
//		});
//
//		model = service.saveAndRefresh(model);
//		editOldInvoiceTerm = false;
//	}
//
//	private Boolean validateOldInvoiceTermList() {
////		Set<String> keySet = new HashSet<>();
//		for (InvoiceTerm sit : model.getOldInvoiceTermList()) {
//			if (sit.getCompany() == null)
//				return FacesContextMessages.ErrorMessages("Company should not be null");
//			else if (sit.getNumero() == null)
//				return FacesContextMessages.ErrorMessages("Numero should not be null");
//			else if (sit.getPhase().isEmpty())
//				return FacesContextMessages.ErrorMessages("Phase should not be null");
//			else if (sit.getDuration() == null)
//				return FacesContextMessages.ErrorMessages("Duration should not be null");
//
//			for (PaymentTerm sitd : sit.getDetailList()) {
//				if (sitd.getNumero() == null)
//					return FacesContextMessages.ErrorMessages("Numero should not be null");
//				else if (sitd.getDuration() == null)
//					return FacesContextMessages.ErrorMessages("Duration should not be null");
//				else if (sitd.getStartPoint() == null)
//					return FacesContextMessages.ErrorMessages("Start Point should not be null");
//			}
//
//			Map<Integer, Double> map = sit.getDetailList().stream()
//					.collect(Collectors.groupingBy(PaymentTerm::getReferenceId, Collectors.summingDouble(PaymentTerm::getPercentage)));
//			if (map.values().stream().filter(i -> i != 100).count() > 0)
//				return FacesContextMessages.ErrorMessages("payment term list : sum percentage by reference id should be equal to 100");
//
//		}
//
//		Map<Integer, Double> map = model.getOldInvoiceTermList().stream()
//				.collect(Collectors.groupingBy(InvoiceTerm::getReferenceId, Collectors.summingDouble(InvoiceTerm::getPercentage)));
//		if (map.values().stream().filter(i -> i != 100).count() > 0)
//			return FacesContextMessages.ErrorMessages("invoice term list : sum percentage by reference id should be equal to 100");
//
//		return true;
//	}
//
//	public void oldInvoiceTermReferenceChangeListener(InvoiceTerm invoiceTerm) {
//		invoiceTerm.setNumero((int) model.getOldInvoiceTermList().stream().filter(i -> invoiceTerm.getReference().equals(i.getReference())).count());
//		invoiceTerm.setPercentage(100 - model.getOldInvoiceTermList().stream().filter(i -> invoiceTerm.getReference().equals(i.getReference())).mapToInt(i -> i.getPercentage()).sum());
//	}
//
//	public void paymentTermReferenceChangeListener(PaymentTerm paymentTerm) {
//		paymentTerm
//				.setNumero((int) paymentTerm.getInvoiceTerm().getDetailList().stream().filter(i -> paymentTerm.getReference().equals(i.getReference())).count());
//		paymentTerm.setPercentage(100 - paymentTerm.getInvoiceTerm().getDetailList().stream()
//				.filter(i -> paymentTerm.getReference().equals(i.getReference())).mapToInt(i -> i.getPercentage()).sum());
//	}
//
//	public Boolean canDeleteOldInvoiceTerm() {
//		return true;
//	}
//
//	public void deleteOldInvoiceTerm(InvoiceTerm oldInvoiceTerm) {
//		if (canDeleteOldInvoiceTerm())
//			model.removeOldInvoiceTerm(oldInvoiceTerm);
//	}

	// files
	private CustomerFile file;
	private String fileType;

	public Boolean canAddFile() {
		return sessionView.getIsInternalAdmin() && sessionView.getInternal();
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
		CustomerFile modelFile = new CustomerFile(file, fileType, event.getFile().getFileName(), sessionView.getUser());
		model.addFile(modelFile);
		synchronized (CustomerView.class) {
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

	// photo
	public Boolean canUploadPhoto() {
		return sessionView.getIsInternalAdmin() && sessionView.getInternal();
	}

	public void handlePhotoUpload(FileUploadEvent event) throws IOException {
		File file = fileUploadView.handlePhotoUpload(event, getClassName2(), 400 * 1024);
		model.setPhoto("files/" + getClassName2() + "/" + file.getName());
		synchronized (CustomerView.class) {
			model = service.saveAndRefresh(model);
		}
	}

	// comments
	private CustomerComment comment = new CustomerComment();

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

	// generic
	public List<Customer> find() {
		return service.find(); // no need to cache cuz it s done on service
	}

	public List<Customer> findByCompany(Integer companyId) {
		return service.findByCompany(companyId); // no need to cache cuz it s done on service
	}

	public List<Customer> findLight() {
		return service.findLight();
	}

	// getters & setters
	public Customer getModel() {
		return model;
	}

	public void setModel(Customer model) {
		this.model = model;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public CustomerFile getFile() {
		return file;
	}

	public void setFile(CustomerFile file) {
		this.file = file;
	}

	public CustomerComment getComment() {
		return comment;
	}

	public void setComment(CustomerComment comment) {
		this.comment = comment;
	}

	public Boolean getEditContact() {
		return editContact;
	}

	public void setEditContact(Boolean editContact) {
		this.editContact = editContact;
		evictCache();
	}

	public Boolean getEditCurrency() {
		return editCurrency;
	}

	public void setEditCurrency(Boolean editCurrency) {
		this.editCurrency = editCurrency;
		evictCache();
	}

	public Boolean getEditRevenueType() {
		return editRevenueType;
	}

	public void setEditRevenueType(Boolean editRevenueType) {
		this.editRevenueType = editRevenueType;
		evictCache();
	}

	public Boolean getEditOldInvoiceTerm() {
		return editOldInvoiceTerm;
	}

	public void setEditOldInvoiceTerm(Boolean editOldInvoiceTerm) {
		this.editOldInvoiceTerm = editOldInvoiceTerm;
		evictCache();
	}

	public CustomerCategory getCategory() {
		return category;
	}

	public void setCategory(CustomerCategory category) {
		this.category = category;
	}

	public InvoiceTerm getOldInvoiceTerm() {
		return oldInvoiceTerm;
	}

	public void setOldInvoiceTerm(InvoiceTerm oldInvoiceTerm) {
		this.oldInvoiceTerm = oldInvoiceTerm;
	}

	public InvoiceTermTemplate getInvoiceTermTemplate() {
		return invoiceTermTemplate;
	}

	public void setInvoiceTermTemplate(InvoiceTermTemplate invoiceTermTemplate) {
		this.invoiceTermTemplate = invoiceTermTemplate;
	}

	public Boolean getEditAccountingCode() {
		return editAccountingCode;
	}

	public void setEditAccountingCode(Boolean editAccountingCode) {
		this.editAccountingCode = editAccountingCode;
	}

	public Boolean getEditServiceCode() {
		return editServiceCode;
	}

	public void setEditServiceCode(Boolean editServiceCode) {
		this.editServiceCode = editServiceCode;
	}

	public Boolean getEditInvoiceTermTemplate() {
		return editInvoiceTermTemplate;
	}

	public void setEditInvoiceTermTemplate(Boolean editInvoiceTermTemplate) {
		this.editInvoiceTermTemplate = editInvoiceTermTemplate;
	}

	public Boolean getEditIncoterms() {
		return editIncoterms;
	}

	public void setEditIncoterms(Boolean editIncoterms) {
		this.editIncoterms = editIncoterms;
	}

	public Boolean getEditCompany() {
		return editCompany;
	}

	public void setEditCompany(Boolean editCompany) {
		this.editCompany = editCompany;
	}

}