package io.baselogic.batch.partition.processors;

import io.baselogic.batch.partition.domain.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;


public class ProductFieldSetMapper implements FieldSetMapper<Product> {
	private Logger LOG = LoggerFactory.getLogger(ProductFieldSetMapper.class);

	public Product mapFieldSet(FieldSet fieldSet) throws BindException {
		Product product = new Product();
		product.setId(fieldSet.readString("PRODUCT_ID"));
		product.setName(fieldSet.readString("NAME"));		
		product.setQuantity(fieldSet.readInt("QUANTITY"));
		product.setUnitPrice(fieldSet.readBigDecimal("UNIT_PRICE"));
		LOG.info("Mapping fiels to Product : {}", product);
		return product;
	}
	
}
