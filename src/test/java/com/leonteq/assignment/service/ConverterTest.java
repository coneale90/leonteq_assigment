package com.leonteq.assignment.service;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.leonteq.assignment.DB.DBManager;
import com.leonteq.assignment.structure.SimpleCache;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SimpleCache.class,DBManager.class})
public class ConverterTest {
	
	private SimpleCache sc;
	
	@Before 
	public void initialize(){
		sc = SimpleCache.getInstance();
		PowerMockito.mockStatic(SimpleCache.class);
		PowerMockito.mockStatic(DBManager.class);
		PowerMockito.when(SimpleCache.getInstance()).thenReturn(sc);
		PowerMockito.when(DBManager.getConnection()).thenReturn(null);
	}
	
	@Test
	public void shortUrlTest(){
		String shortUrl = Converter.shortUrl("https://ch.test.com/test");
		assertTrue("The url was converted wrong", shortUrl.equals("https://tes.com/NndPU"));
	}
	
	@Test
	public void shortUrlDoubleConversionTest(){
		String shortUrl = Converter.shortUrl("https://ch.test.com/testDouble");
		String shortUrlSecond = Converter.shortUrl("https://ch.test.com/testDouble");
		assertTrue("Calling two times the conversion on the same URL the result has to be the same", shortUrl.equals(shortUrlSecond));
	}
	
	@Test
	public void hashFunctionTest(){
		String hashString = "NndPU";
		String calculatedHashString = Converter.hashF("/test");
		assertTrue("The return value of the hash function is wrong", calculatedHashString.equals(hashString));
	}
	
	@Test
	public void checkBadWordTest(){
		String hashString = "5tUp1d";
		String checkedString = "t5pUd1";
		String sanitizedString = Converter.checkBlackList(hashString);
		assertTrue("The bad word was not sanitized", sanitizedString.equals(checkedString));
	}
}
