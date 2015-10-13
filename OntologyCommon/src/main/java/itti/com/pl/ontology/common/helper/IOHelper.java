package itti.com.pl.ontology.common.helper;

import itti.com.pl.ontology.common.exception.ErrorMessages;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * Utility class for common IO operations
 * 
 * @author mawa
 * 
 */
public final class IOHelper {

    private IOHelper() {
    }

    /**
     * Attempts to close Http connection
     * 
     * @param connection
     *            connection to be closed
     */
    public static final void closeConnection(HttpURLConnection connection) {
        if (connection != null) {
            try {
                connection.disconnect();
            } catch (RuntimeException exc) {
            }
        }
    }

    /**
     * Read text data from provided file using 'UTF-8' encoding
     * 
     * @param inputFileName
     *            name of the file to read text data from
     * @return content of the file
     * @throws IOHelperException
     *             could not read data from the file
     */
    public static List<String> readDataFromFile(String inputFileName) throws IOException {

        // file name not provided
        if (StringUtils.isEmpty(inputFileName)) {
            throw new IOException(ErrorMessages.IO_HELPER_NO_INPUT_FILE_PROVIDED.name());
        }

        Path path = Paths.get(inputFileName);
		return Files.readAllLines(path, Charset.defaultCharset());
    }

    /**
     * Writes text data to file specified by its name using 'UTF-8' encoding
     * 
     * @param content
     *            content of the file
     * @param outputFileName
     *            name of the file to write text data to
     * @throws IOHelperException
     *             could not read data from the file
     */
    public static void saveDataToFile(String content, String outputFileName) throws IOException {
        saveDataToFile(content, outputFileName, false);
    }

    /**
     * Writes text data to file specified by its name using 'UTF-8' encoding
     * 
     * @param content
     *            content of the file
     * @param outputFileName
     *            name of the file to write text data to
     * @param append
     *            append or overwrite existing file
     * @throws IOHelperException
     *             could not read data from the file
     */
    public static void saveDataToFile(String content, String outputFileName, boolean append) throws IOException {

        // file name not provided
        if (StringUtils.isEmpty(outputFileName)) {
            throw new IOException(ErrorMessages.IO_HELPER_NO_OUTPUT_FILE_PROVIDED.name());
        }

        // nothing to write
        if (content == null) {
            throw new IOException(String.format(ErrorMessages.IO_HELPER_NO_OUTPUT_DATA_PROVIDED.name(), outputFileName));
        }

        try (OutputStream stream = new FileOutputStream(outputFileName, append)){
            stream.write(content.getBytes(Charset.defaultCharset()));
        } catch (IOException exc) {
            throw new IOException(String.format(ErrorMessages.IO_HELPER_COULD_NOT_WRITE_DATA_TO_FILE.name(), outputFileName,
                    exc.getLocalizedMessage()), exc);
        }
    }
}
