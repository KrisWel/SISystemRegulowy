import javax.swing.*; 
import java.awt.*; 
import java.awt.event.*; 
import java.text.BreakIterator;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.MissingResourceException;
 
import CLIPSJNI.*;



class Beer implements ActionListener {
	JLabel label;
	JButton nextButton;
	JPanel choicesPanel;
	ButtonGroup choicesButtons;
	ResourceBundle beerResources;
	
	Environment clips;
	boolean isExecuting = false;
	Thread thread;
	
	Beer() {
		try {
			beerResources = ResourceBundle.getBundle("resources.Beer", Locale.getDefault());
		}
		catch (MissingResourceException mre) {
			mre.printStackTrace();
			return;
		}
		
		//Frame
		JFrame frame = new JFrame(beerResources.getString("Beer"));
		frame.getContentPane().setLayout(new GridLayout(3, 1));
		frame.setSize(1000, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setIconImage(new ImageIcon("photos/beer.jpg").getImage());
		frame.setVisible(true); 
		
		//Panel
		JPanel displayPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		
		label = new JLabel();
		label.setForeground(Color.BLACK);
		label.setFont(new Font("Times New Roma", Font.PLAIN, 40));
		
		choicesPanel = new JPanel();
		choicesButtons = new ButtonGroup();
		
		nextButton = new JButton(beerResources.getString("Next"));
		nextButton.setActionCommand("Next");
		nextButton.addActionListener(this);
		nextButton.setFont(new Font("Times New Roma", Font.PLAIN, 25));
		
		displayPanel.add(label);
		buttonPanel.add(nextButton);
      
		frame.getContentPane().add(displayPanel); 
		frame.getContentPane().add(choicesPanel); 
		frame.getContentPane().add(buttonPanel); 
		choicesPanel.setBackground(Color.YELLOW);
		buttonPanel.setBackground(Color.YELLOW);
		displayPanel.setBackground(Color.WHITE);

		//Load the beer program
		clips = new Environment();
		clips.load("beer.clp");
		clips.reset();
		runBeer();
	}  

	private void nextUIState() throws Exception {
		//Get the state-list
		String currentUI = "(find-all-facts ((?f state-list)) TRUE)";
		String currentID = clips.eval(currentUI).get(0).getFactSlot("current").toString();

		//Get the current UI state
		currentUI = "(find-all-facts ((?f UI-state)) " + "(eq ?f:id " + currentID + "))";
		PrimitiveValue state = clips.eval(currentUI).get(0);
      
		boolean isFinal = false;
		
		//Determine the Next button states
		if (state.getFactSlot("state").toString().equals("final")) {
			isFinal = true;
			nextButton.setActionCommand("Restart");
			nextButton.setText(beerResources.getString("Restart"));
		}
		else {
			isFinal = false;
			nextButton.setActionCommand("Next");
			nextButton.setText(beerResources.getString("Next"));
		}
      
		//Set up the choices
		choicesPanel.removeAll();
		choicesButtons = new ButtonGroup();
            
		PrimitiveValue fact = state.getFactSlot("valid-answers");
		String selected = state.getFactSlot("response").toString();
     
		for (int i = 0; i < fact.size(); i++) {
			PrimitiveValue choose = fact.get(i);
			JRadioButton radioButton;
                        
			if (choose.toString().equals(selected))
				radioButton = new JRadioButton(beerResources.getString(choose.toString()),true);
			else
				radioButton = new JRadioButton(beerResources.getString(choose.toString()),false);
                     
			radioButton.setActionCommand(choose.toString());
			radioButton.setFont(new Font("Times New Roma", Font.PLAIN, 25));
			choicesPanel.add(radioButton);
			choicesButtons.add(radioButton);
		}
        
		
      
		//Set the label to display text/pictures
		String theText = beerResources.getString(state.getFactSlot("display").symbolValue());
		if(isFinal) {
			theText = "photos/"+theText;
			label.setIcon((Icon) new ImageIcon(theText));
			label.setText(null);
		}
		else {
			label.setIcon(null);
			label.setText(String.format("<html><center><div WIDTH=%d>%s</div></center></html>", 900, theText));
		}
		
		choicesPanel.repaint();
		
		thread = null;
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
		thread = new Thread(runThread);
		thread.start();
	}

	public void onActionPerformed(ActionEvent ae) throws Exception { 
		if (isExecuting) return;
      
		//Get the state-list
		String currentUI = "(find-all-facts ((?f state-list)) TRUE)";
		String currentID = clips.eval(currentUI).get(0).getFactSlot("current").toString();

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
     
	public static void main(String args[]) {  
      // Create the frame on the event dispatching thread.  
		SwingUtilities.invokeLater(new Runnable() {  
			public void run() { new Beer(); }  
		});   
	}  
}