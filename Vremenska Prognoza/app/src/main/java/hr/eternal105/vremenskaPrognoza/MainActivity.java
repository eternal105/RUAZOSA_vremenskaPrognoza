package hr.eternal105.vremenskaPrognoza;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends Activity {
    private TextView tv = null;
    private String tekst = null;
    private ProgressDialog pd = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.text_view);
        final Button b = (Button) findViewById(R.id.button);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DohvatiVrijeme().execute();
            }
        });
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */

    private class DohvatiVrijeme extends AsyncTask<Void, Void, Void> {
        private final String url_get = "http://api.openweathermap.org/data/2.5/weather?id=3195222";

        @Override
        protected Void doInBackground(Void... voids) {
            HTTPServis httpServis = new HTTPServis();
            String json = httpServis.napraviPoziv(url_get);

            Log.d("Odgovor: ", "> " + json);

            if (json != null) {
                try {
                    // spremamo json u varijablu
                    tekst = json;
                    // NAPOMENA: u ovom dijelu koda ne smije ici nikakva interakcija sa kontrolama,
                    // tj. objektima klase View jer ce izbaciti gresku s razlogom! na ovom podrucju
                    // ide striktno obrada podataka, ne i njihov prikaz

                    // pretvaramo json string u json objekt, tj. parsiramo ga i ako hocemo dalje
                    // pohranjujemo vrijednosti iz ovog objekta u vrijednosti atributa pojedinih
                    // klasa i slicno
                    JSONObject jsonObject = new JSONObject(json);

                    // tu iz json objekta izvlacis podatke i spremas u odgovarajuce varijable...

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("HTTPServis", "Nije bilo moguće dohvatiti nijedan podatak iz zadanje putanje");
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Molimo sačekajte...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (pd.isShowing())
                pd.dismiss();

            // prikazujemo json na kontroli
            tv.setText(tekst);
        }
    }
}
