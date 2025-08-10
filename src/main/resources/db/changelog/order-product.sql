-- Create order_product table
CREATE TABLE "order_product" (
                         order_id BIGINT,
                         product_id BIGINT,
                         CONSTRAINT fk_order FOREIGN KEY (order_id) REFERENCES "order" (id),
                         CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES product (id),
                         CONSTRAINT pk_order_product PRIMARY KEY (order_id, product_id)
);