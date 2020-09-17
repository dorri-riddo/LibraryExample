package parsePdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;
import org.apache.pdfbox.util.PDFTextStripper;


public class Parser {
	 static Connection conn = null;
     static Statement stmt = null;  
     static boolean isIndex = false;

	private void setup() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		// DB url / 아이디 / 비밀번호 유의 
	    String url = "jdbc:mysql://localhost:3306/dev?serverTimezone=UTC";
		conn = DriverManager.getConnection(url, "root", "db2019");
		stmt = conn.createStatement();
	}
	
    public String extractData(PDDocument document) throws FileNotFoundException, IOException
    {
        String content = "";
        PDFTextStripper stripper = new PDFTextStripper();
        content = stripper.getText(document);
        if (content.replaceAll(" ", "").indexOf("그림차례") != -1 || content.replaceAll(" ", "").indexOf("표차례") != -1
        		|| content.replaceAll(" ", "").indexOf("그림목차") != -1 || content.replaceAll(" ", "").indexOf("표목차") != -1
        		|| content.indexOf("Contents") != -1) {
        	isIndex = true;
        	}
        
        return content;
    }
    
    public void extractImages(PDDocument document, String filepath) throws IOException {
        @SuppressWarnings("unchecked")
		List<PDPage> pages = document.getDocumentCatalog().getAllPages();
        int pageCount = 0;
        for (PDPage page:pages) {
            pageCount++;
            Map<String, PDXObject> xobj = null;  
            xobj = page.findResources().getXObjects();  
            
            int imageCount =0;
            for (PDXObject obj : xobj.values()) {
                if (obj instanceof PDXObjectImage)  {
                    imageCount++;
                    String imageName = "page_"+pageCount+"image_"+imageCount+".png";
                    PDXObjectImage image = (PDXObjectImage) obj;
                    ImageIO.write(
                            image.getRGBImage(),
                            "PNG",
                            new File(filepath + "\\" + imageName)
                    );
                }
            }
        } 
        
        if (document != null) {
        	document.close();
        }
    }
    
    public void saveFile(String filepath, PDDocument document, String content, String path) throws IOException {
        File file = new File(filepath + "\\" + path);
        FileWriter fw = new FileWriter(file);
        fw.write(content);
        fw.flush();
        fw.close();
    }
    
    //목차가 있는 그림, 표 경우
    public ArrayList<String> extractSentence(String content) throws FileNotFoundException {
        Pattern pattern = Pattern.compile("(\\[|<)[가-힣]*[a-zA-Z]* [0-9]+-?[0-9]*(>|\\]) (.*?)(\r\n)?(.*?)·+\\s?[0-9]+\r");
       	
        ArrayList<String> list = new ArrayList<String>();
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
        	list.add(matcher.group());
        }     
        
        return list;        
        }
    
    //목차가 없는 경우 
    public ArrayList<String> extractSentence2(String content) throws FileNotFoundException {
    	Pattern pattern = Pattern.compile("^(\\[|<)[가-힣]*[a-zA-Z]* [0-9]+-?[0-9]*(>|\\]) (.*?)(\r\n)?(.*?)\r");
    	ArrayList<String> list = new ArrayList<String>();
    	String[] tmpArr = content.split("\r\n");
    	for (int i = 0; i < tmpArr.length; i++) {
			tmpArr[i] += "\r";
	    	Matcher matcher = pattern.matcher(tmpArr[i]);
	    	
	    	while (matcher.find()) {
	    		list.add(matcher.group());
	    	}
    	}
    	return list;
    	    	
    }
    
   
    public void extractDB(ArrayList<String> list, String fileName, int pdf_id) {
    	String type = "";
    	String index = "N";
    	String seq = "";
    	String keyword = "";
    	int page = 0;  
        	
    	Pattern pattern = Pattern.compile("(\\[|<)[가-힣]*[a-zA-Z]* [0-9]+-?[0-9]*(>|\\])");
    	Matcher matcher;
    	for (int i = 0; i < list.size(); i++) {
			String[] strArr = list.get(i).split("·+\\s?(?=[0-9]+)");
			matcher = pattern.matcher(strArr[0]);
			
			int idx = 0;
			if (matcher.find()) {
				idx = matcher.end();
				keyword = strArr[0].substring(idx + 1);
				
				String str = strArr[0].substring(1, idx - 1);
				String[] strArr2 = str.split(" ");
				type = strArr2[0];
				seq = strArr2[1];
			}
			if (isIndex) {
				index = "Y";
				page = Integer.parseInt(strArr[1].trim());
			}
			insertData(type, index, seq, keyword, page, pdf_id);
		}
    }
    
    public int selectPdfId(String fileName) {
      ResultSet rs = null;
      int id = 0;
      try{
          String sql = "SELECT id FROM pdf WHERE NAME = '" + fileName + "'";
          rs = stmt.executeQuery(sql);    
    
          while(rs.next()){
              id = rs.getInt(1);
          }
      }
      catch( SQLException e){
          System.out.println("에러 " + e);
      }
      finally{
          try{
              if( stmt != null && !stmt.isClosed()){
                  stmt.close();
              }
              if( rs != null && !rs.isClosed()){
                  rs.close();
              }
          }
          catch( SQLException e){
              e.printStackTrace();
          }
      }
      return id;
    }
    
    
    // 목차가 따로 있는 그림, 표 등을 추출 
    public void insertData(String type, String index_YN, String seq, String keyword, int page, int pdf_id) {        
        PreparedStatement pstmt = null;

        try{            
            String sql = "INSERT INTO data VALUES (?,?,?,?,?,?,?)";
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, 0);
            pstmt.setString(2, type);
            pstmt.setString(3, index_YN);
            pstmt.setString(4, seq);
            pstmt.setString(5, keyword);
            if (isIndex) {
            	pstmt.setInt(6, page);
            }
            else {
            	pstmt.setNull(6, Types.VARCHAR);
            }
            pstmt.setInt(7, pdf_id);
            pstmt.executeUpdate();
        }
        catch( SQLException e){
            System.out.println("에러 " + e);
        }
         finally{
            try{
                if( pstmt != null && !pstmt.isClosed()){
                    pstmt.close();
                }
            }
            catch( SQLException e){
                e.printStackTrace();
            }
        }
    }
    
    // 법 삽입
    public void insertData(String keyword, int pdf_id) {
        PreparedStatement pstmt = null;

        try{            
            String sql = "INSERT INTO data VALUES (?,?,?,?,?,?,?)";
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, 0);
            pstmt.setString(2, "법률");
            pstmt.setNull(3, Types.VARCHAR);
            pstmt.setNull(4, Types.VARCHAR);
            pstmt.setString(5, keyword);
            pstmt.setNull(6, Types.VARCHAR);
            pstmt.setInt(7, pdf_id);
            pstmt.executeUpdate();
        }
        catch( SQLException e){
            System.out.println("에러 " + e);
        }
         finally{
            try{
                if( pstmt != null && !pstmt.isClosed()){
                    pstmt.close();
                }
            }
            catch( SQLException e){
                e.printStackTrace();
            }
        }
    }
        
    public void insertPdf(String fileName) {
        PreparedStatement pstmt = null;

        try {
            String sql = "INSERT INTO pdf VALUES (?,?)";
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, 0);
            pstmt.setString(2, fileName);            
    		pstmt.executeUpdate();
        }
        catch( SQLException e){
            System.out.println("에러 " + e);
        }
          finally{
            try{
                if( pstmt != null && !pstmt.isClosed()){
                    pstmt.close();
                }
            }
            catch( SQLException e){
                e.printStackTrace();
            }
        }
    }
    
    // 법 추출
    public ArrayList<String> extractLaw(String content, String fileName, int pdf_id) {
		Pattern pattern = Pattern.compile("｢(.*?)(\r\n)?(.*?)｣");
		Matcher matcher = pattern.matcher(content);
		ArrayList<String> list = new ArrayList<String>();
		while (matcher.find()) {
			insertData(matcher.group(), pdf_id);
		}
		
		return list;
    }
    

    public static void main(String[] args) throws IOException, IllegalArgumentException, ClassNotFoundException, SQLException {
    	// filename 과 filepath 유의
    	String fileName = "통일";
    	String filepath = "C:\\Users\\sjw\\Desktop\\abc\\" + fileName;
        PDDocument document = null;
        try {
            document = PDDocument.load(filepath + ".pdf");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        
        File f = new File(filepath);
        if (!f.exists()) {
        	f.mkdir();  
        }
        
        Parser p = new Parser();
        p.setup();
        p.insertPdf(fileName);
        int pdf_id = p.selectPdfId(fileName);
        
        String content = p.extractData(document); 
        p.saveFile(filepath, document, content, "Pdf 텍스트 추출.txt");    
        p.extractImages(document, filepath);          
    
       ArrayList<String> list = p.extractSentence(content);;
       if (!isIndex && list.isEmpty()) {
     	  list = p.extractSentence2(content);
       }
       p.extractDB(list, fileName, pdf_id);    
       p.extractLaw(content, fileName, pdf_id);  
        
        if(conn != null && !conn.isClosed()){
            conn.close();
        }
        
    }
}