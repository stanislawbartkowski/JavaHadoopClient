import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.apache.commons.cli.*;
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

	static class Param {
		String action;
		String path;
	}

	static private Param readPar(String[] args) {
		CommandLineParser parser = new DefaultParser();
		Options options = new Options();
		Option a = new Option("a", "action", true, LIST + ',' + CAT + ',' + PUT);
		a.setRequired(true);
		options.addOption(a);
		Option d = new Option( "f", "fdir", true, " File or directory");
     	d.setRequired(true);
		options.addOption(d);
		try {
			CommandLine line = parser.parse( options, args );
			Param param = new Param();
			param.action = line.getOptionValue('a');
			if (!param.action.equals(LIST) && !param.action.equals(CAT) && !param.action.equals(PUT)) printHelp(options);
			param.path = line.getOptionValue('f');
			return param;
		} catch (ParseException e) {
			printHelp(options);
		}
		return null;
	}

	static private void printHelp(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp( "JavaHadoopClient, list, cat or create a text file in HDFS directory.\n" +
						LIST + ": lists files in HDFS directory\n" +
				PUT +": creates a text file in HDFS path\n" +
				CAT +": cat text file in a HDFS directory\n\n", options );
		System.exit(4);
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
			outs.write("I was created directly by Hadoop Java Client");
			outs.newLine();
			outs.write("I'm good");
			outs.newLine();
			outs.write("How are you? How things are going?");
			outs.newLine();
		}
	}

	public static void main(String[] args) throws IOException {
		Param param = readPar(args);
		Configuration c = new Configuration();
		UserGroupInformation.setConfiguration(c);
		// Subject is taken from current user context
		UserGroupInformation.loginUserFromSubject(null);
		P("Current user:" + UserGroupInformation.getCurrentUser());
		try (FileSystem fs = FileSystem.get(c)) {
			if (param.action.equals(LIST))
				listDirectory(fs, param.path);
			else if (param.action.equals(CAT))
				catFile(fs, param.path);
			else if (param.action.equals(PUT))
				putFile(fs, param.path);
		}
	}

}
