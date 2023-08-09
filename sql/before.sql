alter table bankaccount_file add column parent_id integer not null;
update bankaccount_file set parent_id = bankaccount_idbankaccount ;
alter table bankaccount_file modify column bankaccount_idbankaccount int(11) NULL;

alter table bank_account_comment add column parent_id integer not null;
update bank_account_comment set parent_id = bank_account_idbankaccount ;
alter table bank_account_comment modify column bank_account_idbankaccount int(11) NULL;

alter table bank_account_history add column parent_id integer not null;
update bank_account_history set parent_id = bank_account_idbankaccount ;
alter table bank_account_history modify column bank_account_idbankaccount int(11) NULL;

alter table bu_file add column parent_id integer not null;
update bu_file set parent_id = bu_idbu ;
alter table bu_file modify column bu_idbu int(11) NULL;

alter table bu_file add column user_username varchar(255);

alter table company_comment add column parent_id integer not null;
update company_comment set parent_id = company_idcompany ;
alter table company_comment modify column company_idcompany int(11) NULL;

alter table company_file add column parent_id integer not null;
update company_file set parent_id = company_idcompany ;
alter table company_file modify column company_idcompany int(11) NULL;

alter table company_file add column user_username varchar(255);

alter table company_history add column parent_id integer not null;
update company_history set parent_id = company_idcompany ;
alter table company_history modify column company_idcompany int(11) NULL;

alter table costcenter_file add column parent_id integer not null;
update costcenter_file set parent_id = costcenter_idcostcenter ;
alter table costcenter_file modify column costcenter_idcostcenter int(11) NULL;

alter table costcenter_file add column user_username varchar(255);

alter table lob_file add column parent_id integer not null;
update lob_file set parent_id = lob_idlob ;
alter table lob_file modify column lob_idlob int(11) NULL;

alter table lob_file add column user_username varchar(255);

alter table project_comment add column parent_id integer not null;
update project_comment set parent_id = project_idproject ;
alter table project_comment modify column project_idproject int(11) NULL;

alter table project_file add column parent_id integer not null;
update project_file set parent_id = project_idproject ;
alter table project_file modify column project_idproject int(11) NULL;

alter table project_file add column user_username varchar(255);

alter table project_history add column parent_id integer not null;
update project_history set parent_id = project_idproject ;
alter table project_history modify column project_idproject int(11) NULL;

alter table restriction_comment add column parent_id integer not null;
update restriction_comment set parent_id = restriction_id ;
alter table restriction_comment modify column restriction_id int(11) NULL;

alter table restriction_file add column parent_id integer not null;
update restriction_file set parent_id = restriction_id ;
alter table restriction_file modify column restriction_id int(11) NULL;

alter table restriction_file add column user_username varchar(255);

alter table restriction_history add column parent_id integer not null;
update restriction_history set parent_id = restriction_id ;
alter table restriction_history modify column restriction_id int(11) NULL;

alter table user_file add column parent_id varchar(255) not null;
update user_file set parent_id = user_username ;
update user_file set user_username = null;

alter table user_history add column parent_id varchar(255) not null;
update user_history set parent_id = parent_username ;
alter table user_history modify column parent_username varchar(255) NULL;
update user_history set parent_username = null;

#compta

 alter table exercise_comment add column parent_id integer not null;
 update exercise_comment set parent_id = exercise_id ;
alter table exercise_comment modify column exercise_id int(11) NULL;

 alter table exercise_file add column parent_id integer not null;
 update exercise_file set parent_id = exercise_id ;
alter table exercise_file modify column exercise_id int(11) NULL;

 alter table exercise_file add column user_username varchar(255);
 
 alter table exercise_history add column parent_id integer not null;
 update exercise_history set parent_id = exercise_id ;
alter table exercise_history modify column exercise_id int(11) NULL;

#ibuy
alter table acceptance_comment add column parent_id integer not null;
update acceptance_comment set parent_id = acceptance_idacceptance ;
alter table acceptance_comment modify column acceptance_idacceptance int(11) NULL;

alter table acceptance_file add column parent_id integer not null;
update acceptance_file set parent_id  = acceptance_idacceptance ;
alter table acceptance_file modify column acceptance_idacceptance int(11) NULL;

alter table acceptance_history add column parent_id integer not null;
update acceptance_history set parent_id = acceptance_idacceptance;
alter table acceptance_history modify column acceptance_idacceptance int(11) NULL;



alter table bank_payment_comment add column parent_id integer not null;
update bank_payment_comment set parent_id = bank_payment_id ;
alter table bank_payment_comment modify column bank_payment_id int(11) NULL;

alter table bank_payment_history add column parent_id integer not null;
update bank_payment_history set parent_id = bank_payment_id ;
alter table bank_payment_history modify column bank_payment_id int(11) NULL;

alter table projectCatalog_comment add column parent_id integer not null;
update projectCatalog_comment set parent_id = projectCatalog_idprojectCatalog ;
alter table projectCatalog_comment modify column projectCatalog_idprojectCatalog int(11) NULL;

alter table projectCatalog_file add column parent_id integer not null;
update projectCatalog_file set parent_id = projectCatalog_idprojectCatalog ;	
alter table projectCatalog_file modify column projectCatalog_idprojectCatalog int(11) NULL;

alter table projectCatalog_history add column parent_id integer not null;
update projectCatalog_history set parent_id = projectCatalog_idprojectCatalog ;
alter table projectCatalog_history modify column projectCatalog_idprojectCatalog int(11) NULL;

alter table deduction_comment add column parent_id integer not null;
update deduction_comment set parent_id = deduction_id ;
alter table deduction_comment modify column deduction_id int(11) NULL;

alter table deduction_file add column parent_id integer not null;
update deduction_file set parent_id = deduction_iddeduction ;
alter table deduction_file modify column deduction_iddeduction int(11) NULL;

alter table deduction_history add column parent_id integer not null;
update deduction_history set parent_id = deduction_id ;	
alter table deduction_history modify column deduction_id int(11) NULL;

alter table invoice_comment add column parent_id integer not null;
update invoice_comment set parent_id = invoice_id ;
alter table invoice_comment modify column invoice_id int(11) NULL;

alter table invoice_file add column parent_id integer not null;
update invoice_file set parent_id = invoice_id ;	
alter table invoice_file modify column invoice_id int(11) NULL;

alter table invoice_history add column parent_id integer not null;
update invoice_history set parent_id = invoice_id ;	
alter table invoice_history modify column invoice_id int(11) NULL;

alter table payment_comment add column parent_id integer not null;
update payment_comment set parent_id = payment_idpayment ;
alter table payment_comment modify column payment_idpayment int(11) NULL;

alter table payment_file add column parent_id integer not null;
update payment_file set parent_id = payment_idpayment ;
alter table payment_file modify column payment_idpayment int(11) NULL;

alter table payment_history add column parent_id integer not null;
update payment_history set parent_id = payment_idpayment ;
alter table payment_history modify column payment_idpayment int(11) NULL;

alter table po_comment add column parent_id integer not null;
update po_comment set parent_id = po_idpo ;	
alter table po_comment modify column po_idpo int(11) NULL;

alter table po_file add column parent_id integer not null;
update po_file set parent_id = po_idpo ;
alter table po_file modify column po_idpo int(11) NULL;

alter table po_history add column parent_id integer not null;
update po_history set parent_id = po_idpo ;
alter table po_history modify column po_idpo int(11) NULL;

alter table supplier_comment add column parent_id integer not null;
update supplier_comment set parent_id = supplier_idsupplier ;
alter table supplier_comment modify column supplier_idsupplier int(11) NULL;

alter table supplier_file add column parent_id integer not null;
update supplier_file set parent_id = supplier_idsupplier ;
alter table supplier_file modify column supplier_idsupplier int(11) NULL;
update supplier_file set supplier_idsupplier = null;

alter table supplier_history add column parent_id integer not null;
update supplier_history set parent_id = supplier_idsupplier ;
alter table supplier_history modify column supplier_idsupplier int(11) NULL;

alter table supplier_warning_comment add column parent_id integer not null;
update supplier_warning_comment set parent_id = supplier_warning_id ;
alter table supplier_warning_comment modify column supplier_warning_id int(11) NULL;

alter table supplier_warning_file add column parent_id integer not null;
update supplier_warning_file set parent_id = warning_idwarning ;
alter table supplier_warning_file modify column warning_idwarning int(11) NULL;

alter table supplier_warning_history add column parent_id integer not null;
update supplier_warning_history set parent_id = supplier_warning_id ;
alter table supplier_warning_history modify column supplier_warning_id int(11) NULL;



alter table ibuy_bank_payment_file add column parent_id integer not null;
delete from ibuy_bank_payment_file where bank_payment_id is null;
update ibuy_bank_payment_file set parent_id = bank_payment_id;
alter table ibuy_bank_payment_file modify column bank_payment_id int(11) NULL;




#invoice
alter table contract_comment add column parent_id integer not null;
update contract_comment set parent_id = contract_idcontract ;
alter table contract_comment modify column contract_idcontract int(11) NULL;

alter table contract_file add column parent_id integer not null;
update contract_file set parent_id = contract_idcontract ;
alter table contract_file modify column contract_idcontract int(11) NULL;

alter table contract_history add column parent_id integer not null;
update contract_history set parent_id = contract_idcontract ;
alter table contract_history modify column contract_idcontract int(11) NULL;

alter table customer_comment add column parent_id integer not null;
update customer_comment set parent_id = customer_idcustomer ;
alter table customer_comment modify column customer_idcustomer int(11) NULL;

alter table customer_file add column parent_id integer not null;
update customer_file set parent_id = customer_idcustomer ;
alter table customer_file modify column customer_idcustomer int(11) NULL;

alter table customer_history add column parent_id integer not null;
update customer_history set parent_id = customer_idcustomer ;
alter table customer_history modify column customer_idcustomer int(11) NULL;

alter table bankpayment_file add column parent_id integer not null;
update bankpayment_file set parent_id = bankpayment_idbankpayment ;
alter table bankpayment_file modify column bankpayment_idbankpayment int(11) NULL;


alter table deduction_invoice_file add column parent_id integer not null;
update deduction_invoice_file set parent_id = deduction_iddeduction ;
alter table deduction_invoice_file modify column deduction_iddeduction int(11) NULL;

alter table deduction_invoice_history add column parent_id integer not null;
update deduction_invoice_history set parent_id = deduction_id ;
alter table deduction_invoice_history modify column deduction_id int(11) NULL;

alter table invoice_projectCatalog_history add column parent_id integer not null;
update invoice_projectCatalog_history set parent_id = projectCatalog_id ;
alter table invoice_projectCatalog_history modify column projectCatalog_id int(11) NULL;

alter table inward_bank_payment_history add column parent_id integer not null;
update inward_bank_payment_history set parent_id = bank_payment_idbankpayment ;
alter table inward_bank_payment_history modify column bank_payment_idbankpayment int(11) NULL;

alter table inward_bank_payment_comment add column parent_id integer not null;
update inward_bank_payment_comment set parent_id = bank_payment_idbankpayment ;
alter table inward_bank_payment_comment modify column bank_payment_idbankpayment int(11) NULL;


#files
update invoice_file set link = concat('files/',link) where link not like 'files/%';
update ibuy_bank_payment_file set link = concat('files/',link) where link not like 'files/%';
update acceptance_file set link = concat('files/',link) where link not like 'files/%';
update bankaccount_file set link = concat('files/',link) where link not like 'files/%';
update bankpayment_file set link = concat('files/',link) where link not like 'files/%';
update contract_file set link = concat('files/',link) where link not like 'files/%';
update customer_file set link = concat('files/',link) where link not like 'files/%';
update deduction_file set link = concat('files/',link) where link not like 'files/%';
update deduction_invoice_file set link = concat('files/',link) where link not like 'files/%';
update payment_file set link = concat('files/',link) where link not like 'files/%';
update po_file set link = concat('files/',link) where link not like 'files/%';
update supplier_file set link = concat('files/',link) where link not like 'files/%';
update supplier_warning_file set link = concat('files/',link) where link not like 'files/%';
update user_file set link = concat('files/',link) where link not like 'files/%';


update delivery_request_file set link = concat('files/deliveryRequest/',link) where link not like 'files/%';
update il_warehouse_file set link = concat('files/warehouse/',link) where link not like 'files/%';
update job_request_file set link = concat('files/jobRequest/',link) where link not like 'files/%';
update part_number_file set link = concat('files/partNumber/',link) where link not like 'files/%';
update transporter_file set link = concat('files/transporter/',link) where link not like 'files/%';
update vehicle_file set link = concat('files/vehicle/',link) where link not like 'files/%';



#photos
update users set id_photo = CONCAT('files/',id_photo) where   id_photo not like 'files/%';
update e_general_data set ID_PHOTO = CONCAT('files/',id_photo) where   id_photo not like 'files/%';
update supplier set photo = concat('files/',photo ) where photo not like 'files/%';
update customer set photo = concat('files/',photo ) where photo not like 'files/%';
update supplier_category set photo = concat('files/',photo ) where photo not like 'files/%';
update customer_category set photo = concat('files/',photo ) where photo not like 'files/%';
update job_request_task_detail set value = CONCAT('files/jobRequestTaskDetail/',value ) where value like 'jrtd%.%';
update company set logo = concat('files/',logo ) where logo not like 'files/%';
update company set logo = REPLACE (logo,'files/photos','files/company' ) where logo like '%files/photos%';
update il_brand set image = CONCAT('files/',image) where image not like 'files/%';
update part_number set image = concat('files/partNumber/',image) where image like 'partNumber-%';
update site_category set image = REPLACE(image,'resources/img/site_category/','files/siteCategory/' ) where image like 'resources/img/site_category/%';
update site_type set image = REPLACE(image,'resources/img/site_type/','files/siteType/' ) where image like 'resources/img/site_type/%';
update tool_category_new set image = REPLACE(image,'resources/images/categories/','files/toolCategory/' ) where image like 'resources/images/categories/%';



#divers corrections
update users set status = (case when active is true then 'Active' else 'Inactive' end);


update company_file set extension  = type where extension is null;
update company_file set type = docType;
update company_file set link = CONCAT(link,'.',extension ) where link not like '%.%';
update company set count_files = (select count(*) from company_file where parent_id  = idcompany);

ALTER TABLE gcom.bu_file DROP COLUMN id;
ALTER TABLE gcom.lob_file DROP COLUMN id;
ALTER TABLE gcom.costcenter_file DROP COLUMN id;
ALTER TABLE gcom.project_file DROP COLUMN id;
ALTER TABLE gcom.costcenter_file MODIFY COLUMN link varchar(200) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL;
ALTER TABLE gcom.provisionpayment_file MODIFY COLUMN link varchar(200) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL;


