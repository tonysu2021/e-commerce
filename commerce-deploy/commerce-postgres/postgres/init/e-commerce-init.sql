CREATE DATABASE commerce_db;
    
\c commerce_db;

CREATE SCHEMA cloud;

/** Account Management */
CREATE USER commerce WITH PASSWORD '1qaz2wsx';
    
GRANT CONNECT ON DATABASE commerce_db TO commerce; 

GRANT USAGE ON SCHEMA cloud TO commerce; 

/* Initalise uuid module */
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE  TABLE cloud.basic_table ( 
	from_ip              varchar(256)   ,
	create_time          timestamptz  NOT NULL ,
	modify_time          timestamptz   ,
	create_by            varchar(256)  NOT NULL ,
	modify_by            varchar(256)   
 );

COMMENT ON TABLE cloud.basic_table IS '每張表繼承使用';

CREATE  TABLE cloud.category ( 
	name                 varchar(100)   ,
	description          varchar   ,
	picture              varchar   ,
	category_id          character varying(64)  NOT NULL ,
    LIKE cloud.basic_table INCLUDING ALL ,
	CONSTRAINT pk_category_category_id PRIMARY KEY ( category_id )
 );

COMMENT ON TABLE cloud.category IS '商品目錄';

CREATE  TABLE cloud.customer ( 
	customer_id          character varying(64)  NOT NULL ,
	name                 varchar(100)   ,
	email                varchar(128)   ,
	"password"           varchar(128)   ,
	"enabled"            boolean  NOT NULL ,
    LIKE cloud.basic_table INCLUDING ALL ,
	CONSTRAINT pk_customer_customer_id PRIMARY KEY ( customer_id )
 );

COMMENT ON TABLE cloud.customer IS '消費者';

CREATE  TABLE cloud.shipping ( 
	shipping_id          character varying(64)  NOT NULL ,
	shipping_charge      integer   ,
	address              varchar(100)   ,
	status               char(1)   ,
    LIKE cloud.basic_table INCLUDING ALL ,
	CONSTRAINT pk_shipping_shipping_id PRIMARY KEY ( shipping_id )
 );

COMMENT ON TABLE cloud.shipping IS '物流';

CREATE  TABLE cloud.supplier ( 
	supplier_id          character varying(64)  NOT NULL ,
	supplier_name        varchar(100)   ,
    LIKE cloud.basic_table INCLUDING ALL ,
	CONSTRAINT pk_supplier_supplier_id PRIMARY KEY ( supplier_id )
 );

COMMENT ON TABLE cloud.supplier IS '供應商';

CREATE  TABLE cloud.credit_card ( 
	credit_card_id       character varying(64)  NOT NULL ,
	cc_name              varchar(100)   ,
	cc_number            varchar   ,
	exp_date             date   ,
	customer_id          character varying(64)   ,
    LIKE cloud.basic_table INCLUDING ALL ,
	CONSTRAINT pk_credit_card_credit_card_id PRIMARY KEY ( credit_card_id )
 );

COMMENT ON TABLE cloud.credit_card IS '信用卡';

CREATE  TABLE cloud.payment ( 
	payment_id           character varying(64)  NOT NULL ,
	credit_card_id       character varying(64)   ,
    LIKE cloud.basic_table INCLUDING ALL ,
	CONSTRAINT pk_payment_payment_id PRIMARY KEY ( payment_id )
 );

COMMENT ON TABLE cloud.payment IS '金流';

CREATE  TABLE cloud.product ( 
	name                 varchar(100)   ,
	desciption           varchar   ,
	price                integer   ,
	product_id           character varying(64)  NOT NULL ,
	category_id          character varying(64)  NOT NULL ,
	supplier_id          character varying(64)   ,
    LIKE cloud.basic_table INCLUDING ALL ,
	CONSTRAINT pk_product_product_id PRIMARY KEY ( product_id )
 );

COMMENT ON TABLE cloud.product IS '商品';

COMMENT ON COLUMN cloud.product.name IS '商品名稱';

COMMENT ON COLUMN cloud.product.desciption IS '說明';

COMMENT ON COLUMN cloud.product.category_id IS '商品目錄ID';

CREATE  TABLE cloud.product_detail ( 
	"size"               integer   ,
	color                varchar   ,
	product_id           character varying(64)   ,
	product_detail_id    character varying(64)  NOT NULL ,
    LIKE cloud.basic_table INCLUDING ALL ,
	CONSTRAINT pk_product_detail_product_detail_id PRIMARY KEY ( product_detail_id )
 );

COMMENT ON TABLE cloud.product_detail IS '商品明細';

CREATE  TABLE cloud.inventory ( 
	inventory_id         character varying(64)  NOT NULL ,
	product_id           character varying(64)   ,
	warehouse_id         character varying(64)   ,
	warehouse_address    varchar(100)   ,
	quantity             integer   ,
    LIKE cloud.basic_table INCLUDING ALL ,
	CONSTRAINT pk_inventory_inventory_id PRIMARY KEY ( inventory_id )
 );

COMMENT ON TABLE cloud.inventory IS '商品庫存';

CREATE  TABLE cloud."order" ( 
	order_id             character varying(64)  NOT NULL ,
	shipping_id          character varying(64)  NOT NULL ,
	payment_id           character varying(64)  NOT NULL ,
	customer_id          character varying(64)  NOT NULL ,
	status               varchar(16)   ,
	total_amount         integer   ,
    LIKE cloud.basic_table INCLUDING ALL ,
	CONSTRAINT pk_tbl_order_id PRIMARY KEY ( order_id )
 );

COMMENT ON TABLE cloud."order" IS '訂單';

COMMENT ON COLUMN cloud."order".status IS '訂單狀態';

COMMENT ON COLUMN cloud."order".total_amount IS '總金額';

CREATE  TABLE cloud.order_item ( 
	order_id             character varying(64)   ,
	product_detail_id    character varying(64)   ,
	quantity             integer   ,
	price                integer   ,
    LIKE cloud.basic_table INCLUDING ALL
 );

COMMENT ON TABLE cloud.order_item IS '訂單明細(該商品)';

CREATE  TABLE cloud.invoice ( 
	invoice_id           character varying(64)  NOT NULL ,
	order_id             character varying(64)   ,
	credit_card_id       character varying(64)   ,
    LIKE cloud.basic_table INCLUDING ALL ,
	CONSTRAINT pk_invoice_invoice_id PRIMARY KEY ( invoice_id )
 );

COMMENT ON TABLE cloud.invoice IS '發票/收據';

CREATE  TABLE cloud.invoice_history ( 
	invoice_id           character varying(64)   ,
    LIKE cloud.basic_table INCLUDING ALL 
 );

ALTER TABLE cloud.credit_card ADD CONSTRAINT fk_credit_card_customer FOREIGN KEY ( customer_id ) REFERENCES cloud.customer( customer_id );

ALTER TABLE cloud.inventory ADD CONSTRAINT fk_inventory_product FOREIGN KEY ( product_id ) REFERENCES cloud.product( product_id );

ALTER TABLE cloud.invoice ADD CONSTRAINT fk_invoice_credit_card FOREIGN KEY ( credit_card_id ) REFERENCES cloud.credit_card( credit_card_id );

ALTER TABLE cloud.invoice ADD CONSTRAINT fk_invoice_order FOREIGN KEY ( order_id ) REFERENCES cloud."order"( order_id );

ALTER TABLE cloud.invoice_history ADD CONSTRAINT fk_invoice_history_invoice FOREIGN KEY ( invoice_id ) REFERENCES cloud.invoice( invoice_id );

ALTER TABLE cloud."order" ADD CONSTRAINT fk_order_customer FOREIGN KEY ( customer_id ) REFERENCES cloud.customer( customer_id );

ALTER TABLE cloud."order" ADD CONSTRAINT fk_order_payment FOREIGN KEY ( payment_id ) REFERENCES cloud.payment( payment_id );

ALTER TABLE cloud."order" ADD CONSTRAINT fk_order_shipping FOREIGN KEY ( shipping_id ) REFERENCES cloud.shipping( shipping_id );

ALTER TABLE cloud.order_item ADD CONSTRAINT fk_order_item_order FOREIGN KEY ( order_id ) REFERENCES cloud."order"( order_id );

ALTER TABLE cloud.order_item ADD CONSTRAINT fk_order_item_product_detail FOREIGN KEY ( product_detail_id ) REFERENCES cloud.product_detail( product_detail_id );

ALTER TABLE cloud.payment ADD CONSTRAINT fk_payment_credit_card FOREIGN KEY ( credit_card_id ) REFERENCES cloud.credit_card( credit_card_id );

ALTER TABLE cloud.product ADD CONSTRAINT fk_product_product FOREIGN KEY ( category_id ) REFERENCES cloud.category( category_id );

ALTER TABLE cloud.product ADD CONSTRAINT fk_product_supplier FOREIGN KEY ( supplier_id ) REFERENCES cloud.supplier( supplier_id );

ALTER TABLE cloud.product_detail ADD CONSTRAINT fk_product_detail_product FOREIGN KEY ( product_id ) REFERENCES cloud.product( product_id );

GRANT INSERT, SELECT, UPDATE, DELETE ON ALL TABLES IN SCHEMA cloud TO commerce;
