import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.UserGroupInformation;

public class Main {

	static final private String LIST = "list";
	static final private String CAT = "cat";
	static final private String PUT = "put";

	static private void P(String s) {
		System.out.println(s);
	}

	static private void printHelp() {
		P("Usage: Main /what/ /param/");
		P(" what : " + LIST + ", list directory; param : directory to list");
		P(" what : " + CAT + ", output text file; param : text file to list ");
		P(" what : " + PUT + ", create text file; param : text file to create ");
	}

	private static void listDirectory(FileSystem fs, String dir)
			throws FileNotFoundException, IllegalArgumentException, IOException {
		P("List directory content :" + dir);
		FileStatus[] status = fs.listStatus(new Path(dir));
		for (FileStatus f : status) {
			P("  " + f.getPath().getName());
		}
	}

	private static void catFile(FileSystem fs, String textfile) throws IOException {
		P("Output file " + textfile);
		Path inputPath = new Path(textfile);
		try (InputStream is = fs.open(inputPath);
				BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
			for (String line = reader.readLine(); line != null; line = reader.readLine())
				P(line);
		}
	}

	private static void putFile(FileSystem fs, String textfile) throws IOException {
		P("Create file " + textfile);
		Path outputPath = new Path(textfile);
		try (OutputStream os = fs.create(outputPath);
				BufferedWriter outs = new BufferedWriter(new OutputStreamWriter(os))) {
			outs.write("I was created directly but Hadoop Java Clinet");
			outs.newLine();
			outs.write("I'm good");
			outs.newLine();
			outs.write("How are you? How things are going?");
			outs.newLine();
		}
	}

	public static void main(String[] args) throws IOException {
		if (args.length != 2) {
			printHelp();
			System.exit(4);
		}
		Configuration c = new Configuration();
		UserGroupInformation.setConfiguration(c);
		// Subject is taken from current user context
		UserGroupInformation.loginUserFromSubject(null);
		P("Current user:" + UserGroupInformation.getCurrentUser());
		try (FileSystem fs = FileSystem.get(c)) {
			if (args[0].equals(LIST))
				listDirectory(fs, args[1]);
			else if (args[0].equals(CAT))
				catFile(fs, args[1]);
			else if (args[0].equals(PUT))
				putFile(fs, args[1]);
			else
				printHelp();
		}
	}

}
