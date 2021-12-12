import javax.swing.*; 
import java.awt.*; 
import java.awt.event.*; 
import java.text.BreakIterator;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.MissingResourceException;
 
import CLIPSJNI.*;



class Beer implements ActionListener {
	JLabel displayLabel;
	JButton nextButton;
	JPanel choicesPanel;
	ButtonGroup choicesButtons;
	ResourceBundle beerResources;
	
	Environment clips;
	boolean isExecuting = false;
	Thread executionThread;
	
	Beer() {
		try {
			beerResources = ResourceBundle.getBundle("resources.Beer",Locale.getDefault());
		}
		catch (MissingResourceException mre) {
			mre.printStackTrace();
			return;
		}
		
		JFrame jfrm = new JFrame(beerResources.getString("Beer"));
 
		//Specify FlowLayout manager
		jfrm.getContentPane().setLayout(new GridLayout(3,1));
		jfrm.setSize(640,480);
		
		jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
		JPanel displayPanel = new JPanel();
		displayLabel = new JLabel();
		displayPanel.add(displayLabel);
     
		choicesPanel = new JPanel(); 
		choicesButtons = new ButtonGroup();
		JPanel buttonPanel = new JPanel();
		nextButton = new JButton(beerResources.getString("Next"));
		nextButton.setActionCommand("Next");
		buttonPanel.add(nextButton);
		nextButton.addActionListener(this);
      
		jfrm.getContentPane().add(displayPanel); 
		jfrm.getContentPane().add(choicesPanel); 
		jfrm.getContentPane().add(buttonPanel); 

		//Load the beer program
		clips = new Environment();
		clips.load("beer.clp");
		clips.reset();
		runBeer();
      
		jfrm.setVisible(true);  
	}  

	private void nextUIState() throws Exception {
		//Get the state-list
		String evalStr = "(find-all-facts ((?f state-list)) TRUE)";
		String currentID = clips.eval(evalStr).get(0).getFactSlot("current").toString();

		//Get the current UI state
		evalStr = "(find-all-facts ((?f UI-state)) " + "(eq ?f:id " + currentID + "))";
		PrimitiveValue fv = clips.eval(evalStr).get(0);
      
		//Determine the Next button states
		if (fv.getFactSlot("state").toString().equals("final")) { 
			nextButton.setActionCommand("Restart");
			nextButton.setText(beerResources.getString("Restart")); 
		}
		else { 
			nextButton.setActionCommand("Next");
			nextButton.setText(beerResources.getString("Next"));
		}
      
		//Set up the choices
		choicesPanel.removeAll();
		choicesButtons = new ButtonGroup();
            
		PrimitiveValue pv = fv.getFactSlot("valid-answers");
      
		String selected = fv.getFactSlot("response").toString();
     
		for (int i = 0; i < pv.size(); i++) {
			PrimitiveValue bv = pv.get(i);
			JRadioButton rButton;
                        
			if (bv.toString().equals(selected))
				rButton = new JRadioButton(beerResources.getString(bv.toString()),true);
			else
				rButton = new JRadioButton(beerResources.getString(bv.toString()),false);
                     
			rButton.setActionCommand(bv.toString());
			choicesPanel.add(rButton);
			choicesButtons.add(rButton);
		}
        
		choicesPanel.repaint();
      
		//Set the label to the display text
		String theText = beerResources.getString(fv.getFactSlot("display").symbolValue());
		wrapLabelText(displayLabel,theText);
		
		executionThread = null;
		isExecuting = false;
	}

	public void actionPerformed(ActionEvent ae) { 
		try{ 
			onActionPerformed(ae);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
  
	public void runBeer() {
		Runnable runThread = new Runnable() {
			public void run() {
				clips.run();
               
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						try {
							nextUIState();
						}
						catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		};
      
		isExecuting = true;
		executionThread = new Thread(runThread);
		executionThread.start();
	}

	public void onActionPerformed(ActionEvent ae) throws Exception { 
		if (isExecuting) return;
      
		//Get the state-list
		String evalStr = "(find-all-facts ((?f state-list)) TRUE)";
		String currentID = clips.eval(evalStr).get(0).getFactSlot("current").toString();

		//Handle the Next button
		if (ae.getActionCommand().equals("Next")) {
			if (choicesButtons.getButtonCount() == 0)
				clips.assertString("(next " + currentID + ")");
			else
				clips.assertString("(next " + currentID + " " + choicesButtons.getSelection().getActionCommand() + ")");
           
			runBeer();
		}
		else { 
			clips.reset(); 
			runBeer();
		}
	}
 
	private void wrapLabelText(JLabel label, String text) {
		FontMetrics fm = label.getFontMetrics(label.getFont());
		Container container = label.getParent();
		int containerWidth = container.getWidth();
		int textWidth = SwingUtilities.computeStringWidth(fm,text);
		int desiredWidth;

		if (textWidth <= containerWidth)
			desiredWidth = containerWidth;
		else { 
			int lines = (int) ((textWidth + containerWidth) / containerWidth);
                  
			desiredWidth = (int) (textWidth / lines);
		}
                 
		BreakIterator boundary = BreakIterator.getWordInstance();
		boundary.setText(text);
   
		StringBuffer trial = new StringBuffer();
		StringBuffer real = new StringBuffer("<html><center>");
   
		int start = boundary.first();
		for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary.next()) {
			String word = text.substring(start,end);
			trial.append(word);
			int trialWidth = SwingUtilities.computeStringWidth(fm,trial.toString());
			if (trialWidth > containerWidth) {
				trial = new StringBuffer(word);
				real.append("<br>");
				real.append(word);
			}
			else if (trialWidth > desiredWidth) {
				trial = new StringBuffer("");
				real.append(word);
				real.append("<br>");
			}
			else
				real.append(word);
		}
   
		real.append("</html>");
   
		label.setText(real.toString());
	}
     
	public static void main(String args[]) {  
      // Create the frame on the event dispatching thread.  
		SwingUtilities.invokeLater(new Runnable() {  
			public void run() { new Beer(); }  
		});   
	}  
}