package org.unlaxer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ParsedGetMessageTest {

	@Test
	public void getMessageReturnsStatusPrefixAndMessage() {
		Parsed failed = Parsed.FAILED.setMessage("boom");
		assertEquals("failed:boom", failed.getMessage());
	}

	@Test
	public void getMessageWithNullMessageReturnsNonEmptyPrefix() {
		Parsed succeeded = Parsed.SUCCEEDED;
		String got = succeeded.getMessage();
		assertNotNull(got);
		assertEquals("succeeded:", got);
	}

	@Test
	public void getMessageWithNullMessageForFailedReturnsFailedPrefix() {
		Parsed failed = Parsed.FAILED;
		String got = failed.getMessage();
		assertNotNull(got);
		assertEquals("failed:", got);
	}
}
