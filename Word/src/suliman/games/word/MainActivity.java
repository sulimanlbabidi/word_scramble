package suliman.games.word;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import com.example.word.R;

import android.os.Bundle;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private String[] fourDic = {"game","road","path","fast","tank","tape","play","give","page"};
	private String[] fiveDic = {"speed","crack","smack","world","stone","think","study","group"};
	private String[] sixDic = {"battle","google","orient","ignore","editor","thread"};
	private int fourTotal,fiveTotal,sixTotal;	
	private int fourWin,fiveWin,sixWin;
	private int level;
	private float elapsedTime,averageTime;
	private long startTimer,endTimer;
	private String choosedWord;
	private SharedPreferences savedScore;
	private EditText wordEditText,guessEditText;	
	private Button clearButton,goButton,validateButton;	
	private TextView fourScoreTextView,fiveScoreTextView,sixScoreTextView,averageTimeTextView;
	
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        savedScore = getSharedPreferences("scores",MODE_PRIVATE);        
        averageTimeTextView =(TextView) findViewById(R.id.averageTimeTextView);
        wordEditText = (EditText) findViewById(R.id.wordEditText);
        guessEditText = (EditText) findViewById(R.id.guessEditText);
        goButton = (Button) findViewById(R.id.goButton);
        goButton.setOnClickListener(new goButtonListener());
        validateButton = (Button) findViewById(R.id.validateButton);
        validateButton.setOnClickListener(new validateButtonListener());
        clearButton = (Button) findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new clearButtonListener());
        fourScoreTextView =(TextView) findViewById(R.id.fourScoreTextView);
        fiveScoreTextView =(TextView) findViewById(R.id.fiveScoreTextView);
        sixScoreTextView =(TextView) findViewById(R.id.sixScoreTextView);
        refreshScores();
    }
    private void refreshScores(){
    	fourTotal = savedScore.getInt("fourTotal",0);
        fiveTotal = savedScore.getInt("fiveTotal",0);
        sixTotal = savedScore.getInt("sixTotal", 0);
        fourWin = savedScore.getInt("fourWin", 0);
        fiveWin = savedScore.getInt("fiveWin",0);
        sixWin = savedScore.getInt("sixWin", 0);
        averageTime = savedScore.getFloat("averageTime", 0);
    	fourScoreTextView.setText(fourWin + "/" + fourTotal);
		fiveScoreTextView.setText(fiveWin + "/" + fiveTotal);
		sixScoreTextView.setText(sixWin + "/" + sixTotal);
		averageTimeTextView.setText(getString(R.string.averageTime) + String.format(" %.2f", averageTime));
    }
    
    String pickAndRandomize(){
		level = (int) (Math.random()*4);
		String randomStr;		
		int randIndex;
		if (level==0 || level==1)
		{
			randIndex = (int)(Math.random()*fourDic.length);
			randomStr = fourDic[randIndex];
			choosedWord = randomStr;
		}
		else if (level==2){
			randIndex = (int)(Math.random()*fiveDic.length);
			randomStr = fiveDic[randIndex];
			choosedWord = randomStr;
}
		else{
			randIndex = (int)(Math.random()*sixDic.length);
			randomStr = sixDic[randIndex];
			choosedWord = randomStr;
}
		
		List<String> letters = Arrays.asList(randomStr.split(""));
		  Collections.shuffle(letters);
		  String shuffled = "";
		  for (String letter : letters) {
		    shuffled += letter;
		  }		
		return shuffled;
					  	
    }
    
    
    
   class goButtonListener implements OnClickListener{

	@Override
	public void onClick(View arg0) {
		Calendar cal = Calendar.getInstance();
		startTimer = cal.getTimeInMillis();
		wordEditText.setText(pickAndRandomize());	
		SharedPreferences.Editor editor = savedScore.edit();
		if (level == 0 || level == 1){			
			editor.putInt("fourTotal", ++fourTotal);
		}
		else if(level==2){
			editor.putInt("fiveTotal", ++fiveTotal);
		}
		else{
			editor.putInt("sixTotal", ++sixTotal);
		}
		editor.commit();
		
		
	}
	   
   }
   class validateButtonListener implements OnClickListener{

	@Override
	public void onClick(View arg0) {
		SharedPreferences.Editor editor = savedScore.edit();
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		if (guessEditText.getText().toString().equalsIgnoreCase(choosedWord))
		{
			Calendar cal = Calendar.getInstance();
			endTimer = cal.getTimeInMillis();
			elapsedTime = (float)((endTimer - startTimer) / 1000.00);
			/*String debug = "\n endTimer = " + endTimer + "\n startTimer = " + startTimer +
					"\n endTimer - StartTimer = " + (endTimer-startTimer);*/
			if (level == 0 || level == 1){			
				editor.putInt("fourWin", ++fourWin);
			}
			else if(level==2){
				editor.putInt("fiveWin", ++fiveWin);
			}
			else{
				editor.putInt("sixWin", ++sixWin);
			}							
			builder.setTitle(R.string.validateTitleTrue);
			builder.setMessage(getString(R.string.validateMessageTrue) +
					"\n ElaspedTime = " + elapsedTime /*+ debug*/);
			if (averageTime == 0)
				averageTime = elapsedTime;
			else
				averageTime = (float) ((averageTime + elapsedTime) /2.0);
			editor.putFloat("averageTime", averageTime);
		}
		else
		{
			builder.setTitle(R.string.validateTitleFalse);
			builder.setMessage(R.string.validateMessageFalse);
		}
		
		builder.setPositiveButton(R.string.ok, null);
		builder.setCancelable(true);
		AlertDialog alert = builder.create();
		alert.show();
		InputMethodManager inputMethodManager =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(guessEditText.getWindowToken(), 0);
		editor.commit();
		guessEditText.setText("");
		wordEditText.setText("");
        refreshScores();

		

	}
	   
   }
   class clearButtonListener implements OnClickListener
   {

	@Override
	public void onClick(View arg0) {
		SharedPreferences.Editor editor = savedScore.edit();
		editor.clear();
		editor.commit();
		refreshScores();
		
	}
	   
   }
   
		
	
	   
   }
    

