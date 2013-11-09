package kba.fff.fff_common;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UriMinter {
	
	static Logger log = LoggerFactory.getLogger(UriMinter.class.getName());
	
	
	public static String randomString() {
		return randomString(8);
	}
	
	private static final Pattern nonAlnumPattern = Pattern.compile("([^A-Za-z])");
	public static String randomString(int length) {
		SecureRandom rand = new SecureRandom();
		byte[] randArr = new byte[20];
		rand.nextBytes(randArr);
		String randStr = new String(Arrays.copyOfRange(Base64.encodeBase64(randArr, false, true), 0, length));
		Matcher matcher = nonAlnumPattern.matcher(randStr);
		while (matcher.find()) {
			randStr = randStr.replace(matcher.group(1), "" + (char)(rand.nextInt(26) + 'a'));
		}
		return randStr;
	}
	
	public static String mintURI(String template) {
		
		String uri = template;
		DateTime now = DateTime.now();
		DateTimeFormatter fmt = DateTimeFormat.forPattern("YYYY-MM-dd.HH-mm-ss.SSSS");
		while (uri.contains("${timestamp}"))
			uri = uri.replaceAll("\\$\\{timestamp\\}", now.toString(fmt));
		while (uri.contains("${iso8601}"))
			uri = uri.replaceAll("\\$\\{iso8601\\}", now.toString());
		while (uri.contains("${uuid}"))
			uri = uri.replaceAll("\\$\\{uuid\\}", UUID.randomUUID().toString());
		while (uri.contains("${randomString}"))
			uri = uri.replaceAll("\\$\\{randomString\\}", randomString());
		while (uri.contains("${randomString[")) {
			Pattern pat = Pattern.compile("(\\$\\{randomString\\[(\\d+)\\]\\})");
			Matcher matcher = pat.matcher(uri);
			matcher.find();
			int length = Integer.parseInt(matcher.group(2));
			String randomStr = randomString(length);
			uri = uri.replace(matcher.group(0), randomStr);
		}
		
		return uri;
	}

}
