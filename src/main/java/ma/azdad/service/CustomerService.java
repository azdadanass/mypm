package ma.azdad.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.Customer;
import ma.azdad.repos.CompanyRepos;
import ma.azdad.repos.CustomerRepos;

@Component
public class CustomerService extends GenericService<Integer, Customer, CustomerRepos> {

	@Autowired
	CompanyRepos companyRepos;

	@Override
	@Cacheable("customerService.findAll")
	public List<Customer> findAll() {
		return repos.findAll();
	}

	@Cacheable("customerService.find")
	public List<Customer> find() {
		return repos.find();
	}

	@Cacheable("customerService.findByCategory")
	public List<Customer> findByCategory(Integer customerCategoryId) {
		return repos.findByCategory(customerCategoryId);
	}

	@Cacheable("customerService.findByCompany")
	public List<Customer> findByCompany(Integer companyId) {
		return repos.findByCompany(companyId);
	}

	@Cacheable("customerService.findWithouActiveBankAccount")
	public List<Customer> findWithouActiveBankAccount() {
		return repos.findWithouActiveBankAccount();
	}

	@Cacheable("customerService.countWithouActiveBankAccount")
	public Long countWithouActiveBankAccount() {
		return repos.countWithouActiveBankAccount();
	}

	@Override
	@Cacheable("customerService.findOne")
	public Customer findOne(Integer id) {
		Customer customer = super.findOne(id);
		initialize(customer.getCompanyList());
		initialize(customer.getContactList());
		initialize(customer.getManager());
		customer.getCurrencyList().forEach(i -> initialize(i.getCompany()));
		customer.getIncotermsList().forEach(i -> initialize(i.getCompany()));
		customer.getAccountingCodeList().forEach(i -> initialize(i.getCompany()));
		customer.getServiceCodeList().forEach(i -> initialize(i.getCompany()));
		customer.getRevenueTypeList().forEach(i -> initialize(i.getCompany()));
		customer.getInvoiceTermTemplateList().forEach(i -> initialize(i.getCompany()));
		initialize(customer.getFileList());
		initialize(customer.getHistoryList());
		initialize(customer.getCommentList());
		customer.getInvoiceTermTemplateList().forEach(i -> i.getDetailList().forEach(j -> initialize(j.getDetailList())));
		return customer;
	}

	public Boolean isNameExists(Customer customer) {
		return repos.countByName(customer.getName(), customer.getId()) > 0;
	}

	public Boolean isEmailExists(Customer customer) {
		return repos.countByEmail(customer.getEmail(), customer.getId()) > 0;
	}

	public Boolean isAccountingCodeExists(Customer customer) {
		return repos.countByAccountingCode(customer.getAccountingCode(), customer.getId()) > 0;
	}

//	public void mergeScript() {
//		// create config models
//		List<Customer> list = findAll();
//		for (Customer customer : list) {
//			// customerCompany
//			repos.findCompanyIdList(customer.getId()).forEach(i -> customer.addCompany(companyRepos.findById(i).get()));
//			// customerCurrency
//			repos.findCustomerCurrencyList(customer.getId()).forEach(i -> customer.addCurrency(i));
//			// customerRevenueType
//			repos.findCustomerRevenueTypeList(customer.getId()).forEach(i -> customer.addRevenueType(i));
//			// customerIncoterm
//			if (repos.countMadPo(customer.getId()) != null && repos.countMadPo(customer.getId()) > 0)
//				customer.addIncoterms(new CustomerIncoterms("DAP", companyRepos.findById(1).get()));
//			save(customer);
//		}
//	}

	public Boolean isUniqueAccountingCode(Integer companyId, Integer accountingCode, Integer id) {
		Long count = id != null ? repos.countByCompanyAndAccountingCode(companyId, accountingCode, id) : repos.countByCompanyAndAccountingCode(companyId, accountingCode);
		return count == 0l;
	}

	// inplace
	public void updateInvoiceAccess(Integer id, Boolean invoiceAccess) {
		repos.updateInvoiceAccess(id, invoiceAccess);
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

	@Cacheable("customerService.findCompanyIdList")
	public List<Integer> findCompanyIdList(Integer customerId) {
		return repos.findCompanyIdList(customerId);
	}

	public Integer findMaxCode() {
		return repos.findMaxCode();
	}

	@Cacheable("customerService.findLight")
	public List<Customer> findLight() {
		return repos.findLight();
	}

	public List<Integer> findIdList(String managerUsername) {
		return repos.findIdList(managerUsername);
	}
}
