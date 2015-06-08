package com.example.getscores;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.glass.app.Card;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

//Jason Rainier

public class MainActivity extends Activity {

	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private List<Card> mCards;
	private CardScrollView mCardScrollView;
	ArrayList<String> info = new ArrayList<String>();
	ExampleCardScrollAdapter adapter;
	String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		displaySpeechRecognizer();
		setContentView(R.layout.activity_main);

		try {
			new Scores()
			.execute(url);
		} catch (Exception e) {
		}
		// if (info == null) {
		// Toast.makeText(this, "info is null", Toast.LENGTH_LONG).show();
		// } else {
		// Toast.makeText(this, Integer.toString(info.size()),
		// Toast.LENGTH_LONG).show();
		// }

	}

	private static final int SPEECH_REQUEST = 0;

	private void displaySpeechRecognizer() {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		startActivityForResult(intent, SPEECH_REQUEST);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent data) {
		if (requestCode == SPEECH_REQUEST && resultCode == RESULT_OK) {
			List<String> results = data.getStringArrayListExtra(
					RecognizerIntent.EXTRA_RESULTS);
			String spokenText = results.get(0);
			if(spokenText.toLowerCase() == "soccer"){
				createCard();
			}
			else if(VoiceKeys.SportKeys.containsKey(spokenText)){
				url = VoiceKeys.SportKeys.get(spokenText);
			}
			else{
				url = VoiceKeys.SoccerLeagueKeys.get(spokenText);
			}
		}
		adapter.notifyDataSetChanged();
		super.onActivityResult(requestCode, resultCode, data);
	}

	// create cards with website data on them
	private void createCards(ArrayList<String> info) throws Exception {
		int numCards = info.size();
		mCards = new ArrayList<Card>();
		// return todays date to the user as a footnote on card
		Date date = new Date();

		for (int i = 0; i < numCards; i++) { //i <= numCards; i++) {
			Card card;
			card = new Card(this);
			card.setText(info.get(i));
			card.setFootnote("NCAA Mens Basketball " + dateFormat.format(date));
			mCards.add(card);
		}
		mCardScrollView = new CardScrollView(this);
		adapter = new ExampleCardScrollAdapter();
		mCardScrollView.setAdapter(adapter);
		setContentView(mCardScrollView);
		mCardScrollView.activate();
		adapter.notifyDataSetChanged();
	}
	
	//Creates a single card to display soccer leagues
	//User then chooses again
	private void createCard(){
		mCards = new ArrayList<Card>();
		Card card;
		card = new Card(this);
		card.setText("Premier League \t" + "MLS\n"
				+ "La Liga\t" + "Serie A\n"
				+ "Bundesliga\t" + "Champions League\n"
				+ "or\tEuro Cup?");
		
		mCards.add(card);
		mCardScrollView = new CardScrollView(this);
		adapter = new ExampleCardScrollAdapter();
		mCardScrollView.setAdapter(adapter);
		setContentView(mCardScrollView);
		mCardScrollView.activate();
		adapter.notifyDataSetChanged();
		displaySpeechRecognizer();
	}

	private class ExampleCardScrollAdapter extends CardScrollAdapter {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return mCards.get(position).toView();
		}

		@Override
		public int findIdPosition(Object id) {
			// System.out.println("findIdPosition called");
			return -1;
		}

		@Override
		public int findItemPosition(Object item) {
			// System.out.println("findItemPosition called");
			return mCards.indexOf(item);
		}

		@Override
		public int getCount() {
			// System.out.println("getCount called\n");
			return mCards.size();
		}

		@Override
		public Object getItem(int position) {
			// System.out.println("getItem called|");
			return mCards.get(position);
		}

	}

	// get scoring info from website
	public class Scores extends AsyncTask<String, Void, ArrayList<String>> {

		ArrayList<String> items = new ArrayList<String>();
		ArrayList<String> scores = new ArrayList<String>();
		ArrayList<String> teams = new ArrayList<String>();
		ArrayList<String> times = new ArrayList<String>();

		@Override
		protected ArrayList<String> doInBackground(String... params) {
			String url = params[0];
			if (url != null) {
				Document doc = null;
				try {
					doc = Jsoup.connect(url).get();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				Elements score = doc.select("li[id]");
				Elements team = doc.select("p[id]");
				Elements time = doc.select("p[id]");

				for (Element p : time) {
					if (p.attr("id").contains("statusLine1")) {
						times.add(p.text());
						times.add("fill");
					}
				}

				for (Element id : score) {

					if (id.attr("class").contains("final")) {
						scores.add(id.text());
					}
				}
				for (Element id : team) {
					if (id.attr("class").contains("team-name")) {
						teams.add("(" + id.text().replaceAll("\\D", "") + ")" 
								+ id.text().replaceAll("\\d", ""));
					}
				}
				try {
					for (int i = 0; i < teams.size(); i += 2) {
						if (scores.get(i).equals("0") && scores.get(i + 1).equals("0")) {
							items.add(teams.get(i) + " vs " + teams.get(i + 1) + " :"
									+ times.get(i));

						} else {
							items.add(teams.get(i) + " vs " + teams.get(i + 1) + " ("
									+ times.get(i) + "): " + scores.get(i) + " to "
									+ scores.get(i + 1));

						}
					}
				} catch (IndexOutOfBoundsException e) {		}
			}
			return items;
		}

		@Override
		protected void onPostExecute(ArrayList<String> info) {
			try {
				//    Toast.makeText(MainActivity.this, Integer.toString(info.size()), Toast.LENGTH_LONG).show();
				System.out.println("*************" + info.get(0));
				createCards(info);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public class SoccerScores extends AsyncTask<String, Void, ArrayList<String>> {

		ArrayList<String> items = new ArrayList<String>();
		ArrayList<String> scoreList = new ArrayList<String>();
		ArrayList<String> teamList = new ArrayList<String>();
		ArrayList<String> timeList = new ArrayList<String>();

		@Override
		protected ArrayList<String> doInBackground(String... params) {
			String url = params[0];
			if (url != null) {
				Document doc = null;
				try {
					doc = Jsoup.connect(url).get();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				Elements score = doc.select("li[class]");
				Elements team = doc.select("li[class]");
				Elements time = doc.select("li[class]");

				for(Element li : time){
					if(li.attr("class").contains("status")){
						timeList.add(li.text());
					}
				}

				for (Element li : score) {
					if(li.attr("class").contains("scores")){
						scoreList.add(li.text());
					}
				}

				for(Element li : team){
					if(li.attr("class").contains("teamId-")){
						teamList.add(li.text());
					}
				}
			}
			
			try {
				for (int i = 0; i < teamList.size(); i ++ ) {
					items.add(timeList.get(i) + " " + teamList.get(i) + " " + 
							scoreList.get(i) + " " + teamList.get(i + 1));
				}
			}
			catch(IndexOutOfBoundsException e){		}
			return items;
		}

		@Override
		protected void onPostExecute(ArrayList<String> info) {
			try {
				System.out.println("*************" + info.get(0));
				createCards(info);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
