package gateway;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import java.io.*;

/**
 * Convert the below Html table To Pdf Using Itext library
 *
 * @see StringBuilder
 */

public class PDFGenerate{
	
	public void writeTo(File file, String content) throws IOException, DocumentException{
		// TODO
		//  WARNING: An illegal reflective access operation has occurred
		//  WARNING: Illegal reflective access by com.itextpdf.text.io.ByteBufferRandomAccessSource$1 (file:/home/homelet/Workspace/CSC207_Projects/CSC207-Phase2-Refactor/lib/itextpdf-5.5.6.jar) to method java.nio.DirectByteBuffer.cleaner()
		//  WARNING: Please consider reporting this to the maintainers of com.itextpdf.text.io.ByteBufferRandomAccessSource$1
		//  WARNING: Use --illegal-access=warn to enable warnings of further illegal reflective access operations
		//  WARNING: All illegal access operations will be denied in a future release
		
		OutputStream fos = new FileOutputStream(file);
		Document document = new Document();
		PdfWriter pdfWriter = PdfWriter.getInstance(document, fos);
		
		document.open();
		InputStream is = new ByteArrayInputStream(content.getBytes());
		XMLWorkerHelper.getInstance().parseXHtml(pdfWriter, document, is);
		document.close();
		fos.close();
	}
}
