package fr.ogama.jfsql.query.clause.in;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import fr.ogama.jfsql.JFSQL;
import fr.ogama.utils.parser.JFSQLExecutionException;
import fr.ogama.utils.parser.model.Statement;

public class InTest {

	private static String inDirectory1;
	private static String inDirectory2;
	private static String inDirectory3;
	
	private static File file1;
	private static File file2;
	private static File file3;
	
	@BeforeClass
	public static void setup() {
		inDirectory1 = System.getProperty("user.dir") + "/src/test/resources/inTestFolder/Folder 1";
		inDirectory2 = System.getProperty("user.dir") + "/src/test/resources/inTestFolder/Folder 2";
		inDirectory3 = System.getProperty("user.dir") + "/src/test/resources/inTestFolder/Folder 3";
		
		file1 = new File(inDirectory1 + "/Folder 1.1/File 1.txt");
		file2 = new File(inDirectory2 + "/Folder 2.1/Folder 2.2/File 2.txt");
		file3 = new File(inDirectory3 + "/Folder 3.1/Folder 3.2/Folder 3.3/File 3.txt");
		
		assertThat(file1).exists().isFile();
		assertThat(file2).exists().isFile();
		assertThat(file3).exists().isFile();
		
		assertThat(new File(inDirectory1)).exists().isDirectory();
		assertThat(new File(inDirectory2)).exists().isDirectory();
		assertThat(new File(inDirectory3)).exists().isDirectory();
	}
	
	@Test
	public void should_get_in_given_directory() throws Exception {
		// GIVEN
		String query = "get file in('" + inDirectory1 + "') having type = 'file';";
		Statement fileQuery = JFSQL.parseOneStatement(query);
		
		// WHEN
		List<Comparable> results = fileQuery.execute(new HashMap<String, Comparable>());
		
		// THEN
		assertThat(results).isNotNull().hasSize(1);
		assertThat(results).containsOnly(file1);
	}
	
	@Test
	public void should_get_directories_in_current_directory() throws Exception {
		String query = "get file in ('.');";
		Statement fileQuery = JFSQL.parseOneStatement(query);
		
		// WHEN
		List<Comparable> results = fileQuery.execute(new HashMap<String, Comparable>());
		
		// THEN
		assertThat(results).isNotNull();
	}
	
	@Test
	public void should_get_in_given_directories() throws Exception {
		String query = "get file in ('" + inDirectory1 + "', '" + inDirectory2 + "') having type = 'file';";
		Statement fileQuery = JFSQL.parseOneStatement(query);
		
		// WHEN
		List<Comparable> results = fileQuery.execute(new HashMap<String, Comparable>());
		
		// THEN
		assertThat(results).isNotNull().hasSize(2);
		assertThat(results).containsOnly(file1, file2);
	}

	@Test(expected = Exception.class)
	public void should_get_error_because_directory_not_exists() throws Exception {
		String query = "get file in ('/directory/not/exists');";
		Statement fileQuery = JFSQL.parseOneStatement(query);
		
		// WHEN
		List<Comparable> results = fileQuery.execute(new HashMap<String, Comparable>());
		
		// THEN
	}
	
	@Test(expected = Exception.class)
	public void should_get_error_because_directories_not_exists() throws Exception {
		String query = "get file in ('/directory/not/exists', '/and/this/too');";
		Statement fileQuery = JFSQL.parseOneStatement(query);
		
		// WHEN
		List<Comparable> results = fileQuery.execute(new HashMap<String, Comparable>());
		
		// THEN
	}
	
	@Test(expected = Exception.class)
	public void should_get_error_because_one_directory_not_exists() throws Exception {
		String query = "get file in ('/directory/not/exists', '" + inDirectory1 + "');";
		Statement fileQuery = JFSQL.parseOneStatement(query);
		
		// WHEN
		List<Comparable> results = fileQuery.execute(new HashMap<String, Comparable>());
		
		// THEN
	}
	
	@Test(expected = Exception.class)
	public void should_get_error_because_synthax_error_quote() throws Exception {
		String query = "get file in (" + inDirectory1 + "');";
		Statement fileQuery = JFSQL.parseOneStatement(query);
		
		// WHEN
		
		// THEN
	}
	
	@Test(expected = Exception.class)
	public void should_get_error_because_synthax_error_parenthesis() throws Exception {
		String query = "get file in '" + inDirectory1 + "');";
		Statement fileQuery = JFSQL.parseOneStatement(query);
		
		// WHEN
		
		// THEN
	}
	
	@Test(expected = Exception.class)
	public void should_get_error_because_synthax_error_no_path_specified() throws Exception {
		String query = "get file in ();";
		Statement fileQuery = JFSQL.parseOneStatement(query);
		
		// WHEN
		
		// THEN
	}
	
	@Test(expected = Exception.class)
	public void should_get_error_on_execute_query_because_empty() throws Exception {
		String query = "get file in ('');";
		Statement fileQuery = JFSQL.parseOneStatement(query);
		
		// WHEN
		List<Comparable> results = fileQuery.execute(new HashMap<String, Comparable>());
				
		// THEN
	}
	
	@Test
	public void should_get_directories_in_root() throws Exception {
		String query = "get file in ('/' deep 1) having type = 'directory';";
		Statement fileQuery = JFSQL.parseOneStatement(query);
		
		// WHEN
		List<Comparable> results = fileQuery.execute(new HashMap<String, Comparable>());
		
		// THEN
		assertThat(results).isNotEmpty().hasOnlyElementsOfType(File.class);
	}
	
	@Test
	public void should_get_file_in_in_sub_query() throws Exception {
		String directoryQueryString = "get 2 file in ('.' deep 1) having type is 'directory'";
		Statement directoryQuery = JFSQL.parseOneStatement(directoryQueryString + ";");
		
		String query = "get file in (" + directoryQueryString + " deep 1);";
		Statement fileQuery = JFSQL.parseOneStatement(query);
		
		// WHEN
		List<Comparable> dirResults = directoryQuery.execute(new HashMap<String, Comparable>());
		List<Comparable> results = fileQuery.execute(new HashMap<String, Comparable>());
		
		// THEN
		assertThat(results).isNotEmpty();
		
		for (Comparable result : results) {
			assertThat(result).isExactlyInstanceOf(File.class);
			File file = (File) result;
			assertThat(file.getParentFile()).isIn(dirResults);
		}
	}
}
