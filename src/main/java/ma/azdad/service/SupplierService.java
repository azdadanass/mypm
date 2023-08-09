package ma.azdad.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.Supplier;
import ma.azdad.model.SupplierStatus;
import ma.azdad.repos.CompanyRepos;
import ma.azdad.repos.SupplierRepos;

@Component
public class SupplierService extends GenericService<Integer, Supplier, SupplierRepos> {

	@Autowired
	CompanyRepos companyRepos;

	@Cacheable("supplierService.findLight")
	public List<Supplier> findLight() {
		return repos.findLight();
	}

	@Override
	@Cacheable("supplierService.findAll")
	public List<Supplier> findAll() {
		return repos.findAll();
	}

	@Cacheable("supplierService.find")
	public List<Supplier> find() {
		return repos.find();
	}

	@Cacheable("supplierService.findByCategory")
	public List<Supplier> findByCategory(Integer supplierCategoryId) {
		return repos.findByCategory(supplierCategoryId);
	}

	@Cacheable("supplierService.findByCompany")
	public List<Supplier> findByCompany(Integer companyId, Boolean active, SupplierStatus status) {
		return repos.findByCompany(companyId, active, status);
	}

	@Cacheable("supplierService.findByStatusl")
	public List<Supplier> findByStatus(SupplierStatus status) {
		return repos.findByStatus(status);
	}

	@Cacheable("supplierService.countByStatus")
	public Long countByStatus(SupplierStatus status) {
		return repos.countByStatus(status);
	}

	@Cacheable("supplierService.findWithouActiveBankAccount")
	public List<Supplier> findWithouActiveBankAccount() {
		return repos.findWithouActiveBankAccount();
	}

	@Cacheable("supplierService.countWithouActiveBankAccount")
	public Long countWithouActiveBankAccount() {
		return repos.countWithouActiveBankAccount();
	}

	@Override
	@Cacheable("supplierService.findOne")
	public Supplier findOne(Integer id) {
		Supplier supplier = super.findOne(id);
		initialize(supplier.getCompanyList());
		initialize(supplier.getContactList());
		supplier.getCurrencyList().forEach(i -> initialize(i.getCompany()));
		supplier.getIncotermsList().forEach(i -> initialize(i.getCompany()));
		supplier.getAccountingCodeList().forEach(i -> initialize(i.getCompany()));
		supplier.getServiceCodeList().forEach(i -> initialize(i.getCompany()));
		supplier.getCostTypeList().forEach(i -> initialize(i.getCompany()));
		supplier.getInvoiceTermTemplateList().forEach(i -> initialize(i.getCompany()));
		initialize(supplier.getFileList());
		initialize(supplier.getHistoryList());
		initialize(supplier.getCommentList());
		supplier.getInvoiceTermTemplateList().forEach(i -> i.getDetailList().forEach(j -> initialize(j.getDetailList())));
		return supplier;
	}

	public Boolean isNameExists(Supplier supplier) {
		return repos.countByName(supplier.getName(), supplier.getId()) > 0;
	}

	public Boolean isEmailExists(Supplier supplier) {
		return repos.countByEmail(supplier.getEmail(), supplier.getId()) > 0;
	}

	public Boolean isAccountingCodeExists(Supplier supplier) {
		return repos.countByAccountingCode(supplier.getAccountingCode(), supplier.getId()) > 0;
	}

	public Integer getLastPaymentIndex(Integer id) {
		return repos.getLastPaymentIndex(id);
	}

	public void updateLastPaymentIndex(Integer id, Integer lastPaymentIndex) {
		repos.updateLastPaymentIndex(id, lastPaymentIndex);
	}

	public Boolean isUniqueAccountingCode(Integer companyId, Integer accountingCode, Integer id) {
		Long count = id != null ? repos.countByCompanyAndAccountingCode(companyId, accountingCode, id) : repos.countByCompanyAndAccountingCode(companyId, accountingCode);
		return count == 0l;
	}

	// inplace
	public void updateIbuyAccess(Integer id, Boolean ibuyAccess) {
		repos.updateIbuyAccess(id, ibuyAccess);
		evictCache();
	}

	public void updateInvoiceManagement(Integer id, Boolean invoiceManagement) {
		repos.updateInvoiceManagement(id, invoiceManagement);
		evictCache();
	}

	public void updateReceiptNotifications(Integer id, Boolean receiptNotifications) {
		repos.updateReceiptNotifications(id, receiptNotifications);
		evictCache();
	}

	public void updateCanViewComments(Integer id, Boolean canViewComments) {
		repos.updateCanViewComments(id, canViewComments);
		evictCache();
	}

	public Integer findLastAccountingCodeByCompany(Integer companyId) {
		return repos.findLastAccountingCodeByCompany(companyId);
	}
}
