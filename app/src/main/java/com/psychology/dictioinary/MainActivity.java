package com.psychology.dictioinary;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.psychology.dictioinary.adapter.ListKataAdapter;
import com.psychology.dictioinary.app.DictionaryApplication;
import com.psychology.dictioinary.data.DefaultData;
import com.psychology.dictioinary.helper.KamusHelper;
import com.psychology.dictioinary.model.KamusModel;
import com.psychology.dictioinary.model.KamusObserver;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity
		 implements Observer{

	public ListKataAdapter listKataAdapter;
	private ListView lvKata;
	private ArrayList<KamusModel> listKata;
	private KamusHelper kamusHelper;
    private DictionaryApplication application;

	public static void showMeaningDialog(final Activity activity, final KamusModel item) {
		final Dialog dialog = new Dialog(activity, R.style.AppCompatAlertDialogStyle);
		dialog.setContentView(R.layout.dialog_arti);
		dialog.setCancelable(true);

		TextView txtArti = (TextView) dialog.findViewById(R.id.txtMeaning);
		TextView txtKata = (TextView) dialog.findViewById(R.id.txtWord);
		Button btnTutup = (Button) dialog.findViewById(R.id.btnTutup);

		txtArti.setText(item.getArti());
		txtKata.setText(item.getKata());

		btnTutup.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		dialog.show();

	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		lvKata = (ListView)findViewById(R.id.lvListKata);

		listKata = new ArrayList<KamusModel>();
        application = (DictionaryApplication)getApplication();
        application.getKamusObserver().addObserver(this);

		kamusHelper = new KamusHelper(MainActivity.this);
		kamusHelper.open();

		listKata = kamusHelper.getAllData();

		if (listKata.size()>0) {
			bindData();
		}else{
			insertDefaultData();
		}

		lvKata.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				showMeaningDialog(MainActivity.this, listKata.get(arg2));
			}
		});

	}

	private void insertDefaultData() {
		// TODO Auto-generated method stub
		new StoreDefaultData().execute();
	}

    public void update(Observable observable, Object o) {
        if (o.equals(KamusObserver.NEED_TO_REFRESH)){
            bindData();
        }
    }
	
	@Override
	protected void onDestroy() {
		application.getKamusObserver().deleteObserver(this);
        if (kamusHelper != null){
            kamusHelper.close();
        }
		super.onDestroy();
	}
	
	public void bindData(){
		if (listKata.size()>0) {
			listKata.clear();
		}
		listKata = kamusHelper.getAllData();
		listKataAdapter = new ListKataAdapter(MainActivity.this, listKata);
		lvKata.setAdapter(listKataAdapter);
		listKataAdapter.notifyDataSetChanged();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_activity_main, menu);
		SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

			case R.id.action_login:
				startActivity(new Intent(this, login_admin.class));
				return true;

			case R.id.action_info:
				startActivity(new Intent(this, info_app.class));
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class StoreDefaultData extends AsyncTask<Void, Void, Void> {

		ProgressDialog mProgressDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			mProgressDialog = new ProgressDialog(MainActivity.this);
			mProgressDialog.setTitle(getString(R.string.notify_input_data));
			mProgressDialog.setMessage(getString(R.string.text_please_wait));
			mProgressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			for (int i = 0; i < DefaultData.defaultData.length; i++) {
				kamusHelper.insert(KamusModel.getKamusModel(DefaultData.defaultData[i][0],
						DefaultData.defaultData[i][1]));
			}

			listKata = kamusHelper.getAllData();

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			mProgressDialog.dismiss();
			listKataAdapter = new ListKataAdapter(MainActivity.this, listKata);
			lvKata.setAdapter(listKataAdapter);
		}

	}
}
