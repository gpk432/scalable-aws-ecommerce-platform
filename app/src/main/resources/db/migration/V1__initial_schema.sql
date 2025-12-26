create table products (
  id bigserial primary key,
  sku varchar(80) not null unique,
  name varchar(200) not null,
  price numeric(12,2) not null check (price >= 0),
  stock integer not null check (stock >= 0)
);

create table purchase_orders (
  id bigserial primary key,
  public_id uuid not null unique,
  idempotency_key varchar(200) not null unique,
  customer_email varchar(320) not null,
  status varchar(40) not null,
  payment_reference varchar(120),
  total numeric(12,2) not null check (total >= 0),
  created_at timestamptz not null
);

create table order_lines (
  id bigserial primary key,
  order_id bigint not null references purchase_orders(id) on delete cascade,
  product_id bigint not null,
  sku varchar(80) not null,
  product_name varchar(200) not null,
  unit_price numeric(12,2) not null,
  quantity integer not null check (quantity > 0),
  line_total numeric(12,2) not null
);
create index idx_order_lines_order_id on order_lines(order_id);

insert into products(sku,name,price,stock) values
('KB-001','Mechanical Keyboard',89.00,100),
('MS-002','Wireless Mouse',39.00,150),
('HD-003','USB-C Hub',59.00,80);
