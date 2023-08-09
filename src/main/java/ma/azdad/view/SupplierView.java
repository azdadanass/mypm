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

import ma.azdad.model.InvoiceTerm;
import ma.azdad.model.InvoiceTermTemplate;
import ma.azdad.model.Supplier;
import ma.azdad.model.SupplierAccountingCode;
import ma.azdad.model.SupplierCategory;
import ma.azdad.model.SupplierComment;
import ma.azdad.model.SupplierCompany;
import ma.azdad.model.SupplierContact;
import ma.azdad.model.SupplierCostType;
import ma.azdad.model.SupplierCurrency;
import ma.azdad.model.SupplierFile;
import ma.azdad.model.SupplierHistory;
import ma.azdad.model.SupplierIncoterms;
import ma.azdad.model.SupplierServiceCode;
import ma.azdad.model.SupplierStatus;
import ma.azdad.repos.SupplierRepos;
import ma.azdad.service.CompanyService;
import ma.azdad.service.SupplierService;
import ma.azdad.service.UtilsFunctions;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Scope("view")
public class SupplierView extends GenericView<Integer, Supplier, SupplierRepos, SupplierService> {

	@Autowired
	private SessionView sessionView;

	@Autowired
	FileUploadView fileUploadView;

	@Autowired
	CompanyService companyService;

	private int step = 1;
	private Boolean editContact = false;
	private Boolean editCompany = false;
	private Boolean editCurrency = false;
	private Boolean editIncoterms = false;
	private Boolean editAccountingCode = false;
	private Boolean editServiceCode = false;
	private Boolean editCostType = false;
	private Boolean editInvoiceTermTemplate = false;
	private Boolean editOldInvoiceTerm = false;
	private SupplierCategory category;
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
		} else if ("/supplierList2.xhtml".equals(currentPath))
			switch (pageIndex) {
			case 1:
				list2 = list1 = service.findByStatus(SupplierStatus.EDITED);
				break;
			case 2:
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
			category = new SupplierCategory("All");
			refreshList();
		}

		filterBean(searchBean);
	}

	// toggle status
	public Boolean canToggle() {
		return sessionView.getIsInternalAdmin() && sessionView.getInternal();
	}

	public void toggle() {
		if (!canToggle())
			return;
		model.setActive(!model.getActive());
		model = service.saveAndRefresh(model);
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
			if (!validateStep4())
				break;
			step++;
			break;
		case 5:
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
		if (service.isEmailExists(model))
			return FacesContextMessages.ErrorMessages("Email already exists");
		return true;
	}

	private Boolean validateStep4() {
		if (model.getCompanyList().isEmpty())
			return FacesContextMessages.ErrorMessages("Company list should not be empty");
		if (model.getCompanyList().stream().mapToInt(i -> i.getCompany().getId()).distinct().count() < model.getCompanyList().size())
			return FacesContextMessages.ErrorMessages("Duplicate Line !");

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

		if (isAddPage) {
			model.setUser0(sessionView.getUser());
			model.setDate0(new Date());
		}

		if (!model.getTmpPassword().isEmpty())
			model.setPassword(UtilsFunctions.stringToMD5(model.getTmpPassword()));

		model.setOldCategory(model.getCategory().getName());

		model.addHistory(new SupplierHistory(isAddPage ? "Created" : "Edited", sessionView.getUser(), isAddPage ? null : UtilsFunctions.getChanges(model, old)));
		model = service.save(model);
		return addParameters(viewPage, "faces-redirect=true", "id=" + model.getId());
	}

	public Boolean validate() {
		return true;
	}

	// approve
	public Boolean canApprove() {
		return canApprove(model);
	}

	public Boolean canApprove(Supplier model) {
		return SupplierStatus.EDITED.equals(model.getStatus()) && sessionView.isCfo();
	}

	public void approve(Supplier model) {
		if (!canApprove(model))
			return;
		model.setStatus(SupplierStatus.APPROVED);
		model.setDate1(new Date());
		model.setUser1(sessionView.getUser());
		model.addHistory(new SupplierHistory(model.getStatus().getValue(), sessionView.getUser(), null));
		service.save(model);
	}

	public void approve() {
		if (isViewPage) {
			approve(model);
			model = service.findOne(id);
		} else if ("/supplierList2.xhtml".equals(currentPath) && pageIndex == 1) {
			for (Supplier supplier : list4)
				approve(service.findOne(supplier.getId()));
			refreshList();
		}
	}

	// reject
	public Boolean canReject() {
		return canReject(model);
	}

	public Boolean canReject(Supplier model) {
		return SupplierStatus.EDITED.equals(model.getStatus()) && sessionView.isCfo();
	}

	public void reject(Supplier model) {
		if (!canReject(model))
			return;
		model.setStatus(SupplierStatus.APPROVED);
		model.setDate2(new Date());
		model.setUser2(sessionView.getUser());
		model.addHistory(new SupplierHistory(model.getStatus().getValue(), sessionView.getUser(), null));
		service.save(model);
	}

	public void reject() {
		if (isViewPage) {
			reject(model);
			model = service.findOne(id);
		} else if ("/supplierList2.xhtml".equals(currentPath) && pageIndex == 1) {
			for (Supplier supplier : list4)
				reject(service.findOne(supplier.getId()));
			refreshList();
		}
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

	// contacts
	public Boolean canAddContact() {
		return sessionView.getIsInternalAdmin() && sessionView.getInternal();
	}

	public void addContact() {
		if (canAddContact())
			model.addContact(new SupplierContact());
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

	public void deleteContact(SupplierContact contact) {
		if (canDeleteContact())
			model.removeContact(contact);
	}

	// companys
	public Boolean canAddCompany() {
		return sessionView.getIsInternalAdmin() && sessionView.getInternal();
	}

	public void addCompany() {
		if (canAddCompany())
			model.addCompany(new SupplierCompany());
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
		for (SupplierCompany sc : model.getCompanyList()) {
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

	public void deleteCompany(SupplierCompany company) {
		if (canDeleteCompany())
			model.removeCompany(company);
	}

	// currencys
	public Boolean canAddCurrency() {
		return sessionView.getIsInternalAdmin() && sessionView.getInternal();
	}

	public void addCurrency() {
		if (canAddCurrency())
			model.addCurrency(new SupplierCurrency());
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
		for (SupplierCurrency sc : model.getCurrencyList()) {
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

	public void deleteCurrency(SupplierCurrency currency) {
		if (canDeleteCurrency())
			model.removeCurrency(currency);
	}

	// incotermss
	public Boolean canAddIncoterms() {
		return sessionView.getIsInternalAdmin();
	}

	public void addIncoterms() {
		if (canAddIncoterms())
			model.addIncoterms(new SupplierIncoterms());
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
		for (SupplierIncoterms sc : model.getIncotermsList()) {
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

	public void deleteIncoterms(SupplierIncoterms incoterms) {
		if (canDeleteIncoterms())
			model.removeIncoterms(incoterms);
	}

	// accountingCodes
	public Boolean canAddAccountingCode() {
		return sessionView.getIsInternalAdmin() && sessionView.getInternal();
	}

	public void addAccountingCode() {
		if (canAddAccountingCode())
			model.addAccountingCode(new SupplierAccountingCode());
	}

//	public void changeAccountingCodeCompanyListener(SupplierAccountingCode supplierAccountingCode) {
//		if (supplierAccountingCode.getCompany() != null && supplierAccountingCode.getAccountingCode() == null)
//			supplierAccountingCode.setAccountingCode(service.findLastAccountingCodeByCompany(supplierAccountingCode.getCompany().getId()) + 1);
//	}

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
		for (SupplierAccountingCode sc : model.getAccountingCodeList()) {
			if (sc.getAccountingCode() == null)
				return FacesContextMessages.ErrorMessages("Accounting Code should not be null");
			else if (sc.getCompany() == null)
				return FacesContextMessages.ErrorMessages("Company should not be null");

			if (!service.isUniqueAccountingCode(sc.getCompany().getId(), sc.getAccountingCode(), sc.getId()))
				return FacesContextMessages.ErrorMessages("Accounting code already exists on this company");

			if (String.valueOf(sc.getAccountingCode()).length() != 8)
				return FacesContextMessages.ErrorMessages("Accounting code lentgh shoud be equal to 8");

			companyIdSet.add(sc.getCompany().getId());
			keySet.add(sc.getAccountingCode() + ";" + sc.getCompany().getId());
		}

		if (model.getAccountingCodeList().size() > keySet.size())
			return FacesContextMessages.ErrorMessages("Duplicate Line !");

		if (model.getAccountingCodeList().size() > companyIdSet.size())
			return FacesContextMessages.ErrorMessages("Supplier should have one accounting code by company");

		return true;
	}

	public Boolean canDeleteAccountingCode() {
		return sessionView.getIsInternalAdmin() && sessionView.getInternal();
	}

	public void deleteAccountingCode(SupplierAccountingCode accountingCode) {
		if (canDeleteAccountingCode())
			model.removeAccountingCode(accountingCode);
	}

	// serviceCodes
	public Boolean canAddServiceCode() {
		return sessionView.getIsInternalAdmin() && sessionView.getInternal();
	}

	public void addServiceCode() {
		if (canAddServiceCode())
			model.addServiceCode(new SupplierServiceCode());
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
		for (SupplierServiceCode sc : model.getServiceCodeList()) {
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

	public void deleteServiceCode(SupplierServiceCode serviceCode) {
		if (canDeleteServiceCode())
			model.removeServiceCode(serviceCode);
	}

	// costTypes
	public Boolean canAddCostType() {
		return sessionView.getIsInternalAdmin() && sessionView.getInternal();
	}

	public void addCostType() {
		if (canAddCostType())
			model.addCostType(new SupplierCostType());
	}

	public Boolean canSaveCostTypeList() {
		return canAddCostType() && editCostType;
	}

	public void saveCostTypeList() {
		if (!canSaveCostTypeList())
			return;
		if (!validateCostTypeList())
			return;
		model = service.saveAndRefresh(model);
		editCostType = false;
	}

	private Boolean validateCostTypeList() {
		Set<String> keySet = new HashSet<>();
		for (SupplierCostType sc : model.getCostTypeList()) {
			if (sc.getCostCategory() == null)
				return FacesContextMessages.ErrorMessages("Cost Category should not be null");
			if (sc.getCostType() == null)
				return FacesContextMessages.ErrorMessages("Cost Type should not be null");
			else if (sc.getCompany() == null)
				return FacesContextMessages.ErrorMessages("Company should not be null");
			keySet.add(sc.getCostCategory().ordinal() + ";" + sc.getCostType().ordinal() + ";" + sc.getCompany().getId());
		}

		if (model.getCostTypeList().size() > keySet.size())
			return FacesContextMessages.ErrorMessages("Duplicate Line !");

		return true;
	}

	public Boolean canDeleteCostType() {
		return sessionView.getIsInternalAdmin() && sessionView.getInternal();
	}

	public void deleteCostType(SupplierCostType costType) {
		if (canDeleteCostType())
			model.removeCostType(costType);
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
	private InvoiceTermTemplate invoiceTermTemplate = new InvoiceTermTemplate(true);

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
	private SupplierFile file;
	private String fileType;

	public Boolean canAddFile() {
		return sessionView.getIsInternalAdmin();
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
		SupplierFile modelFile = new SupplierFile(file, fileType, event.getFile().getFileName(), sessionView.getUser());
		model.addFile(modelFile);
		synchronized (SupplierView.class) {
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
		synchronized (SupplierView.class) {
			model = service.saveAndRefresh(model);
		}
	}

	// comments
	private SupplierComment comment = new SupplierComment();

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

	// inplace
	public Boolean canEditInplace() {
		return sessionView.getIsInternalAdmin() && sessionView.getInternal();
	}

	public void updateIbuyAccess() {
		service.updateIbuyAccess(model.getId(), model.getIbuyAccess());
	}

	public void updateInvoiceManagement() {
		service.updateInvoiceManagement(model.getId(), model.getInvoiceManagement());
	}

	public void updateReceiptNotifications() {
		service.updateReceiptNotifications(model.getId(), model.getReceiptNotifications());
	}

	public void updateCanViewComments() {
		service.updateCanViewComments(model.getId(), model.getCanViewComments());
	}

	// generic
	public List<Supplier> find() {
		return service.find(); // no need to cache cuz it s done on service
	}

	public List<Supplier> findActiveByCompany(Integer companyId) {
		return service.findByCompany(companyId, true, SupplierStatus.APPROVED);
	}

	public List<Supplier> findLight() {
		return service.findLight();
	}

	// getters & setters
	public Supplier getModel() {
		return model;
	}

	public void setModel(Supplier model) {
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

	public SupplierFile getFile() {
		return file;
	}

	public void setFile(SupplierFile file) {
		this.file = file;
	}

	public SupplierComment getComment() {
		return comment;
	}

	public void setComment(SupplierComment comment) {
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

	public Boolean getEditCostType() {
		return editCostType;
	}

	public void setEditCostType(Boolean editCostType) {
		this.editCostType = editCostType;
		evictCache();
	}

	public Boolean getEditOldInvoiceTerm() {
		return editOldInvoiceTerm;
	}

	public void setEditOldInvoiceTerm(Boolean editOldInvoiceTerm) {
		this.editOldInvoiceTerm = editOldInvoiceTerm;
		evictCache();
	}

	public SupplierCategory getCategory() {
		return category;
	}

	public void setCategory(SupplierCategory category) {
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