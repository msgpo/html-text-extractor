package com.rekkit.wordextractor;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;

import com.csvreader.CsvWriter;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.StringTokenizer;

@SuppressWarnings("serial")
public class WordExtractorGUI extends JFrame implements ActionListener{
	
	static private final String newline = "\n";

	private JPanel contentPane;
	private JTextField textField_title;
	private JTextField textField_author;
	private JTextField textField_date;
	private JTextArea textArea_content;
	private JTextArea textArea_log;
	private JMenuItem mntmOpenHtmlFile;
	private JMenuItem mntmExtractDistinctWords;
	private JMenuItem mntmAboutThisProgram;
	private JMenuItem mntmExit;
	
	// extractor instances
	JFileChooser fc;
    WebClient client;
    HtmlElement currentPage;
    HtmlPage page;
    
    // HTML file instances
    HtmlElement content;
    HtmlElement contentTitle;
    HtmlElement contentAuthorEndParagraph;
    HtmlElement contentAuthorAfterTitle;
    HtmlElement contentDate;
    String title;
    String author;
    String date;
    String body;

    // File Chooser.
    int returnValOpen;
    
 // Unique Word Counter instances
    HashSet<String> uniquewordList;
    ArrayList<String> words;
    StringTokenizer st;
    private int length;
    
	/**
	 * Create the frame.
	 */
	@SuppressWarnings("deprecation")
	public WordExtractorGUI() {
		
		// Initialize all the crap!
				client = new WebClient();
		        client.setJavaScriptEnabled(false);
		        client.setCssEnabled(false);
		        client.setThrowExceptionOnFailingStatusCode(false);
		        client.setThrowExceptionOnScriptError(false);
		        client.setPrintContentOnFailingStatusCode(false);
		        
		     // For the list of unique words
		        uniquewordList = new HashSet<String>();
		        words = new ArrayList<String>();
		     //   st = new StringTokenizer(body, " ");
		
		setTitle("Dan Portable's HTML word extractor");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 360, 537);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 354, 21);
		contentPane.add(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenu mnTasks = new JMenu("Tasks");
		mnFile.add(mnTasks);
		
		mntmOpenHtmlFile = new JMenuItem("Open HTML File...");
		mnTasks.add(mntmOpenHtmlFile);
		mntmOpenHtmlFile.addActionListener(this);
		
		mntmExtractDistinctWords = new JMenuItem("Extract distinct words to CSV File...");
		mnTasks.add(mntmExtractDistinctWords);
		mntmExtractDistinctWords.addActionListener(this);
		
		mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);
		mntmExit.addActionListener(this);
		
		JMenu mnAbout = new JMenu("Help");
		menuBar.add(mnAbout);
		
		mntmAboutThisProgram = new JMenuItem("About this Program");
		mnAbout.add(mntmAboutThisProgram);
		mntmAboutThisProgram.addActionListener(this);
		
		JLabel lblTitle = new JLabel("Title");
		lblTitle.setBounds(10, 32, 46, 14);
		contentPane.add(lblTitle);
		
		JLabel lblAuthor = new JLabel("Author");
		lblAuthor.setBounds(10, 60, 46, 14);
		contentPane.add(lblAuthor);
		
		textField_title = new JTextField();
		textField_title.setEditable(false);
		textField_title.setBounds(61, 29, 272, 20);
		contentPane.add(textField_title);
		textField_title.setColumns(10);
		
		textField_author = new JTextField();
		textField_author.setEditable(false);
		textField_author.setBounds(61, 60, 272, 20);
		contentPane.add(textField_author);
		textField_author.setColumns(10);
		
		JLabel lblDate = new JLabel("Date");
		lblDate.setBounds(10, 91, 46, 14);
		contentPane.add(lblDate);
		
		textField_date = new JTextField();
		textField_date.setEditable(false);
		textField_date.setBounds(61, 88, 272, 20);
		contentPane.add(textField_date);
		textField_date.setColumns(10);
		
		JLabel lblContent = new JLabel("Content");
		lblContent.setBounds(10, 116, 46, 14);
		contentPane.add(lblContent);
		
		textArea_content = new JTextArea();
		textArea_content.setLineWrap(true);
		textArea_content.setEditable(false);
		textArea_content.setBounds(10, 141, 323, 177);
		contentPane.add(textArea_content);
		
		textArea_log = new JTextArea();
		textArea_log.setLineWrap(true);
		textArea_log.setEditable(false);
		textArea_log.setBounds(10, 350, 323, 132);
		contentPane.add(textArea_log);
		
		JLabel lblLog = new JLabel("Log");
		lblLog.setBounds(10, 329, 46, 14);
		contentPane.add(lblLog);
        
	}
	
	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == mntmOpenHtmlFile)
		{
			fc = new JFileChooser();
			int returnVal = fc.showOpenDialog(this);

            if (returnVal == JFileChooser.APPROVE_OPTION)
            {
                fc.addChoosableFileFilter(new HTMLFilter());
                fc.setAcceptAllFileFilterUsed(false);
                File file = fc.getSelectedFile();
                // Open the file here
                try
                {
                    page = client.getPage(file.toURL());
                    currentPage = page.getBody();
                    contentTitle = currentPage.getFirstByXPath("//div[@id=\"content\"]/h1");
                    content = currentPage.getFirstByXPath("//div[@id=\"content\"]/div[@class=\"base\"]");
                    // Author name is at end of paragraph
                    contentAuthorEndParagraph = currentPage.getFirstByXPath("//*[@id=\"content\"]/div[4]/p[7]/b");
                    // Author name is after title
                    contentAuthorAfterTitle = currentPage.getFirstByXPath("//*[@id=\"content\"]/div[4]/p/a");
                    contentDate = currentPage.getFirstByXPath("//*[@id=\"content\"]/p[1]");
                    
                    title = contentTitle.asText();
                    date = contentDate.asText();
                    body = content.asText();
                    
                    if (contentAuthorEndParagraph != null && contentAuthorAfterTitle == null)
                    	author = contentAuthorEndParagraph.asText();           
                    else if (contentAuthorAfterTitle != null && contentAuthorEndParagraph == null)
                    	author = contentAuthorAfterTitle.asText();                 
                    
                    if (page != null) textArea_content.setText("");
                    
                    textField_title.setText(title);
                    textField_author.setText(author);
                    textField_date.setText(date);
                    textArea_content.append(body + newline);
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                textArea_log.append("Opened: " + file.getName() + "." + newline);
            }
            else
            {
                textArea_log.append("Open command cancelled by user." + newline);
            }
            textArea_log.setCaretPosition(textArea_log.getDocument().getLength());

		}
		else if (e.getSource() == mntmExtractDistinctWords)
		{
			if (page == null)
            {
                //custom title, warning icon
                JOptionPane.showMessageDialog(this,
                        "Open an HTML file first",
                        "ERROR: File Not Found",
                        JOptionPane.WARNING_MESSAGE);
            }
            else
            {
                int returnVal = fc.showSaveDialog(WordExtractorGUI.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    fc.addChoosableFileFilter(new CsvFilter());
                    fc.setAcceptAllFileFilterUsed(false);
                    File file = fc.getSelectedFile();
                        // use FileWriter constructor that specifies open for appending
                    String outputFile = file.getAbsoluteFile().toString();
                    try
                    {
                    	ArrayList<String> words = new ArrayList<String>();
                        st = new StringTokenizer(textArea_content.getText(), " ");
                        length = st.countTokens();
                        for(int i=0;i<length;i++){
                            words.add(st.nextToken());
                        }
                        
                        uniquewordList = returnUniqueWords(words);
                        
                        CsvWriter csvOutput = new CsvWriter(new FileWriter(outputFile, true), ',');
                        for (String uniqueWord : uniquewordList)
                        {
                        	csvOutput.write(uniqueWord);
                        }

/*                        for (int minion = 0; minion < length; minion++)
                        {
                            csvOutput.write(strArr[minion]);
                        } */
                       csvOutput.endRecord();
                       csvOutput.close();
                    }
                    catch (IOException e1)
                    {
                        e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                    textArea_log.append("Words saved to: " + file.getName() + "." + newline + uniquewordList.size() + " unique words extracted." + newline);
                } else {
                    textArea_log.append("Save command cancelled by user." + newline);
                }
                textArea_log.setCaretPosition(textArea_log.getDocument().getLength());
            }
        }
		else if (e.getSource() == mntmAboutThisProgram)
		{
			JOptionPane.showMessageDialog(this,
                    "HTML Text Extractor \nVersion 1.1 \nFinalized 26-JUL-2013");
		}
		else if (e.getSource() == mntmExit)
		{
			System.exit(EXIT_ON_CLOSE);
		}
			
	}
	
	private HashSet<String> returnUniqueWords(ArrayList<String> words)
	{
		HashSet<String> wordSet = new HashSet<String>();
		for (String word : words)
	       {
	           wordSet.add(word); // Words that are duplicates won't get added!
	       }
		
		return wordSet;
		
	}
}

