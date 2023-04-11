package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.Book;
import com.example.demo.service.BookService;


@RestController
public class BookController {

	@Autowired
	private BookService bookService;
	
	@PostMapping("/saveBook")
	public Book saveBook(@RequestBody Book book) {
		return bookService.saveBook(book);
	}
	
	@GetMapping("/get")
	public List<Book> getBooks(@RequestParam int limit){
		return bookService.getBook(limit);
	}

}
