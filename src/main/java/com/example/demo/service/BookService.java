package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.datamodeling.ScanResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.Select;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.example.demo.Entity.Book;



@Service
public class BookService {

	@Autowired
	private DynamoDBMapper dynamoDBMapper;

	public Book saveBook(Book book) {
		dynamoDBMapper.save(book);
		return book;
	}
	
//	public List<Book> getBook(int page,int limit){
//		String hashKeyValue = "1"; 
//		DynamoDBQueryExpression<Book> queryExpression = new DynamoDBQueryExpression<Book>()
//				.withHashKeyValues(new Book(hashKeyValue, hashKeyValue, hashKeyValue)) 
//		        .withLimit(limit)
//		        .withExclusiveStartKey(getLastEvaluatedKey(page, limit));
//
//		PaginatedQueryList<Book> results = dynamoDBMapper.query(Book.class, queryExpression);
//		return results;
//	}
//	
//	private Map<String, AttributeValue> getLastEvaluatedKey(int page, int size) {
//		if (page == 1) {
//			return null;
//		}
//		int itemsToSkip = (page - 1) * size;
//		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
//				.withLimit(itemsToSkip + 1)
//				.withSelect(Select.ALL_ATTRIBUTES);
//		List<Book> books = dynamoDBMapper.scan(Book.class, scanExpression);
//		if (books.size() < itemsToSkip) {
//			return null;
//		}
//		Book lastBook = books.get(itemsToSkip - 1);
//		Map<String, AttributeValue> exclusiveStartKey = new HashMap();
//		System.out.println(lastBook.getId());
//		exclusiveStartKey.put("id", new AttributeValue().withS(lastBook.getId()));
//		exclusiveStartKey.put("title", new AttributeValue().withS(lastBook.getName()));
//		return exclusiveStartKey;
//	}
	
	
	public List<Book> getBook(int limit){
		DynamoDBScanExpression scanPageExpression = new DynamoDBScanExpression().withLimit(limit);
		List pageBookList = new ArrayList<>();
		do {
			
			ScanResultPage<Book> scanPage = dynamoDBMapper.scanPage(Book.class, scanPageExpression);
			for(int i=0; i<scanPage.getResults().size();i++) {
				System.out.println(scanPage.getResults().get(i));
				dynamoDBMapper.delete(scanPage.getResults().get(i));
				System.out.println("Deleted");
			}
			pageBookList.add(scanPage.getResults());
			System.out.println("LastEvaluatedKey=" + scanPage.getLastEvaluatedKey());
			scanPageExpression.setExclusiveStartKey(scanPage.getLastEvaluatedKey());
		} while (scanPageExpression.getExclusiveStartKey() != null);
		return pageBookList;
	}
}